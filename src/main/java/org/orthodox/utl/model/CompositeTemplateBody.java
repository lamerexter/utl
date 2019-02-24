package org.orthodox.utl.model;

import org.orthodox.utl.actions.TemplateBody;

import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CompositeTemplateBody implements TemplateBody, Iterable<TemplateBody> {
    private List<TemplateBody> components = new LinkedList<>();

    public CompositeTemplateBody(List<TemplateBody> components) {
        this.components = components;
    }

    @Override
    public void writeTo(Writer writer) {
        for (TemplateBody component : components) {
            component.writeTo(writer);
        }
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<TemplateBody> iterator() {
        return components.iterator();
    }
}
