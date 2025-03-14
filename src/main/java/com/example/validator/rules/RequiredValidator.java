package com.example.validator.rules;

import jakarta.validation.ValidationException;
import java.util.Map;

public class RequiredValidator implements FieldValidator {
    @Override
    public void validate(String field, Object value, Map<String, Object> rule) {
        if (value == null) {
            throw new ValidationException(field + " is required");
        }
    }

    @Override
    public String getType() {
        return "required";
    }
} 