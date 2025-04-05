package com.clubConnect.demo;

import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.misc.Role;
import com.clubConnect.demo.repository.ClubRepository;
import com.clubConnect.demo.service.ClubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClubServiceTest {

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private ClubService clubService;

    private User adminUser;
    private User officerUser;
    private User regularUser;
    private Club testClub;

    @BeforeEach
    void setUp() {
        // Create users with different roles
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(Role.ADMIN);
        adminUser = new User("Admin User", "admin", "password", adminRoles);
        adminUser.setId(1L);

        Set<Role> officerRoles = new HashSet<>();
        officerRoles.add(Role.OFFICER);
        officerUser = new User("Officer User", "officer", "password", officerRoles);
        officerUser.setId(2L);

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(Role.USER);
        regularUser = new User("Regular User", "user", "password", userRoles);
        regularUser.setId(3L);

        // Create a test club
        testClub = new Club();
        testClub.setId(1L);
        testClub.setName("Comet Racing");
        testClub.setDescription("A club for testing");
        testClub.setTags(Arrays.asList("test", "club", "racing"));
        
        // Add officer to club
        testClub.addOfficer(officerUser);
    }

    @Test
    void TC1_CreateClub() {
        // Arrange
        when(clubRepository.save(any(Club.class))).thenReturn(testClub);

        // Act
        Club createdClub = clubService.createClub(testClub, adminUser);

        // Assert
        assertNotNull(createdClub);
        assertEquals("Comet Racing", createdClub.getName());
        verify(clubRepository).save(testClub);
    }

    @Test
    void TC2_CreateClub() {
        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> 
            clubService.createClub(testClub, regularUser)
        );
        verify(clubRepository, never()).save(any(Club.class));
    }

    @Test
    void TC3_CreateClub() {
        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                clubService.createClub(testClub, null)
        );
        verify(clubRepository, never()).save(any(Club.class));
    }

    @Test
    void TC4_CreateClub() {
        // Act & Assert
        testClub.setName("C");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                clubService.createClub(testClub, adminUser)
        );
        assertEquals("Invalid club name format.", exception.getMessage());
    }

    @Test
    void TC5_CreateClub() {
        // Act & Assert
        testClub.setName("\n");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                clubService.createClub(testClub, adminUser)
        );
        assertEquals("Invalid club name format.", exception.getMessage());
    }

    @Test
    void TC1_SearchClubByTag()
    {
        // Arrange
        List<Club> clubs = Collections.singletonList(testClub);
        when(clubRepository.findByTag("test")).thenReturn(clubs);

        // Act
        List<Club> result = clubService.searchClubsByTag(Collections.singletonList("test"));

        // Assert
        assertEquals(1, result.size());
        assertEquals("Comet Racing", result.get(0).getName());
    }

    @Test
    void TC2_SearchClubByTag()
    {
        // Act
        List<Club> result = clubService.searchClubsByTag(Collections.emptyList());

        // Assert
        assertTrue(result.isEmpty());
        verify(clubRepository, never()).findByTag(anyString());
    }

    @Test
    void TC3_SearchClubByTag()
    {
        // Arrange
        List<Club> clubs = Collections.singletonList(testClub);
        List<String> tags = new ArrayList<>();
        tags.add("test");
        tags.add("test");
        when(clubService.searchClubsByTag(tags)).thenReturn(clubs);

        // Act
        List<Club> result = clubService.searchClubsByTag(Collections.singletonList("test"));

        // Assert
        assertEquals(1, result.size());
        assertEquals("Comet Racing", result.get(0).getName());
    }


    @Test
    void searchClubs_ShouldReturnMatchingClubs() {
        // Arrange
        List<Club> clubs = Collections.singletonList(testClub);
        when(clubRepository.findByNameContainingIgnoreCase("Test")).thenReturn(clubs);

        // Act
        List<Club> result = clubService.searchClubs("Test");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Comet Racing", result.get(0).getName());
    }



    @Test
    void deleteClub_ShouldDeleteClub_WhenUserIsAdmin() {
        // Act
        clubService.deleteClub(1L, adminUser);

        // Assert
        verify(clubRepository).deleteById(1L);
    }

    @Test
    void deleteClub_ShouldThrowException_WhenUserIsNotAdmin() {
        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> 
            clubService.deleteClub(1L, regularUser)
        );
        verify(clubRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateClubName_ShouldUpdateName_WhenUserIsAdmin() {
        // Arrange
        when(clubRepository.findById(1L)).thenReturn(Optional.of(testClub));
        when(clubRepository.save(any(Club.class))).thenReturn(testClub);

        // Act
        Club updatedClub = clubService.updateClubName(1L, "New Club Name", adminUser);

        // Assert
        assertEquals("New Club Name", updatedClub.getName());
        verify(clubRepository).save(testClub);
    }

    @Test
    void updateClubName_ShouldUpdateName_WhenUserIsOfficerOfClub() {
        // Arrange
        when(clubRepository.findById(1L)).thenReturn(Optional.of(testClub));
        when(clubRepository.save(any(Club.class))).thenReturn(testClub);

        // Act
        Club updatedClub = clubService.updateClubName(1L, "New Club Name", officerUser);

        // Assert
        assertEquals("New Club Name", updatedClub.getName());
        verify(clubRepository).save(testClub);
    }

    @Test
    void updateClubName_ShouldThrowException_WhenUserIsNotAdminOrOfficer() {
        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> 
            clubService.updateClubName(1L, "New Club Name", regularUser)
        );
        verify(clubRepository, never()).save(any(Club.class));
    }

    @Test
    void updateClubDescription_ShouldUpdateDescription_WhenUserIsAdmin() {
        // Arrange
        when(clubRepository.findById(1L)).thenReturn(Optional.of(testClub));
        when(clubRepository.save(any(Club.class))).thenReturn(testClub);

        // Act
        Club updatedClub = clubService.updateClubDescription(1L, "New description", adminUser);

        // Assert
        assertEquals("New description", updatedClub.getDescription());
        verify(clubRepository).save(testClub);
    }

    @Test
    void updateClubDescription_ShouldUpdateDescription_WhenUserIsOfficerOfClub() {
        // Arrange
        when(clubRepository.findById(1L)).thenReturn(Optional.of(testClub));
        when(clubRepository.save(any(Club.class))).thenReturn(testClub);

        // Act
        Club updatedClub = clubService.updateClubDescription(1L, "New description", officerUser);

        // Assert
        assertEquals("New description", updatedClub.getDescription());
        verify(clubRepository).save(testClub);
    }

    @Test
    void updateClubDescription_ShouldThrowException_WhenUserIsNotAdminOrOfficer() {
        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> 
            clubService.updateClubDescription(1L, "New description", regularUser)
        );
        verify(clubRepository, never()).save(any(Club.class));
    }

    @Test
    void addOfficer_ShouldAddOfficer_WhenUserIsAdmin() {
        // Arrange
        User newOfficer = new User("New Officer", "newofficer", "password", Collections.singleton(Role.USER));
        newOfficer.setId(4L);
        
        when(clubRepository.findById(1L)).thenReturn(Optional.of(testClub));
        when(clubRepository.save(any(Club.class))).thenReturn(testClub);

        // Act
        Club updatedClub = clubService.addOfficer(1L, newOfficer, adminUser);

        // Assert
        assertTrue(updatedClub.getOfficers().contains(newOfficer));
        verify(clubRepository).save(testClub);
    }

    @Test
    void addOfficer_ShouldThrowException_WhenUserIsNotAdmin() {
        // Arrange
        User newOfficer = new User("New Officer", "newofficer", "password", Collections.singleton(Role.USER));
        newOfficer.setId(4L);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> 
            clubService.addOfficer(1L, newOfficer, regularUser)
        );
        verify(clubRepository, never()).save(any(Club.class));
    }

    @Test
    void getTotalClubs_ShouldReturnTotalCount() {
        // Arrange
        when(clubRepository.count()).thenReturn(5L);

        // Act
        long count = clubService.getTotalClubs();

        // Assert
        assertEquals(5L, count);
        verify(clubRepository).count();
    }
}
