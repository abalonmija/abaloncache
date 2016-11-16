package se.abalon.cache.loader;

import org.apache.log4j.Category;
import se.abalon.cache.exception.ActionException;
import se.abalon.cache.resource.ResourceKeys;

/**
 * @author mied
 * 
 */
public class ReloadCaches {

	private Category log = Category.getInstance(ReloadCaches.class);

	public void execute() throws ActionException {
		doReload();
	}

	public static void doReload() throws ActionException {
		try {
			MayflowerCacher cacher = MayflowerCacher.getMayflowerCacher();
			cacher.emptyAllCaches();
			MainCacheLoader cacheLoader = MainCacheLoader.getMainCacheLoader();
			cacheLoader.setAllCacheLoaderStatus(false);
			cacheLoader.load();

		} catch (Exception e) {
			throw new ActionException(ResourceKeys.CUSTOM_ERROR_MESSAGE, new Object[] { "An error occured while reloading the Mayflower Cache", e });
		}
	}
}