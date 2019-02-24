package org.orthodox.utl.actions;

import org.beanplanet.core.beans.JavaBean;
import org.beanplanet.core.io.FileUtil;
import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.io.resource.FileResource;
import org.beanplanet.core.io.resource.StringResource;
import org.beanplanet.core.lang.TypeUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;

class IncludeActionTest {
    @Test
    public void doAction_IncludeBodyContentUnconditionally() throws Exception {
        // Given
        final String TEMPLATE_CONTENT = "The template content";
        StringWriter templateActionOutput = new StringWriter();
        TemplateBody templateBody = new StaticTemplateBody(new StringResource(TEMPLATE_CONTENT));
        ActionContext actionContext = new DefaultActionContext(templateActionOutput);
        IncludeAction includeAction = new IncludeAction();

        // When
        includeAction.doAction(templateBody, actionContext);

        // Then
        assertThat(templateActionOutput.toString(), equalTo(TEMPLATE_CONTENT));
    }

    @Test
    public void doAction_IncludeStaticContentFromFile() throws Exception {
        File tempFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        try {
            // Given
            StringWriter templateActionOutput = new StringWriter();
            TemplateBody templateBody = mock(TemplateBody.class);
            ActionContext actionContext = new DefaultActionContext(templateActionOutput);
            IncludeAction includeAction = new JavaBean<>(new IncludeAction())
                    .with("file", tempFile)
                    .getBean();
            final String TEMPLATE_CONTENT = "The template content";
            IoUtil.transferAndClose(new StringResource(TEMPLATE_CONTENT), new FileResource(tempFile));

            // When
            includeAction.doAction(templateBody, actionContext);

            // Then
            assertThat(templateActionOutput.toString(), equalTo(TEMPLATE_CONTENT));
        } finally {
            FileUtil.deleteIgnoringErrors(tempFile);
        }
    }

    @Test
    public void doAction_IncludeStaticFromUri() throws Exception {
        File tempFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        try {
            // Given
            StringWriter templateActionOutput = new StringWriter();
            TemplateBody templateBody = mock(TemplateBody.class);
            ActionContext actionContext = new DefaultActionContext(templateActionOutput);
            IncludeAction includeAction = new JavaBean<>(new IncludeAction())
                    .with("uri", tempFile.toURI())
                    .getBean();
            final String TEMPLATE_CONTENT = "The template content";
            IoUtil.transferAndClose(new StringResource(TEMPLATE_CONTENT), new FileResource(tempFile));

            // When
            includeAction.doAction(templateBody, actionContext);

            // Then
            assertThat(templateActionOutput.toString(), equalTo(TEMPLATE_CONTENT));
        } finally {
            FileUtil.deleteIgnoringErrors(tempFile);
        }
    }

    @Test
    public void doAction_IncludeNestedDynamicContentFromFile() throws Exception {
        File outermostTemplateFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        File innerTemplateFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        File innermostTemplateFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        try {
            // Given
            StringWriter templateActionOutput = new StringWriter();
            TemplateBody templateBody = mock(TemplateBody.class);
            ActionContext actionContext = new DefaultActionContext(templateActionOutput);
            IncludeAction includeAction = new JavaBean<>(new IncludeAction())
                    .with("file", outermostTemplateFile)
                    .getBean();

            IoUtil.transferAndClose(
                    new StringResource(
                            String.format(
                                    "O1 file Content - prologue\n"+
                                    "<utl:include file='%s' />\n" +
                                    "O1 file Content - epilogue\n",
                                    innerTemplateFile.getAbsolutePath())
                    ),
                    new FileResource(outermostTemplateFile));
            IoUtil.transferAndClose(
                    new StringResource(
                            String.format(
                                    "O2 file Content - prologue\n"+
                                            "<utl:include file='%s' />\n" +
                                            "O2 file Content - epilogue\n",
                                    innermostTemplateFile.getAbsolutePath())
                    ),
                    new FileResource(innerTemplateFile));
            IoUtil.transferAndClose(new StringResource("This is the innermost content"), new FileResource(innermostTemplateFile));

            // When
            includeAction.doAction(templateBody, actionContext);

            // Then
            assertThat(templateActionOutput.toString(), equalTo("O1 file Content - prologue\nO2 file Content - prologue\nThis is the innermost content\nO2 file Content - epilogue\n\nO1 file Content - epilogue\n"));
        } finally {
            FileUtil.deleteIgnoringErrors(outermostTemplateFile);
            FileUtil.deleteIgnoringErrors(innerTemplateFile);
            FileUtil.deleteIgnoringErrors(innermostTemplateFile);
        }
    }
}