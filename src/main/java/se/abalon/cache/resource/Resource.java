package se.abalon.cache.resource;

import se.abalon.cache.exception.ResourceProviderException;

/**
 * A definition of a resource as well as the object to do "lookup" upon to do the
 * actual fetching of the values of the resource.
 *
 * @author Fredrik Hed [f.hed@abalon.se]
 */
public class Resource {
    private String name;
    private Class<?> theClass;
    private String description;

    public Resource (String aName, Class<?> aClass, String aDescription) {
        name = aName;
        theClass = aClass;
        description = aDescription;
    }

    public String getDescription() { return description;}

    public Class<?> getType() { return theClass;}

    public String getName() { return name;}


    /**
     * Method to fetch the value of the resource. The method asumes that the
     * ComplexResourceProvider is properly initiated by some main class, serlvet or
     * equivalent.
     *
     * @return the value of the resoucer
     * @throws ResourceProviderException if the ComplexResourceProvider is not properly
     *      initiated or if the value of the Resource does not exist.
     */
    public Object lookup() throws ResourceProviderException {
        ResourceProvider provider = ComplexResourceProvider.getInstance();
        Object theResource = null;
        theResource = provider.lookup(this); // throws ResourceProviderException
        return theResource;
    }
}

