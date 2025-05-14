package com.example.fintrack.dao;

import com.example.fintrack.model.Expense;
import com.example.fintrack.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ExpenseDao {

    /*
     * API:
     * createExpense
     * getExpenseById
     * getAllExpenses
     * getExpensesByBudget
     * updateExpense
     * deleteExpense
     * mapResultSetToExpense
     */

    public void createExpense(Expense expense) {
        String sql = """
            INSERT INTO expenses (
                name,
                budget_id,
                category,
                amount,
                created_at
            )
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, expense.getName());
            pstmt.setInt(2, expense.getBudgetID());
            pstmt.setString(3, expense.getCategory());
            pstmt.setDouble(4, expense.getAmount());
            pstmt.setTimestamp(5, Timestamp.valueOf(expense.getCreatedAt()));

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    expense.setExpenseID(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.addSpent(expense.getAmount(), expense.getBudgetID());
    }

    public void addSpent(Double amount, int budgetID) {
        Double currSpent = 0.0;
    
        String selectSql = "SELECT spent FROM budgets WHERE budget_id = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmtSelect = conn.prepareStatement(selectSql)) {
    
            pstmtSelect.setInt(1, budgetID);
            try (ResultSet rs = pstmtSelect.executeQuery()) {
                if (rs.next()) {
                    currSpent = rs.getDouble("spent");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        double newSpent = currSpent + amount;
    
        String updateSql = "UPDATE budgets SET spent = ? WHERE budget_id = ?";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql)) {
    
            pstmtUpdate.setDouble(1, newSpent);
            pstmtUpdate.setInt(2, budgetID);
            pstmtUpdate.executeUpdate();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Expense getExpenseById(int id) {
        String sql = "SELECT * FROM expenses WHERE expense_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToExpense(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Expense> getAllExpenses() {
        ArrayList<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                expenses.add(mapResultSetToExpense(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public ArrayList<Expense> getExpensesByBudget(int budgetId) {
        ArrayList<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE budget_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, budgetId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    expenses.add(mapResultSetToExpense(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public void updateExpense(Expense expense) {
        String sql = """
            UPDATE expenses
            SET
                budget_id = ?,
                category = ?,
                amount = ?,
                created_at = ?
            WHERE
                expense_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, expense.getBudgetID());
            pstmt.setString(2, expense.getCategory());
            pstmt.setDouble(3, expense.getAmount());
            pstmt.setTimestamp(4, Timestamp.valueOf(expense.getCreatedAt()));

            pstmt.setInt(5, expense.getExpenseID());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteExpense(int expenseId) {
        String sql = "DELETE FROM expenses WHERE expense_id = ?";

        // double spentAmount = 0.0; // Default value
        // int budgetId = 0;

        // String selectSql = "SELECT spent, budget_id FROM expenses WHERE expense_id = ?";

        // try (Connection conn = DatabaseConnection.getConnection();
        //      PreparedStatement pstmtSelect = conn.prepareStatement(selectSql)) {

        //     pstmtSelect.setInt(1, expenseId); // Setting the parameter in the query

        //     try (ResultSet rs = pstmtSelect.executeQuery()) {
        //         if (rs.next()) {
        //             spentAmount = rs.getDouble("spent"); // Fetching the "spent" column
        //             budgetId = rs.getInt("budget_id");
        //         }
        //     }
        // } catch (SQLException e) {
        //     e.printStackTrace(); // Handle SQL exception
        // }

        // this.addSpent(-spentAmount, budgetId);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, expenseId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Expense mapResultSetToExpense(ResultSet rs) throws SQLException {
        Expense expense = new Expense();

        expense.setName(rs.getString("name"));
        expense.setExpenseID(rs.getInt("expense_id"));
        expense.setBudgetID(rs.getInt("budget_id"));
        expense.setCategory(rs.getString("category"));
        expense.setAmount(rs.getDouble("amount"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            expense.setCreatedAt(ts.toLocalDateTime());
        }

        return expense;
    }
}
