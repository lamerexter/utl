package org.orthodox.utl.model;

public class Text extends Node {
    /** The text. */
    public String text;

    /** Constructor. */
    public Text(String t) {
        text = t;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public int getLength() {
        return text.length();
    }

    public String toString() {
        return text;
    }
}

