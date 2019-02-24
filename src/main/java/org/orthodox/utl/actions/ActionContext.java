package org.orthodox.utl.actions;

import org.beanplanet.core.io.resource.Resource;

import java.io.Writer;

public interface ActionContext {
    /**
     * Returns the current value of the output where all content produced by the action can be written.
     *
     * @return the writer where action content may be written, which will never be null.
     */
    Writer getOut();

    TemplateBody parse(Resource resource);
}
