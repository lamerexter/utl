package org.orthodox.utl.actions;

import lombok.Getter;
import lombok.Setter;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.ResourceNotFoundException;

import java.util.Optional;

import static org.beanplanet.core.util.StringUtil.notEmpty;

@Getter
@Setter
public class IncludeAction extends ActionBase {
    private String src;
    private String iSrc;
    private TemplateBody body;

    @Override
    public void doAction(final ActionContext context) {
        String includeSrc = null;
        if (src != null) {
            includeSrc = src;
        } else if ( iSrc != null ) {
            includeSrc = context.evaluate(String.class, iSrc);
        }

        if (notEmpty(includeSrc)) {
            TemplateBody dynamicTemplateBody = context.parse(this, includeFromStream(context, includeSrc).orElseThrow(() -> new ResourceNotFoundException("Included resource [" + (src != null ? src : iSrc) + "] not found")));
            dynamicTemplateBody.writeTo(context);
        }

        if (body != null) {
            body.writeTo(context);
        }
    }

    private Optional<Resource> includeFromStream(final ActionContext context, final String includeSrc) {
        return context.resolveResource(includeSrc)
                .map(r -> {
                    if (r.isAbsolute()) return r; // Already an absolute resource so no more resolution necessary

                    //--------------------------------------------------------------------------------------------------
                    // Begin resolution of the relative resource uri through ancestors.
                    //--------------------------------------------------------------------------------------------------
                    IncludeAction fromAction = this;
                    Resource fromResource = r;
                    while ((fromAction = findAncestorOfType(fromAction, IncludeAction.class)) != null) {
                        fromResource = context.resolveResource(fromAction.getSrc()).map(ancestorResource -> ancestorResource.resolve(r.getPath())).orElse(null);
                    }

                    if (fromResource == null) {
                        throw new TemplateActionException("Unable to resolve relative resource from given URI [" + includeSrc + "] through ancestry.");
                    }
                    return fromResource;
                });
    }
}
