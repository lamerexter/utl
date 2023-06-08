package org.orthodox.utl.model.transforms;

import org.orthodox.utl.actions.AbstractTemplateBodyElement;
import org.orthodox.utl.actions.ActionContext;
import org.orthodox.utl.actions.TemplateBodyElement;
import org.orthodox.utl.model.Node;

public class NodeContentEmitter extends AbstractTemplateBodyElement {
    private Node node;

    public NodeContentEmitter(Node node) {
        this.node = node;
    }

    @Override
    public void writeTo(ActionContext context) {
        node.writeContent(context);
    }
}
