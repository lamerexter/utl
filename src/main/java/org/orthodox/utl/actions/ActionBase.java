package org.orthodox.utl.actions;

public abstract class ActionBase implements Action {
    private Action parent;

    @Override
    public Action getParent() {
        return parent;
    }

    @Override
    public void setParent(Action action) {
        this.parent = action;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Action> T findAncestorOfType(Action from, Class<T> ancestorType) {
        if (from == null) return null;

        from = from.getParent();
        while (from != null) {
            if (ancestorType.isAssignableFrom(from.getClass())) return (T)from;
            from = from.getParent();
        }

        return null;
    }

}
