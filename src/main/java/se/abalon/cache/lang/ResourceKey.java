package se.abalon.cache.lang;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;

import org.apache.log4j.Category;

/**
 * Represents a Resourse key for a resource used in an end user dialog.
 *
 * The ResourseKey can also be used to declare useage of resources.
 *
 * @author Fredrik Hed [f.hed@abalon.se]
 */
public class ResourceKey {

    private static Category log = Category.getInstance(ResourceKey.class);

    /**
     * The resource key.
     */
    private String resourceKey;

    /**
     * One ResourceParam per expected parameter for this resource
     */
    private ResourceParam [] resourceParams = {};

    /**
     * Constructor added for JAXB - Web services
     */
    @SuppressWarnings("unused")
    private ResourceKey() {

    }


    public ResourceKey(String key) {
        resourceKey = key;
    }

    public ResourceKey(String key, ResourceParam [] params) {
        resourceKey = key;
        resourceParams = params;
    }
    /**
     * @return
     */
    public ResourceParam [] getResourceParams() {
        return 	resourceParams;
    }

    /**
     * @return
     */
    public String getResourceKey() {
        return resourceKey;
    }

    /**
     * Translates an arbitrary ResourceKey with some parameters using a specific locale handler and locale.
     * @param messageParams the parameters to merge into the resulting string
     * @param locale the Locale to use
     * @param resourceKey the ResourceKey to translate
     *
     * @return a string translated to the given locale
     */
    public String getLocalizedMessage(Object messageParams [] , Locale locale) {
        String str = null;
        LocaleHandler lh = LocaleHandler.getLocaleHandler(locale);

		/*
		 * resolve the parameters
		 */
        Object [] params = null;
        if (messageParams !=null) {
            params = new Object [messageParams.length];
            for (int i = 0; i < messageParams.length; i++) {
                if (getResourceParams().length <= i) {
                    log.warn("Parameters (" + Arrays.toString(getResourceParams()) + ") for message (" + getResourceKey() + ") dont match declared number of params (" + getResourceParams().length + ")");
                }
                else if (getResourceParams()[i].isResolvable()){
                    if (messageParams[i] != null) {
                        params[i] = lh.getString(messageParams[i].toString());
                    }
                    else {
                        params[i] = "";
                    }
                }
                else {
                    params[i] = messageParams[i];
                }
            }
        }

		/*
		 * resolve the resource key and generate output
		 */
        if (lh.isResolvable(resourceKey)) {
            str = lh.getString(resourceKey);

            if (messageParams !=null) {
				/*
				 * merge params with the message
				 */
                return MessageFormat.format(str, params);
            }
		    /*
		     * no params - no merge needed
		     */
            return str;
        }

		/*
		 * return a raw - non translated string for the message
		 */
        if (params != null) {
            return "[" + getResourceKey() + "] " + Arrays.asList(params).toString();
        }
        return "[" + getResourceKey() + "]";
    }


    public String toString() {
        if (resourceKey != null) {
            return resourceKey;
        }
        return "";
    }
}
