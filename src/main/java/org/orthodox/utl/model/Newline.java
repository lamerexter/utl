package org.orthodox.utl.model;

public class Newline extends Node {
    private String endOfLineChars;

    public Newline(String endOfLineChars) {
        this.endOfLineChars = endOfLineChars;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public int getLength() {
        return endOfLineChars.length();
    }

    public String getEndOfLineChars() {
        return endOfLineChars;
    }

    public String toString() {
        return endOfLineChars;
    }
}

