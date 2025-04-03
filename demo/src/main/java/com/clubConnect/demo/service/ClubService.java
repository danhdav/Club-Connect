package com.clubConnect.demo.service;

import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.misc.Role;
import com.clubConnect.demo.repository.ClubRepository;
import com.clubConnect.demo.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClubService {

    private final ClubRepository clubRepository;

    @Autowired
    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    /**
     * Creates a new club (admin only)
     * @param club The club to create
     * @param requester The user creating the club
     * @return The created club
     * @throws AccessDeniedException If the requester is not an admin
     */
    @PreAuthorize("hasRole('ADMIN')")
    public Club createClub(Club club, User requester) throws AccessDeniedException {
        if (requester == null || !requester.getRoles().contains(Role.ADMIN)) {
            throw new AccessDeniedException("Only admins can create clubs.");
        }

        if (!ValidationUtils.isValidClubName(club.getName())) {
            throw new IllegalArgumentException("Invalid club name format.");
        }

        return clubRepository.save(club);
    }

    /**
     * Searches clubs by name
     * @param query The search query
     * @return List of clubs matching the query
     */
    public List<Club> searchClubs(String query) {
        return clubRepository.findByNameContainingIgnoreCase(query);
    }

    /**
     * Searches clubs by tags (AND logic)
     * @param tags List of tags to search for
     * @return List of clubs that have ALL the specified tags
     */
    public List<Club> searchClubsByTag(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Club> result = clubRepository.findByTag(tags.get(0));
        for (int i = 1; i < tags.size(); i++) {
            String currentTag = tags.get(i);
            result = result.stream()
                    .filter(club -> club.getTags().contains(currentTag))
                    .collect(Collectors.toList());
            if (result.isEmpty()) {
                return new ArrayList<>();
            }
        }
        return result;
    }

    /**
     * Deletes a club (admin only)
     * @param clubId The ID of the club to delete
     * @param requester The user requesting the deletion
     * @throws AccessDeniedException If the requester is not an admin
     */
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteClub(Long clubId, User requester) throws AccessDeniedException {
        if (requester == null || !requester.getRoles().contains(Role.ADMIN)) {
            throw new AccessDeniedException("Only admins can delete clubs.");
        }
        clubRepository.deleteById(clubId);
    }

    /**
     * Updates a club's name (admin or officer only)
     * @param clubId The ID of the club to update
     * @param newName The new name for the club
     * @param requester The user requesting the update
     * @return The updated club
     * @throws AccessDeniedException If the requester is not an admin or officer
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER')")
    public Club updateClubName(Long clubId, String newName, User requester) throws AccessDeniedException {
        if (requester == null || !isAdminOrOfficer(requester)) {
            throw new AccessDeniedException("Only admins or officers can update club names.");
        }
        
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club with ID " + clubId + " not found"));
                
        // Check if officer is associated with this specific club
        if (requester.getRoles().contains(Role.OFFICER) && 
            !requester.getRoles().contains(Role.ADMIN) && 
            !club.getOfficers().contains(requester)) {
            throw new AccessDeniedException("Officers can only update clubs they are assigned to.");
        }
        
        club.setName(newName);
        return clubRepository.save(club);
    }

    /**
     * Updates a club's description (admin or officer only)
     * @param clubId The ID of the club to update
     * @param newDescription The new description for the club
     * @param requester The user requesting the update
     * @return The updated club
     * @throws AccessDeniedException If the requester is not an admin or officer
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'OFFICER')")
    public Club updateClubDescription(Long clubId, String newDescription, User requester) throws AccessDeniedException {
        if (requester == null || !isAdminOrOfficer(requester)) {
            throw new AccessDeniedException("Only admins or officers can update club descriptions.");
        }
        
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club with ID " + clubId + " not found"));
                
        // Check if officer is associated with this specific club
        if (requester.getRoles().contains(Role.OFFICER) && 
            !requester.getRoles().contains(Role.ADMIN) && 
            !club.getOfficers().contains(requester)) {
            throw new AccessDeniedException("Officers can only update clubs they are assigned to.");
        }
        
        club.setDescription(newDescription);
        return clubRepository.save(club);
    }

    /**
     * Gets the total number of clubs
     * @return The total number of clubs
     */
    public long getTotalClubs() {
        return clubRepository.count();
    }
    
    /**
     * Adds an officer to a club (admin only)
     * @param clubId The ID of the club
     * @param userId The ID of the user to add as officer
     * @param requester The user making the request
     * @return The updated club
     * @throws AccessDeniedException If the requester is not an admin
     */
    @PreAuthorize("hasRole('ADMIN')")
    public Club addOfficer(Long clubId, User officer, User requester) throws AccessDeniedException {
        if (requester == null || !requester.getRoles().contains(Role.ADMIN)) {
            throw new AccessDeniedException("Only admins can add officers to clubs.");
        }
        
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club with ID " + clubId + " not found"));
                
        club.addOfficer(officer);
        return clubRepository.save(club);
    }

    /**
     * Checks if a user is an admin or officer
     * @param user The user to check
     * @return true if the user is an admin or officer, false otherwise
     */
    private boolean isAdminOrOfficer(User user) {
        return user.getRoles().contains(Role.ADMIN) || user.getRoles().contains(Role.OFFICER);
    }
}