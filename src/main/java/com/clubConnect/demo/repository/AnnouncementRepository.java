package com.clubConnect.demo.repository;

import com.clubConnect.demo.entities.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    // Additional custom query methods can be added here if needed
} 