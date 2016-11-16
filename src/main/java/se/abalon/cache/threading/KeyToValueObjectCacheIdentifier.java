package se.abalon.cache.threading;

import se.abalon.cache.threading.AbstractObjectCacheIdentifier;

/**
 *
 * A KeyToValueObjectCacheIdentifier for identifying key to value caches. A key to value cache contains map entries (Key -> value).
 * <p>
 * The naming of a cache created with a KeyObjectCacheManager is a concatenation of the description of the keys and the description of the values, separated with colon (:)
 * <p>

 * @author Mikael Edgren (m.edgren@abalon.se)
 *
 */
public class KeyToValueObjectCacheIdentifier extends AbstractObjectCacheIdentifier {

    protected String valueDescription;

    /**
     * @param heading
     *            The heading of the cache
     * @param keyDescription
     *            The description of the keys in a cache
     * @param valueDescription
     *            The description of the values in a cache
     */
    public KeyToValueObjectCacheIdentifier(String heading, String keyDescription, String valueDescription) {
        this.heading = heading;
        this.keyDescription = keyDescription;
        this.valueDescription = valueDescription;
        setCacheName();
    }

    public boolean equals(String heading, String keyDescription, String valueDescription) {
        if (cacheName.equals(heading + ":" + keyDescription + ":" + valueDescription)) {
            return true;
        }
        return false;
    }

    /**
     * Get the value description for the cache that this CacheIdentifier corresponds to
     *
     * @return The value description as a String
     */
    public String getValueDescription() {
        return valueDescription;
    }

    /**
     * Set the value description for the cache that this CacheIdentifier corresponds to
     *
     * @param valueDescription
     */
    public void setValueModelName(String valueModelName) {
        this.valueDescription = valueModelName;
        setCacheName();
    }

    public void setCacheName() {
        cacheName = heading + ":" + getKeyDescription() + ":" + getValueDescription();
    }

}
