package se.abalon.cache.threading;

import se.abalon.cache.threading.CacheIdentifier;

/**
 * Interface for identifying Mayflower java object caches.
 * <p>
 * An ObjectCacheIdentifier is used to name and identify caches containing java object that are not subject to the generic cache updates via Mayflower entities' business rules
 *
 * @author Mikael Edgren (m.edgren@abalon.se)
 *
 */
public interface ObjectCacheIdentifier extends CacheIdentifier {

    /**
     * Get the key description that this ObjectCacheIdentifier< corresponds to
     *
     * @return The key description as a String
     */
    public String getKeyDescription();

    /**
     * Set the key description that this ObjectCacheIdentifier< corresponds to
     *
     * @param keyDescription
     */
    public void setKeyDescription(String keyDescription);

}
