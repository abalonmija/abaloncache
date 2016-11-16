package se.abalon.cache.loader;
import java.util.*;

import javax.jdo.PersistenceManager;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.ObjectExistsException;
import net.sf.ehcache.config.CacheConfiguration;
import se.abalon.cache.threading.*;

/**
 * The Cacher grants access to all available caches, enabling adding new caches and putting and receiving entries from the available caches. The Cacher ensures that synchronization is properly used when accessing the caches.
 * <p>
 * <b>IMPORTANT: The cache requires that you have a ehcache.xml settings file in your jboss/server/[envirnment]/conf folder. If you are missing the ehcache.xml file there is a copy in libraries/lib. Copy this to your jboss/server/[envirnment]/conf folder.</b>
 * <p>
 * All caches are stored in the local memory on the server.
 * <p>
 * IMPORTANT: Make sure you have sufficient memory capacity for the caches in use in every environment.
 * <p>
 * Cacher implementations (such as MayflowerCache) shall always use the singleton pattern for instantiation. This ensures that there is always only one Cacher instantiated for every implementation in the JVM.
 * <p>
 * A cache is created with and identified via a CacheIdentifier. A CacheIdentifier is defined by setting the key and (if cache is a mapped cache) value specifications, allowing the Cacher to properly name or identify an already existing cache. All CacheIdentifiers that has been used for creating a
 * cache is held in a list by this Cacher. When using a CacheIdentifier upon access to a cache, it can either be retreived from this Cacher by using the get cache identifiers methods, or it can be instanciated locally.
 * <p>
 * A cache can be one of two types: Simple and Mapped. A Simple cache holds only one value per entry, and is mainly used for checking if a key exists in the cache (for duplicate control and such). A Mapped cache holds keys and mapped values for every key. It is used for fetching object values stored
 * in the cache with another value as key (for instance getting PONs based on objects CODE values, whereas the CODE would be the key and PON the value in the mapped cache).
 * <p>
 * Caches that are always available in the JVM are loaded when a Cacher object is first instantiated in the JVM. The constuctor of the Cacher will use reflection to run the load method in all classes implementing the CacheLoader interface, and that are in the package se.abalon.cache.loader. In order
 * to add a new cache that should always be available in the JVM, create a class implementing the CacheLoader interface that fetches data from the data store and put them in a designated cache using a CacheIdentifier.
 * <p>
 * It is possible to create caches by local code in any class within the JVM. This can be a good way to increase performance within a task, as it avoids multiple data store access. To do this, use the create methods in Cacher. Any cache created by local code will persist until the JVM is shut down.
 * <p>
 * IMPORTANT: As the caches are always stored in the memory of the server, make sure there is enough capacity to store the entries that are put in the cache. If there is not enough capacity performance will be severely reduced.
 * <p>
 *
 * @author Mikael Edgren (m.edgren@abalon.se)
 *
 */
public class MayflowerCacher {

    // private static CacheWorkQueue workQueue = CacheWorkQueue.getCacheeWorkQueue(1);
    private static final String PERSISTENCE_MANAGER_CACHE = "persistenceManagerCache";
    private static MayflowerCacher thisCacher;
    private CacheManager cacheManager;
    private List<CacheIdentifier> cacheIdentifiers;
    private Cache persistenceManagerCache;
    private static Boolean doneInitiating = false;

    /**
     * @throws Exception
     */
    private MayflowerCacher() throws Exception {
        thisCacher = this;
        cacheManager = CacheManager.create();
        cacheIdentifiers = new ArrayList<CacheIdentifier>();
        createPersistenceManagerCache();
        doneInitiating = true;
    }

    /**
     * Get the singleton instance of this MayflowerCacher.
     * <p>
     * If it has not already been instantiated within this JVM, the constructor will invoke all CacheLoaders' load methods.
     *
     * @return The MayflowerCacher instance for this JVM
     * @throws Exception
     */
    public static MayflowerCacher getMayflowerCacher() throws Exception {
        if (thisCacher == null) {
            new MayflowerCacher();
        }
        while (thisCacher != null && doneInitiating.equals(false)) {
            System.out.println("::Waiting in one second iterations for cache initation to be done (off heap initiation may take a while until done)...");
            Thread.currentThread().sleep(1000);
        }

        return (MayflowerCacher) thisCacher;
    }

    /**
     * Creates an empty limited cache.
     * <p>
     * A limited cache is constrained to a specific number of cached entries. If number of entries put in the cache exceeds maxEntriesInMemory, the first entry put in the cache will be evicted
     * <p>
     * Whether the cache is simple or mapped is determined by the type of CacheIdentifier that is provided in the input variable.
     * <p>
     * (NOTE: it seems like the eviction policy "First in first out" is not working, and that it is rather "Last recently used" that is in place. Until this is fixed: make sure you test how eviction pans out in your code)
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to name and identify the created cache, and whether the cache is Simple or Mapped
     * @param maxEntriesInMemory
     *            Maximum number of entries simultaneously held in this cache
     */
    public void createLimitedCache(CacheIdentifier cacheIdentifier, Integer maxEntriesInMemory) throws IllegalStateException, ObjectExistsException, CacheException {
        if (!cacheIdentifiers.contains(cacheIdentifier)) {
            cacheIdentifiers.add(cacheIdentifier);
        }
        if (!cacheManager.cacheExists(cacheIdentifier.getCacheName())) {
            cacheManager.addCache(cacheIdentifier.getCacheName());
            applyMayflowerLimitedEhCacheConfiguration(maxEntriesInMemory, cacheIdentifier);
        }
    }

