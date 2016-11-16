package se.abalon.cache.lang;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.log4j.Category;
import se.abalon.cache.exception.ConfigurationException;
import se.abalon.cache.exception.ResourceProviderException;
import se.abalon.cache.resource.ResourceKeys;
import se.abalon.cache.resource.ResourceMap;

/**
 * Accesspoint to the for resolving ResourceKeys using language
 * resources. Exactly which resources loaded are dependent of the
 * given situation and handeled by this class.
 *
 * Example:
 * A specific web application the the Mayflower Usecase framework
 * loades error message language resource for the Mayflower BOF
 * and Mayflower Usecase frameworks, resources for models is loaded
 * according to the declared models in the BOF configuration
 * (Abalon...) and the current applications in the Mayflower Usecase
 * framework.
 *
 * @author Fredrik Hed [f.hed@abalon.se]
 * @author Fredrik Hellström [f.hellstrom@abalon.se]
 */
public class LocaleHandler implements Serializable {
    private static final long serialVersionUID = -3615598222841021503L;


    /**
     * The resource key for the resolvement mode for ResouceKeys.
     * Valid value for this optional resource key are:
     *
     * TRANSLATED, ONLY_KEYS, KEYS_AND_TRANSLATED, IMAGE_AND_TRANSLATED or
     * HTML_COMMENT_AND_TRANSLATED.
     */
    public static final String RESOLVMENT_MODE_RK = "se.abalon.lang.ResolvementMode";


    /**
     * Normal mode. All keys and parameters are resolved
     * if possible.
     */
    public static final int TRANSLATED = 1;

    /**
     * Development and debug mode. No keys and parameters
     * are resolved.
     */
    public static final int ONLY_KEYS = 2;

    /**
     * Development and debug mode. Keys and parameters are
     * resolved and shown with the keys as well.
     */
    public static final int KEYS_AND_TRANSLATED = 3;

    /**
     * Development and debug mode. Keys and parameters are
     * resolved and shown. The keys is shown as HTML surounded with a div with the classes set to 'key'.
     */
    public static final int IMAGE_AND_TRANSLATED = 4;
    /**
     * Development and debug mode. Keys and parameters are
     * resolved and shown. The keys is shown as HTML comments.
     */
    public static final int HTML_COMMENT_AND_TRANSLATED = 5;
    /**
     * Development and debug mode. Keys and parameters are
     * resolved and shown surounded with '*'
     */
    public static final int STAR_AND_TRANSLATED = 6;


    /**
     * Definition of used Resources.
     *
     * @return the Resources used by this class
     */
    public static ResourceMap getConfigurations() {
        ResourceMap map = new ResourceMap();

        map.put(
                RESOLVMENT_MODE_RK,
                String.class,
                "If and how ResourceKeys and ResourceParams are to be resolved. (TRANSLATED, ONLY_KEYS, KEYS_AND_TRANSLATED, IMAGE_AND_TRANSLATED or HTML_COMMENT_AND_TRANSLATED)");

        map.put("se.abalon.bof.BofPersistenceManager.PersLocale.Language",
                String.class,
                "The personalized Locale language to use");

        map.put("se.abalon.bof.BofPersistenceManager.PersLocale.Country",
                String.class,
                "The personalized Locale country to use");

        map.put("se.abalon.bof.BofPersistenceManager.DefaultLocale.Language",
                String.class,
                "The default Locale language to use");

        map.put("se.abalon.bof.BofPersistenceManager.DefaultLocale.Country",
                String.class,
                "The default Locale country to use");

        return map;
    }

    private static int resolvementMode = TRANSLATED;

    private static Category log = Category.getInstance(LocaleHandler.class);

    /**
     * Cache with all loaded LocaleHandlers, for all languages.
     */
    private static Map<String, LocaleHandler> handlerCache = new HashMap<String,LocaleHandler>();

    /**
     * An ordered list with all loaded ResourceBundles.
     */
    private List<ResourceBundle> bundles = new ArrayList<ResourceBundle>();

