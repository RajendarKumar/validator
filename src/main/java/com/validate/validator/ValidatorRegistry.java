package com.validate.validator;

import com.validate.validator.rules.*;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.HashSet;
import java.util.Set;

public class ValidatorRegistry {
    private static final Logger logger = LoggerFactory.getLogger(ValidatorRegistry.class);
    private final Map<String, FieldValidator> validators = new HashMap<>();

    public ValidatorRegistry() {
        // Load additional validators using SPI
        loadValidatorsFromSPI();
    }

    private void loadValidatorsFromSPI() {
        // Load any additional validators using ServiceLoader
        ServiceLoader<FieldValidator> loader = ServiceLoader.load(FieldValidator.class);
        for (FieldValidator validator : loader) {
            // Only register if not already registered
            if (!validators.containsKey(validator.getType())) {
                registerValidator(validator);
                logger.info("Loaded additional validator via SPI: {}", validator.getType());
            }
        }
    }

    public void registerValidator(FieldValidator validator) {
        validators.put(validator.getType(), validator);
        logger.debug("Registered validator: {}", validator.getType());
    }

    public FieldValidator getValidator(String type) {
        FieldValidator validator = validators.get(type);
        if (validator == null) {
            throw new ValidationException("Unknown validation type: " + type);
        }
        return validator;
    }

    // For testing/debugging
    public Set<String> getRegisteredValidatorTypes() {
        return new HashSet<>(validators.keySet());
    }
} 