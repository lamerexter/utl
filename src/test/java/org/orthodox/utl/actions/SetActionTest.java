package org.orthodox.utl.actions;

import org.junit.jupiter.api.Test;
import org.orthodox.utl.Utl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class SetActionTest {
    @Test
    public void setStaticValue_successfully() {
        // When
        final String result = Utl.expand("<utl:set name='x' value='Hello World!' />${x}");

        // Then
        assertThat(result, equalTo("Hello World!"));
    }

    @Test
    public void setElValue_successfully() {
        // When
        final String result = Utl.expand("<utl:set name='x' elValue=\"'Hello' + ' ' + 'World!'\" />${x}");

        // Then
        assertThat(result, equalTo("Hello World!"));
    }

    @Test
    public void setElValue_canBeUsedInScope_successfully() {
        // When
        final String result = Utl.expand("<utl:set name='x' elValue=\"'Hello' + ' ' + 'World'\" /><utl:set name='y' elValue=\"x + ' ' + 'Again!'\" />${x}, ${y}");

        // Then
        assertThat(result, equalTo("Hello World, Hello World Again!"));
    }
}