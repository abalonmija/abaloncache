package se.abalon.cache.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

/**
 * The class is used to structure arbitrary ResourceProviders in some tree
 * structure for looking up Resources. The declared instance of the
 * CompleResourceProvider can be stored in the LocalThread, to make the
 * initialization and usage of the ComplexResourceProvider more separated
 * <p>
 * Principal usage of the class:
 * <ul>
 * <li>The main program or servlet stores the definition of the current threads
 * usage of this class (one reqest in a webserver is allways executed by one
 * thread).</li>
 * <li> declare the resources you want to use in a <code>getConfigurations</code> method</li>
 * <li> access the resource with the lookup method</li>
 * </ul>
 * <p>
 * Code example:
 * <p>
 * Your servlet/main class does instanciate the resource provider for this thread.
 * This is done only once per request.
 * <p>
 * <code>
 List defaultResource = new ArrayList();<br>
 List userResource = new ArrayList();<br>
 List params = new ArrayList();<br>
 // first load default<br>
 defaultResource.add("resource");<br>
 defaultResource.add("system");<br>
 defaultResource.add("default");<br>
 // then the user specific<br>
 userResource.add("resource");<br>
 userResource.add("user");<br>
 userResource.add("testperson");<br>
 params.add(userResource);<br>
 params.add(defaultResource);<br>
 // This sets the configuration for the <u>LOCAL THREAD</u>.<br>
 ComplexResourceProvider.set(params);<br>
 * </code>
 * <p>
 * Declare a getConfigurations method with all in your class.
 * <p>
 * <code>public static ResourceMap getConfigurations() {<br>
 * &nbsp;&nbsp;&nbsp;ResourceMap map = new ResourceMap();<br>
 * &nbsp;&nbsp;&nbsp;map.put("KEY_A", new Resource ("KEY_A", String.class, "A Demo key"));<br>
 * &nbsp;&nbsp;&nbsp;map.put("KEY_B", String.class, "An other Demo key");<br>
 * &nbsp;&nbsp;&nbsp;return map;<br>
 * }</code>
 * <p>
 * And use the lookup method on the ResouceMap
 * <p>
 * <code>String str = (String)getConfigurations().lookup("KEY_A");</code>
 * <p>
 * @author Fredrik Hed [f.hed@abalon.se]
 */
public class ComplexResourceProvider implements ResourceProvider {
    /**
     * The logger to use
     */
    private static Category log = Category.getInstance(ComplexResourceProvider.class);
    /**
     * The cache of instanciated ComplexResourceProvider. The key in the HashMap
     * is the initparameter to the <code>getInstance</code> method.
     */
    private static Map<List<Object>, ComplexResourceProvider> complextProviderCache = new HashMap<List<Object>, ComplexResourceProvider>();
    /**
     * The storage of the initparameter of the current thread. The List is stored
     * in the ThreadLocal context.
     */
    private static List<Object> staticParam = null;

    /**
     * The list of ResourceProviders for a specific instance of the
     * ComplexResourceProvider
     */
    private List<ResourceProvider> providers = new ArrayList<ResourceProvider>();


    /**
     * Method to fetch data stored in the 'resourceParam' parameter in the
     * ThreadLocal context.
     *
     * This implementation does not fetch the List from the thread local. It
     * fetches the List from a static context. See set(...).
     *
     * @return the current init parameter for the current thread
     */
    public static List<Object> get() {
        return staticParam;
    }

    /**
     * Method to store initparameter in the 'resourceParam' parameter in the
     * ThreadLocal context.
     *
     * This implementation does not store the List in the thread local. It
     * stores it in a static context.
     *
     * @param param the initparameter to store
     */
    public static void set(List<Object> param) {
        if (param == null) {
            log.warn("ComplexResourceProvider.set() called with null parameter");
        }
        staticParam = param;
    }

