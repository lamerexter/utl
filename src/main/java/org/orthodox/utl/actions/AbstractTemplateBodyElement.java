package org.orthodox.utl.actions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractTemplateBodyElement implements TemplateBodyElement {
    private Action enclosingAction;
}
