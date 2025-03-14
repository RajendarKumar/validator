package com.validate.validator.rules;

import jakarta.validation.ValidationException;
import java.util.Map;

public class PatternValidator implements FieldValidator {
    @Override
    public void validate(String field, Object value, Map<String, Object> rule) {
        if (value == null) return;

        String pattern = (String) rule.get("value");
        String message = (String) rule.get("message");
        
        if (message == null) {
            message = "must match pattern: " + pattern;
        }

        if (!value.toString().matches(pattern)) {
            throw new ValidationException(field + " " + message);
        }
    }

    @Override
    public String getType() {
        return "pattern";
    }
} 