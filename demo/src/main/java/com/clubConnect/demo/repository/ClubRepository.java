package com.clubConnect.demo.repository;
import com.clubConnect.demo.entities.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    // Find clubs by name containing the given keyword (case-insensitive)
    List<Club> findByNameContainingIgnoreCase(String query);
    
    // Find clubs by tag - fixed to match the entity structure
    @Query("SELECT c FROM Club c JOIN c.tags t WHERE t = :tag")
    List<Club> findByTag(@Param("tag") String tag);
    

    //testtest

    // Count total number of clubs
    long count();
}

