package org.orthodox.utl.actions;

import org.beanplanet.core.io.FileUtil;
import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.io.resource.ByteArrayOutputStreamResource;
import org.beanplanet.core.io.resource.FileResource;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.StringResource;
import org.beanplanet.core.io.resource.resolution.DefaultResourceResolver;
import org.beanplanet.core.lang.TypeUtil;
import org.junit.jupiter.api.Test;
import org.orthodox.utl.Utl;

import java.io.File;
import java.net.URL;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class IncludeActionTest {
    @Test
    public void doAction_IncludeStaticBodyContentUnconditionally() throws Exception {
        // Given
        final String TEMPLATE_CONTENT = "The template content";

        // When
        final String result = Utl.expand(format("<utl:include>%s</utl:include>", TEMPLATE_CONTENT));

        // Then
        assertThat(result, equalTo(TEMPLATE_CONTENT));
    }

    @Test
    public void doAction_IncludeDynamicBodyContentUnconditionally() throws Exception {
        // Given
        URL resource = getClass().getResource("include-subdir1/some-content.txt");
        String templateStr = format("Prologue<utl:include src='%s' />Epilogue", resource);

        // When
        final String result = Utl.expand(new DefaultResourceResolver(), templateStr);

        // Then
        assertThat(result, equalTo("PrologueSome content line1\nSome content line2Epilogue"));
    }

    @Test
    public void doAction_IncludeStaticContentFromFile() throws Exception {
        // Given
        File tempFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        final String TEMPLATE_CONTENT = "The template content";
        IoUtil.transferAndClose(new StringResource(TEMPLATE_CONTENT), new FileResource(tempFile));

        try {
            // When
            final Resource result = Utl.expand(new DefaultResourceResolver(), new FileResource(tempFile), new ByteArrayOutputStreamResource());

            // Then
            assertThat(result.readFullyAsString(), equalTo(TEMPLATE_CONTENT));
        } finally {
            FileUtil.deleteIgnoringErrors(tempFile);
        }
    }

    @Test
    public void doAction_IncludeStaticContentFromUri() throws Exception {
        // Given
        URL resource = getClass().getResource("include-subdir1/some-content.txt");
        String templateStr = format("<utl:include src='%s' />", resource);

        // When
        final String result = Utl.expand(new DefaultResourceResolver(), templateStr);

        // Then
        assertThat(result, equalTo("Some content line1\nSome content line2"));
    }

    @Test
    public void doAction_IncludeNestedDynamicContentFromFilesAbsolutely() throws Exception {
        // Given
        File outermostTemplateFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        File innerTemplateFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        File innermostTemplateFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        String templateStr = format("<utl:include src='%s' />", outermostTemplateFile.getAbsolutePath());

        try {
            IoUtil.transferAndClose(
                    new StringResource(
                            format(
                                    "O1 file Content - prologue\n" +
                                            "<utl:include src='%s' />\n" +
                                            "O1 file Content - epilogue\n",
                                    innerTemplateFile.getAbsolutePath()
                            )
                    ),
                    new FileResource(outermostTemplateFile)
            );
            IoUtil.transferAndClose(
                    new StringResource(
                            format(
                                    "O2 file Content - prologue\n" +
                                            "<utl:include src='%s' />\n" +
                                            "O2 file Content - epilogue\n",
                                    innermostTemplateFile.getAbsolutePath()
                            )
                    ),
                    new FileResource(innerTemplateFile)
            );
            IoUtil.transferAndClose(new StringResource("This is the innermost content"), new FileResource(innermostTemplateFile));

            // When
            final String result = Utl.expand(new DefaultResourceResolver(), templateStr);

            // Then
            assertThat(result, equalTo("O1 file Content - prologue\nO2 file Content - prologue\nThis is the innermost content\nO2 file Content - epilogue\n\nO1 file Content - epilogue\n"));
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
        String templateStr = format("<utl:include src='%s' />", outermostTemplateFile.getAbsolutePath());

        try {
            // Given
            IoUtil.transferAndClose(
                    new StringResource(
                            format(
                                    "O1 file Content - prologue\n" +
                                            "<utl:include src='%s' />\n" +
                                            "O1 file Content - epilogue\n",
                                    innerTemplateFile.toURI().toURL()
                            )
                    ),
                    new FileResource(outermostTemplateFile)
            );
            IoUtil.transferAndClose(
                    new StringResource(
                            format(
                                    "O2 file Content - prologue\n" +
                                            "<utl:include src='%s' />\n" +
                                            "O2 file Content - epilogue\n",
                                    innermostTemplateFile.toURI().toURL()
                            )
                    ),
                    new FileResource(innerTemplateFile)
            );
            IoUtil.transferAndClose(new StringResource("This is the innermost content"), new FileResource(innermostTemplateFile));

            // When
            final String result = Utl.expand(new DefaultResourceResolver(), templateStr);

            // Then
            assertThat(result, equalTo("O1 file Content - prologue\nO2 file Content - prologue\nThis is the innermost content\nO2 file Content - epilogue\n\nO1 file Content - epilogue\n"));
        } finally {
            FileUtil.deleteIgnoringErrors(outermostTemplateFile);
            FileUtil.deleteIgnoringErrors(innerTemplateFile);
            FileUtil.deleteIgnoringErrors(innermostTemplateFile);
        }
    }

    @Test
    public void doAction_IncludeNested1LevelDynamicContentFromFilesRelatively() throws Exception {
        // Given
        URL resource = getClass().getResource("IncludeAction_nested_1_level.txt");
        String templateStr = format("<utl:include src='%s' />", resource);

        // When
        final String result = Utl.expand(new DefaultResourceResolver(), templateStr);

        // Then
        assertThat(result, equalTo("1Level-Prologue\nSome content line1\nSome content line2\n1Level-Epilogue\n"));
    }

    @Test
    public void doAction_IncludeNested2LevelsDynamicContentFromFilesRelatively() throws Exception {
        // Given
        URL resource = getClass().getResource("IncludeAction_nested_2_levels.txt");
        String templateStr = format("<utl:include src='%s' />", resource.toURI());

        // When
        final String result = Utl.expand(new DefaultResourceResolver(), templateStr);

        // Then
        assertThat(result, equalTo("2Levels-Prologue\n1Level-Prologue\nSome content line1\nSome content line2\n1Level-Epilogue\n\n2Levels-Epilogue\n"));
    }

    @Test
    public void doAction_IncludeBodyContent_withContextFreeInterpolatedValue() throws Exception {
        // Given
        URL resource = getClass().getResource("include/simple-text-with-context-free-interpolated-value.txt");
        String templateStr = format("<utl:include src='%s' />", resource.toURI());

        // When
        final String result = Utl.expand(new DefaultResourceResolver(), templateStr);

        // Then
        assertThat(result, equalTo("Lorem ipsum dolor sit amet2, consectetur adipiscing elit,\nsed do eiusmod tempor incididunt ut4 labore et dolore magna aliqua."));
    }
}