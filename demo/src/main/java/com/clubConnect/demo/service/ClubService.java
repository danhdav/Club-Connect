package com.clubConnect.demo.service;

import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.misc.Role;
import com.clubConnect.demo.repository.ClubRepository;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClubService {

    private final ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public Club createClub(Club club, User creator) throws AccessDeniedException {
        if (!creator.getRoles().contains(Role.ADMIN)) {
            throw new AccessDeniedException("Only admins can create clubs.");
        }
        return clubRepository.save(club);
    }

    public List<Club> searchClubs(String query) {
        return clubRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Club> searchClubsByTag(List<String> tags) {
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

    public void deleteClub(Long clubId, User requester) throws AccessDeniedException {
        if (!requester.getRoles().contains(Role.ADMIN)) {
            throw new AccessDeniedException("Only admins can delete clubs.");
        }
        clubRepository.deleteById(clubId);
    }

    public Club updateClubName(Long clubId, String newName, User requester) throws AccessDeniedException {
        if (!isAdminOrOfficer(requester)) {
            throw new AccessDeniedException("Only admins or officers can update club names.");
        }
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club with ID " + clubId + " not found"));
        club.setName(newName);
        return clubRepository.save(club);
    }

    public Club updateClubDescription(Long clubId, String newDescription, User requester) throws AccessDeniedException {
        if (!isAdminOrOfficer(requester)) {
            throw new AccessDeniedException("Only admins or officers can update club descriptions.");
        }
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club with ID " + clubId + " not found"));
        club.setDescription(newDescription);
        return clubRepository.save(club);
    }

    private boolean isAdminOrOfficer(User user) {
        return user.getRoles().contains(Role.ADMIN) || user.getRoles().contains(Role.OFFICER);
    }
}