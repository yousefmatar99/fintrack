package com.example.fintrack.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Visualization {

    private int reportID;
    private User user;
    private int userID;
    private String reportType;
    private String data;
    private LocalDateTime generatedAt;

    // Constructors
    public Visualization() {
        this.user = new User();
    }

    public Visualization(int reportID, User user, int userID, 
                         String reportType, String data, LocalDateTime generatedAt) {
        this.reportID = reportID;
        this.user = user;
        this.userID = userID;
        this.reportType = reportType;
        this.data = data;
        this.generatedAt = generatedAt;
    }

    // Getters and Setters
    public int getReportID() {return reportID;}
    public void setReportID(int reportID) {this.reportID = reportID;}
    public User getUser() {return user;}
    public void setUser(User user) { this.user = user;}
    public int getUserID() {return userID;}
    public void setUserID(int userID) {this.userID = userID;}
    public String getReportType() {return reportType;}
    public void setReportType(String reportType) {this.reportType = reportType;}
    public String getData() {return data;}
    public void setData(String data) {this.data = data;}
    public LocalDateTime getGeneratedAt() {return generatedAt;}
    public void setGeneratedAt(LocalDateTime generatedAt) {this.generatedAt = generatedAt;}

    // Override equals() for proper comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Visualization)) return false;
        Visualization that = (Visualization) o;
        return reportID == that.reportID;
    }

    // Override hashCode() for consistency with equals()
    @Override
    public int hashCode() {
        return Objects.hash(reportID);
    }

    // toString() override for readability
    @Override
    public String toString() {
        return "Visualization{" +
               "reportID=" + reportID +
               ", userID=" + userID +
               ", reportType='" + reportType + '\'' +
               ", data='" + data + '\'' +
               ", generatedAt=" + generatedAt +
               '}';
    }

}

