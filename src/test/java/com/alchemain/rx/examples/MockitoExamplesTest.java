package com.alchemain.rx.examples;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple examples showing how to use Mockito for testing.
 * Demonstrates basic mocking patterns used in unit tests.
 */
public class MockitoExamplesTest {
    private static final Logger log = LoggerFactory.getLogger(MockitoExamplesTest.class);

    @Mock
    private List<String> mockList;

    public MockitoExamplesTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void demonstrateBasicMocking() {
        log.info("=== Basic Mockito Examples ===");
        
        // Create a mock
        List<String> mockedList = mock(List.class);
        
        // Use the mock
        mockedList.add("one");
        mockedList.clear();
        
        // Verify interactions
        verify(mockedList).add("one");
        verify(mockedList).clear();
        
        log.info("Basic mocking verification passed");
    }

    @Test
    public void demonstrateStubbing() {
        log.info("=== Mockito Stubbing Examples ===");
        
        List<String> mockedList = mock(List.class);
        
        // Stubbing
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException("boom"));
        
        // Test stubbed methods
        assertEquals("first", mockedList.get(0));
        log.info("Stubbed method returned: {}", mockedList.get(0));
        
        try {
            mockedList.get(1);
            fail("Should have thrown exception");
        } catch (RuntimeException e) {
            log.info("Expected exception caught: {}", e.getMessage());
        }
        
        // Following prints null because get(999) was not stubbed
        assertNull(mockedList.get(999));
        log.info("Unstubbed method returned: {}", mockedList.get(999));
    }

    @Test
    public void demonstrateArgumentMatchers() {
        log.info("=== Mockito Argument Matchers Examples ===");
        
        List<String> mockedList = mock(List.class);
        
        // Stubbing using argument matchers
        when(mockedList.get(anyInt())).thenReturn("element");
        when(mockedList.contains(argThat(s -> s.length() > 5))).thenReturn(true);
        
        assertEquals("element", mockedList.get(999));
        assertTrue(mockedList.contains("very long string"));
        assertFalse(mockedList.contains("short"));
        
        log.info("Argument matcher tests passed");
    }

    @Test
    public void demonstrateVerification() {
        log.info("=== Mockito Verification Examples ===");
        
        List<String> mockedList = mock(List.class);
        
        // Use mock
        mockedList.add("once");
        mockedList.add("twice");
        mockedList.add("twice");
        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");
        
        // Verify exact number of invocations
        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");
        
        // Verify using never()
        verify(mockedList, never()).add("never added");
        
        // Verify using atLeast() and atMost()
        verify(mockedList, atLeastOnce()).add("once");
        verify(mockedList, atLeast(2)).add("twice");
        verify(mockedList, atMost(5)).add("three times");
        
        log.info("Verification tests passed");
    }

    @Test
    public void demonstrateMockingServiceClass() {
        log.info("=== Mocking Service Class Example ===");
        
        // Mock a service
        UserService mockUserService = mock(UserService.class);
        
        // Stub the service method
        User mockUser = new User("john@example.com", "John Doe");
        when(mockUserService.findByEmail("john@example.com")).thenReturn(mockUser);
        when(mockUserService.findByEmail("notfound@example.com")).thenReturn(null);
        
        // Test with the mock
        UserController controller = new UserController(mockUserService);
        
        User found = controller.getUser("john@example.com");
        assertNotNull(found);
        assertEquals("John Doe", found.getName());
        log.info("Found user: {}", found.getName());
        
        User notFound = controller.getUser("notfound@example.com");
        assertNull(notFound);
        log.info("User not found as expected");
        
        // Verify the service was called
        verify(mockUserService, times(2)).findByEmail(anyString());
    }

    /**
     * Example service class for mocking demonstration
     */
    public static class UserService {
        public User findByEmail(String email) {
            // In real implementation, this would query a database
            throw new UnsupportedOperationException("Not implemented");
        }
        
        public void save(User user) {
            // In real implementation, this would save to database
            throw new UnsupportedOperationException("Not implemented");
        }
    }

    /**
     * Example controller that uses the service
     */
    public static class UserController {
        private final UserService userService;
        
        public UserController(UserService userService) {
            this.userService = userService;
        }
        
        public User getUser(String email) {
            return userService.findByEmail(email);
        }
    }

    /**
     * Simple user data class
     */
    public static class User {
        private final String email;
        private final String name;
        
        public User(String email, String name) {
            this.email = email;
            this.name = name;
        }
        
        public String getEmail() { return email; }
        public String getName() { return name; }
    }
}