package com.example.validator.rules;

import java.util.Map;

public interface FieldValidator {
    void validate(String field, Object value, Map<String, Object> rule);
    String getType();
} 