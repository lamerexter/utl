package org.orthodox.utl;

import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.io.resource.FileResource;
import org.beanplanet.core.io.resource.UrlResource;
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.util.StringUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.orthodox.universel.Universal.execute;

class UtlTest {
    final String RANDOM_CHARS_EXCLUDING_UNIVERSAL_INTERPOLATED_STRING_ESCAPABLE_CHARS = StringUtil.ASCII_PRINTABLE_SPECIAL_CHARS.replaceAll("\\\\", "").replaceAll("\\$", "");

    @Test
    void empty() {
        assertThat(Utl.expand(""), equalTo(""));
    }

//    @Test
//    void nullValue() {
//        assertThat(Utl.expand("${null}"), equalTo(""));
//    }

    @Test
    void plainText() {
        assertThat(Utl.expand("Hello world!£$%^&'`"), equalTo("Hello world!£$%^&'`"));
    }

    @Test
    void action_topLevel() throws Exception {
        File tempFile = File.createTempFile(TypeUtil.getBaseName(getClass()), ".txt");
        IoUtil.transfer(StringUtil.randomChars(RANDOM_CHARS_EXCLUDING_UNIVERSAL_INTERPOLATED_STRING_ESCAPABLE_CHARS, 100, 100)+"<", tempFile);
        assertThat(Utl.expand(format("<utl:include src='%s' />", tempFile.toURI())), equalTo(new FileResource(tempFile).readFullyAsString()));
    }

    @Test
    void mixed_topLevel_uriResolvedContent() throws Exception {
        assertThat(Utl.expand("<utl:include src='/org/orthodox/utl/actions/include-subdir1/some-content.txt' />"), equalTo(new UrlResource(getClass().getResource("actions/include-subdir1/some-content.txt")).readFullyAsString()));
    }

    @Test
    void simple_interpolatedValue() throws Exception {
        final Map<?, ?> binding = execute("{ 'intOperand1': 5,  'intOperand2': 9 }");
        assertThat(Utl.expand(binding, getClass().getResource("actions/include/simple-text-with-context-interpolated-value.txt")), equalTo("Lorem ipsum dolor sit amet14, consectetur adipiscing elit,\nsed do eiusmod tempor incididunt ut45 labore et dolore magna aliqua."));
    }

    @Test
    void elementAttributes_areInterpolated() throws Exception {
        final Map<?, ?> binding = execute("{ 'attrValue': 'World' }");
        assertThat(Utl.expand(binding, "<aTag anAttr='Hello ${attrValue}!' />"), equalTo("<aTag anAttr='Hello World!'/>"));
        assertThat(Utl.expand(binding, "<aTag anAttr=\"Hello ${attrValue}!\"/>"), equalTo("<aTag anAttr=\"Hello World!\"/>"));
    }
}