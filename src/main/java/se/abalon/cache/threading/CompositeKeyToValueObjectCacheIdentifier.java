package se.abalon.cache.threading;

import se.abalon.cache.KeyToValueObjectCacheIdentifier;

/**
 *
 * A CompositeKeyToValueObjectCacheIdentifier for identifying Composite key to value caches. A Composite key to value cache contains map entries where the key is a composition of two (Composite key [firstKey__secondKey] -> value).
 * <p>
 * The naming of a cache created with a KeyObjectCacheManager is a concatenation of the description of the keys and the description of the values, separated with colon (:). A Composite key seperates the two keys with {@value CompositeKeyToValue#KEY_SEPARATOR}, which then makes out the key in the cache identifier's name
 * <p>

 * @author Mikael Edgren (m.edgren@abalon.se)
 *
 */
public class CompositeKeyToValueObjectCacheIdentifier extends KeyToValueObjectCacheIdentifier {

    public static final String KEY_SEPARATOR = "__";
    /**
     * @param heading
     *            The heading of the cache
     * @param keyDescription
     *            The description of the keys in a cache
     * @param valueDescription
     *            The description of the values in a cache
     */
    public CompositeKeyToValueObjectCacheIdentifier(String heading, String firstKeyDescription, String secondKeyDescription, String valueDescription) {
        super(heading, firstKeyDescription + KEY_SEPARATOR + secondKeyDescription, valueDescription);
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
