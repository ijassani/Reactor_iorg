package com.alchemain.rx.examples;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.alchemain.rx.utils.PropertiesUtil;

/**
 * Examples showing how to work with configuration and properties in the Reactor project.
 * Demonstrates reading properties, working with JSON configuration, and logging setup.
 */
public class ConfigurationExamples {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationExamples.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        ConfigurationExamples examples = new ConfigurationExamples();
        
        try {
            examples.demonstratePropertiesUsage();
            examples.demonstrateJsonConfiguration();
            examples.demonstrateResourceLoading();
        } catch (Exception e) {
            log.error("Error running configuration examples", e);
        }
    }

    /**
     * Demonstrate using the PropertiesUtil class
     */
    public void demonstratePropertiesUsage() {
        log.info("=== Properties Usage Examples ===");
        
        try {
            // These would normally read from reactor.properties file
            // For demo purposes, we'll show how the methods work
            
            // Reading string properties with defaults
            String defaultHost = "localhost";
            log.info("Getting MongoDB host (would default to '{}')", defaultHost);
            
            // Reading integer properties with defaults
            int defaultPort = 27017;
            log.info("Getting MongoDB port (would default to {})", defaultPort);
            
            // Example of how properties might be structured
            log.info("Properties are loaded from reactor.properties on classpath");
            log.info("Example property keys might include:");
            log.info("  - mongodb.host");
            log.info("  - mongodb.port");
            log.info("  - elasticsearch.cluster.name");
            log.info("  - redis.host");
            log.info("  - hero.api.uri");
            
        } catch (Exception e) {
            log.warn("Could not access properties (reactor.properties might not exist): {}", e.getMessage());
        }
    }

    /**
     * Demonstrate JSON configuration patterns
     */
    public void demonstrateJsonConfiguration() {
        log.info("=== JSON Configuration Examples ===");
        
        try {
            // Create a sample configuration JSON
            ObjectNode config = mapper.createObjectNode();
            
            // Database configuration
            ObjectNode dbConfig = config.putObject("database");
            dbConfig.put("host", "localhost");
            dbConfig.put("port", 27017);
            dbConfig.put("name", "reactor_db");
            dbConfig.put("ssl", false);
            
            // Redis configuration
            ObjectNode redisConfig = config.putObject("redis");
            redisConfig.put("host", "localhost");
            redisConfig.put("port", 6379);
            redisConfig.put("timeout", 2000);
            
            // Elasticsearch configuration
            ObjectNode esConfig = config.putObject("elasticsearch");
            esConfig.put("cluster.name", "elasticsearch");
            esConfig.put("host", "localhost");
            esConfig.put("port", 9300);
            
            // Application configuration
            ObjectNode appConfig = config.putObject("application");
            appConfig.put("name", "Reactor Messaging System");
            appConfig.put("version", "0.1");
            appConfig.put("debug", false);
            
            log.info("Sample configuration JSON:");
            log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(config));
            
            // Demonstrate reading from JSON config
            JsonNode dbHost = config.path("database").path("host");
            if (!dbHost.isMissingNode()) {
                log.info("Database host from JSON config: {}", dbHost.asText());
            }
            
            int redisPort = config.path("redis").path("port").asInt(6379);
            log.info("Redis port from JSON config (with default): {}", redisPort);
            
        } catch (Exception e) {
            log.error("Error with JSON configuration", e);
        }
    }

    /**
     * Demonstrate loading resources from classpath
     */
    public void demonstrateResourceLoading() {
        log.info("=== Resource Loading Examples ===");
        
        try {
            // Load sample person.json from resources
            InputStream personStream = getClass().getClassLoader().getResourceAsStream("person.json");
            if (personStream != null) {
                JsonNode personData = mapper.readTree(personStream);
                log.info("Loaded person.json from resources:");
                log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(personData));
                personStream.close();
            } else {
                log.warn("person.json not found in resources");
            }
            
            // Demonstrate loading properties file
            InputStream propsStream = getClass().getClassLoader().getResourceAsStream("reactor.properties.sample");
            if (propsStream != null) {
                Properties props = new Properties();
                props.load(propsStream);
                
                log.info("Loaded properties from reactor.properties.sample:");
                for (String key : props.stringPropertyNames()) {
                    log.info("  {} = {}", key, props.getProperty(key));
                }
                propsStream.close();
            } else {
                log.warn("reactor.properties.sample not found in resources");
            }
            
            // Demonstrate creating a configuration from multiple sources
            ObjectNode combinedConfig = mapper.createObjectNode();
            
            // Add some defaults
            combinedConfig.put("server.port", 8080);
            combinedConfig.put("server.host", "0.0.0.0");
            combinedConfig.put("app.name", "Reactor");
            
            // Override with environment-specific values
            combinedConfig.put("environment", "development");
            combinedConfig.put("debug.enabled", true);
            
            log.info("Combined configuration:");
            log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(combinedConfig));
            
        } catch (Exception e) {
            log.error("Error loading resources", e);
        }
    }

    /**
     * Demonstrate configuration validation
     */
    public void demonstrateConfigurationValidation() {
        log.info("=== Configuration Validation Examples ===");
        
        try {
            ObjectNode config = mapper.createObjectNode();
            config.put("database.host", "localhost");
            config.put("database.port", 27017);
            
            // Validate required properties
            boolean isValid = validateConfiguration(config);
            log.info("Configuration is valid: {}", isValid);
            
        } catch (Exception e) {
            log.error("Error validating configuration", e);
        }
    }
    
    /**
     * Simple configuration validation
     */
    private boolean validateConfiguration(JsonNode config) {
        // Check required database settings
        if (config.path("database.host").isMissingNode()) {
            log.error("Missing required property: database.host");
            return false;
        }
        
        if (config.path("database.port").isMissingNode()) {
            log.error("Missing required property: database.port");
            return false;
        }
        
        // Validate port range
        int port = config.path("database.port").asInt();
        if (port < 1 || port > 65535) {
            log.error("Invalid database port: {}", port);
            return false;
        }
        
        log.info("Configuration validation passed");
        return true;
    }
}