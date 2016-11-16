package se.abalon.cache.loader;


import se.abalon.cache.CacheIdentifier;

/**
 *
 * @author Mikael Edgren (m.edgren@abalon.se)
 *
 */
public class PersistenceManagerCacheEvent {

    static final int PUT = 1;
    static final int ADDED_TO_COLLECTION = 1;
    static final int REMOVED = 2;

    private CacheIdentifier cacheIdentifier;
    private Object key;
    private Object value;
    private Integer event;

    public PersistenceManagerCacheEvent(CacheIdentifier cacheIdentifier, Object key, Object value, Integer event) {
        this.cacheIdentifier = cacheIdentifier;
        this.key = key;
        this.value = value;
        this.event = event;
    }

    /**
     * @return the cacheIdentifier
     */
    public CacheIdentifier getCacheIdentifier() {
        return cacheIdentifier;
    }

    /**
     * @param cacheIdentifier the cacheIdentifier to set
     */
    public void setCacheIdentifier(CacheIdentifier cacheIdentifier) {
        this.cacheIdentifier = cacheIdentifier;
    }

    /**
     * @return the event
     */
    public Integer getEvent() {
        return event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(Integer event) {
        this.event = event;
    }

    /**
     * @return the key
     */
    public Object getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(Object key) {
        this.key = key;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

}
