package se.abalon.cache.exception;

import se.abalon.cache.lang.ResourceKey;

import java.util.Locale;

/**
 * Base class for all exceptions thrown by the
 * Mayflower Framework that throws ResourceKeys
 * instead of static error messages.
 *
 * @author Fredrik Hed [f.hed@abalon.se]
 * @author Henrik Samuelsson [h.samuelsson@abalon.se]
 */
@SuppressWarnings("serial")
public abstract class MayflowerException extends Exception {
    /**
     * The Resource key describing this exception.
     */
    protected ResourceKey rk = null;

    /**
     * Parameters to the ResourceKey
     */
    protected Object [] params = new Object[0];

    public MayflowerException(ResourceKey rk, Object [] params, Throwable rootCause) {
        super(toString(params, rk), rootCause);
        this.rk = rk;
        this.params = params;
    }

    public MayflowerException(ResourceKey rk, Object [] params) {
        super(toString(params, rk));
        this.rk = rk;
        this.params = params;
    }

    public MayflowerException(ResourceKey rk, Throwable rootCause) {
        super(toString(null, rk), rootCause);
        this.rk = rk;
    }

    public MayflowerException(ResourceKey rk) {
        super();
        this.rk = rk;
    }

    protected MayflowerException(Throwable rootCause) {
        super(rootCause);
    }

    protected MayflowerException(MayflowerException e) {
        super(e.getCause());
        rk = e.getResourceKey();
        params = e.getParams();
    }

    protected MayflowerException() {
        super();
    }

    protected MayflowerException(String message) {
        super(message);
    }

    /**
     * Returns the parameters to the ResourceKey describing this exception.
     *
     * @return the parameters to the ResourceKey describing this exception
     */
    public Object [] getParams() {
        return params;
    }

    /**
     * Returns the ResourceKey describing this exception.
     *
     * @return the ResourceKey describing this exception
     */
    public ResourceKey getResourceKey() {
        return rk;
    }

    public String toString() {
        return this.getClass().getName() + ": " + getMessage();
    }

    public String getMessage() {
        String msg = null;
        if(rk != null) {
            msg = rk.getLocalizedMessage(params, Locale.ENGLISH);
        } else {
            return super.getMessage();
        }
        return msg;
    }

    private static String toString(Object[] params, ResourceKey rk) {
        String toReturn = null;
        if (rk != null) {
            toReturn = rk.getLocalizedMessage(params, Locale.ENGLISH);
        } else {
            toReturn = "No message available";
        }
        return toReturn;
    }
}


