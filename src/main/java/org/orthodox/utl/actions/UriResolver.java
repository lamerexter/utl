package org.orthodox.utl.actions;

import org.beanplanet.core.io.resource.Resource;

import java.net.URI;

public interface UriResolver {
    /**
     * Resolves a given URI to a physical resource, or returns null if the URI could not be resolved.
     *
     * @param uri the uri to be resolved, which may not be null.
     * @return a resource which maps to the given URI, or null if no resource could be identified within the
     * scope of this resolver matching the given URI.
     */
    Resource resolve(URI uri);
}
