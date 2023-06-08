package org.orthodox.utl.model.transforms;

import org.orthodox.utl.actions.*;

import java.io.Writer;

public class TemplateActionHandler implements TemplateBodyElement {
    private Action action;

    public TemplateActionHandler(Action action) {
        this.action = action;
    }

    @Override
    public void setEnclosingAction(Action enclosingAction) {
        this.action.setParent(enclosingAction);
    }

    @Override
    public void writeTo(ActionContext context) {
        action.doAction(context);
    }
}