    /**
     * Create an empty cache.
     * <p>
     * A mapped cache holds keys and a values that are mapped together. When referring to a key in the cache the value is always returned. Both map and key can be of any type of Object, including Lists/Maps/Sets.
     * <p>
     * Whether the cache is simple or mapped is determined by the type of CacheIdentifier that is provided in the input variable.
     * <p>
     * A simple cache holds only a list of keys. It is mainly used to see if a key exists in the cache.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to name and identify the created cache
     */
    public void createCache(CacheIdentifier cacheIdentifier) {
        if (!cacheIdentifiers.contains(cacheIdentifier)) {
            cacheIdentifiers.add(cacheIdentifier);
        }
        if (!cacheManager.cacheExists(cacheIdentifier.getCacheName())) {
            cacheManager.addCache(cacheIdentifier.getCacheName());
        }
        applyDefaultEhCacheConfiguration(cacheIdentifier);
    }

    /**
     * Put a an entry in a specific cache. This method will put simple or mapped depending on the provided CacheIdentifier, if simple then value will be ignored.
     * <p>
     * If the cache doesn't exist it will be created.
     * <p>
     * If cache is mapped and there already exists a value for the input key, and the existing value is a Collection<?> the input value will be added to that Collection, otherwise the existing value will be overwritten.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify the cache where the entry is to be stored
     * @param key
     *            The key to be used to find the mapped value in the cache
     * @param value
     *            The value to be set
     */
    public void put(CacheIdentifier cacheIdentifier, Object key, Object value) {
		/*
		 * Commented code is for using threading, which enables loading cache asynchronous with a defined maximum of parallell threads
		 */
        // PutInCacheRunnable putInCacheRunnable = new PutInCacheRunnable(cacheIdentifier, key, value);
        // workQueue.execute(putInCacheRunnable);
        doPut(cacheIdentifier, key, value);
    }

    /**
     * Put a mapped entry in a specific cache with a composite key.
     * <p>
     * If the cache doesn't exist it will be created.
     * <p>
     * If there already exists a value for the input key, and the existing value is a Collection<?> the input value will be added to that Collection, otherwise the existing value will be overwritten.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify the cache where the entry is to be stored
     * @param firstKey
     *            The first key to make out the composite key
     * @param firstKey
     *            The second key to make out the composite key
     * @param value
     *            The value to be stored in the cache
     */
    public void putCompositeKeyToValue(CacheIdentifier cacheIdentifier, Object firstKey, Object secondKey, Object value) {
        Object compositeKey = generateCompositeKey(firstKey, secondKey);
        put(cacheIdentifier, compositeKey, value);
    }

    public String generateCompositeKey(Object firstKey, Object secondKey) {
        return firstKey.toString() + CompositeKeyToValueObjectCacheIdentifier.KEY_SEPARATOR + secondKey.toString();
    }

    /**
     * Put a mapped entry in a specific cache.
     * <p>
     * If the cache doesn't exist it will be created.
     * <p>
     * If there already exists a value for the input key, and the existing value is a Collection<?> the input value will be added to that Collection, otherwise the existing value will be overwritten.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify the cache where the entry is to be stored
     * @param key
     *            The key to be used to find the mapped value in the cache
     * @param value
     *            The value to be stored in the cache
     */
    public void putKeyToValue(CacheIdentifier cacheIdentifier, Object key, Object value) {
        put(cacheIdentifier, key, value);
    }

    /**
     * Put a simple entry in a specific cache.
     * <p>
     * If the cache doesn't exist it will be created.
     * <p>
     * If the key already exists it will be ignored.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify the cache where the entry is to be stored
     * @param key
     *            The key to be stored in the cache
     */
    public void putKey(CacheIdentifier cacheIdentifier, Object key) {
        put(cacheIdentifier, key, null);
    }

    /**
     * Performs the put actions towards the cache. Calls to this method should be threaded. Cacher's other put methods uses CacherThreader's static put methods in order to make calls to this method threaded. See CacherThreader for more information.
     * <p>
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify the cache where the entry is to be stored
     * @param key
     *            The key to be used to find the mapped value in the cache
     * @param value
     *            The value to be set (null if SimpleCacheIdentifier)
     */
    public void doPut(CacheIdentifier cacheIdentifier, Object key, Object value) {
        if (cacheIdentifier instanceof EntityCacheIdentifier && ((EntityCacheIdentifier) cacheIdentifier).getPersistenceManager() != null) {
            doPersistenceManagerCaching(cacheIdentifier, key, value, PersistenceManagerCacheEvent.PUT);
        } else {
            doPutDisregardingPersistenceManager(cacheIdentifier, key, value);
        }

    }

