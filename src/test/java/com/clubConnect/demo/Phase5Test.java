package com.clubConnect.demo;

import com.clubConnect.demo.controller.OfficerController;
import com.clubConnect.demo.controller.UserController;
import com.clubConnect.demo.entities.*;
import com.clubConnect.demo.repository.AnnouncementRepository;
import com.clubConnect.demo.repository.ClubRepository;
import com.clubConnect.demo.repository.CommentRepository;
import com.clubConnect.demo.repository.UserRepository;
import com.clubConnect.demo.service.ClubService;
import com.clubConnect.demo.service.OfficerService;
import com.clubConnect.demo.service.UserService;
import com.clubConnect.demo.service.EmailService;
import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Phase5Test {

    // Mocks
    @Mock private ClubRepository clubRepository;
    @Mock private UserRepository userRepository;
    @Mock private AnnouncementRepository announcementRepository;
    @Mock private CommentRepository commentRepository;
    @Mock private EmailService emailService;

    // Services (real instances created with mocks passed in)
    private OfficerService officerService;
    private ClubService clubService;
    private UserService userService;

    // Controllers (real instances created with services)
    private OfficerController officerController;
    private UserController userController;

    // Entities
    private User adminUser;
    private User officerUser;
    private User regularUser;
    private Club testClub;
    private Announcement testAnnouncement;
    private Comment testComment;

    @BeforeEach
    void setUp() {

        // Create services passing mocks
        clubService = new ClubService(clubRepository, userRepository);
        userService = new UserService(userRepository, clubRepository);
        officerService = new OfficerService(clubRepository, userRepository, emailService);

        // Create controllers, passing services
        officerController = new OfficerController(officerService, userService);
        // For UserController, mock CommentRepository here
        userController = new UserController(userService, clubService, commentRepository, announcementRepository);

        // Create a test club
        testClub = new Club();
        testClub.setId(123L);
        testClub.setName("Test Club");
        testClub.setDescription("A club for testing");
        testClub.setTags(Arrays.asList("test", "club", "java"));


        // Create users

        officerUser = new User();
        officerUser.setUsername("Officer User");
        officerUser.setPassword("password");
        officerUser.setOfficerOf(testClub);
        officerUser.setId(123L);

        regularUser = new User();
        regularUser.setName("Regular User");
        regularUser.setUsername("Regular User");
        regularUser.setPassword("password");
        regularUser.setId(123L);

        testAnnouncement = new Announcement();
        testAnnouncement.setId(123L);
        testAnnouncement.setAuthor(officerUser);
        testAnnouncement.setClub(testClub);
        testAnnouncement.setCreatedAt(new Date());
        testAnnouncement.setContentHtml("Hey Gamers");
        testAnnouncement.setReactions(new ArrayList<Reaction>());
        testClub.getAnnouncements().add(testAnnouncement);

        testComment = new Comment();
        testComment.setId(123L);
        testComment.setAuthor(regularUser);
        testComment.setAnnouncement(testAnnouncement);
        testComment.setText("so cool");
        testComment.setCreatedAt(LocalDateTime.now());
    }


    @Test
    void TC1_CreateAnnouncement() {
        String expectedMessage = "Hey Gamers";
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setContentHtml(expectedMessage);

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        Announcement createdAnnouncement = officerService.createAnnouncement(123L, newAnnouncement, officerUser);

        // Assert
        assertNotNull(createdAnnouncement);
        assertEquals(expectedMessage, createdAnnouncement.getContentHtml());
    }

    @Test
    void TC2_CreateAnnouncement() {
        String expectedMessage = "";
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setContentHtml(expectedMessage);

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                officerService.createAnnouncement(123L, newAnnouncement, officerUser)
        );
    }

    @Test
    void TC3_CreateAnnouncement() {
        String expectedMessage = "        ";
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setContentHtml(expectedMessage);

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                officerService.createAnnouncement(123L, newAnnouncement, officerUser)
        );
    }

    @Test
    void TC4_CreateAnnouncement() {
        String expectedMessage = "Hey Gamers";
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setContentHtml(expectedMessage);

        when(clubRepository.findById(12345L)).thenReturn(Optional.empty());

        // Assert
        assertThrows(IllegalArgumentException.class,
                () -> officerService.createAnnouncement(12345L, newAnnouncement, officerUser)
        );
    }

    @Test
    void TC7_CreateAnnouncement() {
        String expectedMessage = "Hey Gamers";
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setContentHtml(expectedMessage);

        when(clubRepository.findById(-1L)).thenReturn(Optional.empty());

        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                officerService.createAnnouncement(-1L, newAnnouncement, officerUser)
        );
    }

    @Test
    void TC10_CreateAnnouncement() {
        String expectedMessage = "Hey Gamers";
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setContentHtml(expectedMessage);

        when(clubRepository.findById(anyLong())).thenReturn(Optional.of(testClub));

        assertThrows(IllegalStateException.class, () ->
                officerService.createAnnouncement(123L, newAnnouncement, regularUser)
        );
    }

    @Test
    void TC19_CreateAnnouncement() {
        String expectedMessage = "Hey Gamers";
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setContentHtml(expectedMessage);

        when(clubRepository.findById(anyLong())).thenReturn(Optional.of(testClub));

        assertThrows(NullPointerException.class, () ->
                officerService.createAnnouncement(123L, newAnnouncement, null)
        );
    }

    @Test
    void TC1_UpdateAnnouncement() {
        String expectedMessage = "Hey Gamers";

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        Announcement result = officerService.updateAnnouncement(123L, 123L, expectedMessage, officerUser);


        assertNotNull(result);
        assertEquals(123L, result.getId());
        assertEquals(expectedMessage, result.getContentHtml());
    }

    @Test
    void TC2_UpdateAnnouncement() {
        String expectedMessage = "";

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(IllegalArgumentException.class,
                () -> officerService.updateAnnouncement(123L, 123L, expectedMessage, officerUser)
        );
    }

    @Test
    void TC3_UpdateAnnouncement() {
        String expectedMessage = "    ";

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(IllegalArgumentException.class,
                () -> officerService.updateAnnouncement(123L, 123L, expectedMessage, officerUser)
        );
    }

    @Test
    void TC4_UpdateAnnouncement() {
        String expectedMessage = "Hey Gamers";

        when(clubRepository.findById(12345L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> officerService.updateAnnouncement(12345L, 123L, expectedMessage, officerUser)
        );
    }

    @Test
    void TC7_UpdateAnnouncement() {
        String expectedMessage = "Hey Gamers";

        when(clubRepository.findById(-1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> officerService.updateAnnouncement(-1L, 123L, expectedMessage, officerUser)
        );
    }

    @Test
    void TC10_UpdateAnnouncement() {
        String expectedMessage = "Hey Gamers";

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(IllegalArgumentException.class,
                () -> officerService.updateAnnouncement(123L, 12345L, expectedMessage, officerUser)
        );
    }

    @Test
    void TC19_UpdateAnnouncement() {
        String expectedMessage = "Hey Gamers";

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(IllegalArgumentException.class,
                () -> officerService.updateAnnouncement(123L, -1L, expectedMessage, officerUser)
        );
    }

    @Test
    void TC28_UpdateAnnouncement() {
        String expectedMessage = "Hey Gamers";

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(IllegalStateException.class,
                () -> officerService.updateAnnouncement(123L, -1L, expectedMessage, regularUser)
        );
    }

    @Test
    void TC55_UpdateAnnouncement() {
        String expectedMessage = "Hey Gamers";

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(NullPointerException.class,
                () -> officerService.updateAnnouncement(123L, -1L, expectedMessage, null)
        );
    }

    @Test
    void TC1_DeleteAnnouncement() {
        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        officerService.deleteAnnouncement(123L, 123L, officerUser);

        assertTrue(testClub.getAnnouncements().isEmpty());
    }

    @Test
    void TC2_DeleteAnnouncement() {
        when(clubRepository.findById(12345L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> officerService.deleteAnnouncement(12345L, 123L, officerUser)
        );
    }

    @Test
    void TC3_DeleteAnnouncement() {
        when(clubRepository.findById(-1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> officerService.deleteAnnouncement(-1L, 123L, officerUser)
        );
    }

    @Test
    void TC4_DeleteAnnouncement() {
        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(IllegalArgumentException.class,
                () -> officerService.deleteAnnouncement(123L, 12345L, officerUser)
        );
    }

    @Test
    void TC7_DeleteAnnouncement() {
        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(IllegalArgumentException.class,
                () -> officerService.deleteAnnouncement(123L, -1L, officerUser)
        );
    }

    @Test
    void TC10_DeleteAnnouncement() {
        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(IllegalStateException.class,
                () -> officerService.deleteAnnouncement(123L, 123L, regularUser)
        );
    }

    @Test
    void TC19_DeleteAnnouncement() {
        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(NullPointerException.class,
                () -> officerService.deleteAnnouncement(123L, 123L, null)
        );
    }

    @Test
    void TC1_AddReaction()
    {
        Map<String, String> reactionData = new HashMap<>();
        reactionData.put("type", "like");

        when(userRepository.findById(123L)).thenReturn(Optional.of(regularUser));
        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));
        when(announcementRepository.findById(123L)).thenReturn(Optional.of(testAnnouncement));
        when(announcementRepository.save(any(Announcement.class))).thenAnswer(i -> i.getArgument(0));

        ResponseEntity<Object> response = userController.addReaction(123L, 123L, 123L, reactionData);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void TC2_AddReaction()
    {
        Map<String, String> reactionData = new HashMap<>();
        reactionData.put("type", "like");

        when(userRepository.findById(123L)).thenReturn(Optional.of(regularUser));
        when(clubRepository.findById(12345L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.addReaction(123L, 12345L, 123L, reactionData);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void TC3_AddReaction()
    {
        Map<String, String> reactionData = new HashMap<>();
        reactionData.put("type", "like");

        when(userRepository.findById(123L)).thenReturn(Optional.of(regularUser));
        when(clubRepository.findById(-1L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.addReaction(123L, -1L, 123L, reactionData);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void TC4_AddReaction()
    {
        Map<String, String> reactionData = new HashMap<>();
        reactionData.put("type", "like");

        when(userRepository.findById(123L)).thenReturn(Optional.of(regularUser));
        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));
        when(announcementRepository.findById(12345L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.addReaction(123L, 123L, 12345L, reactionData);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void TC7_AddReaction()
    {
        Map<String, String> reactionData = new HashMap<>();
        reactionData.put("type", "like");

        when(userRepository.findById(123L)).thenReturn(Optional.of(regularUser));
        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));
        when(announcementRepository.findById(-1L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.addReaction(123L, 123L, -1L, reactionData);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void TC10_AddReaction()
    {
        Map<String, String> reactionData = new HashMap<>();
        reactionData.put("type", "like");

        when(userRepository.findById(12345L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.addReaction(12345L, 123L, 123L, reactionData);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void TC19_AddReaction()
    {
        Map<String, String> reactionData = new HashMap<>();
        reactionData.put("type", "like");

        when(userRepository.findById(-1L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.addReaction(-1L, 123L, 123L, reactionData);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void TC1_AddComment()
    {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("text", "so cool");

        when(userRepository.findById(123L)).thenReturn(Optional.of(regularUser));
        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));
        when(announcementRepository.findById(123L)).thenReturn(Optional.of(testAnnouncement));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        ResponseEntity<Object> response = userController.addComment(123L, 123L, 123L, commentData);
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void TC2_AddComment()
    {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("text", "");

        ResponseEntity<Object> response = userController.addComment(123L, 123L, 123L, commentData);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void TC3_AddComment()
    {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("text", "      ");

        ResponseEntity<Object> response = userController.addComment(123L, 123L, 123L, commentData);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void TC4_AddComment()
    {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("text", "so cool");

        when(userRepository.findById(123L)).thenReturn(Optional.of(regularUser));
        when(clubRepository.findById(12345L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.addComment(123L, 12345L, 123L, commentData);
        assertEquals(404, response.getStatusCodeValue());
    }
    @Test
    void TC7_AddComment()
    {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("text", "so cool");

        when(userRepository.findById(123L)).thenReturn(Optional.of(regularUser));
        when(clubRepository.findById(-1L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.addComment(123L, -1L, 123L, commentData);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void TC10_AddComment()
    {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("text", "so cool");

        when(userRepository.findById(123L)).thenReturn(Optional.of(regularUser));
        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));
        when(announcementRepository.findById(12345L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.addComment(123L, 123L, 12345L, commentData);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void TC19_AddComment()
    {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("text", "so cool");

        when(userRepository.findById(123L)).thenReturn(Optional.of(regularUser));
        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));
        when(announcementRepository.findById(-1L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.addComment(123L, 123L, -1L, commentData);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void TC28_AddComment()
    {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("text", "so cool");

        when(userRepository.findById(12345L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.addComment(12345L, 123L, 123L, commentData);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void TC55_AddComment()
    {
        Map<String, String> commentData = new HashMap<>();
        commentData.put("text", "so cool");

        when(userRepository.findById(-1L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.addComment(-1L, 123L, 123L, commentData);
        assertEquals(404, response.getStatusCodeValue());
    }
}