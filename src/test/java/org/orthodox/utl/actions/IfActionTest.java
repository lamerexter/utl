package org.orthodox.utl.actions;

import org.junit.jupiter.api.Test;
import org.orthodox.utl.Utl;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.orthodox.utl.Utl.expand;

class IfActionTest {

    public static final String TEMPLATE_CONTENT = "The template content";

    @Test
    public void whenConditionEvaluatesToTrue_thenIncludesThenPart() {
        assertThat(expand(format("<utl:if condition='1 == 1'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(TEMPLATE_CONTENT));
    }

    @Test
    public void whenConditionEvaluatesToFalse_thenDoesNotIncludeThenPart() {
        assertThat(expand(format("<utl:if condition='1 == 2'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(""));
    }

    @Test
    public void whenEmptyEvaluatesToTrue_thenIncludesThenPart() {
        assertThat(expand(format("<utl:if isEmpty='[]'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(TEMPLATE_CONTENT));
        assertThat(expand(format("<utl:if isEmpty='{}'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(TEMPLATE_CONTENT));
        assertThat(expand(format("<utl:if isEmpty='1 == 2'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(TEMPLATE_CONTENT));
        assertThat(expand(format("<utl:if isEmpty='String(\"\")'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(TEMPLATE_CONTENT));
        assertThat(expand(format("<utl:if isEmpty='null'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(TEMPLATE_CONTENT));
    }

    @Test
    public void whenEmptyEvaluatesToFalse_thenDoesNotIncludeThenPart() {
        assertThat(expand(format("<utl:if isEmpty='[1, 2, 3]'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(""));
        assertThat(expand(format("<utl:if isEmpty='{1, 2, 3}'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(""));
        assertThat(expand(format("<utl:if isEmpty='1 == 1'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(""));
        assertThat(expand(format("<utl:if isEmpty='String(\"Hello World!\")'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(""));
    }

    @Test
    public void whenNotEmptyEvaluatesTotrue_thenDoesNotIncludeThenPart() {
        assertThat(expand(format("<utl:if notEmpty='[1, 2, 3]'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(TEMPLATE_CONTENT));
        assertThat(expand(format("<utl:if notEmpty='{1, 2, 3}'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(TEMPLATE_CONTENT));
        assertThat(expand(format("<utl:if notEmpty='1 == 1'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(TEMPLATE_CONTENT));
        assertThat(expand(format("<utl:if notEmpty='String(\"Hello World!\")'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(TEMPLATE_CONTENT));
    }

    @Test
    public void whenNotEmptyEvaluatesToFalse_thenIncludesThenPart() {
        assertThat(expand(format("<utl:if notEmpty='[]'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(""));
        assertThat(expand(format("<utl:if notEmpty='{}'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(""));
        assertThat(expand(format("<utl:if notEmpty='null'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(""));
        assertThat(expand(format("<utl:if notEmpty='1 == 2'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(""));
        assertThat(expand(format("<utl:if notEmpty='String(\"\")'>%s</utl:if>", TEMPLATE_CONTENT)), equalTo(""));
    }
}