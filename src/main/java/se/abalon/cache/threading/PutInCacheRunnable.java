package se.abalon.cache.threading;


import se.abalon.cache.loader.MayflowerCacher;

public class PutInCacheRunnable implements Runnable {

    private CacheIdentifier cacheIdentifier;
    private Object inputKey;
    private Object inputValue;

    public PutInCacheRunnable(CacheIdentifier cacheIdentifier, Object key, Object value) {
        this.cacheIdentifier = cacheIdentifier;
        this.inputKey = key;
        this.inputValue = value;
    }

    public void run() {
        try {
            MayflowerCacher cacher = MayflowerCacher.getMayflowerCacher();
            cacher.doPut(cacheIdentifier, inputKey, inputValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
