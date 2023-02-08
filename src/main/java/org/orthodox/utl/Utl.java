package org.orthodox.utl;

import org.beanplanet.core.io.resource.ByteArrayOutputStreamResource;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.StringResource;
import org.beanplanet.core.io.resource.resolution.DefaultResourceResolver;
import org.beanplanet.core.io.resource.resolution.ResourceResolver;
import org.orthodox.utl.actions.DefaultActionContext;
import org.orthodox.utl.actions.TemplateBody;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;

/**
 * The main entry point of the Universal Template Language (UTL) engine.
 */
public class Utl {
    public static <T extends Resource> Resource expand(ResourceResolver resourceResolver, Resource source, T destination) {
        try (final Writer destinationWriter = destination.getWriter()) {
            final DefaultActionContext actionContext = new DefaultActionContext(resourceResolver, destinationWriter);
            TemplateBody dynamicTemplateBody = actionContext.parse(source);
            dynamicTemplateBody.writeTo(actionContext);
            return destination;
        } catch (IOException ioEx) {
            throw new UncheckedIOException(ioEx);
        }
    }

    public static <T extends Resource> Resource expand(Resource source, T destination) {
        return expand(new DefaultResourceResolver(), source, destination);
    }

    public static String expand(String source) {
        return expand(new StringResource(source), new ByteArrayOutputStreamResource()).readFullyAsString();
    }
}
