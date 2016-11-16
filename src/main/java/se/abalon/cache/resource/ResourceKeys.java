package se.abalon.cache.resource;

import se.abalon.cache.lang.ResourceKey;
import se.abalon.cache.lang.ResourceParam;

/**
 * Resources declared to the se.abalon.rule component.
 *
 * @author Fredrik Hed [f.hed@abalon.se]
 */
public class ResourceKeys {

    private static final ResourceParam[] VAR = {ResourceParam.VARIABLE};
    private static final ResourceParam [] VAR_VAR = {ResourceParam.VARIABLE, ResourceParam.VARIABLE};
    private static final ResourceParam [] VAR_VAR_VAR = {ResourceParam.VARIABLE, ResourceParam.VARIABLE, ResourceParam.VARIABLE};
    private static final ResourceParam [] EMPTY = {};

    public static final ResourceKey ACTION_CREATION_ERROR = new ResourceKey("rule.ACTION_CREATION_ERROR", VAR);
    public static final ResourceKey CLASS_CREATION_ERROR = new ResourceKey("rule.CLASS_CREATION_ERROR", VAR);
    public static final ResourceKey CUSTOM_ERROR_MESSAGE = new ResourceKey("rule.CUSTOM_ERROR_MESSAGE", VAR);
    /**
     * Used for high lever descriptions for exeptions thrown by Business Rules
     *  - Rule name
     *  - Context ("Model", "Query", "Transition")
     *  - The "instance of the context "Action", oql...
     */
    public static final ResourceKey ERROR_EXECUTING_RULE = new ResourceKey("rule.ERROR_EXECUTING_RULE", VAR_VAR_VAR);
    public static final ResourceKey MISSING_INPUT_PARAMS = new ResourceKey("rule.MISSING_INPUT_PARAMS", VAR);
    public static final ResourceKey MISSING_OUTPUT_PARAMS = new ResourceKey("rule.MISSING_OUTPUT_PARAMS", VAR);
    public static final ResourceKey MISSING_VALUEPROVIDER = new ResourceKey("rule.MISSING_VALUEPROVIDER");
    public static final ResourceKey INVALID_PARAMETER_COMBINATION = new ResourceKey("bof.INVALID_PARAMETER_COMBINATION", VAR);

    public static final ResourceKey CONVERSION_ERROR_EXPECTED_KEY_AND_VALUE = new ResourceKey("bof.CONVERSION_ERROR_EXPECTED_KEY_AND_VALUE", VAR);
    public static final ResourceKey CONVERSION_ERROR_EXPECTED_CHAR_BUT_FOUND = new ResourceKey("bof.CONVERSION_ERROR_EXPECTED_CHAR_BUT_FOUND", VAR_VAR);
    public static final ResourceKey CONFIGURATION_MISSING = new ResourceKey("common.CONFIGURATION_MISSING", VAR);
    public static final ResourceKey UNEXPECTED_EXCEPTION = new ResourceKey("common.UNEXPECTED_EXCEPTION", VAR);

    /**
     * Use this key when a resource is missing from a property file.
     * - the name of the missing resource key
     */
    public static final ResourceKey MISSING_RESOURCE = new ResourceKey("resource.MISSING_RESOURCE", VAR);

    /**
     * the resource is defined but the value does not match the resource
     */
    public static final ResourceKey MISSING_RESOURCE_VALUE = new ResourceKey("resource.MISSING_RESOURCE_VALUE", VAR);
    /**
     * Use key to indicate that a lookup of a context variable failed.
     * Arguments:
     * One string (e.g. comma sepatated) with all the context keys that might have caused the lookup to fail.
     */
    public static final ResourceKey CONTEXT_LOOKUP_ERROR = new ResourceKey("common.CONTEXT_LOOKUP_ERROR", ResourceKeys.VAR);

    /**
     * Error resolving parameter from xxx
     */
    public static final ResourceKey PARAMETER_MISSING_IN_SPROCKET = new ResourceKey("common.PARAMETER_MISSING_IN_SPROCKET", ResourceKeys.VAR_VAR);

}
