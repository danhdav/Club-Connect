package com.clubConnect.demo;

import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.misc.Role;
import com.clubConnect.demo.repository.ClubRepository;
import com.clubConnect.demo.repository.UserRepository;
import com.clubConnect.demo.service.ClubService;
import com.clubConnect.demo.controller.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.file.AccessDeniedException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthTest {

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private ClubService clubService;

    @InjectMocks
    private AuthController authController;

    private User adminUser;
    private User regularUser;
    private Club club;

    @BeforeEach
    public void setUp() {
        adminUser = new User("admin", "password", Collections.singleton(Role.ADMIN));
        regularUser = new User("user", "password", Collections.singleton(Role.USER));

        club = new Club();
        club.setName("Tech Club");
        club.setDescription("A club for tech enthusiasts");
        List<String> tags = new ArrayList<>();
        tags.add("Technology");
        tags.add("Robotics");
        club.setTags(tags);
    }

    @Test
    public void testUserLogin_Success() {
        // Arrange
        String username = "testUser";
        String password = "correctPassword";
        Authentication authenticationResult = mock(Authentication.class);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password)))
                .thenReturn(authenticationResult);

        // Act
        ResponseEntity<String> response = authController.login(username, password);

        // Assert
        assertEquals("Login successful", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Test
    public void testUserLogin_Failure_InvalidCredentials() {
        // Arrange
        String username = "testUser";
        String incorrectPassword = "wrongPassword";
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, incorrectPassword)))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Invalid credentials"));

        // Act
        ResponseEntity<String> response = authController.login(username, incorrectPassword);

        // Assert
        assertEquals("Invalid credentials", response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(username, incorrectPassword));
    }

    @Test
    public void testUserLogout_Success_AndAccessDeniedAfterLogout() throws AccessDeniedException {
        // Arrange
        // Simulate a logged-in admin user
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("admin");
        org.springframework.security.core.userdetails.User principal =
                new org.springframework.security.core.userdetails.User("admin", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

        // Act - Logout
        ResponseEntity<String> logoutResponse = authController.logout();

        // Assert - Logout success
        assertEquals("Logout successful", logoutResponse.getBody());
        assertEquals(HttpStatus.OK, logoutResponse.getStatusCode());
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Act & Assert - Attempt to access a protected function after logout
        Club newClub = new Club();
        newClub.setName("New Club After Logout");
        newClub.setDescription("Attempting to create after logout");

        // Expect the NullPointerException because creator is null after logout
        assertThrows(NullPointerException.class, () -> {
            clubService.createClub(newClub, null);
        });
        verify(clubRepository, never()).save(any(Club.class));
    }
}