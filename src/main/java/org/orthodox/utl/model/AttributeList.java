package org.orthodox.utl.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.orthodox.utl.model.TemplateUtil.dequote;

/**
 * A List of Attributes.
 */
public class AttributeList {
    /** The backing List. */
    public List attributes = new ArrayList();

    /** Add. */
    public void addAttribute(Attribute a) {
        attributes.add(a);
    }

    /** Whether the List contains an Attribute with the given name. */
    public boolean contains(String name) {
        for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
            Attribute attribute = (Attribute) iterator.next();
            if (attribute.name.equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    /**
     * Whether the List contains an Attribute with the given name
     * and that Attribute has a non-null value.
     */
    public boolean hasValue(String name) {
        for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
            Attribute attribute = (Attribute) iterator.next();
            if (attribute.name.equalsIgnoreCase(name) && attribute.hasValue)
                return true;
        }
        return false;
    }

    /**
     * @param name the name of the Attribute
     * @return the value of the Attribute with the given name or null
     */
    public String getValue(String name) {
        for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
            Attribute attribute = (Attribute) iterator.next();
            if (attribute.name.equalsIgnoreCase(name) && attribute.hasValue)
                return dequote(attribute.value);
        }
        return null;
    }
}
