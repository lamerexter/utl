package org.orthodox.utl.model.transforms;

import lombok.Data;
import org.beanplanet.core.UncheckedException;
import org.orthodox.utl.actions.Action;
import org.orthodox.utl.actions.ActionBuilder;
import org.orthodox.utl.actions.TemplateBody;
import org.orthodox.utl.actions.TemplateBodyElement;
import org.orthodox.utl.model.*;

import java.util.ArrayDeque;
import java.util.Deque;

@Data
public class TemplateElementProducer implements Visitor {
    private final TemplateConfig templateConfig;
    private final Action enclosingAction;

    private final Deque<Action> enclosingActionStack = new ArrayDeque<>();
    private final Deque<Tag> actionTagStack = new ArrayDeque<>();
    private final Deque<TemplateBodyBuilder> bodyBuilderStack = new ArrayDeque<>();

    public TemplateElementProducer(final TemplateConfig templateConfig, final Action enclosingAction, final TemplateBodyBuilder builder) {
        this.templateConfig = templateConfig;
        this.enclosingAction = enclosingAction;
        bodyBuilderStack.push(builder);
    }

    @Override
    public void visit(Tag t) {
        if ( isTemplateAction(t) ) {
            if (t.isEmptyTag()) {
                emitTemplateActionHandler(t, null);
                return;
            }

            // Stack it and hope there is an end tag...
            actionTagStack.push(t);
            bodyBuilderStack.push(new TemplateBodyBuilder());
        } else {
            emitNodeOutput(t);
        }
    }

    @Override
    public void visit(EndTag t) {
        if ( isTemplateAction(t) ) {
            if ( t.getTagName().equals(actionTagStack.peek().getTagName()) ) {
                final TemplateBody actionBody = !actionTagStack.isEmpty() && !bodyBuilderStack.isEmpty() ? bodyBuilderStack.pop().build() : null;
                emitTemplateActionHandler(actionTagStack.pop(), actionBody);
                return;
            }
            throw new UncheckedException("Unable to process non-aligned end tag -- yet!");
        }

        emitNodeOutput(t);
    }

    private void emitTemplateActionHandler(Tag t, TemplateBody actionBody) {
        // Lookup by tag ns ...
        ActionBuilder actionBuilder = new ActionBuilder();
        actionBuilder.withStartTag(t);
        actionBuilder.withBody(actionBody);
        Action action = actionBuilder.build();

        if (!enclosingActionStack.isEmpty()) {
            action.setParent(enclosingActionStack.peek());
        }

        enclosingActionStack.push(action);
        emitTemplateBodyElement(new TemplateActionHandler(action));
    }

    private boolean isTemplateAction(Tag t) {
        return t.getTagName().startsWith("utl:");
    }

    private boolean isTemplateAction(EndTag t) {
        return t.getTagName().startsWith("utl:");
    }

    private void emitNodeOutput(Node node) {
        emitTemplateBodyElement(new NodeContentEmitter(node));
    }

    private void emitTemplateBodyElement(final TemplateBodyElement templateBodyElement) {
        if (bodyBuilderStack.isEmpty()) bodyBuilderStack.push(new TemplateBodyBuilder());
        bodyBuilderStack.peek().addElement(templateBodyElement);
    }

    @Override
    public void visit(Comment c) {
        emitNodeOutput(c);
    }

    @Override
    public void visit(Text t) {
        emitTemplateBodyElement(templateConfig.isInterpolate() ? new InterpolatedTextContentEmitter(t) : new NodeContentEmitter(t) );
    }

    @Override
    public void visit(Newline n) {
        emitNodeOutput(n);
    }

    @Override
    public void visit(Annotation a) {
        emitNodeOutput(a);
    }
}
