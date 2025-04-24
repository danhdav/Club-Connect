package com.clubConnect.demo.entities;

import jakarta.persistence.*;

@Entity
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // e.g. "like", "heart", "laugh"

    @ManyToOne(optional = false)
    @JoinColumn(name = "reactor_id")
    private User reactor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    public Reaction() {}

    public Reaction(String type, User reactor, Announcement announcement) {
        this.type = type;
        this.reactor = reactor;
        this.announcement = announcement;
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getReactor() {
        return reactor;
    }

    public void setReactor(User reactor) {
        this.reactor = reactor;
    }

    public Announcement getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }
}
