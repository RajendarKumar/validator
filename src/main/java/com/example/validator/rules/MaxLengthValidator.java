package com.example.validator.rules;

import jakarta.validation.ValidationException;
import java.util.Map;

public class MaxLengthValidator implements FieldValidator {
    @Override
    public void validate(String field, Object value, Map<String, Object> rule) {
        if (value == null) return;
        
        int maxLength = ((Number) rule.get("value")).intValue();
        if (value.toString().length() > maxLength) {
            throw new ValidationException(field + " must not exceed " + maxLength + " characters");
        }
    }

    @Override
    public String getType() {
        return "maxLength";
    }
} 