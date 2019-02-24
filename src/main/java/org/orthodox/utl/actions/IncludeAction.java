package org.orthodox.utl.actions;

import org.beanplanet.core.io.resource.FileResource;
import org.beanplanet.core.io.resource.Resource;
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
        TemplateBody dynamicTemplateBody = context.parse(includeFromStream());
        dynamicTemplateBody.writeTo(context.getOut());
    }

    private Resource includeFromStream() {
        Assert.isTrue(uri != null || file != null, "A URI or file template resource must be specified");
        if (uri != null) {
            Resource resource = uriResolver.resolve(uri);
            if (resource == null) {
                throw new TemplateActionException("Unable to resolve resource from given URI ["+uri+"]");
            }
            return resource;
        } else {
            return new FileResource(file);
        }
    }
}
