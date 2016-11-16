package se.abalon.cache.exception;


import se.abalon.cache.lang.ResourceKey;

/**
 * Exception thrown when an {@link Action} object fails to execute.
 *
 * @author Oskar Leijon [o.lejion@abalon.se]
 * @author Fredrik Hed [f.hed@abalon.se]
 * @author Göran Ehrsson [g.ehrsson@abalon.se]
 */
@SuppressWarnings("serial")
public class ActionException extends MayflowerException {

    public ActionException() {

    }

    /**
     * Creates new ActionException with a detailed message.
     *
     * @param msg Detailed message
     */
    public ActionException(String msg) {
        super();
    }

    /**
     * Constructs a new action exception when you needs to throw an exception and include a message about another exception
     * that interfered with its normal operation.
     *
     * @param message   a <code>String</code> containing the text of the exception message
     * @param rootCause the <code>Throwable</code> exception that interfered with your normal operation
     */
    public ActionException(String message, Throwable rootCause) {
        super(rootCause);
    }

    /**
     * Creates new ActionException with a rootCause.
     *
     * @param rootCause the exception that makes this exception necessary
     */
    public ActionException(Throwable rootCause) {
        super(rootCause);
    }

    /**
     * Creates a new ActionException based on an existing MayflowerException.
     *
     * @param rootCause
     */
    public ActionException(MayflowerException rootCause) {
        super(rootCause);
    }

    public ActionException(ResourceKey rk, Object[] params, Throwable rootCause) {
        super(rk, params, rootCause);
    }

    public ActionException(ResourceKey rk, Object[] params) {
        super(rk, params);
    }

    public ActionException(ResourceKey rk, Throwable rootCause) {
        super(rk, rootCause);
    }

    public ActionException(ResourceKey rk) {
        super(rk);
    }
}
