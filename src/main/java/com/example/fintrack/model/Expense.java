package com.example.fintrack.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Expense {

    private int expenseID;
    private String name;
    private Budget budget;
    private int budgetID;
    private String category;
    private double amount;
    private LocalDateTime createdAt;

    // Constructors
    public Expense() {
        this.budget = new Budget();
    }

    public Expense(int expenseID, String name, Budget budget, int budgetID, String category, double amount, LocalDateTime createdAt) {
        this.expenseID = expenseID;
        this.name = name;
        this.budget = budget;
        this.budgetID = budgetID;
        this.category = category;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getExpenseID() { return expenseID; }
    public void setExpenseID(int expenseID) { this.expenseID = expenseID; }
    public String getName() { return name; }
    public void setName(String new_name) { this.name = new_name; }
    public Budget getBudget() { return budget; }
    public void setBudget(Budget budget) { this.budget = budget; }
    public int getBudgetID() { return budgetID; }
    public void setBudgetID(int budgetID) { this.budgetID = budgetID; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount;} 
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Override equals() for proper comparison
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Expense expense = (Expense) obj;
        return expenseID == expense.expenseID;
    }

    // Override hashCode() to maintain consistency
    @Override
    public int hashCode() {
        return Objects.hash(expenseID);
    }

    @Override
    public String toString() {
        return "Expense{ID=" + expenseID + ", Category='" + category 
               + "', Amount=" + amount + ", createdAt=" + createdAt + "}";
    }
}
