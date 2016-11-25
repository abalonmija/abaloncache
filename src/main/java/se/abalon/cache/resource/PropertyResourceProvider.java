package se.abalon.cache.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.Category;
import se.abalon.cache.exception.FormatException;
import se.abalon.cache.exception.ResourceProviderException;
import se.abalon.cache.util.StringUtil;

/**
 * Implementation of a ResourceProvider useing standard java property files as
 * repository for the properties. The Provider has a cache for minimizing the
 * number of objects instansiated.
 * <p>
 * The PropertyResourceProvider acts as a ResourceProvider for a single .property
 * file.
 * <p>
 * Recomended Usage of PropertyResourceProvider is with conjunction with the
 * ComplexResourceProvider. See ComplexResourceProvider.
 * <p>
 * Working but not recomended Usage:
 * <p>
 * <code>
 // this will identify the a .properties files int the class path
 List param = new ArrayList();<br>
 param.add("resource");<br>
 param.add("user");<br>
 param.add("testperson");<br>
 <br>
 ResourceProvider p = null;<br>
 String str;<br>
 <br>
 // lets fetch a PropertyResourceProvider identified with the parameter ArrayList declared<br>
 try {<br>
 &nbsp;&nbsp;&nbsp;p = ResourceProviderFactory.getInstance("PROPERTY", param);<br>
 &nbsp;&nbsp;&nbsp;str = (String)p.lookup(new Resource("KEY_C", String.class, "some desciption"));<br>
 }<br>
 catch (ResourceProviderException ex) {<br>
 &nbsp;&nbsp;&nbsp;...<br>
 }<br>
 </code>
 * <p>
 * Futher development idees:
 * <p>
 * The class (and the ResourceProvider interface as well) could be extended with
 * a number of specialized lookup methods to return type checked return values,
 * e.g lookupString, lookupInt...
 * @author Fredrik Hed [f.hed@abalon.se]
 */
public class PropertyResourceProvider implements ResourceProvider {
    private static Category log = Category.getInstance(PropertyResourceProvider.class);
    private static Map<String, ResourceProvider> resourceProviderCache = new HashMap<String, ResourceProvider>(); // the cache ...
    private String resourcePath;
    private ResourceBundle bundle;

    private PropertyResourceProvider(String path, ResourceBundle bundle){
        resourcePath = path;
        this.bundle = bundle;
    }

    /**
     * Method to retrieve a ResourceProvider. If the provider is instanciated and
     * in the cache the method returns the cached provider, otherwise it creates
     * the provider, stores in the cache and returns it.
     * @param instanciationParameters a List identifying the ResourceProvider like [user, selladmin] or [site, tnt-europa]
     * @return a PresourceProvider matching the instanciationParameters
     * @throws ResourceProviderException if the instanciationParameters is not corresponds to a underlying repository
     */
    public static ResourceProvider getInstance(String path) throws ResourceProviderException {
        if (path == null) {
            throw new ResourceProviderException("No valid instantiation paramters to ResourceProvider", ResourceProviderException.CONFIGURATION_ERROR);
        }

        if (resourceProviderCache.containsKey(path)) {
            return resourceProviderCache.get(path);
        }

        ResourceBundle rb = null;
        try {
            if(path != null && path.startsWith("propertyfile") ){
                rb = loadFromFile(path);
            } else {
                rb = ResourceBundle.getBundle(path);
            }
        }
        catch (MissingResourceException mre) {
            throw new ResourceProviderException("Could not create ResourceProvider (" + path + "). " + mre.getMessage(), ResourceProviderException.CONFIGURATION_ERROR);
        }

        ResourceProvider rp = new PropertyResourceProvider(path, rb);
        resourceProviderCache.put(path, rp);

        return rp;
    }

    private static ResourceBundle loadFromFile(String prop){
        String path = null;
        ResourceBundle bundle = null;

        path = System.getenv(prop);

        System.out.println("PropertyResourceProvider: prop = " + prop + ", path = " + path);

        if(path != null){
            try {

                File file = new File(path);
                if (file.exists()) {
                    bundle = new PropertyResourceBundle(new FileInputStream(path));
                }

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return bundle;
    }


    /**
     * Method to fetch the value of the resource from the current ProprtyResourceProvider.
     * @param rs The resource to lookup
     * @return the value of the resoucse
     * @throws ResourceProviderException if the resouce dont exist within the ResouceProvider
     */
    public Object lookup(Resource rs) throws ResourceProviderException {
        try {
            Object o = null;
            if (bundle instanceof PropertyResourceBundle) {

                // Use value from java -D parameters if the cache didn't wield anything
                if (o == null) {
                    if (System.getProperties().containsKey(rs.getName())) {
                        //log.debug("Using value from java -D" + rs.getName() + ", and not from property file");
                        return System.getProperty(rs.getName());
                    }
                }


                if(o == null) {
                    o = ((PropertyResourceBundle)bundle).handleGetObject(rs.getName());
                }
            }

            if (o == null) {
                return null;
            }

            if (rs.getType().equals(Boolean.class)) {
                return Boolean.valueOf(o.toString());
            }
            else
            if (rs.getType().equals(String.class)) {
                return o.toString();
            }
            else
            if (rs.getType().equals(Map.class)) {
                return StringUtil.stringToMap(o.toString());
            }
            else
            if (rs.getType().equals(List.class) ) {
                return StringUtil.stringToList(o.toString());
            }
            throw new ResourceProviderException("Unsupported type (class). PropertyResourceProvider doesn't support the use of '" + rs.getType() + "'", ResourceProviderException.RESOURE_FORMAT_ERROR);
        }
        catch (FormatException e) {
            log.debug("Format error on resource (" + rs.getName() + "), bundle (" + resourcePath + ") " + e.getMessage());
            throw new ResourceProviderException("Format error on resource (" + rs.getName() + "), bundle (" + resourcePath + ") " + e.getMessage(), ResourceProviderException.RESOURE_FORMAT_ERROR);
        }
        catch (Exception e) {
            log.info("Key (" + rs.getName() + ") not found in bundle (" + resourcePath + ")");
            throw new ResourceProviderException("Key (" + rs.getName() + ") not found in bundle (" + resourcePath + ")", ResourceProviderException.NO_SUCH_RESOURCE);
        }
    }

    public String toString() {
        return "PropertyResourceProvider{path=" + this.resourcePath + ", bundle=" + bundle.toString() + "}";
    }
}

