package com.clubConnect.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    // Adding username field for backward compatibility
    @Column(unique = true)
    private String username;

    @JsonIgnore
    @ManyToMany(mappedBy = "officers")
    private Set<Club> officerOf = new HashSet<>();

    @Column(nullable = false)
    private boolean isAdmin = false;

    @Column(nullable = false)
    private boolean loggedIn = false;
    
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "club_subscriptions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "club_id")
    )
    private Set<Club> subscribedClubs = new HashSet<>();

    // Default constructor for JPA
    public User() {
        this.officerOf = new HashSet<>();
        this.subscribedClubs = new HashSet<>();
    }

    // Constructor for creating a user
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.username = email; // Default username to email for compatibility
        this.officerOf = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Username getter and setter
    @Override
    public String getUsername() {
        return username != null ? username : email; // Fallback to email if username is null
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        // If username is not explicitly set, update it to match email
        if (this.username == null || this.username.equals(this.email)) {
            this.username = email;
        }
    }

    public Set<Club> getOfficerOf() {
        return officerOf;
    }

    public void setAdminStatus(boolean status){
        this.isAdmin = status;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Set<Club> getSubscribedClubs() {
        return subscribedClubs;
    }

    public void subscribeToClub(Club club) {
        subscribedClubs.add(club);
    }

    public void unsubscribeFromClub(Club club) {
        subscribedClubs.remove(club);
    }

    public boolean isAdmin() {
        return isAdmin;
    }
    
    public boolean isOfficer() {
        return !officerOf.isEmpty();
    }
    
    public boolean isOfficerOfClub(Club club) {
        return officerOf.contains(club);
    }

    // UserDetails interface methods
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Add authorities based on the user's status
        if (isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        
        if (isOfficer()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_OFFICER"));
        }
        
        // Add USER role for all users
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        return authorities;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}

