package org.orthodox.utl.actions;

import org.beanplanet.core.io.IoException;
import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.io.resource.Resource;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class StaticTemplateBody implements TemplateBody {
    private Resource resource;
    private String encoding;

    public StaticTemplateBody(Resource resource, String encoding){
        this.resource = resource;
        this.encoding = encoding;
    }

    public StaticTemplateBody(Resource resource){
        this(resource, "UTF-8");
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public void writeTo(Writer writer) {
        try (Reader reader = getResource().getReader(getEncoding())){
            IoUtil.transfer(reader, writer);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }
}
