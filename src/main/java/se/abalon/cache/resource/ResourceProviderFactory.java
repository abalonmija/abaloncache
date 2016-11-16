package se.abalon.cache.resource;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;

/**
 * Factory to get instances of ResourceProviders.
 *
 * @author Fredrik Hed [f.hed@abalon.se]
 */
public class ResourceProviderFactory {
    static private Category log = Category.getInstance(ResourceProviderFactory.class);

    /**
     * Factory method to get instances of ResourceProviders.
     * @param type the type of ResourceProvider to instantiate. Supported types
     *      are: PROPERY and COMPLEX. The default Provider returned are the
     *      ComplexResourceProvider instantiated from the LocalThread.
     *      See ComplexResourceProvider.
     * @param initparams init parameters to the underlying ResourceProvider
     * @return an instance of the ResourceProvider
     * @throws ResourceProviderException various errors could cause an exception like this
     */
    public static ResourceProvider getInstance(List<Object> initparams) throws ResourceProviderException {

        // get reference to first element in list...
        Iterator<Object> itorParams = initparams.iterator();
        String type = (String)itorParams.next();

        if (type.equalsIgnoreCase("PROPERTY")) {
            String path = (String)itorParams.next();
            log.debug("Reference PROPERTY provider path (" + path + ")");
            return PropertyResourceProvider.getInstance(path);
        }
        else if (type.equalsIgnoreCase("COMPLEX")) {
            log.info("Reference COMPLEX provider (" + initparams + ")");
            return ComplexResourceProvider.getInstance(initparams);
        }
        else {
            log.info("Reference to Complex provider fetching params from ThreadLocal");
            return ComplexResourceProvider.getInstance();
        }
    }
}

