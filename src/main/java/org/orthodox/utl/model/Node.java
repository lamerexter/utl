package org.orthodox.utl.model;

import org.beanplanet.core.io.IoException;

import java.io.IOException;
import java.io.Writer;

public abstract class Node implements Visitable, Sized {
    public abstract void accept(Visitor v);
    public void writeContent(Writer writer) {
        try {
            writer.write(toString());
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }
}