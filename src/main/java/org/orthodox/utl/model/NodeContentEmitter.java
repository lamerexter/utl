package org.orthodox.utl.model;

import org.orthodox.utl.actions.ActionContext;
import org.orthodox.utl.actions.TemplateBody;

import java.io.Writer;

public class NodeContentEmitter implements TemplateBody {
    private Node node;

    public NodeContentEmitter(Node node) {
        this.node = node;
    }

    @Override
    public void writeTo(ActionContext context) {
        node.writeContent(context);
    }
}
