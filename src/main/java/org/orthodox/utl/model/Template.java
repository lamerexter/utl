package org.orthodox.utl.model;

public class Template implements Visitable {
    ElementSequence elements;

    /** Constructor. */
    public Template(ElementSequence s) {
        elements = s;
    }

    public ElementSequence getElements() {
        return elements;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    /**
     * Abstract class for HTML elements.  Enforces support for Visitors.
     */
    public static abstract class HtmlElement implements Visitable, Sized {
        public abstract void accept(Visitor v);
    }
}