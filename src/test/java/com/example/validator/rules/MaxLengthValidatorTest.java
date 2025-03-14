package com.example.validator.rules;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MaxLengthValidatorTest {
    private MaxLengthValidator validator;
    private Map<String, Object> rule;

    @BeforeEach
    void setUp() {
        validator = new MaxLengthValidator();
        rule = new HashMap<>();
    }

    @Test
    void shouldPassValidation_WhenLengthIsValid() {
        rule.put("value", 5);
        assertDoesNotThrow(() -> 
            validator.validate("testField", "test", rule)
        );
    }

    @Test
    void shouldThrowException_WhenLengthIsTooLong() {
        rule.put("value", 3);
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate("testField", "test", rule)
        );
        assertEquals("testField must not exceed 3 characters", exception.getMessage());
    }

    @Test
    void shouldReturnCorrectType() {
        assertEquals("maxLength", validator.getType());
    }
} 