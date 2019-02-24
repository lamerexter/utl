package org.orthodox.utl.actions;

public abstract class ActionBase implements Action {
    private Action parent;

    @Override
    public Action getParent() {
        return parent;
    }

    @Override
    public void setParent(Action action) {
        this.parent = parent;
    }
}
