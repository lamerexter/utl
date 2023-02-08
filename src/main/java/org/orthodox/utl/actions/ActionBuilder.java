package org.orthodox.utl.actions;

import org.beanplanet.core.beans.JavaBean;
import org.beanplanet.core.models.Builder;
import org.orthodox.utl.model.ElementSequence;
import org.orthodox.utl.model.EndTag;
import org.orthodox.utl.model.Tag;

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
                .with("src", startTag.hasAttribute("src") ? startTag.getAttributeValue("src") : null)
                .getBean();
    }
}
