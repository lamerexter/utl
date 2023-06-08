package org.orthodox.utl.model.transforms;

import lombok.Data;
import org.beanplanet.core.models.Builder;
import org.orthodox.utl.actions.TemplateBody;
import org.orthodox.utl.actions.TemplateBodyElement;

import java.util.LinkedList;
import java.util.List;

@Data
public class TemplateBodyBuilder implements Builder<TemplateBody> {
    private List<TemplateBodyElement> bodyElements = new LinkedList<>();

    public void addElement(final TemplateBodyElement bodyElement) {
        bodyElements.add(bodyElement);
    }

    @Override
    public TemplateBody build() {
        return new CompositeTemplateBody(bodyElements);
    }
}
