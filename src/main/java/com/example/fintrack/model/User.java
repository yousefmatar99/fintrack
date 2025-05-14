package com.example.fintrack.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

// User.java
public class User {
    private int userID;
    private String name;
    private String email;
    private String password;
    private int age;
    private LocalDateTime createdAt;
    private ArrayList<Budget> budgets;
    private ArrayList<Notification> notifications;
    private ArrayList<Visualization> visualizations;

    // Constructors
    public User() {
        this.budgets = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.visualizations = new ArrayList<>();
    }

    public User(int userID, String name, String email, String password, int age, LocalDateTime createdAt) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.createdAt = createdAt;
        this.budgets = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.visualizations = new ArrayList<>();
    }

    // Getters and Setters
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public ArrayList<Budget> getBudgets() { return budgets; }
    public void setBudgets(ArrayList<Budget> budgets) { this.budgets = budgets; }
    public ArrayList<Notification> getNotifications() { return notifications; }
    public void setNotifications(ArrayList<Notification> notifications) { this.notifications = notifications; }
    public ArrayList<Visualization> getVisualization() { return visualizations; }
    public void setVisualization(ArrayList<Visualization> visualizations) { this.visualizations = visualizations; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Helpers Methods for comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return userID == user.userID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID);
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", createdAt=" + createdAt +
                '}';
    }

    // Helper Methods for User Budgets
    public Budget getBudget(int id) {
        for (Budget b: budgets) {
            if (b.getBudgetID() == id)
                return b;
        }
        return null;
    }

    public void addBudget(Budget b) {
        if (b == null) return;
        b.setUser(this);
        this.budgets.add(b);
    }

    public Budget delBudgetByID(int id) {
        Budget b = getBudget(id);
        if (b == null) return null;
        budgets.remove(b);
        return b;
    }

    public Budget delBudget(Budget b) {
        return delBudgetByID(b.getBudgetID());
    }

    public void updateBudget(Budget newBudget) {
        if (newBudget == null) return;
        Budget b = delBudget(newBudget);
        // if no other budget with the same id, do not update
        if (b == null) return;
        newBudget.setExpenses(b.getExpenses());
        addBudget(newBudget);
    }

    // Helper Methods for User Notifications
    public Notification getNotification(int id) {
        for (Notification n : notifications) {
            if (n.getNotificationID() == id)
                return n;
        }
        return null;
    }

    public void addNotification(Notification n) {
        if (n == null) return;
        n.setUser(this);
        this.notifications.add(n);
    }

    public Notification delNotificationByID(int id) {
        Notification n = getNotification(id);
        if (n == null) return null;
        notifications.remove(n);
        return n;
    }

    public Notification delNotification(Notification n) {
        if (n == null) return null;
        return delNotificationByID(n.getNotificationID());
    }

    public void updateNotification(Notification newNotification) {
        if (newNotification == null) return;
        Notification oldNotification = delNotification(newNotification);
        // if no notification with same id, do not update
        if (oldNotification == null) return;
        addNotification(newNotification);
    }

    // Helper Methods for User Visualizations
    public Visualization getVisualizationByID(int id) {
        for (Visualization v : visualizations) {
            if (v.getReportID() == id)
                return v;
        }
        return null;
    }

    public void addVisualization(Visualization v) {
        if (v == null) return;
        v.setUser(this);
        this.visualizations.add(v);
    }

    public Visualization delVisualizationByID(int id) {
        Visualization v = getVisualizationByID(id);
        if (v == null) return null;
        visualizations.remove(v);
        return v;
    }

    public Visualization delVisualization(Visualization v) {
        if (v == null) return null;
        return delVisualizationByID(v.getReportID());
    }

    public void updateVisualization(Visualization newViz) {
        if (newViz == null) return;
        Visualization oldViz = delVisualization(newViz);
        // if no visualization with same id, do not update
        if (oldViz == null) return;
        addVisualization(newViz);
    }

}