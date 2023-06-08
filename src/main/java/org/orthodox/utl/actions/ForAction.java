package org.orthodox.utl.actions;

import lombok.Getter;
import lombok.Setter;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.beanplanet.core.util.StringUtil.notEmpty;
import static org.beanplanet.core.util.StringUtil.notEmptyAndNotNull;

@Getter
@Setter
public class ForAction extends ActionBase {
    private String var;
    private String indexVar;
    private String in;
    private TemplateBody body;

    @Override
    public void doAction(final ActionContext context) {
        final Iterable<?> inSequence = context.evaluate(Iterable.class, in);

        if (inSequence != null && body != null) {
            int n = 0;
            for (Object value : inSequence) {
                context.setVariable(var, value);
                if ( notEmptyAndNotNull(indexVar) ) {
                    context.setVariable(indexVar, n++);
                }
                body.writeTo(context);
            }
        }
    }
}
