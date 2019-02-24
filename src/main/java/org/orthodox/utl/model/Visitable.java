package org.orthodox.utl.model;

public interface Visitable {
    void accept(Visitor visitor);
}
