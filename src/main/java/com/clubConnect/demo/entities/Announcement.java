package com.clubConnect.demo.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10000)
    private String contentHtml;

    @ElementCollection
    private List<String> imageUrls = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> reactions = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Announcement() {}

    public Announcement(String contentHtml, List<String> imageUrls, Club club, User author) {
        this.contentHtml = contentHtml;
        this.imageUrls = imageUrls;
        this.club = club;
        this.author = author;
        this.createdAt = new Date();
    }

    @PrePersist
    public void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = new Date();
        }
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
