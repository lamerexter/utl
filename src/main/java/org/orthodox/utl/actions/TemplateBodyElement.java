package org.orthodox.utl.actions;

public interface TemplateBodyElement {
    void setEnclosingAction(Action enclosingAction);
    void writeTo(ActionContext context);
}