    /**
     * Populate LocaleHandler with configured ResourceBundles as well as
     * the one for the given application.
     * @param locale the locale to use
     *
     * @return a populated LocaleHendler instance
     */
    public static LocaleHandler getLocaleHandler(Locale locale) {
    	/*
		 * fetch localHandle if it already loaded
		 */
        String key = locale.getLanguage() + "_" + locale.getCountry();
        LocaleHandler newLocaleHandler = null;

        if (handlerCache.containsKey(key)) {
            return handlerCache.get(key);
        }

		/*
         * Read resolvement mode from properties.
         */
        try {
            String resMode = (String) getConfigurations().lookup(RESOLVMENT_MODE_RK);
            if (resMode.equalsIgnoreCase("TRANSLATED")) {
                resolvementMode = TRANSLATED;
            }
            else if (resMode.equalsIgnoreCase("ONLY_KEYS")) {
                resolvementMode = ONLY_KEYS;
            }
            else if (resMode.equalsIgnoreCase("KEYS_AND_TRANSLATED")) {
                resolvementMode = KEYS_AND_TRANSLATED;
            }
            else if (resMode.equalsIgnoreCase("IMAGE_AND_TRANSLATED")) {
                resolvementMode = IMAGE_AND_TRANSLATED;
            }
            else if (resMode.equalsIgnoreCase("HTML_COMMENT_AND_TRANSLATED")) {
                resolvementMode = HTML_COMMENT_AND_TRANSLATED;
            }
            else if (resMode.equalsIgnoreCase("STAR_AND_TRANSLATED")) {
                resolvementMode = STAR_AND_TRANSLATED;
            }
            else {
                log.warn("The resource key " + RESOLVMENT_MODE_RK + " does not have a valid value. Defaults to TRANSLATED.");
                resolvementMode = TRANSLATED;
            }
        }
        catch (ResourceProviderException e) {
            resolvementMode = TRANSLATED;
        }

		/*
         * Read global bundles from properties.
         */
        List<String> globalBundles = null;
        ResourceBundle rb = null;
        String propFile = "mayflower";

        if(System.getProperty("mayflower-property-file") != null) {
            propFile = System.getProperty("mayflower-property-file");
        }
        try {
            rb = ResourceBundle.getBundle(propFile);
        }
        catch (MissingResourceException mre) {
            throw new RuntimeException("Missing mayflower.properties. Can not instantiate LocaleHandler", mre);
        }

        try {
            String globalBundlesStr = rb.getString("language-files");
            globalBundles = Arrays.asList(globalBundlesStr.split(" "));
        }
        catch (MissingResourceException e1) {
            throw new RuntimeException("Missing resource language-files.properties", e1);
        }


		/*
		 * localHandle does exist, create new
		 */
        List<String> resourcesToLoad = new ArrayList<String>();
        if (globalBundles != null) {
            Iterator<String> itor = globalBundles.iterator();
            while (itor.hasNext()) {
                String bundle = itor.next();
                resourcesToLoad.add(bundle);
            }
        }
        newLocaleHandler = new LocaleHandler(resourcesToLoad, locale);
        handlerCache.put(key, newLocaleHandler);
        return newLocaleHandler;
    }

    private LocaleHandler(List<String> toLoad, Locale locale) {
        Iterator<String> itor = toLoad.iterator();
        while (itor.hasNext()) {
            String baseName = itor.next();

            try {
                bundles.add(ResourceBundle.getBundle(baseName, locale));
            }
            catch (RuntimeException e) {
                log.info("Missing language resource bundle: [" + baseName + "]");
            }
        }
    }

    /**
     * Lookup the key and format it according to the resolvement mode.
     *
     * @param key the key to find the internationalized string for
     * @param tryVocabulary	if true the vocabulary is checked for translation
     * @return the internationalized string
     */
    public String getString(String key) {

        StringBuffer sb = new StringBuffer();
        switch (resolvementMode) {
            case TRANSLATED:
                sb.append(resolve(key));
                break;
            case ONLY_KEYS:
                sb.append("[");
                sb.append(key);
                sb.append("]");
                break;
            case KEYS_AND_TRANSLATED:
                sb.append("[");
                sb.append(key);
                sb.append("]");
                sb.append(resolve(key));
                break;
            case IMAGE_AND_TRANSLATED:
                sb.append("<img border=\"0\" alt=\"");
                sb.append(key);
                sb.append("\" src=\"mf/core/images/icons/info.gif\"/>");
                sb.append(resolve(key));
                break;
            case STAR_AND_TRANSLATED:
                sb.append("*");
                sb.append(resolve(key));
                sb.append("*");
                break;
            case HTML_COMMENT_AND_TRANSLATED:
                sb.append("<!--");
                sb.append(key);
                sb.append("-->");
                sb.append(resolve(key));
                break;
        }
        return sb.toString();
    }

