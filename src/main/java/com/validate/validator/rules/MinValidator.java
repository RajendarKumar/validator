package com.validate.validator.rules;

import jakarta.validation.ValidationException;
import java.util.Map;

public class MinValidator implements FieldValidator {
    @Override
    public void validate(String field, Object value, Map<String, Object> rule) {
        if (value == null) return;
        
        int min = ((Number) rule.get("value")).intValue();
        if (((Number) value).intValue() < min) {
            throw new ValidationException(field + " must be at least " + min);
        }
    }

    @Override
    public String getType() {
        return "min";
    }
} 