    private void doPutDisregardingPersistenceManager(CacheIdentifier cacheIdentifier, Object key, Object value) {
        // System.out.println("For cache: " + cacheIdentifier.getCacheName() + ". Putting key: " + key + ", and value: " + value);
        if (cacheManager.getCache(cacheIdentifier.getCacheName()) == null) {
            createCache(cacheIdentifier);
        }
        Cache cache = cacheManager.getCache(cacheIdentifier.getCacheName());
        if (cache.get(key) != null) {
            cache.acquireWriteLockOnKey(key);
            Object cacheValue = getValue(cacheIdentifier, key);
            if (cacheValue != null && cacheValue instanceof Collection<?>) {
                if (value instanceof Collection<?>) {
                    ((Collection) cacheValue).addAll((Collection) value);
                } else {
                    ((Collection) cacheValue).add(value);
                }
            } else if (cacheValue != null) {
                cacheValue = value;
            } else {
                cacheValue = value;
            }
            try {
                cache.put(new Element(key, cacheValue));
            } finally {
                cache.releaseWriteLockOnKey(key);
            }
        } else {
            cache.put(new Element(key, value));
        }
		/*
		 * Commented code is for developer logging
		 */
        // // Create a pause to test parallel threading
        // doPause(5);
        // // Prints the put result from the cache
        // for (CacheIdentifier ci : cacheIdentifiers) {
        // System.out.println("CACHE: " + ci.getCacheName() + " = " +
        // cacheManager.getCache(ci.getCacheName()).getAll(cacheManager.getCache(ci.getCacheName()).getKeys()));
        // }

    }

