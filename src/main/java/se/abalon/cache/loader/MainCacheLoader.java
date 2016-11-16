package se.abalon.cache.loader;

import java.util.HashMap;
import java.util.Map;

import se.abalon.cache.loader.WorkPlaceCacheLoader;
import se.abalon.bof.cache.MayflowerCacher;

/**
 * The MainCacheLoader invokes the load method on all other implementations of CacheLoader.
 *
 * @author mied
 */
public class MainCacheLoader {

    private static MainCacheLoader thisLoader;
    private static MayflowerCacher cacher;
    private static Map<String, Boolean> classes = new HashMap<String, Boolean>();

    private MainCacheLoader() throws Exception {
        cacher = MayflowerCacher.getMayflowerCacher();
        thisLoader = this;
        listCacheLoaders();
    }

    private static void listCacheLoaders() {
        classes.clear();
        classes.put(CountryCacheLoader.class.getName(), false);
        classes.put(CurrencyCacheLoader.class.getName(), false);
        classes.put(CurrencyPeriodCacheLoader.class.getName(), false);
        classes.put(EmailCacheLoader.class.getName(), false);
        classes.put(MemberAccountCacheLoader.class.getName(), false);
        classes.put(MobileNumberCacheLoader.class.getName(), false);
        classes.put(OfferCacheLoader.class.getName(), false);
        classes.put(OfferValidityCacheLoader.class.getName(), false);
        classes.put(PARB2CCacheLoader.class.getName(), false);
        classes.put(PersonCacheLoader.class.getName(), false);
        classes.put(ProductGTINCacheLoader.class.getName(), false);
        classes.put(ProductCacheLoader.class.getName(), false);
        classes.put(ProductCategoryCacheLoader.class.getName(), false);
        classes.put(ProductGroupCacheLoader.class.getName(), false);
        classes.put(ProductMainGroupCacheLoader.class.getName(), false);
        classes.put(TerminalCacheLoader.class.getName(), false);
        classes.put(WorkPlaceCacheLoader.class.getName(), false);
        classes.put(DecidasCacheLoader.class.getName(), false);
        classes.put(OfferUsageCacheLoader.class.getName(), false);
        classes.put(CommunicationMonitorCacheLoader.class.getName(), false);
        System.out.println("::MAYFLOWER CACHE:: The following CacheLoaders will be executed by the MainCacheLoader:");
        for (String cacheLoaderName : classes.keySet()) {
            System.out.println("::MAYFLOWER CACHE:: " + cacheLoaderName);
        }
    }

    public void setAllCacheLoaderStatus(Boolean loaded) {
        for (String className : classes.keySet()) {
            setCacheLoaderStatus(className, loaded);
        }
    }

    public static void setCacheLoaderStatus(String className, Boolean loaded) {
        classes.put(className, loaded);
    }

    public static MainCacheLoader getMainCacheLoader() throws Exception {
        if (thisLoader == null) {
            new MainCacheLoader();
        }
        return (MainCacheLoader) thisLoader;
    }

    public void load() throws Exception {
        if (!isLoaded()) {
            Long timeBefore = System.currentTimeMillis();

            Integer numberOfThreads = classes.size();
            CacheWorkQueue workQueue = CacheWorkQueue.getCacheWorkQueue(numberOfThreads);
            System.out.println("::MAYFLOWER CACHE:: Starting cache loading with " + classes.size() + " CacheLoaders...");
            for (String cacheLoaderName : classes.keySet()) {
                Class<? extends AbstractCacheLoader> cacheLoader = (Class<? extends AbstractCacheLoader>) Class.forName(cacheLoaderName);
                AbstractCacheLoader thisCacheLoader = cacheLoader.newInstance();
                workQueue.execute(thisCacheLoader);
            }
            while (isLoaded().equals(false)) {
                Thread.currentThread().sleep(1000);
            }
            System.out.println("::MAYFLOWER CACHE:: ...Cache loading complete in " + (System.currentTimeMillis() - timeBefore) / 1000 + " seconds");
        }

    }

    public Boolean isLoaded() throws Exception {

        Boolean allCachesAreLoaded = true;
        for (String cacheLoader : classes.keySet()) {
            if (!classes.get(cacheLoader)) {
                allCachesAreLoaded = false;
                break;
            }
        }
        return allCachesAreLoaded;
    }

}