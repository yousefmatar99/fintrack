package com.example.fintrack.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a Notification entity linked to a User (one User, many Notifications).
 */
public class Notification {

    private int notificationID;      // Primary key (auto-increment)
    private User user;              // Reference to User (object)
    private int userID;             // FK to users.user_id
    private String message;         // message content
    private LocalDateTime createdAt; // created_at in DB

    // Constructors
    public Notification() {
        this.user = new User();
    }

    public Notification(int notificationID, User user, int userID, 
                        String message, LocalDateTime createdAt) {
        this.notificationID = notificationID;
        this.user = user;
        this.userID = userID;
        this.message = message;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getNotificationID() {return notificationID;}
    public void setNotificationID(int notificationID) {this.notificationID = notificationID;}
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
    public int getUserID() {return userID;}
    public void setUserID(int userID) {this.userID = userID;}
    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}
    public LocalDateTime getCreatedAt() { return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    // Override equals() for proper comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return notificationID == that.notificationID;
    }

    // Override hashCode() for consistency with equals()
    @Override
    public int hashCode() {
        return Objects.hash(notificationID);
    }

    // toString() override for readability
    @Override
    public String toString() {
        return "Notification{" +
               "notificationID=" + notificationID +
               ", userID=" + userID +
               ", message='" + message + '\'' +
               ", createdAt=" + createdAt +
               '}';
    }
}
