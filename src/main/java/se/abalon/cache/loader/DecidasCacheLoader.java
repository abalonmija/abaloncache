package se.abalon.cache.loader;

import se.abalon.cache.threading.KeyObjectCacheIdentifier;
import se.abalon.cache.threading.KeyToValueObjectCacheIdentifier;

public class DecidasCacheLoader{// extends AbstractCacheLoader {

	private static MayflowerCacher cacher;

	public DecidasCacheLoader() throws Exception {
		cacher = MayflowerCacher.getMayflowerCacher();
	}

	public void load() throws Exception {
		/*setLoaded(false);
		try {
			createEmptyDecidasPersonCaches();
		} catch (Exception e) {
			throw e;
		}
		setLoaded(true);
*/
	}
/*
	private static void createEmptyDecidasPersonCaches() {
		try {
			KeyToValueObjectCacheIdentifier cacheIdentifier = new KeyToValueObjectCacheIdentifier("Decidas_PersonSearch", "ORGNBR", "DecidasPerson");
			cacher.createCache(cacheIdentifier);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String heading = "DecidasHandlePerson";
		String description = "ORGNBR";
		try {
			KeyObjectCacheIdentifier cacheIdentifier = new KeyObjectCacheIdentifier(heading, description);
			cacher.createCache(cacheIdentifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
}
