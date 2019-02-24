package org.orthodox.utl.model;

import static org.orthodox.utl.model.TemplateUtil.dequote;

/**
 * A Tag Attribute.
 */
public class Attribute implements Sized {
    /** The name of this Attribute. */
    public String name;
    /** The value of this Attribute, including any surrounding quotes. */
    public String value;
    /** Whether the Attribute has a value. */
    public boolean hasValue;

    /** Constructor. */
    public Attribute(String n) {
        name = n;
        hasValue = false;
    }

    /** Constructor. */
    public Attribute(String n, String v) {
        name = n;
        if (v != null) {
            value = v;
            hasValue = true;
        }
    }

    /**
     * Whether quotes are included is dependant upon the source document.
     *
     * {@inheritDoc}
     * @see Sized#getLength()
     */
    public int getLength() {
        return (hasValue ? name.length() + 1 + value.length() : name.length());
    }

    public String toString() {
        return (hasValue ? name + "=" + value : name);
    }

    /**
     * @return the value with quotes removed
     */
    public String getValue() {
        return dequote(value);
    }

    /**
     * @param v the value to set, may be null
     */
    public void setValue(String v) {
        value = v;
        if (v == null)
            hasValue = false;
        else
            hasValue = true;
    }
}
