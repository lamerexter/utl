package org.orthodox.utl.actions;

import org.beanplanet.core.beans.JavaBean;
import org.beanplanet.core.models.Builder;
import org.orthodox.utl.model.EndTag;
import org.orthodox.utl.model.Tag;

public class ActionBuilder implements Builder<Action> {
    private Tag startTag;
    private TemplateBody body;
    private EndTag endTag;

    public void withStartTag(Tag startTag) {
        this.startTag = startTag;
    }

    public void withBody(TemplateBody body) {
        this.body = body;
    }

    @Override
    public Action build() {
        Action action = null;
        // Should lookup up from registry here ...
        if ( "utl:include".equals(startTag.getTagName()) ) {
            action = new JavaBean<>(new IncludeAction())
                    .with("src", startTag.hasAttribute("src") ? startTag.getAttributeValue("src") : null)
                    .with("ISrc", startTag.hasAttribute("isrc") ? startTag.getAttributeValue("isrc") : null)
                    .with("body", body)
                    .getBean();
        } else if ( "utl:for".equals(startTag.getTagName()) ) {
            action = new JavaBean<>(new ForAction())
                    .with("var", startTag.hasAttribute("var") ? startTag.getAttributeValue("var") : null)
                    .with("indexVar", startTag.hasAttribute("indexVar") ? startTag.getAttributeValue("indexVar") : null)
                    .with("in", startTag.hasAttribute("in") ? startTag.getAttributeValue("in") : null)
                    .with("body", body)
                    .getBean();
        } else if ( "utl:set".equals(startTag.getTagName()) ) {
            action = new JavaBean<>(new SetAction())
                    .with("name", startTag.hasAttribute("name") ? startTag.getAttributeValue("name") : null)
                    .with("elValue", startTag.hasAttribute("elValue") ? startTag.getAttributeValue("elValue") : null)
                    .with("value", startTag.hasAttribute("value") ? startTag.getAttributeValue("value") : null)
                    .getBean();
        } else if ( "utl:if".equals(startTag.getTagName()) ) {
            action = new JavaBean<>(new IfAction())
                    .with("condition", startTag.hasAttribute("condition") ? startTag.getAttributeValue("condition") : null)
                    .with("empty", startTag.hasAttribute("isEmpty") ? startTag.getAttributeValue("isEmpty") : null)
                    .with("notEmpty", startTag.hasAttribute("notEmpty") ? startTag.getAttributeValue("notEmpty") : null)
                    .with("then", body)
                    .getBean();
        }

        if ( action != null ) {
            if ( body != null ){
                body.setEnclosingAction(action);
            }
            return action;
        }

        throw new IllegalStateException("Unknown UTL tag: " + startTag.getTagName());
    }
}
