package se.abalon.cache.exception;


import se.abalon.cache.lang.ResourceKey;

/**
 * Exception thrown by the ConfigurationSetup.
 *
 * @author Fredrik Hed [f.hed@abalon.se]
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ConfigurationException extends MayflowerException {

    /**
     * Constructs a exception and include a message about the root cause.
     *
     * @deprecated use ConfigurationSetupException(ResourceKey)
     * @param message a String containing the text of the exception message
     */
    public ConfigurationException(String message) {
        super();
    }

    /**
     * Constructs the default Exception.
     * @deprecated use ConfigurationSetupException(ResourceKey)
     */
    public ConfigurationException() {
        super();
    }

    /**
     * Constructs a exception and include a message about the root cause
     * @param message a String containing the text of the exception message
     * @param rootCause the Throwable exception that interfered with your normal operation
     */
    public ConfigurationException(String message, Throwable rootCause) {
        super(rootCause);
    }

    /**
     * Constructs a exception and include the root cause.
     *
     * @param rootCause the system exception that makes this exception necessary
     */
    public ConfigurationException(Throwable rootCause) {
        super(rootCause);
    }

    public ConfigurationException(ResourceKey rk) {
        super(rk);
    }

    public ConfigurationException(ResourceKey rk, Object [] params) {
        super(rk, params);
    }

    public ConfigurationException(ResourceKey rk, Throwable rootCause){
        super(rk, rootCause);
    }

    public ConfigurationException(ResourceKey rk, Object [] params, Throwable rootCause) {
        super(rk, params, rootCause);
    }
}
