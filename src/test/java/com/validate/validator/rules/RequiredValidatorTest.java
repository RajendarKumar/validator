package com.validate.validator.rules;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequiredValidatorTest {
    private RequiredValidator validator;
    private Map<String, Object> rule;

    @BeforeEach
    void setUp() {
        validator = new RequiredValidator();
        rule = new HashMap<>();
    }

    @Test
    void shouldPassValidation_WhenValuePresent() {
        assertDoesNotThrow(() -> 
            validator.validate("testField", "value", rule)
        );
    }

    @Test
    void shouldThrowException_WhenValueNull() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate("testField", null, rule)
        );
        assertEquals("testField is required", exception.getMessage());
    }

    @Test
    void shouldReturnCorrectType() {
        assertEquals("required", validator.getType());
    }
} 