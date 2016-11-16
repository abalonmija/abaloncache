package se.abalon.cache.threading;

import se.abalon.cache.threading.AbstractObjectCacheIdentifier;

/**
 *
 * A KeyOjectCacheIdentifier for identifying key caches created with ObjectCacheIdentifiers. A key cache holds only a key and no value per entry in the cache.
 * <p>
 * The naming of a cache created with a KeyObjectCacheManager is the same as the description of the keys
 * <p>
 *
 * @author Mikael Edgren (m.edgren@abalon.se)
 *
 */
public class KeyObjectCacheIdentifier extends AbstractObjectCacheIdentifier {

    /**
     * @param heading
     *            The heading of this cache
     * @param keyDescription
     *            The description of the keys in this cache
     */
    public KeyObjectCacheIdentifier(String heading, String keyDescription) {
        super.heading = heading;
        super.keyDescription = keyDescription;
        setCacheName();
    }

    /**
     * Set the key description that this CacheIdentifier corresponds to
     *
     * @param modelName
     */
    public void setKeyDescription(String keyDescription) {
        super.keyDescription = keyDescription;
        setCacheName();
    }

    public void setCacheName() {
        cacheName = heading + ":" + getKeyDescription();
    }

    public Boolean equals(String heading, String keyDescription) {
        if (cacheName.equals(heading + ":" + keyDescription)) {
            return true;
        }
        return false;
    }

}
