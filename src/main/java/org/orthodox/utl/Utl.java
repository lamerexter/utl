package org.orthodox.utl;

import org.beanplanet.core.io.resource.ByteArrayOutputStreamResource;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.StringResource;
import org.beanplanet.core.io.resource.UrlResource;
import org.beanplanet.core.io.resource.resolution.DefaultResourceResolver;
import org.beanplanet.core.io.resource.resolution.ResourceResolver;
import org.orthodox.utl.actions.DefaultActionContext;
import org.orthodox.utl.actions.TemplateBody;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.net.URL;

/**
 * The main entry point of the Universal Template Language (UTL) engine.
 */
public class Utl {
    public static <T extends Resource> Resource expand(final ELContext elContext, final Object binding, final ResourceResolver resourceResolver, final Resource source, final T destination) {
        try (final Writer destinationWriter = destination.getWriter()) {
            final DefaultActionContext actionContext = new DefaultActionContext(elContext, binding, resourceResolver, destinationWriter);
            TemplateBody dynamicTemplateBody = actionContext.parse(source);
            dynamicTemplateBody.writeTo(actionContext);
            return destination;
        } catch (IOException ioEx) {
            throw new UncheckedIOException(ioEx);
        }
    }

    public static <T extends Resource> Resource expand(final Object binding, final ResourceResolver resourceResolver, final Resource source, final T destination) {
        return expand(null, binding, resourceResolver, source, destination);
    }

    public static String expand(final ResourceResolver resourceResolver, final String source) {
        return expand(resourceResolver, new StringResource(source), new ByteArrayOutputStreamResource()).readFullyAsString();
    }

    public static Resource expand(final Resource source) {
        return expand((Object)null, source);
    }

    public static <T extends Resource> Resource expand(final Object binding, final ResourceResolver resourceResolver, final Resource source) {
        return expand(null, binding, resourceResolver, source);
    }

    public static <T extends Resource> Resource expand(final ELContext elContext, final Object binding, final ResourceResolver resourceResolver, final Resource source) {
        return expand(elContext, binding, resourceResolver, source, new ByteArrayOutputStreamResource());
    }

    public static <T extends Resource> Resource expand(final ResourceResolver resourceResolver, final Resource source, final T destination) {
        return expand(null, resourceResolver, source, destination);
    }

    public static <T extends Resource> Resource expand(final Object binding, final Resource source, final T destination) {
        return expand(binding, null, source, destination);
    }

    public static <T extends Resource> Resource expand(final Resource source, final T destination) {
        return expand((ELContext)null, source, destination);
    }

    public static <T extends Resource> Resource expand(final ELContext elContext, final Resource source, final T destination) {
        return expand(elContext, null, new DefaultResourceResolver(), source, destination);
    }

    public static String expand(final String source) {
        return expand((ELContext)null, source);
    }

    public static String expand(final Object binding, final String source) {
        return expand(binding, new DefaultResourceResolver(), new StringResource(source)).readFullyAsString();
    }

    public static String expand(final ELContext elContext, final String source) {
        return expand(elContext, new StringResource(source), new ByteArrayOutputStreamResource()).readFullyAsString();
    }

    public static Resource expand(final Object binding, final Resource source) {
        return expand(binding, source, new ByteArrayOutputStreamResource());
    }

    public static String expand(final URL source) {
        return expand(null, source);
    }

    public static String expand(final Object binding, final URL source) {
        return expand(binding, null, new UrlResource(source), new ByteArrayOutputStreamResource()).readFullyAsString();
    }
}
