package se.abalon.cache.exception;

/**
 * This exception is thrown when when a error in creation of
 * the provider or in the usage of the provider happends occurred
 *
 * @author Fredrik Hed [f.hed@abalon.se]
 */
@SuppressWarnings("serial")
public class ResourceProviderException extends Exception {
    public static final int RESOURE_FORMAT_ERROR = 1;
    public static final int NO_SUCH_RESOURCE = 2;
    public static final int CONFIGURATION_ERROR = 3;

    private int type = RESOURE_FORMAT_ERROR;
    /**
     * Constructs an Exception without a message.
     */
/*	public ResourceProviderException() {
		super();
	}
*/

    /**
     * Constructs an Exception with a detailed message.
     *
     * @param Message the message associated with the exception
     */
/*	public ResourceProviderException(String message) {
		super(message);
	}
*/
    /**
     * Constructs an Exception with a detailed message.
     *
     * @param Message the message associated with the exception
     * @param type the type of exception
     */
    public ResourceProviderException(String message, int type) {
        super(message);
        this.type = type;
    }

    public int getType() {
        return type;
    }
}

