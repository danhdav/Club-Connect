package com.clubConnect.demo.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ElementCollection
    private List<String> tags;  // Predefined tags, e.g., "sports", "music"

    @ManyToMany
    private List<User> officers;  // List of officers

    // Getters and Setters
    public void addOfficer(User officer) {
        this.officers.add(officer);
    }

    public void removeOfficer(User officer) {
        this.officers.remove(officer);
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setDescription(String newDescription) {
        description = newDescription;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTags(){
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}

