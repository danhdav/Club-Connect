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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    private OfficerService officerService;

    @InjectMocks
    private ClubService clubService;
    @InjectMocks
    private OfficerController officerController;

    private User adminUser;
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
        adminUser = new User();
        adminUser.setUsername("Admin User");
        adminUser.setAdminStatus(true);
        adminUser.setPassword("password");
        adminUser.setId(1L);

        Set<Club> officerClubs = new HashSet<>();
        officerClubs.add(testClub);
        officerUser = new User();
        officerUser.setUsername("Officer User");
        officerUser.setPassword("password");
        officerUser.setOfficerOf(officerClubs);
        officerUser.setId(123L);

        regularUser = new User();
        officerUser.setUsername("Regular User");
        officerUser.setPassword("password");
        regularUser.setId(1234L);


        // Add officer to club
        testClub.addOfficer(officerUser);
    }

    @Test
    void TC1_CreateAnnouncement()
    {
        String expectedContent = "Hey Gamers";
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setContentHtml(expectedContent);

        Announcement createdAnnouncement = officerService.createAnnouncement(123L, newAnnouncement, officerUser);

        // Assert
        assertNotNull(createdAnnouncement);
        assertEquals(expectedContent, createdAnnouncement.getContentHtml());
        verify(clubRepository).save(testClub);
    }
}