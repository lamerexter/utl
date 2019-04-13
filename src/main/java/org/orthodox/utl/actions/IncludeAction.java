package org.orthodox.utl.actions;

import org.beanplanet.core.io.resource.FileResource;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.UriResource;
import org.beanplanet.core.lang.Assert;

import java.io.File;
import java.net.URI;

public class IncludeAction extends ActionBase {
    private static final UriResolver DEFAULT_URI_RESOLVER = new DefaultUriResolver();
    private File file;
    private URI uri;
    private UriResolver uriResolver = DEFAULT_URI_RESOLVER;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public UriResolver getUriResolver() {
        return uriResolver;
    }

    public void setUriResolver(UriResolver uriResolver) {
        this.uriResolver = uriResolver;
    }

    @Override
    public void doAction(TemplateBody body, ActionContext context) {
        Resource resourceToInclude = includeFromStream();
        if (resourceToInclude != null) {
            TemplateBody dynamicTemplateBody = context.parse(this, resourceToInclude);
            dynamicTemplateBody.writeTo(context.getOut());
        }

        if (body != null) {
            body.writeTo(context.getOut());
        }
    }

    private Resource includeFromStream() {
        Assert.isTrue(uri != null
                || file != null
                || (uri == null && file == null), "A URI or file template resource must be specified or both must be null");
        if (uri != null) {
            if (uri.isAbsolute()) {
                Resource resource = uriResolver.resolve(uri);
                if (resource == null) {
                    throw new TemplateActionException("Unable to resolve resource from given URI ["+uri+"]");
                }
                return resource;
            }

            //----------------------------------------------------------------------------------------------------------
            // Begin resolution of the relative resource uri through ancestors.
            //----------------------------------------------------------------------------------------------------------
            IncludeAction fromAction = this;
            Resource resource = new UriResource(uri);
            while ((fromAction = findAncestorOfType(fromAction, IncludeAction.class)) != null) {
                Resource ancestorResource = fromAction.getFile() != null ? new FileResource(fromAction.getFile()) : new UriResource(fromAction.getUri());
                resource = ancestorResource.resolve(resource.getPath());
            }

            if (resource == null) {
                throw new TemplateActionException("Unable to resolve relative resource from given URI ["+uri+"] through ancestry.");
            }
            return resource;
        } else if (file != null){
            return new FileResource(file);
        }

        return null;
    }
}
