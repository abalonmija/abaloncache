package se.abalon.cache.loader;

/**
 * The purpose of a CacheLoader is to execute queries and put values from the result sets in the Mayflower cache, for caches that are supposed to be available at all time in the JVM.
 * 
 * @author Mikael Edgren (m.edgren@abalon.se)
 * 
 */
public interface CacheLoader extends Runnable {

 	public void load() throws Exception;
 	
}

