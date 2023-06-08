package org.orthodox.utl.actions;

import lombok.Getter;
import lombok.Setter;
import org.beanplanet.core.util.StringUtil;

import static org.beanplanet.core.util.StringUtil.isBlank;

@Getter
@Setter
public class SetAction extends ActionBase {
    private String name;
    private String value;
    private String elValue;

    @Override
    public void doAction(final ActionContext context) {
        Object setValue = null;
        if ( !isBlank(elValue) ) {
            setValue = context.evaluate(Object.class, elValue);
        } else {
            setValue = value;
        }

        context.setVariable(name, setValue);
    }
}