    /**
     * This method fetches a ComplexResourceProvider from cache or creates a new
     * one.
     * @param initparam A list of Lists with initparameters for
     *     ResourceProviders. The format is like this.
     *     A List of <code>[Type, List of initparamters]</code>
     *     one for each ResourceProvider to instansiate
     * @return an instance of a ComplexResourceProvider
     * @throws ResourceProviderException if the instanciation fails
     */
    @SuppressWarnings("unchecked")
    public static ResourceProvider getInstance(List<Object> initparam) throws ResourceProviderException {
        if (initparam == null) {
            throw new ResourceProviderException("No valid initparam (null) to getInstance.", ResourceProviderException.CONFIGURATION_ERROR);
        }

        if (complextProviderCache.containsKey(initparam)) {
            return complextProviderCache.get(initparam);
        }

        List<ResourceProvider> providers = new ArrayList<ResourceProvider>();
        Iterator<Object> iterator = initparam.iterator();
        String type;

        // fetching Provider type - COMPLEX
        if (iterator.hasNext()) {
            try {
                type  = (String)iterator.next();
                if (!type.equalsIgnoreCase("COMPLEX")) {
                    throw new ResourceProviderException	("Expected to find string with COMPLEX", ResourceProviderException.CONFIGURATION_ERROR);
                }
            }
            catch (ClassCastException cce) {
                throw new ResourceProviderException	("Expected to find string with COMPLEX, but found " + cce.getMessage() + " instead", ResourceProviderException.CONFIGURATION_ERROR);
            }
        }


        // for each list of ResourceProvider parameters ...
        while (iterator.hasNext()) {
            List<Object> aItem = (List<Object>)iterator.next();
            log.debug("Recursive creating ResourceProvider (" + aItem.toString() + ")");
            providers.add(ResourceProviderFactory.getInstance(aItem));
        }

        ComplexResourceProvider p = new ComplexResourceProvider(providers);
        complextProviderCache.put(initparam, p);
        return p;
    }

    /**
     * Method to use if initparameters are set with the <code>ComplexResourceProvider.set()</code>
     * method.
     * @return an instance of a ComplexResourceProvider
     * @throws ResourceProviderException if the instanciation fails
     */
    public static ResourceProvider getInstance() throws ResourceProviderException {
        List<Object> param = get();
        return getInstance(param);
    }

    /**
     * Private constructor - used by getInstance if no cached
     * ComplexResourceProvider exists
     * @param p a list of references to instanciated ResourceProviders
     */
    private ComplexResourceProvider(List<ResourceProvider> p) {
        providers = p;
    }

    /**
     * Method to recursively do lookup on the ResourceProviders for the instance of
     * this object. The first hit for the key is returned and the execution of the
     * method ends.
     * @param rs The resource to lookup
     * @return the value matching the resource
     * @throws ResourceProviderException if the resource is not found of an error in
     *      the fetching of ResourceProivider occurs
     */
    public Object lookup(Resource rs) throws ResourceProviderException {
        Iterator<ResourceProvider> iterator = providers.iterator();

        while (iterator.hasNext()) {
            ResourceProvider p = iterator.next();
            try {
                Object o = p.lookup(rs);
                if (o != null) {
                    return o;
                }
            }
            catch (ResourceProviderException re) {
                if (re.getType() == ResourceProviderException.RESOURE_FORMAT_ERROR) {
                    throw re;
                }
            }
        }
        throw new ResourceProviderException("Could not lookup resource: no such key (" + rs.getName() + ")", ResourceProviderException.NO_SUCH_RESOURCE);
    }

    /**
     * Method to recursively do lookup on only the PropertyResourceProviders for the instance of
     * this object. The first hit for the key is returned and the execution of the
     * method ends.
     * @param rs The resource to lookup
     * @return the value matching the resource
     * @throws ResourceProviderException if the resource is not found of an error in
     *      the fetching of ResourceProivider occurs
     */
    public Object lookupOnlyPropertyResourceNoCache(Resource rs) throws ResourceProviderException {
        Iterator<ResourceProvider> iterator = providers.iterator();

        while (iterator.hasNext()) {
            ResourceProvider p = iterator.next();
            if(p instanceof PropertyResourceProvider) {
                PropertyResourceProvider prp = (PropertyResourceProvider) p;
                try {
                    Object o = prp.lookup(rs);
                    if (o != null) {
                        return o;
                    }
                }
                catch (ResourceProviderException re) {
                    if (re.getType() == ResourceProviderException.RESOURE_FORMAT_ERROR) {
                        throw re;
                    }
                }
            }
        }
        throw new ResourceProviderException("Could not lookup resource: no such key (" + rs.getName() + ")", ResourceProviderException.NO_SUCH_RESOURCE);
    }

    public String toString() {
        return "ComplexResourceProvider{providers="+ this.providers.toString() + "}";
    }
}

