package com.clubConnect.demo.service;

import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.misc.Role;
import com.clubConnect.demo.repository.UserRepository;
import com.clubConnect.demo.utils.ValidationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create a user with initial roles (e.g., during registration)
     * @param name User's full name
     * @param username User's username
     * @param password User's password (will be encoded)
     * @param initialRoles Initial roles for the user
     * @return The created user
     */
    public User createUser(String name, String username, String password, Set<Role> initialRoles) {
        if (!ValidationUtils.isValidName(name) || !ValidationUtils.isValidEmail(username)) {
            throw new IllegalArgumentException("Invalid name or email format.");
        }

        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        User user = new User(name, username, passwordEncoder.encode(password), initialRoles);
        return userRepository.save(user);
    }

    /**
     * Add a role to a user (restricted to admins)
     * @param userId ID of the user to update
     * @param role Role to add
     * @param requester User making the request
     * @return The updated user
     * @throws AccessDeniedException If requester is not an admin
     */
    @PreAuthorize("hasRole('ADMIN')")
    public User addRoleToUser(Long userId, Role role, User requester) throws AccessDeniedException {
        if (requester == null || !requester.getRoles().contains(Role.ADMIN)) {
            throw new AccessDeniedException("Only admins can assign roles.");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.addRole(role);
        return userRepository.save(user);
    }

    /**
     * Remove a role from a user (restricted to admins)
     * @param userId ID of the user to update
     * @param role Role to remove
     * @param requester User making the request
     * @return The updated user
     * @throws AccessDeniedException If requester is not an admin
     */
    @PreAuthorize("hasRole('ADMIN')")
    public User removeRoleFromUser(Long userId, Role role, User requester) throws AccessDeniedException {
        if (requester == null || !requester.getRoles().contains(Role.ADMIN)) {
            throw new AccessDeniedException("Only admins can remove roles.");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.removeRole(role);
        return userRepository.save(user);
    }
    
    /**
     * Find a user by username
     * @param username Username to search for
     * @return The user if found
     * @throws UsernameNotFoundException If user not found
     */
    public User findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
    
    /**
     * Implementation of UserDetailsService for Spring Security
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username);
    }

    /**
     * Find a user by ID
     * @param id User ID to search for
     * @return Optional containing the user if found
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}