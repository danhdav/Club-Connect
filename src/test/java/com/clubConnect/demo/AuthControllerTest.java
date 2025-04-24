/*
package com.clubConnect.demo.controller;


import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.misc.Role;
import com.clubConnect.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController authController;

    private User testUser;

    @BeforeEach
    void setUp() {
        Set<Role> roles = Collections.singleton(Role.USER);
        testUser = new User("Test User", "testuser", "password", roles);
    }

    @Test
    void login_ShouldReturnSuccessResponse_WhenCredentialsAreValid() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testUser);

        // Act
        ResponseEntity<Map<String, Object>> response = authController.login("testuser", "password");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Login successful", response.getBody().get("message"));
        assertEquals("testuser", response.getBody().get("username"));
    }

    @Test
    void login_ShouldReturnErrorResponse_WhenCredentialsAreInvalid() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act
        ResponseEntity<Map<String, Object>> response = authController.login("testuser", "wrongpassword");

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid credentials", response.getBody().get("error"));
    }

    @Test
    void register_ShouldReturnSuccessResponse_WhenRegistrationIsSuccessful() {
        // Arrange
        when(userService.createUser(eq("Test User"), eq("testuser"), eq("password"), any()))
                .thenReturn(testUser);

        // Act
        ResponseEntity<Map<String, Object>> response = authController.register("Test User", "testuser", "password");

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Registration successful", response.getBody().get("message"));
        assertEquals("testuser", response.getBody().get("username"));
    }

    @Test
    void register_ShouldReturnErrorResponse_WhenUsernameAlreadyExists() {
        // Arrange
        when(userService.createUser(eq("Test User"), eq("testuser"), eq("password"), any()))
                .thenThrow(new IllegalArgumentException("Username already exists"));

        // Act
        ResponseEntity<Map<String, Object>> response = authController.register("Test User", "testuser", "password");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Username already exists", response.getBody().get("error"));
    }

    @Test
    void logout_ShouldReturnSuccessResponse() {
        // Act
        ResponseEntity<Map<String, Object>> response = authController.logout();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Logout successful", response.getBody().get("message"));
    }
}

 */