package com.example.validator;

import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ObjectValidator {
    private static final Logger logger = LoggerFactory.getLogger(ObjectValidator.class);
    private final ValidatorRegistry validatorRegistry;

    public ObjectValidator() {
        this.validatorRegistry = new ValidatorRegistry();
    }

    public ValidatorRegistry getValidatorRegistry() {
        return validatorRegistry;
    }

    public void validateObject(Object obj, List<Map<String, Object>> validations) {
        if (obj == null) {
            throw new ValidationException("Object cannot be null");
        }

        for (Map<String, Object> validation : validations) {
            String fieldName = (String) validation.get("field");
            try {
                Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(obj);

                // Check if the field is required
                boolean isRequired = isFieldRequired(validation);

                String type = (String) validation.get("type");
                if ("object".equals(type)) {
                    validateNestedObject(value, (List<Map<String, Object>>) validation.get("fields"), isRequired);
                } else if ("list".equals(type)) {
                    validateList(value, validation, isRequired);
                } else {
                    validateField(fieldName, value, (List<Map<String, Object>>) validation.get("rules"));
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error("Error validating field: {}", fieldName, e);
                throw new ValidationException("Error accessing field: " + fieldName, e);
            }
        }
    }

    private boolean isFieldRequired(Map<String, Object> validation) {
        List<Map<String, Object>> rules = (List<Map<String, Object>>) validation.get("rules");
        if (rules == null) {
            return false;
        }
        return rules.stream()
            .anyMatch(rule -> "required".equals(rule.get("type")));
    }

    private void validateNestedObject(Object value, List<Map<String, Object>> fields, boolean isRequired) {
        if (value == null) {
            if (isRequired) {
                throw new ValidationException("Required nested object cannot be null");
            }
            return; // Exit validation if object is null and not required
        }
        
        // If object is present (not null), validate its fields regardless of whether the object itself is required
        validateObject(value, fields);
    }

    private void validateList(Object value, Map<String, Object> validation, boolean isRequired) {
        // First check if list is null
        if (value == null) {
            if (isRequired) {
                throw new ValidationException("Required list cannot be null");
            }
            return; // Exit validation if list is null and not required
        }

        List<Object> list = (List<Object>) value;
        List<Map<String, Object>> rules = (List<Map<String, Object>>) validation.get("rules");
        List<Map<String, Object>> itemValidations = 
            (List<Map<String, Object>>) validation.get("itemValidations");

        // Check if list is empty when required
        if (isRequired && list.isEmpty()) {
            throw new ValidationException("Required list cannot be empty");
        }

        // Validate list rules (e.g., minSize) only if list is not empty
        if (!list.isEmpty() && rules != null) {
            for (Map<String, Object> rule : rules) {
                validatorRegistry.getValidator((String) rule.get("type"))
                    .validate("list", list, rule);
            }
        }

        // Validate each item in the list if itemValidations are present
        if (itemValidations != null && !list.isEmpty()) {
            for (Object item : list) {
                validateObject(item, itemValidations);
            }
        }
    }

    private void validateField(String field, Object value, List<Map<String, Object>> rules) {
        for (Map<String, Object> rule : rules) {
            String type = (String) rule.get("type");
            validatorRegistry.getValidator(type).validate(field, value, rule);
        }
    }
} 