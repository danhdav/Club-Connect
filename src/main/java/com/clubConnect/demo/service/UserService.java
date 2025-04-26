package com.clubConnect.demo.service;

import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.entities.User;
import com.clubConnect.demo.repository.ClubRepository;
import com.clubConnect.demo.repository.UserRepository;
import com.clubConnect.demo.utils.ValidationUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    public UserService(UserRepository userRepository, ClubRepository clubRepository) {
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
    }

    public User createUser(String name, String email, String password) {
        if (!ValidationUtils.isValidName(name) || !ValidationUtils.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid name or email format.");
        }
        
        if (!ValidationUtils.isValidPassword(password)) {
            throw new IllegalArgumentException("Invalid password format. Password must be at least 10 characters long.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists.");
        }

        User user = new User(name, email, password);
        user.setLoggedIn(true);
        return userRepository.save(user);
    }

    /**
     * Authenticates a user with the given email and password.
     * @param email The user's email
     * @param password The user's password
     * @return Optional containing the user if authentication is successful, empty otherwise
     */
    public Optional<User> authenticate(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)) {
            User user = optionalUser.get();
            user.setLoggedIn(true);
            userRepository.save(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    /**
     * Legacy login method - delegates to authenticate
     * @deprecated Use authenticate() instead
     */
    @Deprecated
    public boolean login(String email, String password) {
        return authenticate(email, password).isPresent();
    }

    public void logout(User user) {
        user.setLoggedIn(false);
        userRepository.save(user);
    }

    public User updateEmail(Long userId, String newEmail) {
        if (!ValidationUtils.isValidEmail(newEmail)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        
        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email already exists.");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEmail(newEmail);
        return userRepository.save(user);
    }

    public User updateName(Long userId, String newName) {
        if (!ValidationUtils.isValidName(newName)) {
            throw new IllegalArgumentException("Invalid name format.");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setName(newName);
        return userRepository.save(user);
    }

    public User updatePassword(Long userId, String newPassword) {
        if (!ValidationUtils.isValidPassword(newPassword)) {
            throw new IllegalArgumentException("Invalid password format. Password must be at least 10 characters long.");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    public void deleteAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Subscribe a user to a club by providing the Club object
     * @param userId User ID
     * @param club Club object
     */
    public void subscribeToClub(Long userId, Club club) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.subscribeToClub(club);
        userRepository.save(user);
    }

    /**
     * Unsubscribe a user from a club by providing the Club object
     * @param userId User ID
     * @param club Club object
     */
    public void unsubscribeFromClub(Long userId, Club club) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.unsubscribeFromClub(club);
        userRepository.save(user);
    }

    /**
     * Subscribe a user to a club by IDs
     * @param userId User ID
     * @param clubId Club ID
     */
    public void subscribeToClub(Long userId, Long clubId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));

        user.subscribeToClub(club);
        userRepository.save(user);
    }

    /**
     * Unsubscribe a user from a club by IDs
     * @param userId User ID
     * @param clubId Club ID
     */
    public void unsubscribeFromClub(Long userId, Long clubId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));

        user.unsubscribeFromClub(club);
        userRepository.save(user);
    }

    public Set<Club> getSubscribedClubs(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return user.getSubscribedClubs();
    }

    /**
     * Set a user's admin status
     * @param userId User ID
     * @param isAdmin Whether the user should be an admin
     * @return Updated user
     */
    public User setAdminStatus(Long userId, boolean isAdmin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setAdminStatus(isAdmin);
        return userRepository.save(user);
    }

    /**
     * Save a user entity
     * @param user User to save
     * @return Saved user
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Get detailed information about the clubs a user is subscribed to
     * @param userId User ID
     * @return List of club details
     */
    public List<Map<String, Object>> getSubscribedClubsDetails(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Set<Club> subscribedClubs = user.getSubscribedClubs();
        return subscribedClubs.stream().map(club -> {
            Map<String, Object> clubDetails = new HashMap<>();
            clubDetails.put("id", club.getId());
            clubDetails.put("name", club.getName());
            clubDetails.put("description", club.getDescription());
            clubDetails.put("tags", club.getTags());
            clubDetails.put("officers", club.getOfficers().stream().map(User::getName).toList());
            
            // Use actual images if available, or provide placeholders
            List<String> imageUrls = club.getImages();
            if (imageUrls == null || imageUrls.isEmpty()) {
                imageUrls = List.of(
                        "/images/placeholders/club-default-1.jpg",
                        "/images/placeholders/club-default-2.jpg"
                );
            }
            clubDetails.put("images", imageUrls);
            
            return clubDetails;
        }).toList();
    }

    /**
     * Get all announcements from clubs a user is subscribed to
     * @param userId User ID
     * @return List of announcements
     */
    public List<Map<String, Object>> getUserAnnouncements(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Get all clubs the user is subscribed to
        Set<Club> subscribedClubs = user.getSubscribedClubs();
        
        // Collect and flatten all announcements from these clubs
        return subscribedClubs.stream()
                .flatMap(club -> club.getAnnouncements().stream())
                .map(announcement -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", announcement.getId());
                    data.put("contentHtml", announcement.getContentHtml());
                    data.put("clubId", announcement.getClub().getId());
                    data.put("clubName", announcement.getClub().getName());
                    data.put("imageUrls", announcement.getImageUrls());
                    data.put("author", announcement.getAuthor().getName());
                    data.put("createdAt", announcement.getCreatedAt());
                    
                    // Include comments
                    data.put("comments", announcement.getComments().stream().map(c -> Map.of(
                            "id", c.getId(),
                            "author", c.getAuthor().getName(),
                            "text", c.getText(),
                            "createdAt", c.getCreatedAt()
                    )).toList());
                    
                    // Include reactions
                    data.put("reactions", announcement.getReactions().stream().map(r -> Map.of(
                            "id", r.getId(),
                            "type", r.getType(),
                            "reactor", r.getReactor().getName()
                    )).toList());
                    
                    return data;
                })
                .sorted((a1, a2) -> {
                    // Sort by creation date (newest first)
                    Date d1 = (Date) a1.get("createdAt");
                    Date d2 = (Date) a2.get("createdAt");
                    return d2.compareTo(d1);
                })
                .toList();
    }

    /**
     * Find a user by username
     * @param username Username to search for
     * @return User with the specified username
     * @throws UsernameNotFoundException if user not found
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    /**
     * Implementation of UserDetailsService for Spring Security
     * @param username Username to search for
     * @return UserDetails for the user
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
    
    /**
     * Get all admin users
     * @return List of admin users
     */
    public List<User> getAdminUsers() {
        return userRepository.findByIsAdminTrue();
    }
    
    /**
     * Get all users who are officers of at least one club
     * @return List of officer users
     */
    public List<User> getOfficerUsers() {
        return userRepository.findAllOfficers();
    }

}
