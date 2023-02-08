package org.orthodox.utl.model;

import org.beanplanet.core.io.IoException;
import org.orthodox.utl.actions.ActionContext;

import java.io.IOException;
import java.io.Writer;

public abstract class Node implements Visitable, Sized {
    public abstract void accept(Visitor v);
    public void writeContent(ActionContext context) {
        try {
            context.getOut().write(toString());
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }
}