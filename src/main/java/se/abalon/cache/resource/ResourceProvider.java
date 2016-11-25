package se.abalon.cache.resource;

import se.abalon.cache.exception.ResourceProviderException;

/** @author Fredrik Hed <f.hed@abalon.se> */
public interface ResourceProvider {

    Object lookup(Resource rs) throws ResourceProviderException;
}