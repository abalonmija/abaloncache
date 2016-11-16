package se.abalon.cache.lang;

/**
 * Class for declaring instances of ResourcesKeys with
 * ResourceParameters. This parameter metadata can during
 * runtime be used to verify that a Resource is properly
 * used.
 *
 * @author Fredrik Hed [f.hed@abalon.se]
 */
public class ResourceParam {
    /**
     * Used for ResourceParams which shall be resolved at runtime.
     */
    private static final int RESOLVABLE = 1;

    /**
     * Used for ResourceParams which shall not be resolved at runtime.
     */
    private static final int PLAIN_VALUE = 2;

    /**
     * Used to declare ResourceKeys with parameters
     * that shall be resolved at runtime
     */
    public static final ResourceParam MODEL_NAME =
            new ResourceParam(RESOLVABLE, "Name of a BOF model to be used as a Resource key during presentation");

    /**
     * Used to declare ResourceKeys with parameters
     * representing a name of a model to resolved at runtime.
     */
    public static final ResourceParam MODEL_FIELD =
            new ResourceParam(RESOLVABLE, "Name of a field for BOF model to be used as a Resource key during presentation");

    /**
     * Used to declare ResourceKeys with parameters
     * representing a variable.
     */
    public static final ResourceParam VARIABLE =
            new ResourceParam(PLAIN_VALUE, "Some object reference. Shall not be resolved during presentation");

    /**
     * Should the parameter be resolved at runtime or not.
     */
    private int resourceType;

    /**
     * The description of the ResourceParam.
     */
    private String resourceDesc;

    /**
     * Created a ResourceParam metadata object.
     *
     * @param type Shall the parameter be resolved at runtime of not
     * @param desc The description of the ResourceParam
     */
    private ResourceParam(final int type, final String desc) {
        resourceType = type;
        resourceDesc = desc;
    }

    /**
     * Is the ResourceParam representing a resolvable value?
     *
     * @return true if the ResourceParam representing a resolvable value
     */
    public boolean isResolvable() {
        return (resourceType == RESOLVABLE);
    }

    /**
     * Is the ResourceParam representing a plain value?
     *
     * @return true if the ResourceParam representing a plain value
     */
    public boolean isPlainValue() {
        return (resourceType == PLAIN_VALUE);
    }

    /**
     * Returns the description of the ResourceParam.
     *
     * @return The description of the ResourceParam
     */
    public String getDescription() {
        return resourceDesc;
    }

    public String toString() {
        return "[ResourceParam { resourceType=" + (resourceType == RESOLVABLE ? "RESOLVABLE" : "PLAIN_VALUE") + " resourceDesc=" + resourceDesc + "}]";
    }
}
