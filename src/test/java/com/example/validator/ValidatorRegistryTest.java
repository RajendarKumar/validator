package com.example.validator;

import com.example.validator.rules.FieldValidator;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorRegistryTest {
    private ValidatorRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new ValidatorRegistry();
    }

    @Test
    void shouldLoadAllDefaultValidators() {
        assertTrue(registry.getRegisteredValidatorTypes().contains("required"));
        assertTrue(registry.getRegisteredValidatorTypes().contains("pattern"));
        assertTrue(registry.getRegisteredValidatorTypes().contains("minLength"));
        assertTrue(registry.getRegisteredValidatorTypes().contains("maxLength"));
        assertTrue(registry.getRegisteredValidatorTypes().contains("min"));
    }

    @Test
    void shouldRegisterNewValidator() {
        FieldValidator customValidator = new CustomTestValidator();
        registry.registerValidator(customValidator);
        assertTrue(registry.getRegisteredValidatorTypes().contains("test"));
    }

    @Test
    void shouldThrowException_WhenValidatorTypeNotFound() {
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> registry.getValidator("nonexistent")
        );
        assertEquals("Unknown validation type: nonexistent", exception.getMessage());
    }

    // Custom validator for testing
    private static class CustomTestValidator implements FieldValidator {
        @Override
        public void validate(String field, Object value, Map<String, Object> rule) {
            // Test implementation
        }

        @Override
        public String getType() {
            return "test";
        }
    }
} 