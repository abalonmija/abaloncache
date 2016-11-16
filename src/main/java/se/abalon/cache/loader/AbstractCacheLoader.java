package se.abalon.cache.loader;

/**
 * By extending this class the cache loading will be able to be run in separate thread. The MainCacheLoader contains a list of CacheLoaders extending this abstract class, all of which will be executed in parallel threads when the MainCacheLoader's load() is executed.
 * <p>
 * Note that the CacheLoaders need to be manually added to the list in the MainCacheLoader.
 * 
 * @author Mikael Edgren (m.edgren@abalon.se)
 * 
 */
public abstract class
AbstractCacheLoader implements CacheLoader {

	private Long timeBefore = null;

	public AbstractCacheLoader() {
		timeBefore = System.currentTimeMillis();
	}

	public void run() {
		try {
			load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setLoaded(Boolean isLoaded) {
		MainCacheLoader.setCacheLoaderStatus(getClass().getName(), isLoaded);
		if (isLoaded) {
			System.out.println("::MAYFLOWER CACHE:: " + getClass().getSimpleName() + " loaded in " + (System.currentTimeMillis() - timeBefore) / 1000 + " seconds");
		}
	}

}
