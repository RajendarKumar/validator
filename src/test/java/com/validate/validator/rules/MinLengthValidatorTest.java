package com.validate.validator.rules;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MinLengthValidatorTest {
    private MinLengthValidator validator;
    private Map<String, Object> rule;

    @BeforeEach
    void setUp() {
        validator = new MinLengthValidator();
        rule = new HashMap<>();
    }

    @Test
    void shouldPassValidation_WhenLengthIsValid() {
        rule.put("value", 3);
        assertDoesNotThrow(() -> 
            validator.validate("testField", "test", rule)
        );
    }

    @Test
    void shouldThrowException_WhenLengthIsTooShort() {
        rule.put("value", 5);
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate("testField", "test", rule)
        );
        assertEquals("testField must be at least 5 characters", exception.getMessage());
    }

    @Test
    void shouldReturnCorrectType() {
        assertEquals("minLength", validator.getType());
    }
} 