package com.example.fintrack.model;

import java.util.ArrayList;
import java.util.Objects;
import java.time.LocalDateTime;

// Budget.java
public class Budget {
    private int budgetID;
    private User user;
    private int userID;
    private String budgetName;
    private double limit;
    private double spent;
    private LocalDateTime createdAt;    // date + time
    private ArrayList<Expense> expenses = new ArrayList<Expense>();

    // Constructors
    public Budget() {
        this.user = new User();
        this.expenses = new ArrayList<>();
    }

    public Budget(int budgetID, User user, int userID, String budgetName,
            double limit, double spent, LocalDateTime createdAt) {
        this.budgetID = budgetID;
        this.user = user;
        this.userID = userID;
        this.budgetName = budgetName;
        this.limit = limit;
        this.spent = spent;
        this.createdAt = createdAt;
        this.expenses = new ArrayList<>();
    }

    // Getters and Setters
    public int getBudgetID() { return budgetID; }
    public void setBudgetID(int budgetID) { this.budgetID = budgetID; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    public String getBudgetName() { return budgetName; }
    public void setBudgetName(String budgetName) { this.budgetName = budgetName; }
    public double getLimit() { return limit; }
    public void setLimit(double limit) { this.limit = limit; }
    public double getSpent() { return spent; }
    public void setSpent(double spent) { this.spent = spent; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public ArrayList<Expense> getExpenses() { return expenses; }
    public void setExpenses(ArrayList<Expense> expenses) { this.expenses = expenses; }

    // Helper Methods
    public Expense getExpense(int id) {
        for (Expense e: expenses) {
            if (e.getExpenseID() == id)
                return e;
        }
        return null;
    }

    public void addExpense(Expense e) {
        if (e == null) return;
        e.setBudget(this);
        this.expenses.add(e);
    }

    public Expense delExpenseByID(int id) {
        Expense e = getExpense(id);
        if (e == null) return null;
        expenses.remove(e);
        return e;
    }

    public Expense delExpense(Expense e) {
        return delExpenseByID(e.getExpenseID());
    }

    public void updateExpense(Expense newExpense) {
        if (newExpense == null) return;
        Expense e = delExpense(newExpense);
        // if no other expense with the same id, do not update
        if (e == null) return;
        addExpense(newExpense);
    }

    // Override equals() for proper comparison
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Budget budget = (Budget) obj;
        return budgetID == budget.getBudgetID();
    }

    // Override hashCode() to maintain consistency
    @Override
    public int hashCode() {
        return Objects.hash(budgetID);
    }

    @Override
    public String toString() {
        return "Budget{ID=" + budgetID + ", Name='" + budgetName + "', Limit=" + limit + "}";
    }

}