    public String getString(ResourceKey key) {
        return getString(key.getResourceKey());
    }

    /**
     * Lookup the key in the loaded resourcebundles for the given localeHandler.
     *
     * @param key the key to find the internationalized string for
     * @param tryVocabulary should we try using vocabulary first, before searching in the resourcebundles?
     * @return the internationalized string
     */
    private String resolve(String key) {
        // If stated, first try to fetch key from vocabulary


        // Then try to fetch it from resource bundles
        Iterator<ResourceBundle> itor = bundles.iterator();
        while (itor.hasNext()) {
            PropertyResourceBundle bundle = (PropertyResourceBundle) itor.next();
            Object o = bundle.handleGetObject(key);
            if (o != null) {
                return o.toString();
            }
        }

        // If all else fails, return the key within brackets
        return "[" + key + "]";
    }
    /**
     * Checks if a internationalized string exists for a given key.
     *
     * @param key the key to check if there exists a internationalized string for
     * @param tryVocabulary should we try using vocabulary first, before searching in the resourcebundles?
     * @return true if a internationalized string for exists
     */
    public boolean isResolvable(String key) {


        Iterator<ResourceBundle> itor = bundles.iterator();
        while (itor.hasNext()) {
            PropertyResourceBundle bundle = (PropertyResourceBundle) itor.next();
            Object o = bundle.handleGetObject(key);
            if (o != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a personalized locale. Uses default values for langauage and country if not found for the current user.
     *
     * @return the personalized locale.
     * @throws ConfigurationException
     */
    public static Locale getPersonalizedLocale() throws ConfigurationException {
        return getPersonalizedLocale(true);
    }

    /**
     * Gets a personalized locale.
     *
     * @param useDefaultIfNotFound Use the default language and/or country if not found for the current user.
     *
     * @return the personalized locale.
     * @throws ModelException If default locale not found
     */
    public static Locale getPersonalizedLocale(boolean useDefaultIfNotFound) throws ConfigurationException {
        String lang = null;
        String country = null;

        // First try to get personalized locale settings
        try {
            lang = (String) getConfigurations().lookup("se.abalon.bof.BofPersistenceManager.PersLocale.Language");
        } catch (ResourceProviderException e) {
            log.debug("Personalized language not found. Using default language.");
        }
        if (lang == null && useDefaultIfNotFound == true) {
            try {
                lang = (String) getConfigurations().lookup("se.abalon.bof.BofPersistenceManager.DefaultLocale.Language");
            } catch (ResourceProviderException e) {
                throw new ConfigurationException(ResourceKeys.MISSING_RESOURCE, new Object[] {"se.abalon.bof.BofPersistenceManager.DefaultLocale.Language, se.abalon.bof.BofPersistenceManager.DefaultLocale.Country"});
            }
        }

        try {
            country = (String) getConfigurations().lookup("se.abalon.bof.BofPersistenceManager.PersLocale.Country");
        } catch (ResourceProviderException e) {
            log.debug("Personalized country not found. Using default country.");
        }
        if (country == null && useDefaultIfNotFound == true) {
            try {
                country = (String) getConfigurations().lookup("se.abalon.bof.BofPersistenceManager.DefaultLocale.Country");
            } catch (ResourceProviderException e) {
                throw new ConfigurationException(ResourceKeys.MISSING_RESOURCE, new Object[] {"se.abalon.bof.BofPersistenceManager.DefaultLocale.Language, se.abalon.bof.BofPersistenceManager.DefaultLocale.Language"});
            }
        }

        if(lang == null && country == null) {
            return null;
        }

        return new Locale(lang, country);
    }

    /**
     * Get all keys from all resource bundles
     * @return all keys found in the resource bundles
     */
    public Collection<String> getKeys() {
        Collection<String> allKeys = new HashSet<String>();
        Iterator<ResourceBundle> itor = bundles.iterator();
        while (itor.hasNext()) {
            PropertyResourceBundle bundle = (PropertyResourceBundle) itor.next();
            Set<String> keySet = bundle.keySet();
            allKeys.addAll(keySet);
        }
        return allKeys;
    }
}
