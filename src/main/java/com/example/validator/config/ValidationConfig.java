package com.example.validator.config;

import org.yaml.snakeyaml.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;

public class ValidationConfig {
    private static final Logger logger = LoggerFactory.getLogger(ValidationConfig.class);
    private Map<String, Object> config;

    public ValidationConfig() {
        loadConfig();
    }

    private void loadConfig() {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("validation-rules.yml");
            config = yaml.load(inputStream);
            logger.info("Validation configuration loaded successfully");
        } catch (Exception e) {
            logger.error("Failed to load validation configuration", e);
            throw new RuntimeException("Failed to load validation configuration", e);
        }
    }

    public Map<String, Object> getRequestValidation(String endpoint) {
        Map<String, Object> requests = (Map<String, Object>) config.get("requests");
        return (Map<String, Object>) requests.get(endpoint);
    }

    public Map<String, Object> getResponseValidation(String endpoint) {
        Map<String, Object> responses = (Map<String, Object>) config.get("responses");
        return (Map<String, Object>) responses.get(endpoint);
    }
} 