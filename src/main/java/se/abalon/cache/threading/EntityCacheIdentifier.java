package se.abalon.cache.threading;

import se.abalon.cache.CacheIdentifier;

import javax.jdo.PersistenceManager;

/**
 * Interface for identifying Mayflower entity caches.
 * <p>
 * An EntityCacheIdentifier is used to name and identify caches storing Mayflower entity models and/or values. NOTE! Caches created with an EntityCacheManager will be subject of generic and automatic cache updates via the entites BusinessRules. If this behaviour is not wanted an
 * ObjectCacheIdentifier should be used instead.
 * <p>
 * An EntityCacheIdentifier can be associated with a PersistenceManager. If it is, the cache will rollback if the manager's transactions fail
 *
 * @author Mikael Edgren (m.edgren@abalon.se)
 *
 */
public interface EntityCacheIdentifier extends CacheIdentifier {

    /**
     * Get the persistence manager associated with this EntityCacheIdentifier
     *
     * @return The persistence manager
     */
    public PersistenceManager getPersistenceManager();

    /**
     * Set the persitence manager associated with this EntityCacheIdentifier, in order to enable cache rollback if database transactions fail
     *
     * @return The persistence manager
     */
    public void setPersistenceManager(PersistenceManager persistenceManager);

    /**
     * Get the field name that this CacheIdentifier corresponds to
     *
     * @return
     */
    public String getFieldName();

    /**
     * Get the model name that this EntityCacheIdentifier corresponds to
     *
     * @return The model name as a String
     */
    public String getModelName();

    /**
     * Set the field name that this EntityCacheIdentifier corresponds to
     *
     * @param fieldName
     */
    public void setFieldName(String keyFieldName);

    /**
     * Set the model name that this EntityCacheIdentifier corresponds to
     *
     * @param modelName
     */
    public void setModelName(String modelName);

}
