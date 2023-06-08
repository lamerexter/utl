package org.orthodox.utl.model;

public class EndTag extends Node {

    /** The name of the Tag. */
    public String tagName;

    /** Constructor. */
    public EndTag(String t) {
        tagName = t;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public int getLength() {
        return 3 + tagName.length();
    }

    public String getTagName() {
        return tagName;
    }

    public String toString() {
        return "</" + tagName + ">";
    }
}

