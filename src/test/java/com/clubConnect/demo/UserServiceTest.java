/*
package com.clubConnect.demo.service;

import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.misc.Role;
import com.clubConnect.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User adminUser;
    private User regularUser;

    @BeforeEach
    void setUp() {
        // Create an admin user
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(Role.ADMIN);
        adminUser = new User("Admin User", "admin", "password", adminRoles);
        adminUser.setId(1L);

        // Create a regular user
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(Role.USER);
        regularUser = new User("Regular User", "user", "password", userRoles);
        regularUser.setId(2L);
        
        // Mock password encoder
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
    }

    @Test
    void createUser_ShouldCreateUserWithCorrectRoles() {
        // Arrange
        Set<Role> roles = Collections.singleton(Role.USER);
        User newUser = new User("New User", "newuser", "encodedPassword", roles);
        
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        User createdUser = userService.createUser("New User", "newuser", "password", roles);

        // Assert
        assertNotNull(createdUser);
        assertEquals("New User", createdUser.getName());
        assertEquals("newuser", createdUser.getUsername());
        assertTrue(createdUser.getRoles().contains(Role.USER));
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenUsernameExists() {
        // Arrange
        when(userRepository.existsByUsername("user")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            userService.createUser("Another User", "user", "password", Collections.singleton(Role.USER))
        );
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void addRoleToUser_ShouldAddRole_WhenRequesterIsAdmin() {
        // Arrange
        when(userRepository.findById(2L)).thenReturn(Optional.of(regularUser));
        when(userRepository.save(any(User.class))).thenReturn(regularUser);

        // Act
        User updatedUser = userService.addRoleToUser(2L, Role.OFFICER, adminUser);

        // Assert
        assertTrue(updatedUser.getRoles().contains(Role.OFFICER));
        verify(userRepository).save(regularUser);
    }

    @Test
    void addRoleToUser_ShouldThrowException_WhenRequesterIsNotAdmin() {
        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> 
            userService.addRoleToUser(1L, Role.OFFICER, regularUser)
        );
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void removeRoleFromUser_ShouldRemoveRole_WhenRequesterIsAdmin() {
        // Arrange
        regularUser.addRole(Role.OFFICER);
        when(userRepository.findById(2L)).thenReturn(Optional.of(regularUser));
        when(userRepository.save(any(User.class))).thenReturn(regularUser);

        // Act
        User updatedUser = userService.removeRoleFromUser(2L, Role.OFFICER, adminUser);

        // Assert
        assertFalse(updatedUser.getRoles().contains(Role.OFFICER));
        verify(userRepository).save(regularUser);
    }

    @Test
    void removeRoleFromUser_ShouldThrowException_WhenRequesterIsNotAdmin() {
        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> 
            userService.removeRoleFromUser(1L, Role.USER, regularUser)
        );
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(regularUser));

        // Act
        User foundUser = userService.findByUsername("user");

        // Assert
        assertNotNull(foundUser);
        assertEquals("Regular User", foundUser.getName());
    }

    @Test
    void findByUsername_ShouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> 
            userService.findByUsername("nonexistent")
        );
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        // Arrange
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(regularUser));

        // Act
        User userDetails = (User) userService.loadUserByUsername("user");

        // Assert
        assertNotNull(userDetails);
        assertEquals("Regular User", userDetails.getName());
        assertEquals("user", userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> 
            userService.loadUserByUsername("nonexistent")
        );
    }
}

 */
