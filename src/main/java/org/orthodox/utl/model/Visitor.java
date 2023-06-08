package org.orthodox.utl.model;

public interface Visitor {
    void visit(Tag t);

    void visit(EndTag t);

    void visit(Comment c);

    void visit(Text t);

    void visit(Newline n);

    void visit(Annotation a);

    default void visit(TagBlock bl) {
        bl.startTag.accept(this);
        visit(bl.body);
        bl.endTag.accept(this);
    }

    default void visit(ElementSequence s) {
        for (Node node : s) {
            node.accept(this);
        }
    }

    default void visit(Template d) {
        start();
        visit(d.elements);
        finish();
    }

    default void start() {
    }

    default void finish() {
    }
}
