package org.orthodox.utl.actions;

import lombok.Getter;
import lombok.Setter;
import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.logging.Logger;
import org.beanplanet.core.util.StringUtil;

import java.util.Collection;

import static org.beanplanet.core.util.StringUtil.isBlank;
import static org.beanplanet.core.util.StringUtil.notEmpty;

@Getter
@Setter
public class IfAction extends ActionBase implements Logger {
    private String condition;
    private String empty;
    private String notEmpty;
    private TemplateBody then;

    @Override
    public void doAction(final ActionContext context) {
        Assert.isFalse((isBlank(condition) ? 0 : 1) + (isBlank(empty) ? 0 : 1) + (isBlank(notEmpty) ? 0 : 1) == 0, "A test condition of \"condition\", \"empty\" or \"notEmpty\" is required");
        Assert.isFalse((isBlank(condition) ? 0 : 1) + (isBlank(empty) ? 0 : 1) + (isBlank(notEmpty) ? 0 : 1) > 1, "If action contains more than one test condition of \"condition\", \"empty\" or \"notEmpty\"");

        Boolean evaluateThen;
        if ( notEmpty(condition) )
            evaluateThen = evaluateCondition(context);
        else if ( notEmpty(notEmpty) )
            evaluateThen = evaluateNotEmpty(context);
        else
            evaluateThen = evaluateEmpty(context, empty);

        if ( evaluateThen ) {
            then.writeTo(context);
        }
    }

    private Boolean evaluateNotEmpty(ActionContext context) {
        return !evaluateEmpty(context, notEmpty);
    }

    private Boolean evaluateEmpty(ActionContext context, final String emptyCondition) {
        Object result = context.evaluate(Object.class, emptyCondition);
        if ( result == null ) return Boolean.TRUE;
        if ( result.equals(Boolean.TRUE) ) return Boolean.FALSE; // TRUE =~= !empty
        if ( result.equals(Boolean.FALSE) ) return Boolean.TRUE; // FALSE =~= empty
        if (result instanceof Collection) return ((Collection)result).isEmpty();
        if (result instanceof CharSequence) return isBlank((CharSequence)result);

        return false; // Non-null
    }

    private Boolean evaluateCondition(final ActionContext context) {
        Boolean result = context.evaluate(Boolean.class, condition);
        if ( result == null ) {
            warning("Test condition {0} returned no result...assuming truthy value as false", condition);
            return false;
        }

        return result;
    }
}
