package com.validate.validator.config;

import org.yaml.snakeyaml.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SuppressWarnings("unchecked")
public class ValidationConfig {
    private static final Logger logger = LoggerFactory.getLogger(ValidationConfig.class);
    private Map<String, Map<String, Object>> rules ;
    private static final String PROPERTIES_FILE = "validation-config.properties";

    public ValidationConfig() {
        rules = new HashMap<>();
        Properties properties = loadProperties();
        String rules= (String) properties.get("validate.validator.rule.names");
        String [] names = rules.split(",");
        for(String name : names)
            loadConfig(name.concat(".yml"));
    }
    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new IOException("Unable to find " + PROPERTIES_FILE);
            }
            props.load(input);
            logger.info("Successfully loaded validation properties");
        } catch (IOException e) {
            logger.error("Failed to load properties file: {}", PROPERTIES_FILE, e);
            throw new RuntimeException("Failed to load properties file", e);
        }
        return props;
    }

    private void loadConfig(final String rulesName) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(rulesName);
            Map<String, Object> config = yaml.load(inputStream);
            rules.put(rulesName.replace(".yml",""),config);
            logger.info("Validation configuration loaded successfully");
        } catch (Exception e) {
            logger.error("Failed to load validation configuration", e);
            throw new RuntimeException("Failed to load validation configuration", e);
        }
    }

    public Map<String, Object> getRequestValidation(String ruleName, String endpoint) {
        Map<String, Object> config = rules.get(ruleName);
        Map<String, Object> requests = (Map<String, Object>) config.get("requests");
        return (Map<String, Object>) requests.get(endpoint);
    }

    public Map<String, Object> getResponseValidation(String ruleName, String endpoint) {
        Map<String, Object> config = rules.get(ruleName);
        Map<String, Object> responses = (Map<String, Object>) config.get("responses");
        return (Map<String, Object>) responses.get(endpoint);
    }
} 