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

import java.nio.file.AccessDeniedException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClubServiceTest {

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private ClubService clubService;

    private User adminUser;
    private User regularUser;
    private Club club;

    @BeforeEach
    public void setUp() {
        adminUser = new User("admin", "password", Collections.singleton(Role.ADMIN));

        regularUser = new User("user", "password", Collections.singleton(Role.USER));

        club = new Club();
        club.setName("Tech Club");
        club.setDescription("A club for tech enthusiasts");
        List<String> tags = new ArrayList<>();
        tags.add("Technology");
        tags.add("Robotics");
        club.setTags(tags);
    }

    @Test
    public void testCreateClub_Success_AdminUser() throws AccessDeniedException {
        when(clubRepository.save(club)).thenReturn(club);

        Club result = clubService.createClub(club, adminUser);

        assertNotNull(result);
        assertEquals("Tech Club", result.getName());
        assertEquals("A club for tech enthusiasts", result.getDescription());
        verify(clubRepository, times(1)).save(club);
    }

    @Test
    public void testCreateClub_Failure_NonAdminUser() {

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            clubService.createClub(club, regularUser);
        });
        assertEquals("Only admins can create clubs.", exception.getMessage());
        verify(clubRepository, never()).save(any(Club.class));
    }

    @Test
    public void testSearchClubs_Success() {
        // Arrange
        Club club1 = new Club();
        club1.setName("Tech Club");
        Club club2 = new Club();
        club2.setName("Tech Enthusiasts");
        List<Club> expectedClubs = Arrays.asList(club1, club2);

        when(clubRepository.findByNameContainingIgnoreCase("tech")).thenReturn(expectedClubs);

        List<Club> result = clubService.searchClubs("tech");

        assertEquals(2, result.size());
        assertEquals("Tech Club", result.get(0).getName());
        assertEquals("Tech Enthusiasts", result.get(1).getName());
        verify(clubRepository, times(1)).findByNameContainingIgnoreCase("tech");
    }

    @Test
    public void testSearchClubsByTags_Success() {
        Club club1 = new Club();
        club1.setName("Tech Robotics Club");
        club1.setTags(Arrays.asList("Technology", "Robotics"));

        Club club2 = new Club();
        club2.setName("Technology Club");
        club2.setTags(Arrays.asList("Technology", "Science"));

        Club club3 = new Club();
        club3.setName("Robotics Club");
        club3.setTags(Arrays.asList("Robotics", "Engineering"));

        List<String> tags1 = Arrays.asList("Technology");
        when(clubRepository.findByTag("Technology")).thenReturn(Arrays.asList(club1, club2));
        List<Club> result1 = clubService.searchClubsByTag(tags1);
        assertEquals(2, result1.size());
        assertEquals("Tech Robotics Club", result1.get(0).getName());
        assertEquals("Technology Club", result1.get(1).getName());

    }

    @Test
    public void testSearchClubs_NoResults() {
        when(clubRepository.findByNameContainingIgnoreCase("xyz")).thenReturn(Arrays.asList());

        List<Club> result = clubService.searchClubs("xyz");

        assertTrue(result.isEmpty());
        verify(clubRepository, times(1)).findByNameContainingIgnoreCase("xyz");
    }

    @Test
    public void testDeleteClub_Success_AdminUser() throws AccessDeniedException {
        Long clubId = 1L;
        doNothing().when(clubRepository).deleteById(clubId);

        clubService.deleteClub(clubId, adminUser);

        verify(clubRepository, times(1)).deleteById(clubId);
    }

    @Test
    public void testDeleteClub_Failure_NonAdminUser() {
        Long clubId = 1L;

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            clubService.deleteClub(clubId, regularUser);
        });
        assertEquals("Only admins can delete clubs.", exception.getMessage());
        verify(clubRepository, never()).deleteById(anyLong());
    }
}