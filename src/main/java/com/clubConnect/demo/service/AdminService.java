package com.clubConnect.demo.service;

import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.repository.ClubRepository;
import com.clubConnect.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    public AdminService(ClubRepository clubRepository, UserRepository userRepository) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
    }

    public Club createClub(Club club, User admin) {
        if (!admin.isAdmin()) {
            throw new RuntimeException("Only admins can create clubs.");
        }
        return clubRepository.save(club);
    }

    public Club updateClub(Long clubId, String name, String description, List<String> tags, User admin) {
        if (!admin.isAdmin()) {
            throw new RuntimeException("Only admins can edit clubs.");
        }

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));

        if (name != null) club.setName(name);
        if (description != null) club.setDescription(description);
        if (tags != null) club.setTags(tags);

        return clubRepository.save(club);
    }

    public void deleteClub(Long clubId, User admin) {
        if (!admin.isAdmin()) {
            throw new RuntimeException("Only admins can delete clubs.");
        }
        clubRepository.deleteById(clubId);
    }

    public Club updateClubOfficers(Long clubId, List<Long> userIds, User admin) {
        if (!admin.isAdmin()) {
            throw new RuntimeException("Only admins can update officers.");
        }

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));

        List<User> officers = userRepository.findAllById(userIds);
        club.setOfficers(officers);

        return clubRepository.save(club);
    }
}
