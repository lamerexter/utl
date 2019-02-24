package org.orthodox.utl.model;

import org.beanplanet.core.io.IoException;

import java.io.*;

public class TemplatePrinter implements Visitor {
    protected Writer out;

    /** Constructor. */
    public TemplatePrinter(Writer out) {
        this.out = out;
    }

    public void finish() {
        try {
            out.flush();
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void visit(Tag t) {
        safeWrite(t.toString());
    }

    public void visit(EndTag t) {
        safeWrite(t.toString());
    }

    public void visit(Comment c) {
        safeWrite(c.toString());
    }

    public void visit(Text t) {
        safeWrite(t.toString());
    }

    public void visit(Newline n) {
        safeWrite(n.toString());
    }

    public void visit(Annotation a) {
        safeWrite(a.toString());
    }

    private void safeWrite(String s) {
        try {
            out.write(s);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }
}
