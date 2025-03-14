package com.validate.validator.rules;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PatternValidatorTest {
    private PatternValidator validator;
    private Map<String, Object> rule;

    @BeforeEach
    void setUp() {
        validator = new PatternValidator();
        rule = new HashMap<>();
    }

    @Test
    void shouldPassValidation_WhenPatternMatches() {
        rule.put("value", "^[A-Z]{3}-\\d{3}$");
        assertDoesNotThrow(() -> 
            validator.validate("testField", "ABC-123", rule)
        );
    }

    @Test
    void shouldThrowException_WhenPatternDoesNotMatch() {
        rule.put("value", "^[A-Z]{3}-\\d{3}$");
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate("testField", "invalid", rule)
        );
        assertTrue(exception.getMessage().contains("testField"));
    }

    @Test
    void shouldUseCustomMessage_WhenProvided() {
        rule.put("value", "^[A-Z]{3}-\\d{3}$");
        rule.put("message", "custom message");
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate("testField", "invalid", rule)
        );
        assertEquals("testField custom message", exception.getMessage());
    }

    @Test
    void shouldReturnCorrectType() {
        assertEquals("pattern", validator.getType());
    }
} 