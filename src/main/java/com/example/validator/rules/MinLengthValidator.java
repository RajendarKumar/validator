package com.example.validator.rules;

import jakarta.validation.ValidationException;
import java.util.Map;

public class MinLengthValidator implements FieldValidator {
    @Override
    public void validate(String field, Object value, Map<String, Object> rule) {
        if (value == null) return;
        
        int minLength = ((Number) rule.get("value")).intValue();
        if (value.toString().length() < minLength) {
            throw new ValidationException(field + " must be at least " + minLength + " characters");
        }
    }

    @Override
    public String getType() {
        return "minLength";
    }
} 