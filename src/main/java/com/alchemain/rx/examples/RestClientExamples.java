package com.alchemain.rx.examples;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Examples showing how to use Apache HTTP Components Fluent API
 * for making REST API calls. Demonstrates GET, POST, PUT, DELETE operations.
 */
public class RestClientExamples {
    private static final Logger log = LoggerFactory.getLogger(RestClientExamples.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    
    // Example API endpoints (these are mock endpoints for demonstration)
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String POSTS_URL = BASE_URL + "/posts";
    private static final String USERS_URL = BASE_URL + "/users";

    public static void main(String[] args) {
        RestClientExamples examples = new RestClientExamples();
        
        try {
            examples.demonstrateGetRequest();
            examples.demonstratePostRequest();
            examples.demonstratePutRequest();
            examples.demonstrateDeleteRequest();
            examples.demonstrateErrorHandling();
        } catch (Exception e) {
            log.error("Error running REST client examples", e);
        }
    }

    /**
     * Demonstrate HTTP GET request
     */
    public void demonstrateGetRequest() {
        log.info("=== HTTP GET Request Examples ===");
        
        try {
            // Simple GET request
            String response = Request.Get(POSTS_URL + "/1")
                .connectTimeout(5000)
                .socketTimeout(5000)
                .execute()
                .returnContent()
                .asString();
            
            JsonNode post = mapper.readTree(response);
            log.info("GET request successful!");
            log.info("Post title: {}", post.path("title").asText());
            log.info("Post body: {}", post.path("body").asText());
            
            // GET request with query parameters
            String usersResponse = Request.Get(USERS_URL + "?_limit=3")
                .execute()
                .returnContent()
                .asString();
            
            JsonNode users = mapper.readTree(usersResponse);
            log.info("GET with query params - found {} users", users.size());
            
        } catch (IOException e) {
            log.error("Error with GET request", e);
        }
    }

    /**
     * Demonstrate HTTP POST request
     */
    public void demonstratePostRequest() {
        log.info("=== HTTP POST Request Examples ===");
        
        try {
            // Create JSON payload
            ObjectNode newPost = mapper.createObjectNode();
            newPost.put("title", "My New Post");
            newPost.put("body", "This is the content of my new post");
            newPost.put("userId", 1);
            
            String jsonPayload = mapper.writeValueAsString(newPost);
            
            // POST request with JSON body
            String response = Request.Post(POSTS_URL)
                .connectTimeout(5000)
                .socketTimeout(5000)
                .addHeader("Content-Type", "application/json")
                .body(new StringEntity(jsonPayload, ContentType.APPLICATION_JSON))
                .execute()
                .returnContent()
                .asString();
            
            JsonNode createdPost = mapper.readTree(response);
            log.info("POST request successful!");
            log.info("Created post ID: {}", createdPost.path("id").asText());
            log.info("Created post title: {}", createdPost.path("title").asText());
            
        } catch (IOException e) {
            log.error("Error with POST request", e);
        }
    }

    /**
     * Demonstrate HTTP PUT request
     */
    public void demonstratePutRequest() {
        log.info("=== HTTP PUT Request Examples ===");
        
        try {
            // Create JSON payload for update
            ObjectNode updatePost = mapper.createObjectNode();
            updatePost.put("id", 1);
            updatePost.put("title", "Updated Post Title");
            updatePost.put("body", "This post has been updated");
            updatePost.put("userId", 1);
            
            String jsonPayload = mapper.writeValueAsString(updatePost);
            
            // PUT request
            String response = Request.Put(POSTS_URL + "/1")
                .connectTimeout(5000)
                .socketTimeout(5000)
                .addHeader("Content-Type", "application/json")
                .body(new StringEntity(jsonPayload, ContentType.APPLICATION_JSON))
                .execute()
                .returnContent()
                .asString();
            
            JsonNode updatedPost = mapper.readTree(response);
            log.info("PUT request successful!");
            log.info("Updated post title: {}", updatedPost.path("title").asText());
            
        } catch (IOException e) {
            log.error("Error with PUT request", e);
        }
    }

    /**
     * Demonstrate HTTP DELETE request
     */
    public void demonstrateDeleteRequest() {
        log.info("=== HTTP DELETE Request Examples ===");
        
        try {
            // DELETE request
            org.apache.http.HttpResponse response = Request.Delete(POSTS_URL + "/1")
                .connectTimeout(5000)
                .socketTimeout(5000)
                .execute()
                .returnResponse();
            
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("DELETE request completed with status code: {}", statusCode);
            
            if (statusCode >= 200 && statusCode < 300) {
                log.info("DELETE request successful!");
            } else {
                log.warn("DELETE request returned non-success status: {}", statusCode);
            }
            
        } catch (IOException e) {
            log.error("Error with DELETE request", e);
        }
    }

    /**
     * Demonstrate error handling and timeouts
     */
    public void demonstrateErrorHandling() {
        log.info("=== Error Handling Examples ===");
        
        try {
            // Request to non-existent endpoint
            try {
                String response = Request.Get(BASE_URL + "/nonexistent")
                    .connectTimeout(2000)
                    .socketTimeout(2000)
                    .execute()
                    .returnContent()
                    .asString();
                
                log.info("Unexpected success: {}", response);
                
            } catch (IOException e) {
                log.info("Expected error for non-existent endpoint: {}", e.getMessage());
            }
            
            // Request with very short timeout
            try {
                String response = Request.Get(POSTS_URL)
                    .connectTimeout(1) // Very short timeout
                    .socketTimeout(1)
                    .execute()
                    .returnContent()
                    .asString();
                
                log.info("Request completed despite short timeout");
                
            } catch (IOException e) {
                log.info("Timeout occurred as expected: {}", e.getMessage());
            }
            
            // Demonstrate custom headers
            try {
                String response = Request.Get(POSTS_URL + "/1")
                    .addHeader("User-Agent", "Reactor-Client/1.0")
                    .addHeader("Accept", "application/json")
                    .addHeader("X-Custom-Header", "custom-value")
                    .execute()
                    .returnContent()
                    .asString();
                
                log.info("Request with custom headers successful");
                
            } catch (IOException e) {
                log.error("Error with custom headers", e);
            }
            
        } catch (Exception e) {
            log.error("Unexpected error in error handling demo", e);
        }
    }

    /**
     * Helper method to create a sample user JSON
     */
    private ObjectNode createSampleUser() {
        ObjectNode user = mapper.createObjectNode();
        user.put("name", "John Doe");
        user.put("username", "johndoe");
        user.put("email", "john@example.com");
        
        ObjectNode address = user.putObject("address");
        address.put("street", "123 Main St");
        address.put("city", "Anytown");
        address.put("zipcode", "12345");
        
        return user;
    }

    /**
     * Demonstrate working with complex JSON responses
     */
    public void demonstrateComplexJsonHandling() {
        log.info("=== Complex JSON Handling Examples ===");
        
        try {
            String response = Request.Get(USERS_URL + "/1")
                .execute()
                .returnContent()
                .asString();
            
            JsonNode user = mapper.readTree(response);
            
            // Navigate nested JSON
            String name = user.path("name").asText();
            String email = user.path("email").asText();
            String city = user.path("address").path("city").asText();
            String company = user.path("company").path("name").asText();
            
            log.info("User details:");
            log.info("  Name: {}", name);
            log.info("  Email: {}", email);
            log.info("  City: {}", city);
            log.info("  Company: {}", company);
            
        } catch (IOException e) {
            log.error("Error handling complex JSON", e);
        }
    }
}