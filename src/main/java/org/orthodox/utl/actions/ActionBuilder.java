package org.orthodox.utl.actions;

import com.sun.jndi.toolkit.url.Uri;
import org.beanplanet.core.beans.JavaBean;
import org.beanplanet.core.models.Builder;
import org.beanplanet.core.net.UriBuilder;
import org.orthodox.utl.model.ElementSequence;
import org.orthodox.utl.model.EndTag;
import org.orthodox.utl.model.Tag;

import java.io.File;
import java.net.URI;

public class ActionBuilder implements Builder<Action> {
    private Tag startTag;
    private ElementSequence childElements;
    private EndTag endTag;

    public void withStartTag(Tag startTag) {
        this.startTag = startTag;
    }

    @Override
    public Action build() {
        // Should lookup up from registry here ...

        return new JavaBean<>(new IncludeAction())
                .with("file", startTag.hasAttribute("file") ? new File(startTag.getAttributeValue("file")) : null)
                .with("uri", startTag.hasAttribute("uri") ? URI.create(startTag.getAttributeValue("uri")) : null)
                .getBean();
    }
}
