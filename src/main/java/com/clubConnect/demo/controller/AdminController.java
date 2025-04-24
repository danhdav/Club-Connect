package com.clubConnect.demo.controller;

import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.service.AdminService;
import com.clubConnect.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @PostMapping("/clubs")
    public ResponseEntity<Object> createClub(@RequestBody Club club, @RequestParam Long userId) {
        try {
            User admin = getUserById(userId);
            if (!admin.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only admins can perform this action"));
            }
            
            Club createdClub = adminService.createClub(club, admin);
            return ResponseEntity.ok(createdClub);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create club: " + e.getMessage()));
        }
    }

    @PutMapping("/clubs/{clubId}/officers")
    public ResponseEntity<Object> updateOfficers(
            @PathVariable Long clubId,
            @RequestBody Map<String, List<Long>> body,
            @RequestParam Long userId) {
        try {
            User admin = getUserById(userId);
            if (!admin.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only admins can perform this action"));
            }

            List<Long> officerIds = body.get("userIds");

            Club updatedClub = adminService.updateClubOfficers(clubId, officerIds, admin);
            return ResponseEntity.ok(updatedClub);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update officers: " + e.getMessage()));
        }
    }


    @PutMapping("/clubs/edit")
    public ResponseEntity<Object> editClub(
            @RequestParam Long clubId,
            @RequestBody Map<String, Object> updates,
            @RequestParam Long userId) {
        try {
            User admin = getUserById(userId);
            if (!admin.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only admins can perform this action"));
            }

            String name = (String) updates.get("name");
            String description = (String) updates.get("description");

            // Fix unchecked cast by creating a new typed list
            List<String> tags = null;
            if (updates.containsKey("tags")) {
                Object tagsObj = updates.get("tags");
                if (tagsObj instanceof List<?>) {
                    tags = ((List<?>) tagsObj).stream()
                            .filter(item -> item instanceof String)
                            .map(item -> (String) item)
                            .toList();
                }
            }

            Club updatedClub = adminService.updateClub(clubId, name, description, tags, admin);
            return ResponseEntity.ok(updatedClub);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update club: " + e.getMessage()));
        }
    }


    @DeleteMapping("/clubs/{clubId}")
    public ResponseEntity<Object> deleteClub(@PathVariable Long clubId, @RequestParam Long userId) {
        try {
            User admin = getUserById(userId);
            if (!admin.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Only admins can perform this action"));
            }
            
            adminService.deleteClub(clubId, admin);
            return ResponseEntity.ok(Map.of("message", "Club deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete club: " + e.getMessage()));
        }
    }
    
    /**
     * Helper method to get a user by ID
     * @param userId User ID
     * @return User entity
     * @throws IllegalArgumentException if user not found
     */
    private User getUserById(Long userId) {
        return userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
