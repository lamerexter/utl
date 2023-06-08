package org.orthodox.utl.actions;

import org.beanplanet.core.io.resource.Resource;
import org.orthodox.utl.model.TemplateConfig;

import java.io.Writer;
import java.util.Optional;

public interface ActionContext extends VariableHolder {
    /** The default configuration used in parsing, building and applying templates. */
    TemplateConfig DEFAULT_TEMPLATE_CONFIG = TemplateConfig.builder().interpolate(true).build();

    /**
     * Returns the current value of the output where all content produced by the action can be written.
     *
     * @return the writer where action content may be written, which will never be null.
     */
    Writer getOut();

    default TemplateBody parse(Resource resource) {
        return parse(null, resource);
    }

    default TemplateBody parse(Action enclosingAction, Resource resource) {
        return parse(enclosingAction, resource, DEFAULT_TEMPLATE_CONFIG);
    }

    TemplateBody parse(Action enclosingAction, Resource resource, TemplateConfig templateConfig);

    Optional<Resource> resolveResource(String resourceSpec);

    <T> T evaluate(Class<T> resultType, String expression);
}
