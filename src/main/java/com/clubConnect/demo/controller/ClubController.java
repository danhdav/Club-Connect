package com.clubConnect.demo.controller;

import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.service.ClubService;
import com.clubConnect.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clubs")
@Tag(name = "Club Management", description = "APIs for club creation, management, and browsing")
public class ClubController {


    private final ClubService clubService;
    private final UserService userService;

    public ClubController(ClubService clubService, UserService userService) {
        this.clubService = clubService;
        this.userService = userService;
    }

    @Operation(summary = "Create a new club", description = "Create a new club (admin only)")
    @ApiResponse(responseCode = "201", description = "Club created successfully")
    @ApiResponse(responseCode = "403", description = "User is not an admin")
    @PostMapping("/create")
    public ResponseEntity<Object> createClub(
            @RequestBody Club club, 
            @Parameter(description = "ID of admin user creating the club") @RequestParam Long userId) {
        try {
            Club createdClub = clubService.createClub(club, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdClub);
        } catch (IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create club: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @Operation(summary = "Search clubs by name", description = "Search for clubs by name")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    @GetMapping("/search")
    public ResponseEntity<List<Club>> searchClubs(
            @Parameter(description = "Search query string") @RequestParam String query) {
        return ResponseEntity.ok(clubService.searchClubs(query));
    }

    @Operation(summary = "Search clubs by tags", description = "Search for clubs by tags")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    @GetMapping("/search/tags")
    public ResponseEntity<List<Club>> searchClubsByTags(
            @Parameter(description = "List of tags to search for") @RequestParam List<String> tags) {
        return ResponseEntity.ok(clubService.searchClubsByTag(tags));
    }

    /**
     * Delete a club (admin only)
     * @param id Club ID
     * @param userId ID of the user deleting the club
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteClub(@PathVariable Long id, @RequestParam Long userId) {
        try {
            clubService.deleteClub(id, userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Club deleted successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
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
     * @param userId ID of the user updating the club
     * @return Updated club
     */
    @PutMapping("/{id}/name")
    public ResponseEntity<Object> updateClubName(
            @PathVariable Long id,
            @RequestParam String newName,
            @RequestParam Long userId) {
        try {
            Club updatedClub = clubService.updateClubName(id, newName, userId);
            return ResponseEntity.ok(updatedClub);
        } catch (IllegalStateException e) {
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
     * @param userId ID of the user updating the club
     * @return Updated club
     */
    @PutMapping("/{id}/description")
    public ResponseEntity<Object> updateClubDescription(
            @PathVariable Long id,
            @RequestParam String newDescription,
            @RequestParam Long userId) {
        try {
            Club updatedClub = clubService.updateClubDescription(id, newDescription, userId);
            return ResponseEntity.ok(updatedClub);
        } catch (IllegalStateException e) {
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
     * @param officerId User ID to add as officer
     * @param userId ID of the user (admin) adding the officer
     * @return Updated club
     */
    @PostMapping("/{clubId}/officers/{officerId}")
    public ResponseEntity<Object> addOfficer(
            @PathVariable Long clubId,
            @PathVariable Long officerId,
            @RequestParam Long userId) {
        try {
            Club updatedClub = clubService.addOfficer(clubId, officerId, userId);
            return ResponseEntity.ok(updatedClub);
        } catch (IllegalStateException e) {
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

    @Operation(summary = "Get all clubs", description = "Get a list of all clubs")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved clubs")
    @GetMapping("/getAllClubs")
    public ResponseEntity<Map<String, Object>> getAllClubs() {
        List<Club> clubs = clubService.getAllClubs();
        Map<String, Object> response = new HashMap<>();
        response.put("clubs", clubs);
        response.put("count", clubs.size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get club details", description = "Get detailed information about a club")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved club details")
    @ApiResponse(responseCode = "404", description = "Club not found")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getClubInfo(
            @Parameter(description = "Club ID") @PathVariable Long id) {
        try {
            return ResponseEntity.ok(clubService.getClubPageInfo(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get club announcements", description = "Get all announcements for a club")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved announcements")
    @ApiResponse(responseCode = "404", description = "Club not found")
    @GetMapping("/{id}/announcements")
    public ResponseEntity<Object> getClubAnnouncements(
            @Parameter(description = "Club ID") @PathVariable Long id) {
        try {
            return ResponseEntity.ok(clubService.getAnnouncementsWithDetails(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Toggle user subscription to a club
     * @param clubId Club ID
     * @param userId User ID
     * @return Success message
     */
    @PostMapping("/{clubId}/toggle-subscription")
    public ResponseEntity<Object> toggleSubscription(
            @PathVariable Long clubId,
            @RequestParam Long userId) {
        try {
            boolean subscribed = clubService.toggleSubscription(clubId, userId);
            String action = subscribed ? "subscribed to" : "unsubscribed from";
            return ResponseEntity.ok(Map.of(
                "message", "User successfully " + action + " club",
                "subscribed", subscribed
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to toggle subscription: " + e.getMessage()));
        }
    }
}
