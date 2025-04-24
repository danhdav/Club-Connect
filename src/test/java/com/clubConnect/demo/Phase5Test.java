package com.clubConnect.demo;

import com.clubConnect.demo.controller.OfficerController;
import com.clubConnect.demo.entities.Announcement;
import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.repository.ClubRepository;
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

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Phase5Test {

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private OfficerService officerService;
    @InjectMocks
    private ClubService clubService;
    @InjectMocks
    private UserService userService;
    @InjectMocks
    private OfficerController officerController;

    private User adminUser;
    private User dummyAdminUser;
    private User officerUser;
    private User regularUser;
    private Club testClub;
    private Announcement existingAnn;

    @BeforeEach
    void setUp() {
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
        regularUser.setUsername("Regular User");
        regularUser.setPassword("password");
        regularUser.setId(12345L);

        existingAnn = new Announcement();
        existingAnn.setId(123L);
        existingAnn.setAuthor(officerUser);
        existingAnn.setClub(testClub);
        existingAnn.setCreatedAt(new Date());
        existingAnn.setContentHtml("Hey Gamers");
        testClub.getAnnouncements().add(existingAnn);
    }



    @Test
    void TC1_CreateAnnouncement()
    {
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
    void TC2_CreateAnnouncement()
    {
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
    void TC3_CreateAnnouncement()
    {
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
    void TC4_CreateAnnouncement()
    {
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
    void TC7_CreateAnnouncement()
    {
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
    void TC10_CreateAnnouncement()
    {
        String expectedMessage = "Hey Gamers";
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setContentHtml(expectedMessage);

        when(clubRepository.findById(anyLong())).thenReturn(Optional.of(testClub));

        assertThrows(IllegalStateException.class, () ->
                officerService.createAnnouncement(123L, newAnnouncement, regularUser)
        );
    }

    @Test
    void TC19_CreateAnnouncement()
    {
        String expectedMessage = "Hey Gamers";
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setContentHtml(expectedMessage);

        when(clubRepository.findById(anyLong())).thenReturn(Optional.of(testClub));

        assertThrows(NullPointerException.class, () ->
                officerService.createAnnouncement(123L, newAnnouncement, null)
        );
    }

    @Test
    void TC1_UpdateAnnouncement()
    {
        String expectedMessage = "Hey Gamers";

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        Announcement result =  officerService.updateAnnouncement(123L, 123L, expectedMessage, officerUser);


        assertNotNull(result);
        assertEquals(123L, result.getId());
        assertEquals(expectedMessage, result.getContentHtml());
    }

    @Test
    void TC2_UpdateAnnouncement()
    {
        String expectedMessage = "";

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(IllegalArgumentException.class,
                () -> officerService.updateAnnouncement(123L, 123L, expectedMessage, officerUser)
        );
    }

    @Test
    void TC3_UpdateAnnouncement()
    {
        String expectedMessage = "    ";

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(IllegalArgumentException.class,
                () -> officerService.updateAnnouncement(123L, 123L, expectedMessage, officerUser)
        );
    }

    @Test
    void TC4_UpdateAnnouncement()
    {
        String expectedMessage = "Hey Gamers";

        when(clubRepository.findById(12345L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> officerService.updateAnnouncement(12345L, 123L, expectedMessage, officerUser)
        );
    }

    @Test
    void TC7_UpdateAnnouncement()
    {
        String expectedMessage = "Hey Gamers";

        when(clubRepository.findById(-1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> officerService.updateAnnouncement(-1L, 123L, expectedMessage, officerUser)
        );
    }

    @Test
    void TC10_UpdateAnnouncement()
    {
        String expectedMessage = "Hey Gamers";

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(IllegalArgumentException.class,
                () -> officerService.updateAnnouncement(123L, 12345L, expectedMessage, officerUser)
        );
    }

    @Test
    void TC19_UpdateAnnouncement()
    {
        String expectedMessage = "Hey Gamers";

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(IllegalArgumentException.class,
                () -> officerService.updateAnnouncement(123L, -1L, expectedMessage, officerUser)
        );
    }

    @Test
    void TC28_UpdateAnnouncement()
    {
        String expectedMessage = "Hey Gamers";

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(IllegalStateException.class,
                () -> officerService.updateAnnouncement(123L, -1L, expectedMessage, regularUser)
        );
    }

    @Test
    void TC55_UpdateAnnouncement()
    {
        String expectedMessage = "Hey Gamers";

        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(NullPointerException.class,
                () -> officerService.updateAnnouncement(123L, -1L, expectedMessage, null)
        );
    }

    @Test
    void TC1_DeleteAnnouncement()
    {
        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        officerService.deleteAnnouncement(123L, 123L, officerUser);

        assertTrue(testClub.getAnnouncements().isEmpty());
    }

    @Test
    void TC2_DeleteAnnouncement()
    {
        when(clubRepository.findById(12345L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> officerService.deleteAnnouncement(12345L, 123L, officerUser)
        );
    }

    @Test
    void TC3_DeleteAnnouncement()
    {
        when(clubRepository.findById(-1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> officerService.deleteAnnouncement(-1L, 123L, officerUser)
        );
    }

    @Test
    void TC4_DeleteAnnouncement()
    {
        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(IllegalArgumentException.class,
                () -> officerService.deleteAnnouncement(123L, 12345L, officerUser)
        );
    }

    @Test
    void TC7_DeleteAnnouncement()
    {
        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(IllegalArgumentException.class,
                () -> officerService.deleteAnnouncement(123L, -1L, officerUser)
        );
    }

    @Test
    void TC10_DeleteAnnouncement()
    {
        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(IllegalStateException.class,
                () -> officerService.deleteAnnouncement(123L, 123L, regularUser)
        );
    }

    @Test
    void TC19_DeleteAnnouncement()
    {
        when(clubRepository.findById(123L)).thenReturn(Optional.of(testClub));

        assertThrows(NullPointerException.class,
                () -> officerService.deleteAnnouncement(123L, 123L, null)
        );
    }

}