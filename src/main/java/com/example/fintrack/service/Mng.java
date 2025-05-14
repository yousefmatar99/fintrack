package com.example.fintrack.service;

import com.example.fintrack.dao.UserDao;
import com.example.fintrack.dao.BudgetDao;
import com.example.fintrack.dao.ExpenseDao;
import com.example.fintrack.dao.NotificationDao;
import com.example.fintrack.dao.VisualizationDao;
import com.example.fintrack.model.User;
import com.example.fintrack.model.Budget;
import com.example.fintrack.model.Expense;
import com.example.fintrack.model.Notification;
import com.example.fintrack.model.Visualization;

import java.util.ArrayList;

public class Mng {

    private final UserDao userDao;
    private final BudgetDao budgetDao;
    public final ExpenseDao expenseDao;
    private final NotificationDao notificationDao;
    private final VisualizationDao visualizationDao;
    private final ArrayList<User> userList;

    @SuppressWarnings("unused")
    private User currUser;

    public Mng(UserDao userDao,
               BudgetDao budgetDao,
               ExpenseDao expenseDao,
               NotificationDao notificationDao,
               VisualizationDao visualizationDao) {

        this.userDao = userDao;
        this.budgetDao = budgetDao;
        this.expenseDao = expenseDao;
        this.notificationDao = notificationDao;
        this.visualizationDao = visualizationDao;
        this.userList = new ArrayList<>();
    }

    public User getLoggedInUser() { return currUser; }
    public void setLoggedInUser(User user) { this.currUser = user; }

    // User List Helpers

    public void loadAllUsersIntoMemory() {
        userList.clear();
        userList.addAll(userDao.getAllUsers());
    }

    public ArrayList<User> getAllUsersInMemory() {
        return userList;
    }

    public User getUserInMemoryById(int userId) {
        for (User u : userList) {
            if (u.getUserID() == userId) {
                return u;
            }
        }
        return null;
    }

    public User getUserInMemByEmail(String email) {
        for (User u : userList) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    public void addUser(User user) {
        userDao.createUser(user);
        userList.add(user);
    }

    public void updateUserPassword(User user, String newPassword) {
        user.setPassword(newPassword);
        userDao.updateUserPassword(user, newPassword);
    }

    public void deleteUser(int userId) {
        userDao.deleteUser(userId);
        removeUserFromMemory(userId);
    }

    private void removeUserFromMemory(int userId) {
        userList.removeIf(u -> u.getUserID() == userId);
    }

    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public ArrayList<User> getAllUsersFromDB() {
        return userDao.getAllUsers();
    }

    //  Budget methods

    public void createBudgetForUser(int userId, Budget budget) {
        budget.setUserID(userId);
        budgetDao.createBudget(budget);
        currUser.addBudget(budget);
    }

    public Budget getBudgetById(int budgetId) {
        return budgetDao.getBudgetById(budgetId);
    }

    public Budget getBudgetInMemById(int budgetId) {
        Budget b = getBudgetById(budgetId);
        User u = getUserInMemoryById(b.getUserID());
        return u.getBudget(budgetId);
    }

    public ArrayList<Budget> getBudgetsByUser(int userId) {
        return budgetDao.getBudgetsByUserID(userId);
    }

    public ArrayList<Budget> getBudgetsInMemByUser(int userId) {
        User u = getUserInMemoryById(userId);
        return u.getBudgets();
    }

    public void updateBudget(Budget budget) {
        budgetDao.updateBudget(budget);
    }

    public void deleteBudget(int budgetId) {
        budgetDao.deleteBudget(budgetId);
        Budget b = getBudgetById(budgetId);
        User user = userDao.getUserById(b.getUserID());
        user.delBudgetByID(budgetId);
    }

    //  Expense methods

    public void createExpense(Expense expense) {
        expenseDao.createExpense(expense);
        Budget budget = budgetDao.getBudgetById(expense.getBudgetID());
        budget.addExpense(expense);
    }

    public Expense getExpenseById(int expenseId) {
        return expenseDao.getExpenseById(expenseId);
    }

    public Expense getExpenseInMemById(int expenseId) {
        Expense e = getExpenseById(expenseId);
        Budget b = getBudgetInMemById(e.getBudgetID());
        return b.getExpense(expenseId);
    }

    public ArrayList<Expense> getExpensesByBudget(int budgetId) {
        return expenseDao.getExpensesByBudget(budgetId);
    }

    public ArrayList<Expense> getExpensesInMemByBudget(int budgetId) {
        Budget b = getBudgetInMemById(budgetId);
        return b.getExpenses();
    }

    public ArrayList<Expense> getAllExpenses() {
        return expenseDao.getAllExpenses();
    }

    public void updateExpense(Expense expense) {
        expenseDao.updateExpense(expense);
    }

    public void deleteExpense(int expenseId) {
        expenseDao.deleteExpense(expenseId);
        //Expense e = getExpenseById(expenseId);
        //Budget budget = budgetDao.getBudgetById(e.getBudgetID());
        //budget.delExpenseByID(expenseId);
    }

    //  Notification methods

    public void createNotification(Notification notif) {
        notificationDao.createNotification(notif);
    }

    public Notification getNotificationById(int notifId) {
        return notificationDao.getNotificationById(notifId);
    }

    public ArrayList<Notification> getNotificationsByUser(int userId) {
        return notificationDao.getNotificationsByUser(userId);
    }

    public ArrayList<Notification> getAllNotifications() {
        return notificationDao.getAllNotifications();
    }

    public void updateNotification(Notification notif) {
        notificationDao.updateNotification(notif);
    }

    public void deleteNotification(int notifId) {
        notificationDao.deleteNotification(notifId);
    }

    //  Visualization

    public void createVisualization(Visualization viz) {
        visualizationDao.createVisualization(viz);
    }

    public Visualization getVisualizationById(int reportId) {
        return visualizationDao.getVisualizationById(reportId);
    }

    public ArrayList<Visualization> getVisualizationsByUser(int userId) {
        return visualizationDao.getVisualizationsByUser(userId);
    }

    public ArrayList<Visualization> getAllVisualizations() {
        return visualizationDao.getAllVisualizations();
    }

    public void updateVisualization(Visualization viz) {
        visualizationDao.updateVisualization(viz);
    }

    public void deleteVisualization(int reportId) {
        visualizationDao.deleteVisualization(reportId);
    }

}
