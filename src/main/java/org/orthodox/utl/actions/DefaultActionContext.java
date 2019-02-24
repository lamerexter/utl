package org.orthodox.utl.actions;

import org.beanplanet.core.io.IoException;
import org.beanplanet.core.io.resource.Resource;
import org.orthodox.utl.model.Template;
import org.orthodox.utl.model.TemplateBodyBuilder;
import org.orthodox.utl.parser.ParseException;
import org.orthodox.utl.parser.TemplateParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

public class DefaultActionContext implements ActionContext {
    private Writer out;

    public DefaultActionContext(Writer out) {
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
    public TemplateBody parse(Resource resource) {
        try (InputStream resourceIs = resource.getInputStream()) {
            TemplateParser parser = new TemplateParser(resourceIs, "UTF-8");
            Template template = parser.Template();

            return createTemplateBody(template);
        } catch (IOException | ParseException ex) {
            throw new IoException(ex);
        }
    }

    private TemplateBody createTemplateBody(Template template) {
        TemplateBodyBuilder builder = new TemplateBodyBuilder();
        builder.visit(template);
        return builder.build();
    }
}
