package com.clubConnect.demo.repository;

import com.clubConnect.demo.entities.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {

    List<Club> findByNameContainingIgnoreCase(String query);

    @Query("SELECT c FROM Club c JOIN c.tags t WHERE t = :tag")
    List<Club> findByTag(@Param("tag") String tag);
}
