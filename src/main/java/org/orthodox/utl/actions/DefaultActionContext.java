package org.orthodox.utl.actions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.beanplanet.core.io.IoException;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.resolution.ResourceResolver;
import org.orthodox.universel.Universal;
import org.orthodox.utl.ELContext;
import org.orthodox.utl.model.Template;
import org.orthodox.utl.model.TemplateConfig;
import org.orthodox.utl.model.transforms.TemplateBodyBuilder;
import org.orthodox.utl.model.transforms.TemplateElementProducer;
import org.orthodox.utl.parser.ParseException;
import org.orthodox.utl.parser.TemplateParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@AllArgsConstructor
public class DefaultActionContext implements ActionContext {
    /** The optional Expression Language context enclosing this action contest. */
    private final ELContext elContext;
    /** The optional binding for interpolated expressions in the template. */
    private final Object binding;

    /** The output for all content produced by actions. */
    private final Writer out;

    /** A resolver of resources referenced by actions. */
    private final ResourceResolver resourceResolver;

    private final Map<String, Object> actionVariables = new HashMap<>();

    public DefaultActionContext(final Writer out) {
        this(null, out);
    }

    public DefaultActionContext(final ResourceResolver resourceResolver, final Writer out) {
        this(null, resourceResolver, out);
    }

    public DefaultActionContext(final Object binding, final ResourceResolver resourceResolver, final Writer out) {
        this(null, binding, resourceResolver, out);
    }

    public DefaultActionContext(final ELContext elContext, final Object binding, final ResourceResolver resourceResolver, final Writer out) {
        this.elContext = elContext;
        this.binding = binding;
        this.resourceResolver = resourceResolver;
        this.out = out;
    }

    @Override
    public TemplateBody parse(final Action enclosingAction, final Resource resource, final TemplateConfig templateConfig) {
        try (InputStream resourceIs = resource.getInputStream()) {
            TemplateParser parser = new TemplateParser(resourceIs, "UTF-8");
            Template template = parser.Template();

            return createTemplateBody(enclosingAction, template, templateConfig);
        } catch (IOException | ParseException ex) {
            throw new IoException(ex);
        }
    }

    @Override
    public Optional<Resource> resolveResource(String resourceSpec) {
        return resourceResolver == null ? Optional.empty() : resourceResolver.resolveResource(resourceSpec);
    }

    private TemplateBody createTemplateBody( final Action enclosingAction, final Template template, final TemplateConfig templateConfig) {
        TemplateBodyBuilder builder = new TemplateBodyBuilder();
        TemplateElementProducer producer = new TemplateElementProducer(templateConfig, enclosingAction, builder);
        producer.visit(template);
        final TemplateBody body = builder.build();
        body.setEnclosingAction(enclosingAction);
        return body;
    }

    public <T> T evaluate(final Class<T> resultType, final String expression) {
        return Universal.execute(resultType, expression, binding != null ? binding : this);
    }

    @Override
    public boolean hasVariable(String name) {
        return actionVariables.containsKey(name) || (elContext != null && elContext.hasVariable(name));
    }

    @Override
    public <T> T getVariable(String name, Class<T> expectedType) {
        if (actionVariables.containsKey(name)) return (T)actionVariables.get(name);

        return elContext == null ? null : (T)elContext.getVariable(name);
    }

    @Override
    public void setVariable(String name, Object value) {
        actionVariables.put(name, value);
    }

    @Override
    public void removeVariable(String name) {

        if (actionVariables.containsKey(name)) actionVariables.remove(name);

        if (elContext.hasVariable(name)) elContext.removeVariable(name);
    }
}
