package org.orthodox.utl.model.transforms;

import org.orthodox.utl.actions.Action;
import org.orthodox.utl.actions.ActionContext;
import org.orthodox.utl.actions.TemplateBody;
import org.orthodox.utl.actions.TemplateBodyElement;

import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CompositeTemplateBody implements TemplateBody {
    private List<TemplateBodyElement> components;

    public CompositeTemplateBody(List<TemplateBodyElement> components) {
        this.components = components;
    }

    @Override
    public void setEnclosingAction(Action enclosingAction) {
        components.forEach(e -> e.setEnclosingAction(enclosingAction));
    }

    @Override
    public void writeTo(ActionContext context) {
        for (TemplateBodyElement component : components) {
            component.writeTo(context);
        }
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<TemplateBodyElement> iterator() {
        return components.iterator();
    }
}
