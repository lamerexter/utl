package org.orthodox.utl.actions;

public interface VariableHolder {
    boolean hasVariable(String name);

    <T> T getVariable(String name, Class<T> expectedType);

    default Object getVariable(String name) {
        return getVariable(name, Object.class);
    }

    void setVariable(String name, Object value);

    void removeVariable(String name);
}
