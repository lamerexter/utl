package org.orthodox.utl.model;

/**
 * Annotations.  These are not part of the HTML document, but
 * provide a way for HTML-processing applications to insert
 * annotations into the document.  These annotations can be used by
 * other programs or can be brought to the user's attention at a
 * later time.  For example, the HtmlCollector might insert an
 * annotation to indicate that there is no corresponding start tag
 * for an end tag.
 */
public class Annotation extends Node {
    String type, text;

    /** Constructor. */
    public Annotation(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public int getLength() {
        return 14 + type.length() + text.length();
    }

    public String toString() {
        return "<!--NOTE(" + type + ") " + text + "-->";
    }
}

