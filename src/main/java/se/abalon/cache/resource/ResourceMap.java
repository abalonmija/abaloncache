package se.abalon.cache.resource;

import se.abalon.cache.exception.ResourceProviderException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A map of resources that have the abitity to lookup a specified Resource
 * within the map.
 *
 * @author Fredrik Hed [f.hed@abalon.se]
 */
public class ResourceMap {
    private Map<String, Resource> map = new HashMap<String, Resource>();

    /**
     * Method to get a resource with a specified key.
     * @param key the key of the resource to get
     * @return the Resouce coresponding to the key
     */
    public Resource get(String key) {
        return map.get(key);
    }

    /**
     * Method to add a Resouce to the ResourceMap
     * @param key the key to use for the resouce
     * @param resource the resouce to bind to the ResourceMap
     */
    public void put(String key, Resource resource) {
        map.put(key, resource);
    }

    /**
     * Method th add a resouce to the ResouceMap
     * @param key the key to use
     * @param aClass the class of the resouce
     * @param description the description to of the resouce
     */
    public void put(String key, Class<?> aClass, String description) {
        map.put(key, new Resource (key, aClass, description));
    }

    /**
     * Metod to lookup the value of the resouce. The method uses the Resource.lookup method
     * @param key the key to lookup
     * @return the value of the resource
     * @throws ResourceProviderException if the key dont exist withing the ResourceProvider or if an error occures in the initialisation proces of the ResourceProvider.
     *      See Resouce.lookup
     */
    public Object lookup(String key) throws ResourceProviderException {
        if (map.containsKey(key)) {
            return map.get(key).lookup();
        }
        throw new ResourceProviderException("No such key declared in ResourceMap (" + key + ")", ResourceProviderException.CONFIGURATION_ERROR);
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public Set<String> keySet() {
        return map.keySet();
    }


}

