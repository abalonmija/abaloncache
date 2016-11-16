package se.abalon.cache.threading;

import se.abalon.cache.ObjectCacheIdentifier;

/**
 * Abstract class containing the common methods for an ObjectCacheIdentifier.
 *
 * @author Mikael Edgren (m.edgren@abalon.se)
 *
 */
public abstract class AbstractObjectCacheIdentifier implements ObjectCacheIdentifier {

    public String heading;
    public String keyDescription;
    public String cacheName;

    /**
     * Get the name of the cache that this CacheIdentifier corresponds to
     *
     * @return The cache name as a String
     */
    public String getCacheName() {
        return cacheName;
    }
    /**
     * Get the heading that this CacheIdentifier corresponds to
     *
     * @return The heading as a String
     */
    public String getHeading() {
        return heading;
    }

    /**
     * Set the heading that this CacheIdentifier corresponds to
     *
     * @return The heading as a String
     */
    public void setHeading(String heading) {
        this.heading = heading;
    }

    /**
     * Get the key description that this CacheIdentifier corresponds to
     *
     * @return The keyDescription as a String
     */
    public String getKeyDescription() {
        return keyDescription;
    }

    /**
     * Set the key description that this CacheIdentifier corresponds to
     *
     * @return The keyDescription as a String
     */
    public void setKeyDescription(String keyDescription) {
        this.keyDescription = keyDescription;
    }

    public String toString() {
        return cacheName;
    }

    public boolean equals(Object object) {
        if (object instanceof AbstractObjectCacheIdentifier) {
            AbstractObjectCacheIdentifier abstractCacheIdentifier = (AbstractObjectCacheIdentifier) object;
            if (this.cacheName.equals(abstractCacheIdentifier.getCacheName())) {
                return true;
            }
        }
        return false;
    }
}
