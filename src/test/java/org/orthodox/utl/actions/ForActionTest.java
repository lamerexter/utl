package org.orthodox.utl.actions;

import org.junit.jupiter.api.Test;
import org.orthodox.utl.Utl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ForActionTest {
    @Test
    public void iteratesOverBodyContent_successfully() throws Exception {
        // Given

        // When
        final String result = Utl.expand("<utl:for var='x' in='1..5'>Hello World!</utl:for>");

        // Then
        assertThat(result, equalTo("Hello World!Hello World!Hello World!Hello World!Hello World!"));
    }

    @Test
    public void iteratesOverBodyContentAndProvidesVariablesForInterpolation_successfully() throws Exception {
        // Given

        // When
        final String result = Utl.expand("<utl:for var='x' indexVar='n' in='1..5'>Hello World ${n}:${x}!</utl:for>");

        // Then
        assertThat(result, equalTo("Hello World 0:1!Hello World 1:2!Hello World 2:3!Hello World 3:4!Hello World 4:5!"));
    }
}