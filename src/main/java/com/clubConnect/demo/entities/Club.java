package com.clubConnect.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.*;


@Entity
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ElementCollection
    private List<String> tags = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "club_officers",
        joinColumns = @JoinColumn(name = "club_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> officers = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "subscribedClubs")
    private Set<User> subscribers = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Announcement> announcements = new ArrayList<>();

    @ElementCollection
    private List<String> images = new ArrayList<>();

    // Default constructor for JPA
    public Club() {
        this.tags = new ArrayList<>();
        this.officers = new ArrayList<>();
        this.subscribers = new HashSet<>();
        this.announcements = new ArrayList<>();
        this.images = new ArrayList<>();
    }

    public Club(String name, String description, List<String> tags) {
        this.name = name;
        this.description = description;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<User> getOfficers() {
        return officers;
    }

    public void setOfficers(List<User> officers) {
        this.officers = officers;
    }

    public void addOfficer(User officer) {
        if (this.officers == null) {
            this.officers = new ArrayList<>();
        }
        if (!this.officers.contains(officer)) {
            this.officers.add(officer);
        }
    }
    public void removeOfficer(User officer) {
        if (this.officers != null) {
            this.officers.remove(officer);
        }
    }
    public Set<User> getSubscribers() {
        return subscribers;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return images;
    }
}


