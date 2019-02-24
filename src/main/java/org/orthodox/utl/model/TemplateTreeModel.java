package org.orthodox.utl.model;

import org.beanplanet.core.models.tree.AbstractTree;
import org.beanplanet.core.models.tree.SwingTreeModelAdapter;
import org.orthodox.utl.parser.TemplateParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class TemplateTreeModel extends AbstractTree<Object> {
    public TemplateTreeModel(Object root) {
        super(root);
    }

    public Object getParent(Object child) {
        return null;
    }

    public Object getChild(Object parent, int childIndex) {
        if (parent instanceof Template) {
            Template template = (Template)parent;
            if (template.getElements() != null) {
                return template.getElements().getElement(childIndex);
            }
        } else if (parent instanceof ElementSequence) {
            return ((ElementSequence)parent).getElement(childIndex);
        }

        return null;
    }

    public int getNumberOfChildren(Object parent) {
        if (parent instanceof Template) {
            Template template = (Template)parent;
            if (template.getElements() != null) {
                return template.getElements().size();
            }
        } else if (parent instanceof ElementSequence) {
            return ((ElementSequence)parent).size();
        }

        return 0;
    }

    public int getIndexOfChild(Object parent, Object child) {
        return 0;
    }

    public List<Object> getChildren(Object parent) {
        return null;
    }

    public static void main(String[] args) throws Exception {
        try (InputStream is = new URL("https://www.google.com").openStream()) {
            TemplateParser parser = new TemplateParser(is, "UTF-8");
            Template doc = parser.Template();

            JFrame frame = new JFrame("Template Model");
            frame.addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                    System.exit(0);
                }
            });

            JTree tree = new JTree(new SwingTreeModelAdapter(new TemplateTreeModel(doc)));
            JScrollPane scroller = new JScrollPane(tree);
            frame.getContentPane().add(scroller, BorderLayout.CENTER);
            frame.setSize(800, 600);
            frame.setVisible(true);
        }
    }
}
