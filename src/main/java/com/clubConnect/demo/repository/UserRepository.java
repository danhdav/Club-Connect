package com.clubConnect.demo.repository;

import com.clubConnect.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (used for login)
    Optional<User> findByEmail(String email);

    // Check if a user exists with a given email
    boolean existsByEmail(String email);

    // Optional: still support username-based lookup if needed
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    // Find admin users
    List<User> findByIsAdminTrue();
    
    // Find officer users
    @Query("SELECT DISTINCT u FROM User u JOIN u.officerOf c WHERE SIZE(u.officerOf) > 0")
    List<User> findAllOfficers();
}
