package org.orthodox.utl.actions;

import lombok.Getter;
import lombok.Setter;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.ResourceNotFoundException;

import java.util.Optional;

@Getter @Setter
public class IncludeAction extends ActionBase {
    private String src;

    @Override
    public void doAction(final TemplateBody body, final ActionContext context) {
        if ( src != null ) {
            TemplateBody dynamicTemplateBody = context.parse(this, includeFromStream(context).orElseThrow(() -> new ResourceNotFoundException("Included resource [" + src + "] not found")));
            dynamicTemplateBody.writeTo(context);
        }

        if (body != null) {
            body.writeTo(context);
        }
    }

    private Optional<Resource> includeFromStream(final ActionContext context) {
        return context.resolveResource(src)
                .map(r -> {
                    if ( r.isAbsolute() ) return r; // Already an absolute resource so no more resolution necessary

                    //--------------------------------------------------------------------------------------------------
                    // Begin resolution of the relative resource uri through ancestors.
                    //--------------------------------------------------------------------------------------------------
                    IncludeAction fromAction = this;
                    Resource fromResource = r;
                    while ((fromAction = findAncestorOfType(fromAction, IncludeAction.class)) != null) {
                        fromResource = context.resolveResource(fromAction.getSrc()).map(ancestorResource -> ancestorResource.resolve(r.getPath())).orElse(null);
                    }

                    if (fromResource == null) {
                        throw new TemplateActionException("Unable to resolve relative resource from given URI ["+src+"] through ancestry.");
                    }
                    return fromResource;
                });
    }
}
