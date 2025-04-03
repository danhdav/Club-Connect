package com.clubConnect.demo.repository;
import com.clubConnect.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Find user by username for authentication
    Optional<User> findByUsername(String username);
    
    // Find users by role
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findByRole(@Param("role") String role);
    
    // Check if username exists (for registration validation)
    boolean existsByUsername(String username);
    Optional<User> findById(Long id);
}

