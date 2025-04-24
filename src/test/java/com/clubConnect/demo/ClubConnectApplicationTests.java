/*
package com.clubConnect.demo;

import com.clubConnect.demo.controller.UserController;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.misc.Role;
import com.clubConnect.demo.repository.UserRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClubConnectApplicationTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;
    
    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController authController;

    private User validUser;
    private User invalidEmailUser;
    private User exceptionalEmailUser;

    @BeforeEach
    void setUp() {
        validUser = new User("Valid User", "xyz@gmail.com", "abcdefghij", Collections.singleton(Role.USER));
        validUser.setId(1L);
        
        invalidEmailUser = new User("Invalid Email User", "@.com", "abcdefghij", Collections.singleton(Role.USER));
        invalidEmailUser.setId(2L);
        
        exceptionalEmailUser = new User("Exceptional Email User", "xyz@@gmail.com", "abcdefghij", Collections.singleton(Role.USER));
        exceptionalEmailUser.setId(3L);
    }

    @Test
    void TC_01_Login_ValidCredentials() {
        // Arrange
        String email = "xyz@gmail.com"; // Valid email
        String password = "abcdefghij"; // Valid password
        
        // Mock user repository
        lenient().when(userRepository.findByUsername(email)).thenReturn(Optional.of(validUser));
        
        // Mock authentication
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(validUser);
        
        // Act
        ResponseEntity<?> response = authController.login(email, password);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void TC_02_Login_ValidEmailInvalidPassword() {
        // Arrange
        String email = "xyz@gmail.com"; // Valid email
        String password = "abc"; // Invalid password (too short)
        
        // Mock user repository
        lenient().when(userRepository.findByUsername(email)).thenReturn(Optional.of(validUser));
        
        // Mock authentication to throw exception
        doThrow(new BadCredentialsException("Invalid credentials"))
            .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        
        // Act
        ResponseEntity<?> response = authController.login(email, password);
        
        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void TC_03_Login_ValidEmailExceptionalPassword() {
        // Arrange
        String email = "xyz@gmail.com"; // Valid email
        String password = "abcdefghijklmnopqrstuvwxyz"; // Exceptional password (too long)
        
        // Mock user repository
        lenient().when(userRepository.findByUsername(email)).thenReturn(Optional.of(validUser));
        
        // Mock authentication to throw exception
        doThrow(new BadCredentialsException("Invalid credentials"))
            .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        
        // Act
        ResponseEntity<?> response = authController.login(email, password);
        
        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void TC_04_Login_InvalidEmailValidPassword() {
        // Arrange
        String email = "@.com"; // Invalid email
        String password = "abcdefghij"; // Valid password
        
        // Mock user repository to return our invalid email user
        lenient().when(userRepository.findByUsername(email)).thenReturn(Optional.of(invalidEmailUser));
        
        // Mock authentication to throw exception for invalid email
        doThrow(new BadCredentialsException("Invalid credentials"))
            .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        
        // Act
        ResponseEntity<?> response = authController.login(email, password);
        
        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void TC_07_Login_ExceptionalEmailValidPassword() {
        // Arrange
        String email = "xyz@@gmail.com"; // Exceptional email (multiple @)
        String password = "abcdefghij"; // Valid password
        
        // Mock user repository to return our exceptional email user
        lenient().when(userRepository.findByUsername(email)).thenReturn(Optional.of(exceptionalEmailUser));
        
        // Mock authentication to throw exception for exceptional email
        doThrow(new BadCredentialsException("Invalid credentials"))
            .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        
        // Act
        ResponseEntity<?> response = authController.login(email, password);
        
        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}

 */