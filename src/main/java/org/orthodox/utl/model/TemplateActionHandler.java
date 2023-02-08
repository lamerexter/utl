package org.orthodox.utl.model;

import org.orthodox.utl.actions.Action;
import org.orthodox.utl.actions.ActionContext;
import org.orthodox.utl.actions.DefaultActionContext;
import org.orthodox.utl.actions.TemplateBody;

import java.io.Writer;

public class TemplateActionHandler implements TemplateBody {
    private Action action;

    public TemplateActionHandler(Action action) {
        this.action = action;
    }

    @Override
    public void writeTo(ActionContext context) {
        action.doAction(null, context);
    }
}
