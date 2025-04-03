package com.clubConnect.demo.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ElementCollection
    private List<String> tags = new ArrayList<>();  // Initialize to avoid NullPointerException

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "club_officers",
        joinColumns = @JoinColumn(name = "club_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> officers = new ArrayList<>();  // Initialize to avoid NullPointerException

    // Getters and Setters
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
}


