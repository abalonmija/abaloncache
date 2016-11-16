package se.abalon.cache.threading;

import se.abalon.cache.threading.AbstractEntityCacheIdentifier;

/**
 *
 * A KeyEntityCacheIdentifier for identifying key caches created with EntityCacheIdentifiers. A key cache holds only a key and no value per entry in the cache.
 * <p>
 * The naming of a cache created with a KeyEntityCacheManager is a concatenation of the entity's name (modelName) and the field's name (fieldName), separated with colon (:)
 * <p>
 *
 * @author Mikael Edgren (m.edgren@abalon.se)
 *
 */
public class KeyEntityCacheIdentifier extends AbstractEntityCacheIdentifier {

    /**
     * @param modelName
     *            The name of the model that the key belongs to
     * @param fieldName
     *            The name of the field on the model that the key belongs to
     */
    public KeyEntityCacheIdentifier(String modelName, String fieldName) {
        super.modelName = modelName;
        super.fieldName = fieldName;
        setCacheName();
    }

    /**
     * Set the field name that this CacheIdentifier corresponds to
     *
     * @param fieldName
     */
    public void setFieldName(String fieldName) {
        super.fieldName = fieldName;
        setCacheName();
    }

    /**
     * Set the model name that this CacheIdentifier corresponds to
     *
     * @param modelName
     */
    public void setModelName(String modelName) {
        super.modelName = modelName;
        setCacheName();
    }

    public void setCacheName() {
        cacheName = getModelName() + ":" + getFieldName();
    }

    public Boolean equals(String modelName, String fieldName) {
        if (cacheName.equals(modelName + ":" + fieldName)) {
            return true;
        }
        return false;
    }

}
