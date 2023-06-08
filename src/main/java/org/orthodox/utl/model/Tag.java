package org.orthodox.utl.model;

import org.beanplanet.core.io.IoException;
import org.orthodox.utl.actions.ActionContext;

import java.io.IOException;
import java.util.Iterator;

public class Tag extends Node {
    /** The name of the tag. */
    private String tagName;
    /** A List of the tags Attributes. */
    private AttributeList attributeList;

    /**
     * Whether the tag has an empty content model
     * eg the BR and HR tags.
     */
    public boolean emptyTag = false;

    /** Constructor. */
    public Tag(String t, AttributeList a) {
        tagName = t;
        attributeList = a;
    }

    public String getTagName() {
        return tagName;
    }

    public AttributeList getAttributeList() {
        return attributeList;
    }

    public boolean isEmptyTag() {
        return emptyTag;
    }

    /** Set Tag type to Empty. */
    public void setEmpty(boolean b) {
        emptyTag = b;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    /** Whether Tag has an Attribute with given name. */
    public boolean hasAttribute(String name) {
        return attributeList.contains(name);
    }

    /**
     * Whether Tag has an Attribute with given name
     * and that Attribute has a non-null value.
     */
    public boolean hasAttributeValue(String name) {
        return attributeList.hasValue(name);
    }

    /**
     * @return the value of the Attribute with the given name or null
     */
    public String getAttributeValue(String name) {
        return attributeList.getValue(name);
    }

    public int getLength() {
        int length = 0;
        for (Iterator iterator = attributeList.attributes.iterator(); iterator.hasNext();) {
            Attribute attribute = (Attribute) iterator.next();
            length += 1 + (attribute.getLength());
        }
        return length + tagName.length() + 2 + (emptyTag ? 1 : 0);
    }

    public void writeContent(ActionContext context) {
        try {
            StringBuilder s = new StringBuilder();
            s.append("<");
            s.append(tagName);
            for (Iterator iterator = attributeList.attributes.iterator(); iterator.hasNext();) {
                Attribute attribute = (Attribute) iterator.next();
                s.append(" ");

                String attrValue = attribute.toString();
                boolean attrIsInterpolated = attrValue.contains("${");
                s.append(attrIsInterpolated ? toInterpolatedValue(context, attribute) : attribute.toString());
            }
            if (emptyTag) s.append("/");
            s.append(">");
            context.getOut().write(s.toString());
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    private String toInterpolatedValue(ActionContext context, Object obj) {
        final String text = obj.toString();
        final String interpolatedQuotedString = "\"\"\"" + text + "\"\"\"";
        return context.evaluate(String.class, interpolatedQuotedString);
    }
//
//    public String toString() {
//        StringBuilder s = new StringBuilder();
//        s.append("<");
//        s.append(tagName);
//        for (Iterator iterator = attributeList.attributes.iterator(); iterator.hasNext();) {
//            Attribute attribute = (Attribute) iterator.next();
//            s.append(" ");
//            s.append(attribute.toString());
//        }
//        if (emptyTag) s.append("/");
//        s.append(">");
//        return s.toString();
//    }
}
