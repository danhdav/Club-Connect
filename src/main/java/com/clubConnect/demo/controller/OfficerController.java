package com.clubConnect.demo.controller;

import com.clubConnect.demo.entities.Announcement;
import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.service.OfficerService;
import com.clubConnect.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/officer")
public class OfficerController {

    private final OfficerService officerService;
    private final UserService userService;

    public static class ClubPageUpdateDto {
        public String description;
        public List<String> images;
    }

    public OfficerController(OfficerService officerService, UserService userService) {
        this.officerService = officerService;
        this.userService = userService;
    }

    @PostMapping("/clubs/{clubId}/announcements")
    public ResponseEntity<Object> createAnnouncement(
            @PathVariable Long clubId,
            @RequestBody Announcement announcement,
            @RequestParam Long officerId) {
        try {
            // We need access to userService to get the real officer
            User officer = getUserById(officerId);
            Announcement created = officerService.createAnnouncement(clubId, announcement, officer);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create announcement: " + e.getMessage()));
        }
    }

    @PutMapping("/clubs/{clubId}/announcements/{announcementId}")
    public ResponseEntity<Object> updateAnnouncement(
            @PathVariable Long clubId,
            @PathVariable Long announcementId,
            @RequestBody Announcement announcement,
            @RequestParam Long officerId) {
        try {
            User officer = getUserById(officerId);
            // now pass the entire Announcement object to your service
            Announcement updated = officerService.updateAnnouncement(clubId, announcementId, announcement.getContentHtml(), officer);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update announcement: " + e.getMessage()));
        }
    }

    @DeleteMapping("/clubs/{clubId}/announcements/{announcementId}")
    public ResponseEntity<Object> deleteAnnouncement(
            @PathVariable Long clubId,
            @PathVariable Long announcementId,
            @RequestParam Long officerId) {
        try {
            User officer = getUserById(officerId);
            officerService.deleteAnnouncement(clubId, announcementId, officer);
            return ResponseEntity.ok(Map.of("message", "Announcement deleted"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete announcement: " + e.getMessage()));
        }
    }

    @DeleteMapping("/clubs/{clubId}/announcements/{announcementId}/comments/{commentId}")
    public ResponseEntity<Object> deleteComment(
            @PathVariable Long clubId,
            @PathVariable Long announcementId,
            @PathVariable Long commentId,
            @RequestParam Long officerId) {
        try {
            User officer = getUserById(officerId);
            officerService.deleteComment(clubId, announcementId, commentId, officer);
            return ResponseEntity.ok(Map.of("message", "Comment deleted"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete comment: " + e.getMessage()));
        }
    }

    @PutMapping("/clubs/{clubId}/page")
    public ResponseEntity<Object> updateClubPage(
            @PathVariable Long clubId,
            @RequestBody ClubPageUpdateDto dto,
            @RequestParam Long officerId) {
        try {
            User officer = getUserById(officerId);
            Club updated = officerService.updateClubPage(
                    clubId,
                    dto.description,
                    dto.images,
                    officer
            );

            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update club page: " + e.getMessage()));
        }
    }

    @DeleteMapping("/clubs/{clubId}")
    public ResponseEntity<Object> updateClubPage(
            @PathVariable Long clubId,
            @RequestParam Long officerId) {
        try {
            User officer = getUserById(officerId);
            Club updated = officerService.removeImages(
                    clubId,
                    officer
            );

            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update club page: " + e.getMessage()));
        }
    }




    @GetMapping("/clubs/{clubId}/members")
    public ResponseEntity<Object> getClubMembers(@PathVariable Long clubId, @RequestParam Long officerId) {
        try {
            User officer = getUserById(officerId);
            List<User> members = officerService.getAllClubMembers(clubId, officer);
            return ResponseEntity.ok(Map.of(
                "members", members,
                "count", members.size()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get club members: " + e.getMessage()));
        }
    }

    @DeleteMapping("/clubs/{clubId}/members/{memberId}")
    public ResponseEntity<Object> removeClubMember(
            @PathVariable Long clubId,
            @PathVariable Long memberId,
            @RequestParam Long officerId) {
        try {
            User officer = getUserById(officerId);
            officerService.removeClubMember(clubId, memberId, officer);
            return ResponseEntity.ok(Map.of("message", "Member removed"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to remove member: " + e.getMessage()));
        }
    }

    @PutMapping("/clubs/{clubId}/page-content")
    public ResponseEntity<Object> updateClubPageContent(
            @PathVariable Long clubId,
            @RequestBody Map<String, Object> payload,
            @RequestParam Long officerId) {
        try {
            User officer = getUserById(officerId);
            String description = (String) payload.get("description");
            List<String> images = (List<String>) payload.get("images");
            
            Club updatedClub = officerService.updateClubPageContent(clubId, description, images, officer);
            return ResponseEntity.ok(updatedClub);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update club page content: " + e.getMessage()));
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
