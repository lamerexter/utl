package org.orthodox.utl.model;

import org.beanplanet.core.models.Builder;
import org.orthodox.utl.actions.Action;
import org.orthodox.utl.actions.ActionBuilder;
import org.orthodox.utl.actions.IncludeAction;
import org.orthodox.utl.actions.TemplateBody;

import java.util.LinkedList;
import java.util.List;

public class TemplateBodyBuilder implements Visitor, Builder<TemplateBody> {
    private List<TemplateBody> bodyElements = new LinkedList<>();

    @Override
    public void visit(Tag t) {
        if (t.emptyTag && isTemplateAction(t)) {
            emitTemplateActionHandler(t);
            return;
        }

        emitNodeOutput(t);
    }

    private void emitTemplateActionHandler(Tag t) {
        // Lookup by tag ns ...
        ActionBuilder actionBuilder = new ActionBuilder();
        actionBuilder.withStartTag(t);
        bodyElements.add(new TemplateActionHandler(actionBuilder.build()));
    }

    private boolean isTemplateAction(Tag t) {
        return t.tagName.startsWith("utl:");
    }

    private void emitNodeOutput(Node node) {
        pushNodeHandler(new NodeContentEmitter(node));
    }

    private void pushNodeHandler(TemplateBody contentHandler) {
        bodyElements.add(contentHandler);
    }

    @Override
    public void visit(EndTag t) {
        emitNodeOutput(t);
    }

    @Override
    public void visit(Comment c) {
        emitNodeOutput(c);
    }

    @Override
    public void visit(Text t) {
        emitNodeOutput(t);
    }

    @Override
    public void visit(Newline n) {
        emitNodeOutput(n);
    }

    @Override
    public void visit(Annotation a) {
        emitNodeOutput(a);
    }

    @Override
    public TemplateBody build() {
        return new CompositeTemplateBody(bodyElements);
    }
}
