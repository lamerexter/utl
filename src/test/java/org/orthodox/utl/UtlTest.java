package org.orthodox.utl;

import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.io.resource.FileResource;
import org.beanplanet.core.io.resource.UrlResource;
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.util.StringUtil;
import org.junit.jupiter.api.Test;

import java.io.File;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class UtlTest {
    @Test
    void empty() {
        assertThat(Utl.expand(""), equalTo(""));
    }

    @Test
    void plainText() {
        assertThat(Utl.expand("Hello world!£$%^&'`"), equalTo("Hello world!£$%^&'`"));
    }

    @Test
    void action_topLevel() throws Exception {
        File tempFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        IoUtil.transfer(StringUtil.randomAlphanumericAsciiPrintableSpecialChars(200), tempFile);
        assertThat(Utl.expand(format("<utl:include src='%s' />", tempFile.toURI())), equalTo(new FileResource(tempFile).readFullyAsString()));
    }

    @Test
    void mixed_topLevel_uriResolvedContent() throws Exception {
        assertThat(Utl.expand("<utl:include src='/org/orthodox/utl/actions/include-subdir1/some-content.txt' />"), equalTo(new UrlResource(getClass().getResource("actions/include-subdir1/some-content.txt")).readFullyAsString()));
    }
}