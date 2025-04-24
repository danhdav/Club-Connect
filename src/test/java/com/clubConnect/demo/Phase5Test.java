package com.clubConnect.demo;

import com.clubConnect.demo.controller.OfficerController;
import com.clubConnect.demo.entities.Announcement;
import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.misc.Role;
import com.clubConnect.demo.repository.ClubRepository;
import com.clubConnect.demo.repository.UserRepository;
import com.clubConnect.demo.service.ClubService;
import com.clubConnect.demo.service.OfficerService;
import com.clubConnect.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

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
    }



    @Test
    void TC1_CreateAnnouncement()
    {
        String expectedMessage = "Hey Gamers";
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setContentHtml(expectedMessage);

        when(clubRepository.findById(anyLong())).thenReturn(Optional.of(testClub));

        Announcement createdAnnouncement = officerService.createAnnouncement(123L, newAnnouncement, officerUser);

        // Assert
        assertNotNull(createdAnnouncement);
        assertEquals(expectedMessage, createdAnnouncement.getContentHtml());
    }

    @Test
    void TC2_CreateAnnouncement()
    {
        String expectedMessage = "Hey Gamers";
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setContentHtml(expectedMessage);

        when(clubRepository.findById(anyLong())).thenReturn(Optional.of(testClub));

        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                officerService.createAnnouncement(123L, newAnnouncement, officerUser)
        );
    }

    @Test
    void TC3_CreateAnnouncement()
    {
        String expectedMessage = "Hey Gamers";
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setContentHtml(expectedMessage);

        when(clubRepository.findById(anyLong())).thenReturn(Optional.of(testClub));

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

        // Assert
        assertThrows(IllegalArgumentException.class, () ->
                officerService.createAnnouncement(12345L, newAnnouncement, officerUser)
        );
    }

    @Test
    void TC7_CreateAnnouncement()
    {
        String expectedMessage = "Hey Gamers";
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setContentHtml(expectedMessage);

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
}