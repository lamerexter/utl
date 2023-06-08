package org.orthodox.utl;

import org.junit.jupiter.api.Test;
import org.orthodox.universel.Universal;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.orthodox.universel.Universal.execute;

class VariableScopeTest {
    @Test
    public void elContext_isDelegatedTo_successfully() {
        // When
        MapBasedELContext elContext = new MapBasedELContext(execute(Map.class, "{ 'x': 'Hello World!' }"));
        final String result = Utl.expand(elContext, "${x}");

        // Then
        assertThat(result, equalTo("Hello World!"));
    }

    private static class MapBasedELContext implements ELContext {
        private final Map<String, Object> variables;

        public MapBasedELContext(Map<String, Object> variables) {
            this.variables = variables;
        }

        @Override
        public boolean hasVariable(String name) {
            return variables.containsKey(name);
        }

        @Override
        public <T> T getVariable(String name, Class<T> expectedType) {
            return (T)variables.get(name);
        }

        @Override
        public void setVariable(String name, Object value) {
            variables.put(name, value);
        }

        @Override
        public void removeVariable(String name) {
            variables.remove(name);
        }
    }
}