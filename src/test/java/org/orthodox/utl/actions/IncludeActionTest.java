package org.orthodox.utl.actions;

import org.beanplanet.core.beans.JavaBean;
import org.beanplanet.core.io.FileUtil;
import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.io.resource.FileResource;
import org.beanplanet.core.io.resource.StringResource;
import org.beanplanet.core.io.resource.resolution.DefaultResourceResolver;
import org.beanplanet.core.lang.TypeUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;

class IncludeActionTest {
    @Test
    public void doAction_IncludeStaticBodyContentUnconditionally() throws Exception {
        // Given
        final String TEMPLATE_CONTENT = "The template content";
        StringWriter templateActionOutput = new StringWriter();
        TemplateBody templateBody = new StaticTemplateBody(new StringResource(TEMPLATE_CONTENT));
        ActionContext actionContext = new DefaultActionContext(new DefaultResourceResolver(), templateActionOutput);
        IncludeAction includeAction = new IncludeAction();

        // When
        includeAction.doAction(templateBody, actionContext);

        // Then
        assertThat(templateActionOutput.toString(), equalTo(TEMPLATE_CONTENT));
    }

    @Test
    public void doAction_IncludeDynamicBodyContentUnconditionally() throws Exception {
        // Given
        StringWriter templateActionOutput = new StringWriter();
        ActionContext actionContext = new DefaultActionContext(new DefaultResourceResolver(), templateActionOutput);
        URL resource = getClass().getResource("include-subdir1/some-content.txt");
        String templateStr = String.format("Prologue<utl:include src='%s' />Epilogue", resource);
        TemplateBody templateBody = actionContext.parse(new StringResource(templateStr));
        IncludeAction includeAction = new IncludeAction();

        // When
        includeAction.doAction(templateBody, actionContext);

        // Then
        assertThat(templateActionOutput.toString(), equalTo("PrologueSome content line1\nSome content line2Epilogue"));
    }

    @Test
    public void doAction_IncludeStaticContentFromFile() throws Exception {
        File tempFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        try {
            // Given
            StringWriter templateActionOutput = new StringWriter();
            TemplateBody templateBody = mock(TemplateBody.class);
            ActionContext actionContext = new DefaultActionContext(new DefaultResourceResolver(), templateActionOutput);
            IncludeAction includeAction = new JavaBean<>(new IncludeAction())
                    .with("src", tempFile.getAbsolutePath())
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
    public void doAction_IncludeStaticContentFromUri() throws Exception {
        // Given
        TemplateBody templateBody = mock(TemplateBody.class);
        StringWriter templateActionOutput = new StringWriter();
        ActionContext actionContext = new DefaultActionContext(new DefaultResourceResolver(), templateActionOutput);
        URL resource = getClass().getResource("include-subdir1/some-content.txt");
        IncludeAction includeAction = new JavaBean<>(new IncludeAction())
                .with("src", resource.toURI().toString())
                .getBean();
        // When
        includeAction.doAction(templateBody, actionContext);

        // Then
        assertThat(templateActionOutput.toString(), equalTo("Some content line1\nSome content line2"));
    }

    @Test
    public void doAction_IncludeNestedDynamicContentFromFilesAbsolutely() throws Exception {
        File outermostTemplateFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        File innerTemplateFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        File innermostTemplateFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        try {
            // Given
            StringWriter templateActionOutput = new StringWriter();
            TemplateBody templateBody = mock(TemplateBody.class);
            ActionContext actionContext = new DefaultActionContext(new DefaultResourceResolver(), templateActionOutput);
            IncludeAction includeAction = new JavaBean<>(new IncludeAction())
                    .with("src", outermostTemplateFile.getAbsolutePath())
                    .getBean();

            IoUtil.transferAndClose(
                    new StringResource(
                            String.format(
                                    "O1 file Content - prologue\n"+
                                    "<utl:include src='%s' />\n" +
                                    "O1 file Content - epilogue\n",
                                    innerTemplateFile.getAbsolutePath())
                    ),
                    new FileResource(outermostTemplateFile));
            IoUtil.transferAndClose(
                    new StringResource(
                            String.format(
                                    "O2 file Content - prologue\n"+
                                            "<utl:include src='%s' />\n" +
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

    @Test
    public void doAction_IncludeNestedDynamicContentFromUrisAbsolutely() throws Exception {
        File outermostTemplateFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        File innerTemplateFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        File innermostTemplateFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        try {
            // Given
            StringWriter templateActionOutput = new StringWriter();
            TemplateBody templateBody = mock(TemplateBody.class);
            ActionContext actionContext = new DefaultActionContext(new DefaultResourceResolver(), templateActionOutput);
            IncludeAction includeAction = new JavaBean<>(new IncludeAction())
                    .with("src", outermostTemplateFile.getAbsolutePath())
                    .getBean();

            IoUtil.transferAndClose(
                    new StringResource(
                            String.format(
                                    "O1 file Content - prologue\n"+
                                            "<utl:include src='%s' />\n" +
                                            "O1 file Content - epilogue\n",
                                    innerTemplateFile.toURI().toURL())
                    ),
                    new FileResource(outermostTemplateFile));
            IoUtil.transferAndClose(
                    new StringResource(
                            String.format(
                                    "O2 file Content - prologue\n"+
                                            "<utl:include src='%s' />\n" +
                                            "O2 file Content - epilogue\n",
                                    innermostTemplateFile.toURI().toURL())
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

    @Test
    public void doAction_IncludeNested1LevelDynamicContentFromFilesRelatively() throws Exception {
        // Given
        TemplateBody templateBody = mock(TemplateBody.class);
        StringWriter templateActionOutput = new StringWriter();
        ActionContext actionContext = new DefaultActionContext(new DefaultResourceResolver(), templateActionOutput);
        URL resource = getClass().getResource("IncludeAction_nested_1_level.txt");
        IncludeAction includeAction = new JavaBean<>(new IncludeAction())
                .with("src", resource.toURI())
                .getBean();
        // When
        includeAction.doAction(templateBody, actionContext);

        // Then
        assertThat(templateActionOutput.toString(), equalTo("1Level-Prologue\nSome content line1\nSome content line2\n1Level-Epilogue\n"));
    }

    @Test
    public void doAction_IncludeNested2LevelsDynamicContentFromFilesRelatively() throws Exception {
        // Given
        TemplateBody templateBody = mock(TemplateBody.class);
        StringWriter templateActionOutput = new StringWriter();
        ActionContext actionContext = new DefaultActionContext(new DefaultResourceResolver(), templateActionOutput);
        URL resource = getClass().getResource("IncludeAction_nested_2_levels.txt");
        IncludeAction includeAction = new JavaBean<>(new IncludeAction())
                .with("src", resource.toURI())
                .getBean();
        // When
        includeAction.doAction(templateBody, actionContext);

        // Then
        assertThat(templateActionOutput.toString(), equalTo("2Levels-Prologue\n1Level-Prologue\nSome content line1\nSome content line2\n1Level-Epilogue\n\n2Levels-Epilogue\n"));
    }
}