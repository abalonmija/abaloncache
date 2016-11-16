package se.abalon.cache.loader;

import se.abalon.cache.threading.KeyToValueObjectCacheIdentifier;

public class PARB2CCacheLoader extends AbstractCacheLoader {

	private static MayflowerCacher cacher;

	public PARB2CCacheLoader() throws Exception {
		cacher = MayflowerCacher.getMayflowerCacher();
	}

	public void load() throws Exception {
		setLoaded(false);
		try {
			createEmptyPARB2CDownloadedCache();
		} catch (Exception e) {
			throw e;
		}
		setLoaded(true);

	}

	private static void createEmptyPARB2CDownloadedCache() {
		try {
			KeyToValueObjectCacheIdentifier cacheIdentifier = new KeyToValueObjectCacheIdentifier("PARB2C_Downloaded", "ORGNBR", "PARB2CResponse");
			cacher.createCache(cacheIdentifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