    private void doPersistenceManagerCaching(CacheIdentifier cacheIdentifier, Object key, Object value, int event) {
        try {
            if (cacheIdentifier instanceof EntityCacheIdentifier && ((EntityCacheIdentifier) cacheIdentifier).getPersistenceManager() != null) {
                PersistenceManager persistenceManager = ((EntityCacheIdentifier) cacheIdentifier).getPersistenceManager();
                PersistenceManagerCacheEvent persistenceManagerCacheEvent = new PersistenceManagerCacheEvent(cacheIdentifier, key, value, event);
                if (persistenceManagerCache.get(persistenceManager) != null) {
                    persistenceManagerCache.acquireWriteLockOnKey(persistenceManager);
                    Object cacheValue = doGet(persistenceManager, persistenceManagerCache, true, false);
                    if (cacheValue != null) {
                        ((Collection) cacheValue).add(persistenceManagerCacheEvent);
                    } else {
                        List<PersistenceManagerCacheEvent> cacheList = new ArrayList<PersistenceManagerCacheEvent>();
                        cacheList.add(persistenceManagerCacheEvent);
                        cacheValue = cacheList;
                    }
                    try {
                        persistenceManagerCache.put(new Element(persistenceManager, cacheValue));
                    } finally {
                        persistenceManagerCache.releaseWriteLockOnKey(persistenceManager);

                    }
                } else {
                    List<PersistenceManagerCacheEvent> cacheList = new ArrayList<PersistenceManagerCacheEvent>();
                    cacheList.add(persistenceManagerCacheEvent);
                    persistenceManagerCache.put(new Element(persistenceManager, cacheList));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doPause(int iTimeInSeconds) {
        long t0, t1;
//		System.out.println("timer start");
        t0 = System.currentTimeMillis();
        t1 = System.currentTimeMillis() + (iTimeInSeconds * 1000);

//		System.out.println("T0: " + t0);
//		System.out.println("T1: " + t1);

        do {
            t0 = System.currentTimeMillis();

        } while (t0 < t1);

//		System.out.println("timer end");

    }

    /**
     * Get an entry value from a specific cache using a composite key.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify an existing cache where the value is stored. NOTE: If CacheIdentifer is a SimpleCacheIdentifier then the return value will always be null
     * @param firstKey
     *            The first key of the composite key
     * @param secondKey
     *            The second key of the composite key
     * @return The value stored in the cache
     */
    public Object getValueByCompositeKey(CacheIdentifier cacheIdentifier, Object firstKey, Object secondKey) {
        try {
            Cache cache = cacheManager.getCache(cacheIdentifier.getCacheName());
            return doGet(firstKey.toString() + CompositeKeyToValueObjectCacheIdentifier.KEY_SEPARATOR + secondKey.toString(), cache, true, false);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Get an entry value from a specific cache.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify an existing cache where the value is stored. NOTE: If CacheIdentifer is a SimpleCacheIdentifier then the return value will always be null
     * @param key
     *            The key used to find the mapped value in the cache
     * @return The value mapped to the provided key
     */
    public Object getValue(CacheIdentifier cacheIdentifier, Object key) {
        try {
            Cache cache = cacheManager.getCache(cacheIdentifier.getCacheName());
            return doGet(key, cache, true, false);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Get an entry value from a specific cache, with option of case insensitivity. If caseInsensitive is true and input key or key in cache are not Strings, normal cache doGet() will be applied
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify an existing cache where the value is stored. NOTE: If CacheIdentifer is a SimpleCacheIdentifier then the return value will always be null
     * @param key
     *            The key used to find the mapped value in the cache
     * @param caseInsensitive
     *            Boolean indicating case insensitivity
     * @return The value mapped to the provided key
     */
    public Object getValue(CacheIdentifier cacheIdentifier, Object key, Boolean caseInsensitive) {
        try {
            Cache cache = cacheManager.getCache(cacheIdentifier.getCacheName());
            return doGet(key, cache, true, caseInsensitive);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Convenience method to fetch values by cross referencing between two different caches. The key provided will be used to look for value in the first cache, the key cache, this value will in its turn be used as key in the second cache, the target cache, and return the value mapped to that key.
     *
     * @param keyCacheIdentifier
     *            The CacheIdentifier for the cache holding the value to be used as key for the target cache.
     * @param valueCacheIdentifier
     *            The CacheIDentifier for the cache holding the value to be returned
     * @param key
     *            The key to be used in the key cache
     * @return The value found in the target cache
     */
    public Object getValue(KeyToValueEntityCacheIdentifier keyCacheIdentifier, KeyToValueEntityCacheIdentifier valueCacheIdentifier, Object key) {
        try {
            Cache keyCache = cacheManager.getCache(keyCacheIdentifier.getCacheName());
            Cache valueCache = cacheManager.getCache(valueCacheIdentifier.getCacheName());
            return doGet(doGet(key, keyCache, true, false), valueCache, true, false);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Convenience method to fetch values by cross referencing between two different caches, with option of case insensitivity. The key provided will be used to look for value in the first cache, the key cache, this value will in its turn be used as key in the second cache, the target cache, and
     * return the value mapped to that key. If caseInsensitive is true and input key or keys in caches are not a String, normal cache doGet() will be applied.
     *
     * @param keyCacheIdentifier
     *            The CacheIdentifier for the cache holding the value to be used as key for the target cache.
     * @param valueCacheIdentifier
     *            The CacheIDentifier for the cache holding the value to be returned
     * @param key
     *            The key to be used in the key cache
     * @param caseInsensitive
     *            Boolean indicating case insensitivity
     * @return The value found in the target cache
     */
    public Object getValue(KeyToValueEntityCacheIdentifier keyCacheIdentifier, KeyToValueEntityCacheIdentifier valueCacheIdentifier, Object key, Boolean caseInsensitive) {
        try {
            Cache keyCache = cacheManager.getCache(keyCacheIdentifier.getCacheName());
            Cache valueCache = cacheManager.getCache(valueCacheIdentifier.getCacheName());
            return doGet(doGet(key, keyCache, true, caseInsensitive), valueCache, true, caseInsensitive);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Checks if the composite key exists in the specified cache.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify an existing cache where the entry is stored
     * @param secondKey
     *            The first key
     * @param firstKey
     *            The second key
     * @return true if the key exists, false if it does not.
     */
    public Boolean keyExists(CacheIdentifier cacheIdentifier, Object firstKey, Object secondKey) {
        try {
            Object value = getKey(cacheIdentifier, firstKey.toString() + CompositeKeyToValueObjectCacheIdentifier.KEY_SEPARATOR + secondKey.toString());
            if (value != null) {
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Checks if the key exists in the specified cache.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify an existing cache where the entry is stored
     * @param key
     *            The key to look for
     * @return true if the key exists, false if it does not.
     */
    public Boolean keyExists(CacheIdentifier cacheIdentifier, Object key) {
        try {
            Object value = getKey(cacheIdentifier, key);
            if (value != null) {
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Checks if the key exists in the specified cache, with option of case insensitivity. If caseInsensitive is true and input key or key in cache are not Strings, normal cache doGet() will be applied
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify an existing cache where the entry is stored
     * @param key
     *            The key to look for
     * @param caseInsensitive
     *            Boolean indicating case insensitivity
     * @return true if the key exists, false if it does not.
     */
    public Boolean keyExists(CacheIdentifier cacheIdentifier, Object key, Boolean caseInsensitive) {
        try {
            Object value = getKey(cacheIdentifier, key, caseInsensitive);
            if (value != null) {
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
    }

    private Object doGet(Object key, Cache cache, Boolean getValue, Boolean caseInsensitive) throws IllegalStateException, CacheException {
        Object returnValue = null;

        // This was an attempt to implement searchable caches, but it seems like the searh attributes needs to be defined before the eh cache manager is instatiated, which
        // doesn't fit our "one cache manager for all" strategy

        // if (cache.getName().equalsIgnoreCase("EMailAddress:TEXT:EMailAddress:ADDR_OWNER")) {
        // Attribute<String> keySearch = cache.getSearchAttribute("TEXT");
        // Query query = cache.createQuery().addCriteria(keySearch.ilike(key.toString()));
        // if (getValue.equals(true)) {
        // query.includeValues();
        // Results results = query.execute();
        // Result result = results.all().get(0);
        // returnValue = result.getValue();
        // } else {
        // query.includeKeys();
        // Results results = query.execute();
        // Result result = results.all().get(0);
        // returnValue = result.getKey();
        // }
        //
        // } else {

        if (caseInsensitive && key instanceof String) {
            List<Object> keys = cache.getKeys();
            for (Object object : keys) {
                if (object instanceof String && ((String) object).equalsIgnoreCase((String) key)) {
                    if (getValue.equals(true)) {
                        returnValue = cache.get(object).getObjectValue();
                    } else {
                        returnValue = cache.get(object).getObjectKey();
                    }
                    break;
                }
            }

        } else {
            try {
                cache.acquireReadLockOnKey(key);
                if (getValue.equals(true)) {
                    returnValue = cache.get(key).getObjectValue();
                } else {
                    returnValue = cache.get(key).getObjectKey();
                }
            } finally {
                cache.releaseReadLockOnKey(key);
            }
        }
        return returnValue;
    }

    /**
     * Gets the key for an existing entry in the specified cache.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify an existing cache where the entry is stored
     * @param key
     *            The key to look for
     * @return The key found in the target cache.
     */
    public Object getKey(CacheIdentifier cacheIdentifier, Object key) throws IllegalStateException, CacheException {
        Object returnValue = null;
        try {
            Cache cache = cacheManager.getCache(cacheIdentifier.getCacheName());
            returnValue = doGet(key, cache, false, false);
        } catch (NullPointerException e) {
            return null;
        }
        return returnValue;
    }

    /**
     * Gets the key for an existing entry in the specified cache, with option of case insensitivity. If caseInsensitive is true and input key or key in cache are not Strings, normal cache doGet() will be applied
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify an existing cache where the entry is stored
     * @param key
     *            The key to look for
     * @param caseInsensitive
     *            Boolean indicating case insensitivity
     * @return The key found in the target cache.
     */
    public Object getKey(CacheIdentifier cacheIdentifier, Object key, Boolean caseInsensitive) throws IllegalStateException, CacheException {
        Object returnValue = null;
        try {
            Cache cache = cacheManager.getCache(cacheIdentifier.getCacheName());
            returnValue = doGet(key, cache, false, caseInsensitive);
        } catch (NullPointerException e) {
            return null;
        }
        return returnValue;
    }

    /**
     * Gets all the keys in the specified cache.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify an existing cache where the entry is stored
     * @return Collection with the keys found in the target cache.
     */
    public List<Object> getAllKeys(CacheIdentifier cacheIdentifier) throws IllegalStateException, CacheException {
        List<Object> returnValue = null;
        try {
            Cache cache = cacheManager.getCache(cacheIdentifier.getCacheName());
            returnValue = cache.getKeys();
        } catch (NullPointerException e) {
            return null;
        }
        return returnValue;
    }

    /**
     * Returns a List containing all CacheIdentifiers for the existing caches, mapped and single caches.
     *
     * @return A List containing all CacheIdentifiers for existing caches
     */
    public List<CacheIdentifier> getAllExistingCacheIdentifiers() {
        return cacheIdentifiers;
    }

    /**
     * Returns a List containing all ObjectCacheIdentifiers for the existing caches, mapped and single caches.
     *
     * @return A List containing all ObjectCacheIdentifiers for existing caches
     */
    public List<ObjectCacheIdentifier> getAllExistingObjectCacheIdentifiers() {
        List<ObjectCacheIdentifier> objectCacheIdentifiers = new ArrayList<ObjectCacheIdentifier>();
        for (CacheIdentifier cacheIdentifier : cacheIdentifiers) {
            if (cacheIdentifier instanceof ObjectCacheIdentifier) {
                objectCacheIdentifiers.add((ObjectCacheIdentifier) cacheIdentifier);
            }
        }
        return objectCacheIdentifiers;
    }

    /**
     * Returns a List containing all EntityCacheIdentifiers for the existing caches, mapped and single caches.
     *
     * @return A List containing all EntityCacheIdentifiers for existing caches
     */
    public List<EntityCacheIdentifier> getAllExistingEntityCacheIdentifiers() {
        List<EntityCacheIdentifier> entityCacheIdentifiers = new ArrayList<EntityCacheIdentifier>();
        for (CacheIdentifier cacheIdentifier : cacheIdentifiers) {
            if (cacheIdentifier instanceof EntityCacheIdentifier) {
                entityCacheIdentifiers.add((EntityCacheIdentifier) cacheIdentifier);
            }
        }
        return entityCacheIdentifiers;
    }

    /**
     * Returns a specific CacheIdentifier based on the cache name
     *
     * @param cacheName
     *            Name of the cache
     * @return A CacheIdentifier
     */
    public CacheIdentifier getExistingCacheIdentifier(String cacheName) {
        for (CacheIdentifier cacheIdentifier : cacheIdentifiers) {
            if (cacheIdentifier.getCacheName().equals((cacheName))) {
                return cacheIdentifier;
            }
        }
        return null;
    }

    /**
     * Returns a specific KeyToValueEntityCacheIdentifier based on the provided attributes.
     *
     * @param keyModelName
     *            Name of the Model that the key belongs to
     * @param keyFieldName
     *            Name of the Field on the key Model
     * @param valueModelName
     *            Name of the Model that the mapped value belongs to
     * @param valueFieldName
     *            Name of the Field on the value Model
     * @return A MappedCacheIdentifier
     */
    public KeyToValueEntityCacheIdentifier getExistingKeyToValueEntityCacheIdentifier(String keyModelName, String keyFieldName, String valueModelName, String valueFieldName) {
        for (CacheIdentifier cacheIdentifier : cacheIdentifiers) {
            if (cacheIdentifier instanceof KeyToValueEntityCacheIdentifier) {
                if (((KeyToValueEntityCacheIdentifier) cacheIdentifier).equals(keyModelName, keyFieldName, valueModelName, valueFieldName)) {
                    KeyToValueEntityCacheIdentifier mappedCacheIdentifier = (KeyToValueEntityCacheIdentifier) cacheIdentifier;
                    return mappedCacheIdentifier;
                }
            }
        }
        return null;
    }

    /**
     * Returns a specific KeyEntityCacheIdentifier based on the provided attributes.
     *
     * @param modelName
     *            Name of the Model that the entry belongs to
     * @param fieldName
     *            Name of the Field on the Model
     * @return A SimpleCacheIdentifier
     */
    public KeyEntityCacheIdentifier getExistingKeyEntityCacheIdentifier(String modelName, String fieldName) {
        for (CacheIdentifier cacheIdentifier : cacheIdentifiers) {
            if (cacheIdentifier instanceof KeyEntityCacheIdentifier) {
                if (((KeyEntityCacheIdentifier) cacheIdentifier).equals(modelName, fieldName)) {
                    KeyEntityCacheIdentifier simpleCacheIdentifier = (KeyEntityCacheIdentifier) cacheIdentifier;
                    return simpleCacheIdentifier;
                }
            }
        }
        return null;
    }

    /**
     * Returns a specific KeyToValueObjectCacheIdentifier based on the provided attributes.
     *
     * @param heading
     *            Heading of the cache
     * @param keyDescription
     *            Description of the Keys in the cache
     * @param valueDescription
     *            Description of the values in the cache
     * @return A KeyToValueObjectCacheIdentifier
     */
    public KeyToValueObjectCacheIdentifier getExistingKeyToValueObjectCacheIdentifier(String heading, String keyDescription, String valueDescription) {
        for (CacheIdentifier cacheIdentifier : cacheIdentifiers) {
            if (cacheIdentifier instanceof KeyToValueObjectCacheIdentifier) {
                if (((KeyToValueObjectCacheIdentifier) cacheIdentifier).equals(heading, keyDescription, valueDescription)) {
                    KeyToValueObjectCacheIdentifier keyToValueCacheIdentifier = (KeyToValueObjectCacheIdentifier) cacheIdentifier;
                    return keyToValueCacheIdentifier;
                }
            }
        }
        return null;
    }

    /**
     * Returns a specific CompositeKeyToValueObjectCacheIdentifier based on the provided attributes.
     *
     * @param heading
     *            Heading of the cache
     * @param firstKeyDescription
     *            Description of the Keys in the cache
     * @param secondKeyDescription
     *            Description of the Keys in the cache
     * @param valueDescription
     *            Description of the values in the cache
     * @return A KeyToValueObjectCacheIdentifier
     */
    public KeyToValueObjectCacheIdentifier getExistingCompositeKeyToValueObjectCacheIdentifier(String heading, String firstKeyDescription, String secondKeyDescription, String valueDescription) {
        for (CacheIdentifier cacheIdentifier : cacheIdentifiers) {
            if (cacheIdentifier instanceof KeyToValueObjectCacheIdentifier) {
                if (((KeyToValueObjectCacheIdentifier) cacheIdentifier).equals(heading, firstKeyDescription + CompositeKeyToValueObjectCacheIdentifier.KEY_SEPARATOR + secondKeyDescription, valueDescription)) {
                    KeyToValueObjectCacheIdentifier keyToValueCacheIdentifier = (KeyToValueObjectCacheIdentifier) cacheIdentifier;
                    return keyToValueCacheIdentifier;
                }
            }
        }
        return null;
    }

    /**
     * Returns a specific KeyObjectCacheIdentifier based on the provided attributes.
     *
     * @param keyDescription
     *            Description of the Keys in the cache
     * @return A SimpleCacheIdentifier
     */
    public KeyObjectCacheIdentifier getExistingKeyObjectCacheIdentifier(String heading, String keyDescription) {
        for (CacheIdentifier cacheIdentifier : cacheIdentifiers) {
            if (cacheIdentifier instanceof KeyObjectCacheIdentifier) {
                if (((KeyObjectCacheIdentifier) cacheIdentifier).equals(heading, keyDescription)) {
                    KeyObjectCacheIdentifier keyCacheIdentifier = (KeyObjectCacheIdentifier) cacheIdentifier;
                    return keyCacheIdentifier;
                }
            }
        }
        return null;
    }

    private void applyMayflowerLimitedEhCacheConfiguration(Integer maxEntriesInMemory, CacheIdentifier cacheIdentifier) throws IllegalStateException, ObjectExistsException, CacheException {
        CacheConfiguration ehCacheConfiguration = getCachesEhCacheConfiguration(cacheIdentifier);
        applyMayflowerDefaultEhCacheConfigurationAttributes(ehCacheConfiguration);
        ehCacheConfiguration.setEternal(false);
        ehCacheConfiguration.setMaxEntriesLocalHeap(maxEntriesInMemory);
    }

    // This was an attempt to implement searchable caches, but it seems like the searh attributes needs to be defined before the eh cache manager is instatiated, which
    // doesn't fit our "one cache manager for all" strategy
    // private void applyMayflowerCaseInsensitiveEhCacheConfiguration(CacheIdentifier cacheIdentifier) throws IllegalStateException, ObjectExistsException, CacheException {
    // CacheConfiguration ehCacheConfiguration = getCachesEhCacheConfiguration(cacheIdentifier);
    // applyMayflowerDefaultEhCacheConfigurationAttributes(ehCacheConfiguration);
    // Searchable searchable = new Searchable();
    // ehCacheConfiguration.addSearchable(searchable);
    // searchable.addSearchAttribute(new SearchAttribute().name(cacheIdentifier.getFieldName()));
    // }

    private void applyDefaultEhCacheConfiguration(CacheIdentifier cacheIdentifier) {
        applyMayflowerDefaultEhCacheConfigurationAttributes(getCachesEhCacheConfiguration(cacheIdentifier));
    }

    private void applyMayflowerDefaultEhCacheConfigurationAttributes(CacheConfiguration ehCacheConfiguration) {
        // The default configuration is defined in the ehcache.xml file located in the jboss/server/[envirnment]/conf folder
        // If you are missing the ehcache.xml file in your jboss/server/[envirnment]/conf folder there is a copy in libraries/lib. Copy this to your jboss/server/[envirnment]/conf folder
    }

    private void createPersistenceManagerCache() {
        // The persistence manager cache is defined in the ehcache.xml file located in the jboss/server/[envirnment]/conf folder
        // If you are missing the ehcache.xml file in your jboss/server/[envirnment]/conf folder there is a copy in libraries/lib. Copy this to your jboss/server/[envirnment]/conf folder
        persistenceManagerCache = cacheManager.getCache(PERSISTENCE_MANAGER_CACHE);
    }

    private CacheConfiguration getCachesEhCacheConfiguration(CacheIdentifier cacheIdentifier) {
        return cacheManager.getCache(cacheIdentifier.getCacheName()).getCacheConfiguration();
    }

    /**
     * Performs the remove actions towards the cache. Calls to this method should be threaded. Cacher's other put methods uses CacherThreader's static remove methods in order to make calls to this method threaded. See CacherThreader for more information.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify the cache where the entry is stored
     * @param key
     *            The key for the entry to be removed
     * @param caseInsensitive
     *            Boolean indicating case insensitivity
     */
    public void doRemove(CacheIdentifier cacheIdentifier, Object key, Boolean caseInsensitive) {
        if (cacheIdentifier instanceof EntityCacheIdentifier && ((EntityCacheIdentifier) cacheIdentifier).getPersistenceManager() != null) {
            doPersistenceManagerCaching(cacheIdentifier, key, null, PersistenceManagerCacheEvent.REMOVED);
        } else {
            doRemoveDisregardingPersistenceManager(cacheIdentifier, key, caseInsensitive);
        }
    }

    private void doRemoveDisregardingPersistenceManager(CacheIdentifier cacheIdentifier, Object key, Boolean caseInsensitive) {
        if (cacheManager.getCache(cacheIdentifier.getCacheName()) != null) {
            Cache cache = cacheManager.getCache(cacheIdentifier.getCacheName());
            if (getKey(cacheIdentifier, key, caseInsensitive) != null) {
                Object keyInCache = getKey(cacheIdentifier, key, caseInsensitive);
                cache.acquireWriteLockOnKey(keyInCache);
                try {
                    cache.remove(keyInCache);
                } finally {
                    cache.releaseWriteLockOnKey(keyInCache);
                }
            }
        }
        // for (CacheIdentifier ci : cacheIdentifiers) {
        // System.out.println("CACHE: " + ci.getCacheName() + " = " +
        // cacheManager.getCache(ci.getCacheName()).getAll(cacheManager.getCache(ci.getCacheName()).getKeys()));
        // }
    }

    /**
     * Removes an entry from a specific cache. If entry doesn't exist nothing will happen.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify the cache where the entry is stored
     * @param key
     *            The key for the entry to be removed
     */
    public void removeEntry(CacheIdentifier cacheIdentifier, Object key) {
		/*
		 * Commented code is for using threading, which enables loading cache asynchronous with a defined maximum of parallell threads
		 */
        // RemoveFromCacheRunnable removeFromCacheRunnable = new
        // RemoveFromCacheRunnable(cacheIdentifier, key);
        // workQueue.execute(removeFromCacheRunnable);
        doRemove(cacheIdentifier, key, false);
    }

    /**
     * Removes an entry from a specific cache, with option of case insensitivity. If caseInsensitive is true and input key or key in cache are not Strings, normal cache doGet() will be applied. If entry doesn't exist nothing will happen.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify the cache where the entry is stored
     * @param key
     *            The key for the entry to be removed
     * @param caseInsensitive
     *            Boolean indicating case insensitivity
     */
    public void removeEntry(CacheIdentifier cacheIdentifier, Object key, Boolean caseInsensitive) {
		/*
		 * Commented code is for using threading, which enables loading cache asynchronous with a defined maximum of parallell threads
		 */
        // RemoveFromCacheRunnable removeFromCacheRunnable = new
        // RemoveFromCacheRunnable(cacheIdentifier, key);
        // workQueue.execute(removeFromCacheRunnable);
        doRemove(cacheIdentifier, key, caseInsensitive);
    }

    /**
     * Removes all entries in all of the existing caches, leaving all caches empty.
     *
     */
    public void emptyAllCaches() {
        cacheManager.clearAll();
    }

    /**
     * Empties a specific cache.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify an existing cache to be emptied.
     */
    public void emptyCache(CacheIdentifier cacheIdentifier) {
        CacheConfiguration cacheConfiguration = cacheManager.getCache(cacheIdentifier.getCacheName()).getCacheConfiguration();
        if (cacheConfiguration.getMaxEntriesLocalHeap() == 0) {
            cacheManager.removeCache(cacheIdentifier.getCacheName());
            cacheIdentifiers.remove(cacheIdentifier);
            createCache(cacheIdentifier);
        } else {
            Integer maxEntriesInMemory = new Long(cacheConfiguration.getMaxEntriesLocalHeap()).intValue();
            cacheManager.removeCache(cacheIdentifier.getCacheName());
            cacheIdentifiers.remove(cacheIdentifier);
            createLimitedCache(cacheIdentifier, maxEntriesInMemory);
        }

    }

    /**
     * Rolls back the caching made for cacheIdentifiers associated with this PersistenceManager
     *
     * @param persistenceManager
     *            The PersistenceManager to do rollback for
     */
    public void rollbackCache(PersistenceManager persistenceManager) {
        if (persistenceManagerCache.get(persistenceManager) != null) {
            persistenceManagerCache.remove(persistenceManager);
        }
    }

    /**
     * Removes the temporarily put cache elements for all cache actions associated with this PersistenceManager.
     *
     * @param persistenceManager
     *            The PersistenceManager to do treat as commited
     */
    public void setCacheToCommited(PersistenceManager persistenceManager) {
        if (persistenceManagerCache != null && persistenceManagerCache.get(persistenceManager) != null) {
            List<PersistenceManagerCacheEvent> managerEvents = (List<PersistenceManagerCacheEvent>) persistenceManagerCache.get(persistenceManager).getObjectValue();
            for (PersistenceManagerCacheEvent persistenceManagerCacheEvent : managerEvents) {
                if (persistenceManagerCacheEvent.getEvent().equals(PersistenceManagerCacheEvent.PUT) || persistenceManagerCacheEvent.getEvent().equals(PersistenceManagerCacheEvent.ADDED_TO_COLLECTION)) {
                    doPutDisregardingPersistenceManager(persistenceManagerCacheEvent.getCacheIdentifier(), persistenceManagerCacheEvent.getKey(), persistenceManagerCacheEvent.getValue());
                } else if (persistenceManagerCacheEvent.getEvent().equals(PersistenceManagerCacheEvent.REMOVED)) {
                    doRemoveDisregardingPersistenceManager(persistenceManagerCacheEvent.getCacheIdentifier(), persistenceManagerCacheEvent.getKey(), false);
                } else if (persistenceManagerCacheEvent.getEvent().equals(PersistenceManagerCacheEvent.ADDED_TO_COLLECTION)) {
                }
            }
            persistenceManagerCache.remove(persistenceManager);
        }
    }

    /**
     * Returns the content of a specified cache as a map.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify the cache to get dump for
     * @return
     */
    public Map<Object, Object> getCacheDump(CacheIdentifier cacheIdentifier) {
        Map<Object, Object> cacheDump = new HashMap<Object, Object>();
        for (Object key : getAllKeys(cacheIdentifier)) {
            cacheDump.put(key, getValue(cacheIdentifier, key));
        }
        return cacheDump;
    }

    /**
     * Prints the content of a cache.
     *
     * @param cacheIdentifier
     *            The CacheIdentifier used to identify the cache to get dump for
     * @return
     */
    public void printCacheDump(CacheIdentifier cacheIdentifier) {
        System.out.println(":: CACHE NAME: " + cacheIdentifier.getCacheName() + ";    NUMBER OF ENTRIES: " + getAllKeys(cacheIdentifier).size() + ";    CONTENT: " + getCacheDump(cacheIdentifier));
    }

    /**
     * Prints the content of all caches.
     *
     * @return
     */
    public void printCacheDumps() {
        for (CacheIdentifier cacheIdentifier : getAllExistingCacheIdentifiers()) {
            printCacheDump(cacheIdentifier);
        }
    }
}

