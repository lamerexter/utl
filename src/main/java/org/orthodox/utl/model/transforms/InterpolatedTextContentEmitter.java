package org.orthodox.utl.model.transforms;

import org.beanplanet.core.io.IoException;
import org.orthodox.utl.actions.AbstractTemplateBodyElement;
import org.orthodox.utl.actions.ActionContext;
import org.orthodox.utl.model.Node;

import java.io.IOException;

public class InterpolatedTextContentEmitter extends AbstractTemplateBodyElement {
    private Node node;

    public InterpolatedTextContentEmitter(Node node) {
        this.node = node;
    }

    @Override
    public void writeTo(ActionContext context) {
        final String text = node.toString();
        final String interpolatedQuotedString = "\"\"\"" + text + "\"\"\"";
        final String interpolatedResult  = context.evaluate(String.class, interpolatedQuotedString);

        try {
        context.getOut().write(interpolatedResult);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }
}
