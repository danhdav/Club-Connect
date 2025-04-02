package com.clubConnect.demo.controller;

import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.service.ClubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createClub(@RequestBody Club club, @AuthenticationPrincipal User user) {
        try {
            Club createdClub = clubService.createClub(club, user);
            return ResponseEntity.ok(createdClub);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Club>> searchClubs(@RequestParam String query) {
        return ResponseEntity.ok(clubService.searchClubs(query));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClub(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            clubService.deleteClub(id, user);
            return ResponseEntity.ok("Club deleted successfully");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
