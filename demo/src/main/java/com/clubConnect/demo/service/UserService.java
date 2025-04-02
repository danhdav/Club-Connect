package com.clubConnect.demo.service;

import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.misc.Role;
import com.clubConnect.demo.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create a user with initial roles (e.g., during registration)
    public User createUser(String username, String password, Set<Role> initialRoles) {
        User user = new User(username, password, initialRoles);
        return userRepository.save(user);
    }

    // Add a role to a user (restricted to admins)
    @PreAuthorize("hasRole('ADMIN')") // Spring Security annotation
    public User addRoleToUser(Long userId, Role role, User requester) throws AccessDeniedException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Optional: Additional check if requester is authorized
        if (!requester.getRoles().contains(Role.ADMIN)) {
            throw new AccessDeniedException("Only admins can assign roles.");
        }

        user.addRole(role);
        return userRepository.save(user);
    }

    // Remove a role from a user (restricted to admins)
    @PreAuthorize("hasRole('ADMIN')")
    public User removeRoleFromUser(Long userId, Role role, User requester) throws AccessDeniedException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!requester.getRoles().contains(Role.ADMIN)) {
            throw new AccessDeniedException("Only admins can remove roles.");
        }

        user.removeRole(role);
        return userRepository.save(user);
    }
}