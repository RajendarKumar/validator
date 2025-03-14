package com.validate.validator.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ValidationConfigTest {
    private ValidationConfig config;

    @BeforeEach
    void setUp() {
        config = new ValidationConfig();
    }

    @Test
    void shouldLoadRequestValidation() {
        Map<String, Object> validation = config.getRequestValidation("createOrder");
        assertNotNull(validation);
        assertEquals("/api/orders", validation.get("path"));
        assertEquals("POST", validation.get("method"));
    }

    @Test
    void shouldReturnNull_WhenEndpointNotFound() {
        Map<String, Object> validation = config.getRequestValidation("nonexistentEndpoint");
        assertNull(validation);
    }
} 