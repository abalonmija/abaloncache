package se.abalon.cache.threading;

import javax.jdo.PersistenceManager;

/**
 * Abstract class containing the common methods for an EntityCacheIdentifier.
 *
 * @author Mikael Edgren (m.edgren@abalon.se)
 *
 */
public abstract class AbstractEntityCacheIdentifier implements EntityCacheIdentifier {

    public String modelName;
    public String fieldName;
    public String cacheName;
    private PersistenceManager persistenceManager = null;

    /**
     * Get the persistence manager associated with this cacheIdentifier
     *
     * @return The persistence manager
     */
    public PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    /**
     * Set the persistence manager associated with this cacheIdentifier, in order to enable cache rollback if database transactions fail
     *
     * @return The persistence manager
     */
    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    /**
     * Get the name of the cache that this CacheIdentifier corresponds to
     *
     * @return The cache name as a String
     */
    public String getCacheName() {
        return cacheName;
    }

    /**
     * Get the field name that this CacheIdentifier corresponds to
     *
     * @return The field name as a String
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Get the model name that this CacheIdentifier corresponds to
     *
     * @return The model name as a String
     */
    public String getModelName() {
        return modelName;
    }

    public String toString() {
        return cacheName;
    }

    public boolean equals(Object object) {
        if (object instanceof AbstractEntityCacheIdentifier) {
            AbstractEntityCacheIdentifier abstractCacheIdentifier = (AbstractEntityCacheIdentifier) object;
            if (this.cacheName.equals(abstractCacheIdentifier.getCacheName())) {
                return true;
            }
        }
        return false;
    }
}
