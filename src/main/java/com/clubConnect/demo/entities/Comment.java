package com.clubConnect.demo.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String text;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(optional = false)
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    private LocalDateTime createdAt;

    public Comment() {}

    public Comment(String text, User author, Announcement announcement) {
        this.text = text;
        this.author = author;
        this.announcement = announcement;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Announcement getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

