package org.orthodox.utl.actions;

public interface Action {
    /**
     * Performs the action. This is guaranteed to be called after any parent action has been set by the template engine
     * through a call to {@link #setParent(Action)}.
     *
     * @param body the body of the tag which may be invoked zero or more times by the action and which will be null if
     *             the action has no body.
     * @param context the context under which this action is being invoked.
     */
    void doAction(TemplateBody body, ActionContext context);

    /**
     * The parent action, if any, for collaboration purposes.
     *
     * @return the parent enclosing action, or null if this action has no parent.
     */
    Action getParent();

    /**
     * Called by the template engine to set the parent enclosing action of this action, if any.
     *
     * @param action the action which encloses this action, which may be null if the action has no parent.
     */
    void setParent(Action action);
}
