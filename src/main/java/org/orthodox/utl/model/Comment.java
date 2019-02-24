package org.orthodox.utl.model;

public class Comment extends Node {
    /**
     * Note that a Comment starts and ends with two hyphen characters.
     */
    public String comment;

    /** Constructor. */
    public Comment(String c) {
        comment = c;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public int getLength() {
        return 3 + comment.length();
    }

    public String toString() {
        return "<!" + comment + ">";
    }
}

