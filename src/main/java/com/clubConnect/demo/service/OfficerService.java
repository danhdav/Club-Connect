package com.clubConnect.demo.service;

import com.clubConnect.demo.entities.Announcement;
import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.repository.AnnouncementRepository;
import com.clubConnect.demo.repository.ClubRepository;
import com.clubConnect.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OfficerService {

    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final AnnouncementRepository announcementRepository;

    public OfficerService(ClubRepository clubRepository, UserRepository userRepository, EmailService emailService, AnnouncementRepository announcementRepository) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.announcementRepository = announcementRepository;
    }

    public Announcement createAnnouncement(Long clubId, Announcement announcement, User officer) {
        // Get the club and validate officer access
        Club club = validateOfficerAccess(clubId, officer);
        
        // Set up the announcement
        announcement.setClub(club);
        announcement.setAuthor(officer);
        announcement.setCreatedAt(new Date());
        
        // Add to club and save the club
        club.getAnnouncements().add(announcement);
        Club savedClub = clubRepository.save(club);
        
        // Get the saved announcement (should now have an ID)
        Announcement savedAnnouncement = findLastAnnouncementByOfficer(club, officer);

        try {
            emailService.sendAnnouncementNotificationsToSubscribers(savedAnnouncement, savedClub);
        } catch (Exception e) {
            System.err.println("Failed to send notification emails: " + e.getMessage());
        }

        return savedAnnouncement;
    }

    public Announcement updateAnnouncement(Long clubId, Long announcementId, String newContent, User officer) {
        Club club = validateOfficerAccess(clubId, officer);
        Announcement announcement = findAnnouncementById(club, announcementId);
        announcement.setContentHtml(newContent);
        return announcementRepository.save(announcement);
    }

    public void deleteAnnouncement(Long clubId, Long announcementId, User officer) {
        Club club = validateOfficerAccess(clubId, officer);
        Announcement announcement = findAnnouncementById(club, announcementId);
        club.getAnnouncements().remove(announcement);
        clubRepository.save(club);
    }
    public void deleteComment(Long clubId, Long announcementId, Long commentId, User officer) {
        Club club = validateOfficerAccess(clubId, officer);
        Announcement announcement = findAnnouncementById(club, announcementId);
        announcement.getComments().removeIf(c -> c.getId().equals(commentId));
    }

    public Club updateClubPage(Long clubId, String description, List<String> images, User officer) {
        Club club = validateOfficerAccess(clubId, officer);
        if (description != null) club.setDescription(description);
        if (images != null) club.setImages(images);


        return clubRepository.save(club);
    }

    public Club removeImages(Long clubId, User officer) {
        Club club = validateOfficerAccess(clubId, officer);
        club.removeImages();
        return clubRepository.save(club);
    }


    public List<User> getAllClubMembers(Long clubId, User officer) {
        Club club = validateOfficerAccess(clubId, officer);
        return userRepository.findAll().stream()
                .filter(u -> u.getSubscribedClubs().contains(club))
                .collect(Collectors.toList());
    }

    public void removeClubMember(Long clubId, Long memberId, User officer) {
        Club club = validateOfficerAccess(clubId, officer);
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.unsubscribeFromClub(club);
    }

    private Club validateOfficerAccess(Long clubId, User officer) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));
        if (!officer.getOfficerOf().contains(club)) {
            throw new IllegalStateException("Officer does not have access to this club.");
        }
        return club;
    }

    private Announcement findAnnouncementById(Club club, Long announcementId) {
        return club.getAnnouncements().stream()
                .filter(a -> a.getId().equals(announcementId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Announcement not found"));
    }

    public Club updateClubPageContent(Long clubId, String description, List<String> images, User officer) {
        Club club = validateOfficerAccess(clubId, officer);

        if (description != null) {
            club.setDescription(description);
        }

        if (images != null && !images.isEmpty()) {
            club.setImages(images);
        }

        return club;
    }

    /**
     * Helper method to find the most recent announcement by an officer in a club
     */
    private Announcement findLastAnnouncementByOfficer(Club club, User officer) {
        return club.getAnnouncements().stream()
            .filter(a -> a.getAuthor().getId().equals(officer.getId()))
            .max(Comparator.comparing(Announcement::getCreatedAt))
            .orElseThrow(() -> new IllegalStateException("Announcement not found after saving"));
    }

}
