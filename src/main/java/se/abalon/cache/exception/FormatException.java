package se.abalon.cache.exception;


import se.abalon.cache.lang.ResourceKey;

public class FormatException extends MayflowerException {

    private static final long serialVersionUID = 1183663987237983171L;

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     * @deprecated use FormatException(ResourceKey)
     */
    @Deprecated
    public FormatException(String message) {
        super();
    }

    /**
     * Constructs the default Exception.
     * @deprecated use FormatException(ResourceKey)
     */
    @Deprecated
    public FormatException() {
        super();
    }

    /**
     * Constructs a exception and include a message about the root cause
     * @param message a String containing the text of the exception message
     * @param rootCause the Throwable exception that interfered with your normal operation
     * @deprecated use FormatException(ResourceKey)
     */
    @Deprecated
    public FormatException(String message, Throwable rootCause) {
        super(rootCause);
    }

    /**
     * @param rootCause the system exception that makes this exception necessary
     */
    public FormatException(Throwable rootCause) {
        super(rootCause);
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     * @deprecated use FormatException(ResourceKey)
     */
    @Deprecated
    public FormatException(ResourceKey rk) {
        super(rk);
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param rk The ResourceKey associated with the exception.
     * @param rootCause the Throwable exception that interfered with your normal operation
     */
    public FormatException(ResourceKey rk, Throwable rootCause) {
        super(rk, rootCause);
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param rk The ResourceKey associated with the exception.
     * @param params The parameters for the resourceKey
     */
    public FormatException(ResourceKey rk, Object [] params) {
        super(rk, params);
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param rk The ResourceKey associated with the exception.
     * @param params The parameters for the resourceKey
     * @param rootCause the Throwable exception that interfered with your normal operation
     */
    public FormatException(ResourceKey rk, Object [] params, Throwable rootCause) {
        super(rk, params, rootCause);
    }
}