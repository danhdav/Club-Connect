package com.clubConnect.demo.service;

import com.clubConnect.demo.entities.Announcement;
import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.repository.ClubRepository;
import com.clubConnect.demo.repository.UserRepository;
import com.clubConnect.demo.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClubService {

    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    @Autowired
    public ClubService(ClubRepository clubRepository, UserRepository userRepository) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
    }

    public Club createClub(Club club, Long userId) {
        User requester = getUserById(userId);
        if (requester == null || !requester.isAdmin()) {
            throw new IllegalStateException("Only admins can create clubs.");
        }
        if (!ValidationUtils.isValidClubName(club.getName())) {
            throw new IllegalArgumentException("Invalid club name format.");
        }
        return clubRepository.save(club);
    }

    public List<Club> searchClubs(String query) {
        return clubRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Club> searchClubsByTag(List<String> tags) {
        if (tags == null || tags.isEmpty()) return new ArrayList<>();
        List<Club> result = clubRepository.findByTag(tags.get(0));
        for (int i = 1; i < tags.size(); i++) {
            String currentTag = tags.get(i);
            result = result.stream()
                    .filter(club -> club.getTags().contains(currentTag))
                    .collect(Collectors.toList());
            if (result.isEmpty()) return new ArrayList<>();
        }
        return result;
    }

    public void deleteClub(Long clubId, Long userId) {
        User requester = getUserById(userId);
        if (requester == null || !requester.isAdmin()) {
            throw new IllegalStateException("Only admins can delete clubs.");
        }
        clubRepository.deleteById(clubId);
    }

    public Club updateClubName(Long clubId, String newName, Long userId) {
        Club club = checkAccessAndGetClub(clubId, userId);
        club.setName(newName);
        return clubRepository.save(club);
    }

    public Club updateClubDescription(Long clubId, String newDescription, Long userId) {
        Club club = checkAccessAndGetClub(clubId, userId);
        club.setDescription(newDescription);
        return clubRepository.save(club);
    }

    public Club addOfficer(Long clubId, Long officerId, Long requesterId) {
        User requester = getUserById(requesterId);
        if (requester == null || !requester.isAdmin()) {
            throw new IllegalStateException("Only admins can add officers to clubs.");
        }
        
        User officer = getUserById(officerId);
        if (officer == null) {
            throw new IllegalArgumentException("Officer not found");
        }
        
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));
        club.addOfficer(officer);
        return clubRepository.save(club);
    }

    public long getTotalClubs() {
        return clubRepository.count();
    }

    /**
     * Get all clubs in the system
     * @return List of all clubs
     */
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public Map<String, Object> getClubPageInfo(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("id", club.getId());
        response.put("name", club.getName());
        response.put("description", club.getDescription());
        response.put("tags", club.getTags());
        response.put("officers", club.getOfficers().stream().map(User::getName).toList());
        
        // Use actual images if available, or provide placeholders
        List<String> imageUrls = club.getImages();
        if (imageUrls == null || imageUrls.isEmpty()) {
            // Provide placeholder images
            imageUrls = List.of(
                    "/images/placeholders/club-default-1.jpg",
                    "/images/placeholders/club-default-2.jpg"
            );
        }
        response.put("images", imageUrls);
        
        response.put("welcomeText", "Welcome to " + club.getName() + "! Join us today!");

        return response;
    }

    public List<Map<String, Object>> getAnnouncementsWithDetails(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));

        List<Announcement> announcements = club.getAnnouncements();

        return announcements.stream().map(announcement -> {
            Map<String, Object> data = new HashMap<>();
            data.put("id", announcement.getId());
            data.put("contentHtml", announcement.getContentHtml());
            data.put("imageUrls", announcement.getImageUrls());
            data.put("author", announcement.getAuthor().getName());
            data.put("createdAt", announcement.getCreatedAt());

            data.put("comments", announcement.getComments().stream().map(c -> Map.of(
                    "author", c.getAuthor().getName(),
                    "text", c.getText(),
                    "createdAt", c.getCreatedAt()
            )).toList());

            data.put("reactions", announcement.getReactions().stream().map(r -> Map.of(
                    "type", r.getType(),
                    "reactor", r.getReactor().getName()
            )).toList());

            return data;
        }).toList();
    }

    public Announcement getAnnouncementFromId(Long clubId, Long announcementId)
    {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));

        List<Announcement> announcements = club.getAnnouncements();

        for (Announcement announcementEntry : announcements)
        {
            if (announcementEntry.getId().equals(announcementId))
            {
                return announcementEntry;
            }
        }

        throw new IllegalArgumentException("Announcement not found with ID: " + announcementId);
    }

    /**
     * Toggle a user's subscription status for a club
     * @param clubId Club ID
     * @param userId User ID
     * @return true if subscribed after toggle, false if unsubscribed
     */
    public boolean toggleSubscription(Long clubId, Long userId) {
        User user = getUserById(userId);
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));
        
        boolean wasSubscribed = user.getSubscribedClubs().contains(club);
        
        if (wasSubscribed) {
            user.unsubscribeFromClub(club);
        } else {
            user.subscribeToClub(club);
        }
        
        userRepository.save(user);
        return !wasSubscribed; // Return new subscription status
    }

    private Club checkAccessAndGetClub(Long clubId, Long userId) {
        User requester = getUserById(userId);
        if (requester == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        if (!isAdminOrOfficer(requester)) {
            throw new IllegalStateException("Only admins or officers can modify clubs.");
        }
        
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));
                
        if (!requester.isAdmin() && !club.getOfficers().contains(requester)) {
            throw new IllegalStateException("Officer not assigned to this club.");
        }
        
        return club;
    }

    private boolean isAdminOrOfficer(User user) {
        return user.isAdmin() || user.getOfficerOf().size() > 0;
    }
    
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    /**
     * Get a club by its ID
     * @param clubId Club ID
     * @return Club entity
     * @throws IllegalArgumentException if club not found
     */
    public Club getClubById(Long clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));
    }
}
