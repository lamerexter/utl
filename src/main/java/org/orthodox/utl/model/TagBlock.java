package org.orthodox.utl.model;

import java.util.Iterator;

/**
 * A tag block is a composite structure consisting of a start tag
 * a sequence of HTML elements, and a matching end tag.
 */
public  class TagBlock extends Node {
    /** Tag at start of Block.*/
    public Tag startTag;
    /** Tag at end of Block.*/
    public EndTag endTag;
    /** The sequance of elements which make up the body.*/
    public ElementSequence body;

    /** Constructor. */
    public TagBlock(String name, AttributeList aList, ElementSequence b) {
        startTag = new Tag(name, aList);
        endTag = new EndTag(name);
        body = b;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public int getLength() {
        int bodyLength = 0;
        for (Iterator iterator = body.iterator(); iterator.hasNext();) {
            Template.HtmlElement htmlElement = (Template.HtmlElement) iterator.next();
            bodyLength += htmlElement.getLength();
        }
        return startTag.getLength() + bodyLength + endTag.getLength();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(startTag.toString());
        for (Iterator iterator = body.iterator(); iterator.hasNext();) {
            Template.HtmlElement htmlElement = (Template.HtmlElement) iterator.next();
            sb.append(htmlElement.toString());
        }
        sb.append(endTag.toString());
        return sb.toString();
    }

    /**
     * @return the text within a tag block
     */
    public String text() {
        StringBuilder sb = new StringBuilder();
        for (Iterator iterator = body.iterator(); iterator.hasNext();) {
            Node htmlElement = (Node) iterator.next();
            if (htmlElement instanceof Text) {
                sb.append(htmlElement.toString());
            } else if(htmlElement instanceof TagBlock)
                sb.append(((TagBlock)htmlElement).text());
        }
        return sb.toString();
    }
}

