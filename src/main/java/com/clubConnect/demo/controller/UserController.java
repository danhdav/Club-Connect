package com.clubConnect.demo.controller;

import com.clubConnect.demo.entities.Announcement;
import com.clubConnect.demo.entities.Comment;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.entities.Reaction;
import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.repository.AnnouncementRepository;
import com.clubConnect.demo.repository.CommentRepository;
import com.clubConnect.demo.service.UserService;
import com.clubConnect.demo.service.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "User Management", description = "APIs for user authentication and actions")
public class UserController {

    private final UserService userService;
    private final ClubService clubService;
    private final CommentRepository commentRepository;
    private final AnnouncementRepository announcementRepository;

    public UserController(UserService userService, 
                         ClubService clubService, 
                         CommentRepository commentRepository,
                         AnnouncementRepository announcementRepository) {
        this.userService = userService;
        this.clubService = clubService;
        this.commentRepository = commentRepository;
        this.announcementRepository = announcementRepository;
    }

    @Operation(summary = "User login", description = "Authenticate a user with email and password")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        try {
            validateLoginRequest(credentials);
            
            String email = credentials.get("email");
            String password = credentials.get("password");

            Optional<User> userOptional = userService.authenticate(email, password);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("userId", user.getId());
                response.put("email", user.getEmail());
                response.put("name", user.getName());
                response.put("isAdmin", user.isAdmin());
                response.put("isOfficer", user.isOfficer());
                
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid credentials"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }

    @Operation(summary = "User registration", description = "Register a new user")
    @ApiResponse(responseCode = "201", description = "Registration successful")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> userData) {
        try {
            validateRegisterRequest(userData);
            
            String name = userData.get("name");
            String email = userData.get("email");
            String password = userData.get("password");

            User user = userService.createUser(name, email, password);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration successful");
            response.put("userId", user.getId());
            response.put("email", user.getEmail());
            response.put("name", user.getName());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    /**
     * User verification endpoint - checks if user exists and returns user information
     * @param userId User ID to verify
     * @return User information if found
     */
    @GetMapping("/verify/{userId}")
    public ResponseEntity<Map<String, Object>> verifyUser(@PathVariable Long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getId());
            response.put("email", user.getEmail());
            response.put("name", user.getName());
            response.put("isAdmin", user.isAdmin());
            response.put("isOfficer", user.isOfficer());
            
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }
    }

