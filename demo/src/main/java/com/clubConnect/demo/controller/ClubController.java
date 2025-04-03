package com.clubConnect.demo.controller;

import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.service.ClubService;
import com.clubConnect.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;
    private final UserService userService;

    public ClubController(ClubService clubService, UserService userService) {
        this.clubService = clubService;
        this.userService = userService;
    }

    /**
     * Create a new club (admin only)
     * @param club Club details
     * @param user Authenticated user
     * @return Created club
     */
    @PostMapping("/create")
    public ResponseEntity<Object> createClub(@RequestBody Club club, @AuthenticationPrincipal User user) {
        try {
            Club createdClub = clubService.createClub(club, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdClub);
        } catch (AccessDeniedException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create club: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Search clubs by name
     * @param query Search query
     * @return List of matching clubs
     */
    @GetMapping("/search")
    public ResponseEntity<List<Club>> searchClubs(@RequestParam String query) {
        return ResponseEntity.ok(clubService.searchClubs(query));
    }

    /**
     * Search clubs by tags
     * @param tags List of tags
     * @return List of matching clubs
     */
    @GetMapping("/search/tags")
    public ResponseEntity<List<Club>> searchClubsByTags(@RequestParam List<String> tags) {
        return ResponseEntity.ok(clubService.searchClubsByTag(tags));
    }

    /**
     * Delete a club (admin only)
     * @param id Club ID
     * @param user Authenticated user
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteClub(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            clubService.deleteClub(id, user);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Club deleted successfully");
            return ResponseEntity.ok(response);
        } catch (AccessDeniedException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete club: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Update club name (admin or officer)
     * @param id Club ID
     * @param newName New club name
     * @param user Authenticated user
     * @return Updated club
     */
    @PutMapping("/{id}/name")
    public ResponseEntity<Object> updateClubName(
            @PathVariable Long id,
            @RequestParam String newName,
            @AuthenticationPrincipal User user) {
        try {
            Club updatedClub = clubService.updateClubName(id, newName, user);
            return ResponseEntity.ok(updatedClub);
        } catch (AccessDeniedException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update club name: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Update club description (admin or officer)
     * @param id Club ID
     * @param newDescription New club description
     * @param user Authenticated user
     * @return Updated club
     */
    @PutMapping("/{id}/description")
    public ResponseEntity<Object> updateClubDescription(
            @PathVariable Long id,
            @RequestParam String newDescription,
            @AuthenticationPrincipal User user) {
        try {
            Club updatedClub = clubService.updateClubDescription(id, newDescription, user);
            return ResponseEntity.ok(updatedClub);
        } catch (AccessDeniedException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update club description: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Add an officer to a club (admin only)
     * @param clubId Club ID
     * @param userId User ID to add as officer
     * @param user Authenticated user
     * @return Updated club
     */
    @PostMapping("/{clubId}/officers/{userId}")
    public ResponseEntity<Object> addOfficer(
            @PathVariable Long clubId,
            @PathVariable Long userId,
            @AuthenticationPrincipal User user) {
        try {
            User officer = userService.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            
            Club updatedClub = clubService.addOfficer(clubId, officer, user);
            return ResponseEntity.ok(updatedClub);
        } catch (AccessDeniedException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to add officer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Get total number of clubs
     * @return Count of clubs
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalClubs() {
        return ResponseEntity.ok(clubService.getTotalClubs());
    }
}
