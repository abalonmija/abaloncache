package se.abalon.cache.loader;

import se.abalon.cache.threading.KeyEntityCacheIdentifier;

public class PersonCacheLoader extends AbstractCacheLoader {

    private static MayflowerCacher cacher;

    public PersonCacheLoader() throws Exception {
        cacher = MayflowerCacher.getMayflowerCacher();
    }

    public void load() throws Exception {
        setLoaded(false);
        try {
            createEmptyOrgnrPkToLimitedCache();
        } catch (Exception e) {
            throw e;
        }
        setLoaded(true);

    }

    private static void createEmptyOrgnrPkToLimitedCache() {
        String modelName = "Person";
        String fieldName = "ORGNBR";
        try {
            KeyEntityCacheIdentifier cacheIdentifier = new KeyEntityCacheIdentifier(modelName, fieldName);
            cacher.createLimitedCache(cacheIdentifier, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