    // POST /auth/logout
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestParam Long userId) {
        try {
            User user = userService.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            userService.logout(user);
            return ResponseEntity.ok(Map.of("message", "Logout successful"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to logout: " + e.getMessage()));
        }
    }

    // PUT /auth/update/email
    @PutMapping("/update/email")
    public ResponseEntity<Map<String, Object>> updateEmail(
            @RequestParam Long userId,
            @RequestParam String newEmail) {
        try {
            User user = userService.updateEmail(userId, newEmail);
            return ResponseEntity.ok(Map.of(
                "message", "Email updated", 
                "email", user.getEmail()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update email: " + e.getMessage()));
        }
    }

    // PUT /auth/update/name
    @PutMapping("/update/name")
    public ResponseEntity<Map<String, Object>> updateName(
            @RequestParam Long userId,
            @RequestParam String newName) {
        try {
            User user = userService.updateName(userId, newName);
            return ResponseEntity.ok(Map.of(
                "message", "Name updated", 
                "name", user.getName()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update name: " + e.getMessage()));
        }
    }

    // PUT /auth/update/password
    @PutMapping("/update/password")
    public ResponseEntity<Map<String, Object>> updatePassword(
            @RequestParam Long userId,
            @RequestParam String newPassword) {
        try {
            User user = userService.updatePassword(userId, newPassword);
            return ResponseEntity.ok(Map.of("message", "Password updated"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update password: " + e.getMessage()));
        }
    }

    // DELETE /auth/delete
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteAccount(@RequestParam Long userId) {
        try {
            userService.deleteAccount(userId);
            return ResponseEntity.ok(Map.of("message", "Account deleted"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete account: " + e.getMessage()));
        }
    }

    /**
     * Set a user as an admin - this is a protected endpoint for demo purposes only
     * @param userId ID of user to make admin
     * @return Updated user information
     */
    @PutMapping("/admin/{userId}")
    public ResponseEntity<Map<String, Object>> setUserAsAdmin(@PathVariable Long userId) {
        try {
            User user = userService.setAdminStatus(userId, true);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User set as admin successfully");
            response.put("userId", user.getId());
            response.put("email", user.getEmail());
            response.put("name", user.getName());
            response.put("isAdmin", user.isAdmin());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Validate login request data
     * @param credentials Login credentials
     * @throws IllegalArgumentException if validation fails
     */
    private void validateLoginRequest(Map<String, String> credentials) {
        if (credentials == null) {
            throw new IllegalArgumentException("Request body cannot be empty");
        }
        
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
    }
    
    /**
     * Validate registration request data
     * @param userData User registration data
     * @throws IllegalArgumentException if validation fails
     */
    private void validateRegisterRequest(Map<String, String> userData) {
        if (userData == null) {
            throw new IllegalArgumentException("Request body cannot be empty");
        }
        
        String name = userData.get("name");
        String email = userData.get("email");
        String password = userData.get("password");
        
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
    }

    @Operation(summary = "Get subscribed clubs", description = "Get all clubs a user is subscribed to")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved clubs")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{userId}/subscribed-clubs")
    public ResponseEntity<Object> getSubscribedClubs(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> subscribedClubs = userService.getSubscribedClubsDetails(userId);
            return ResponseEntity.ok(Map.of(
                "clubs", subscribedClubs,
                "count", subscribedClubs.size()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get subscribed clubs: " + e.getMessage()));
        }
    }

    /**
     * Get all announcements a user has received from subscribed clubs
     * @param userId User ID
     * @return List of announcements
     */
    @GetMapping("/{userId}/announcements")
    public ResponseEntity<Object> getUserAnnouncements(@PathVariable Long userId) {
        try {
            List<Map<String, Object>> announcements = userService.getUserAnnouncements(userId);
            return ResponseEntity.ok(Map.of(
                "announcements", announcements,
                "count", announcements.size()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get announcements: " + e.getMessage()));
        }
    }

    @Operation(summary = "Subscribe to club", description = "Subscribe a user to a club")
    @ApiResponse(responseCode = "200", description = "Successfully subscribed")
    @ApiResponse(responseCode = "404", description = "User or club not found")
    @PostMapping("/{userId}/subscribe")
    public ResponseEntity<Map<String, Object>> subscribeToClub(
            @Parameter(description = "ID of the user") @PathVariable Long userId,
            @Parameter(description = "ID of the club") @RequestParam Long clubId) {
        try {
            userService.subscribeToClub(userId, clubId);
            return ResponseEntity.ok(Map.of(
                "message", "Successfully subscribed to club",
                "subscribed", true
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to subscribe to club: " + e.getMessage()));
        }
    }

    @Operation(summary = "Unsubscribe from club", description = "Unsubscribe a user from a club")
    @ApiResponse(responseCode = "200", description = "Successfully unsubscribed")
    @ApiResponse(responseCode = "404", description = "User or club not found")
    @PostMapping("/{userId}/unsubscribe")
    public ResponseEntity<Map<String, Object>> unsubscribeFromClub(
            @Parameter(description = "ID of the user") @PathVariable Long userId,
            @Parameter(description = "ID of the club") @RequestParam Long clubId) {
        try {
            userService.unsubscribeFromClub(userId, clubId);
            return ResponseEntity.ok(Map.of(
                "message", "Successfully unsubscribed from club",
                "subscribed", false
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to unsubscribe from club: " + e.getMessage()));
        }
    }

    @Operation(summary = "Add comment to announcement", description = "Add a comment to an announcement")
    @ApiResponse(responseCode = "201", description = "Comment added successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "User, club, or announcement not found")
    @PostMapping("/{userId}/comment")
    public ResponseEntity<Object> addComment(
            @Parameter(description = "ID of the user adding the comment") @PathVariable Long userId,
            @Parameter(description = "ID of the club the announcement belongs to") @RequestParam Long clubId,
            @Parameter(description = "ID of the announcement to comment on") @RequestParam Long announcementId,
            @RequestBody Map<String, String> commentData) {
        
        try {
            // Validate input
            if (!commentData.containsKey("text") || commentData.get("text").trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Comment text is required"));
            }
            
            // Get the user
            User user = userService.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Get the club and verify it exists
            Club club = clubService.getClubById(clubId);

            // Get the announcement
            Announcement announcement = announcementRepository.findById(announcementId)
                    .orElseThrow(() -> new IllegalArgumentException("Announcement not found"));
            
            // Create and save the comment
            Comment comment = new Comment(commentData.get("text"), user, announcement);
            Comment savedComment = commentRepository.save(comment);
            
            // Return response
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", savedComment.getId(),
                "text", savedComment.getText(),
                "authorId", savedComment.getAuthor().getId(),
                "authorName", savedComment.getAuthor().getName(),
                "announcementId", savedComment.getAnnouncement().getId(),
                "createdAt", savedComment.getCreatedAt()
            ));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to add comment: " + e.getMessage()));
        }
    }

    @Operation(summary = "Add reaction to announcement", description = "Add a reaction to an announcement")
    @ApiResponse(responseCode = "200", description = "Reaction added successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "User, club, or announcement not found")
    @PostMapping("/{userId}/react")
    public ResponseEntity<Object> addReaction(
            @Parameter(description = "ID of the user adding the reaction") @PathVariable Long userId,
            @Parameter(description = "ID of the club the announcement belongs to") @RequestParam Long clubId,
            @Parameter(description = "ID of the announcement to react to") @RequestParam Long announcementId,
            @RequestBody Map<String, String> reactionData) {
        
        try {
            // Validate input
            if (!reactionData.containsKey("type") || reactionData.get("type").trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Reaction type is required"));
            }
            
            // Get the user
            User user = userService.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            
            // Get the club and verify it exists
            Club club = clubService.getClubById(clubId);
            
            // Get the announcement and verify it exists and belongs to the specified club
            Announcement announcement = announcementRepository.findById(announcementId)
                    .orElseThrow(() -> new IllegalArgumentException("Announcement not found"));

            if (!announcement.getClub().getId().equals(clubId)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Announcement does not belong to the specified club"));
            }
            
            // Create and save the reaction
            Reaction reaction = new Reaction();
            reaction.setType(reactionData.get("type"));
            reaction.setReactor(user);
            reaction.setAnnouncement(announcement);
            
            // Add reaction to announcement
            announcement.getReactions().add(reaction);
            announcementRepository.save(announcement);
            
            // Return success message
            return ResponseEntity.ok(Map.of(
                "message", "Reaction added successfully",
                "type", reaction.getType(),
                "userId", user.getId(),
                "announcementId", announcement.getId(),
                "clubId", club.getId()
            ));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to add reaction: " + e.getMessage()));
        }
    }
}
