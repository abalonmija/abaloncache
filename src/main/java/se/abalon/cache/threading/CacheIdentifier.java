package se.abalon.cache.threading;

/**
 * Interface for identifying caches.
 * <p>
 * The cache name in the CacheIdentifier is used for naming new caches when they are created and for identifying exisitng caches.
 * <p>
 *
 * @author Mikael Edgren (m.edgren@abalon.se)
 *
 */
public interface CacheIdentifier {

    /**
     * Get the name of the cache that this CacheIdentifier corresponds to
     *
     * @return The cache name as a String
     */
    public String getCacheName();

    /**
     * Sets the cache's name based on its fields
     */
    public void setCacheName();

}
