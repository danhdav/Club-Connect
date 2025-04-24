package com.clubConnect.demo.repository;

import com.clubConnect.demo.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Additional custom query methods can be added here if needed
} 