package se.abalon.cache.threading;

import se.abalon.cache.AbstractEntityCacheIdentifier;

import javax.jdo.PersistenceManager;

/**
 *
 * A KeyToValueEntityCacheIdentifier for identifying key to value caches. A key to value cache contains map entries (Key -> value).
 * <p>
 * The naming of a cache created with a KeyEntityCacheManager is a concatenation of the key's entity's name (modelName) and the field's name (fieldName), and the value's entity's name (modelName) and the field's name (fieldName), separated with colon (:)
 * <p>
 *
 * @author Mikael Edgren (m.edgren@abalon.se)
 *
 */
public class KeyToValueEntityCacheIdentifier extends AbstractEntityCacheIdentifier {

    private String valueModelName;
    private String valueFieldName;

    /**
     * @param modelName
     *            The name of the model that the key belongs to
     * @param fieldName
     *            The name of the field on the model that the key belongs to
     * @param valueModelName
     *            The name of the model that the value belongs to
     * @param valueFieldName
     *            The name of the field on the model that the value belongs to
     */
    public KeyToValueEntityCacheIdentifier(String modelName, String fieldName, String valueModelName, String valueFieldName) {
        super.modelName = modelName;
        super.fieldName = fieldName;
        this.valueModelName = valueModelName;
        this.valueFieldName = valueFieldName;
        setCacheName();
    }

    public boolean equals(String modelName, String fieldName, String valueModelName, String valueFieldName) {
        if (cacheName.equals(modelName + ":" + fieldName + ":" + valueModelName + ":" + valueFieldName)) {
            return true;
        }
        return false;
    }

    /**
     * Set the field name for the value in the cache that this CacheIdentifier corresponds to
     *
     * @return The field name as a String
     */
    public String getValueFieldName() {
        return valueFieldName;
    }

    /**
     * Get the model name for the value in the cache that this CacheIdentifier corresponds to
     *
     * @return The model name as a String
     */
    public String getValueModelName() {
        return valueModelName;
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

    /**
     * Set the field name for the value in the cache that this CacheIdentifier corresponds to
     *
     * @param fieldDescription
     */
    public void setValueFieldName(String valueFieldName) {
        this.valueFieldName = valueFieldName;
        setCacheName();
    }

    /**
     * Set the model name for the value in the cache that this CacheIdentifier corresponds to
     *
     * @param keyName
     */
    public void setValueModelName(String valueModelName) {
        this.valueModelName = valueModelName;
        setCacheName();
    }

    public void setCacheName() {
        cacheName = getModelName() + ":" + getFieldName() + ":" + getValueModelName() + ":" + getValueFieldName();
    }

    public Boolean equals(String modelName, String fieldName) {
        if (cacheName.equals(modelName + ":" + fieldName)) {
            return true;
        }
        return false;
    }

}
