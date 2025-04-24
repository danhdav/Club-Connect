package com.clubConnect.demo.service;

import com.clubConnect.demo.entities.Announcement;
import com.clubConnect.demo.entities.Club;
import com.clubConnect.demo.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EmailService {

    @Value("${spring.mail.username:club-connect@example.com}")
    private String fromEmail;
    
    /**
     * Send announcement notification to a user
     * @param user User to notify
     * @param announcement Announcement details
     * @param club Club the announcement belongs to
     */
    @Async("emailTaskExecutor")
    public void sendAnnouncementNotification(User user, Announcement announcement, Club club) {
        // Log the email for demo purposes
        logEmailSent(user, announcement, club);
    }
    
    /**
     * Send announcement notifications to all subscribers of a club
     * @param announcement New announcement
     * @param club Club the announcement belongs to
     */
    @Async("emailTaskExecutor")
    public void sendAnnouncementNotificationsToSubscribers(Announcement announcement, Club club) {
        Set<User> subscribers = club.getSubscribers();
        for (User subscriber : subscribers) {
            sendAnnouncementNotification(subscriber, announcement, club);
        }
        System.out.println("Notifications sent to " + subscribers.size() + " subscribers of " + club.getName());
    }
    
    /**
     * Log email information for demo purposes
     */
    private void logEmailSent(User user, Announcement announcement, Club club) {
        System.out.println("--------------------------------");
        System.out.println("DEMO EMAIL NOTIFICATION");
        System.out.println("To: " + user.getEmail());
        System.out.println("From: " + fromEmail);
        System.out.println("Subject: New Announcement from " + club.getName());
        System.out.println("--------------------------------");
        System.out.println("Hello " + user.getName() + ",");
        System.out.println();
        System.out.println(club.getName() + " has posted a new announcement:");
        System.out.println();
        System.out.println(stripHtml(announcement.getContentHtml()));
        System.out.println();
        System.out.println("Visit the club page to see more details and join the discussion.");
        System.out.println("--------------------------------");
    }
    
    /**
     * Simple method to strip HTML tags for text emails
     * @param html HTML content
     * @return Plain text content
     */
    private String stripHtml(String html) {
        if (html == null) {
            return "";
        }
        return html.replaceAll("<[^>]*>", "")
                .replaceAll("&nbsp;", " ")
                .replaceAll("&amp;", "&")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .trim();
    }
} 