package org.orthodox.utl.actions;

import org.beanplanet.core.io.IoException;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.resolution.ResourceResolver;
import org.orthodox.utl.model.Template;
import org.orthodox.utl.model.TemplateBodyBuilder;
import org.orthodox.utl.parser.ParseException;
import org.orthodox.utl.parser.TemplateParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Optional;

public class DefaultActionContext implements ActionContext {
    private Writer out;

    private ResourceResolver resourceResolver;

    public DefaultActionContext(final Writer out) {
        this.out = out;
    }

    public DefaultActionContext(final ResourceResolver resourceResolver, final Writer out) {
        this.resourceResolver = resourceResolver;
        this.out = out;
    }

    /**
     * Returns the current value of the output where all content produced by the action can be written.
     *
     * @return the writer where action content may be written, which will never be null.
     */
    @Override
    public Writer getOut() {
        return out;
    }

    public void setOut(Writer out) {
        this.out = out;
    }

    @Override
    public TemplateBody parse(Action actionContext, Resource resource) {
        try (InputStream resourceIs = resource.getInputStream()) {
            TemplateParser parser = new TemplateParser(resourceIs, "UTF-8");
            Template template = parser.Template();

            return createTemplateBody(actionContext, template);
        } catch (IOException | ParseException ex) {
            throw new IoException(ex);
        }
    }

    @Override
    public Optional<Resource> resolveResource(String resourceSpec) {
        return resourceResolver == null ? Optional.empty() : resourceResolver.resolveResource(resourceSpec);
    }

    private TemplateBody createTemplateBody(Action actionContext, Template template) {
        TemplateBodyBuilder builder = new TemplateBodyBuilder();
        if (actionContext != null) {
            builder.setEnclosingAction(actionContext);
        }

        builder.visit(template);
        return builder.build();
    }
}
