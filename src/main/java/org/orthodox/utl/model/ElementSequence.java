package org.orthodox.utl.model;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A sequence of UTL document elements.
 */
public class ElementSequence implements Iterable<Node> {
    private List<Node> elements;

    /** Constructor. */
    public ElementSequence(int n) {
        elements = new ArrayList(n);
    }

    /** Constructor. */
    public ElementSequence() {
        elements = new ArrayList();
    }

    /** Add element to list. */
    public void addElement(Node o) {
        elements.add(o);
    }

    /**
     * @return the number of elements in this list.
     */
    public int size() {
        return elements.size();
    }

    /**
     * @return an iterator over the elements in this list in proper sequence.
     */
    public Iterator<Node> iterator() {
        return elements.iterator();
    }

    public Object getElement(int index) {
        return elements.get(index);
    }

    /**
     * Clear current elements and replace with given Collection.
     *
     * @param collection to replace elements with
     */
    public void setElements(List collection) {
        elements.clear();
        elements.addAll(collection);
    }
}
