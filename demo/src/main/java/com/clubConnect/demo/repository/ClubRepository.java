package com.clubConnect.demo.repository;
import com.clubConnect.demo.entities.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    // You can add custom queries here, e.g., find by name or tags
    List<Club> findByNameContaining(String keyword);  // For search

    List<Club> findByNameContainingIgnoreCase(String query);

    List<Club> findByTag(String tag);
    long count();
}

