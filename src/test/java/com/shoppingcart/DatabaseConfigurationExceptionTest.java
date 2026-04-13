package com.shoppingcart;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class DatabaseConfigurationExceptionTest {

    @Test
    void constructorWithMessage_setsMessage() {
        DatabaseConfigurationException exception =
                new DatabaseConfigurationException("missing config");

        assertEquals("missing config", exception.getMessage());
    }

    @Test
    void constructorWithMessageAndCause_setsMessageAndCause() {
        RuntimeException cause = new RuntimeException("root cause");
        DatabaseConfigurationException exception =
                new DatabaseConfigurationException("load error", cause);

        assertEquals("load error", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
}
