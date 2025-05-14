package com.example.fintrack.dao;

import com.example.fintrack.model.Budget;
import com.example.fintrack.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class BudgetDao {

    /*
     * API:
     * createBudget
     * getBudgetById
     * getBudgetsByUserId
     * getAllBudgets
     * updateBudget
     * deleteBudget
     * mapResultSetToBudget
     */

    public void createBudget(Budget budget) {
        String sql = """
            INSERT INTO budgets (
                user_id,
                budget_name,
                budget_limit,
                spent,
                created_at
            )
            VALUES (?, ?, ?, ?, ?)
        """;
    
        try (Connection conn = DatabaseConnection.getConnection();
             // Use the RETURN_GENERATED_KEYS flag
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
    
            pstmt.setInt(1, budget.getUserID());
            pstmt.setString(2, budget.getBudgetName());
            pstmt.setDouble(3, budget.getLimit());
            pstmt.setDouble(4, 0.0);
            pstmt.setTimestamp(5, Timestamp.valueOf(budget.getCreatedAt()));
    
            pstmt.executeUpdate();
    
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedID = rs.getInt(1);
                    budget.setBudgetID(generatedID);
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Budget getBudgetById(int id) {
        String sql = "SELECT * FROM budgets WHERE budget_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBudget(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Budget> getBudgetsByUserID(int userID) {
        ArrayList<Budget> budgets = new ArrayList<>();
        String sql = "SELECT * FROM budgets WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    budgets.add(mapResultSetToBudget(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return budgets;
    }

    public ArrayList<Budget> getAllBudgets() {
        ArrayList<Budget> budgets = new ArrayList<>();
        String sql = "SELECT * FROM budgets";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                budgets.add(mapResultSetToBudget(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return budgets;
    }

    public void updateBudget(Budget budget) {
        String sql = """
            UPDATE budgets
            SET
                user_id = ?,
                budget_name = ?,
                budget_limit = ?,
                created_at = ?
            WHERE
                budget_id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, budget.getUserID());
            pstmt.setString(2, budget.getBudgetName());
            pstmt.setDouble(3, budget.getLimit());
            pstmt.setTimestamp(4, Timestamp.valueOf(budget.getCreatedAt()));
            pstmt.setInt(5, budget.getBudgetID());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBudget(int budgetId) {
        String deleteExpensesSql = "DELETE FROM expenses WHERE budget_id = ?";
        String deleteBudgetSql = "DELETE FROM budgets WHERE budget_id = ?";
    
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
    
            try (PreparedStatement delExpStmt = conn.prepareStatement(deleteExpensesSql)) {
                delExpStmt.setInt(1, budgetId);
                delExpStmt.executeUpdate();
            }
    
            try (PreparedStatement delBudgetStmt = conn.prepareStatement(deleteBudgetSql)) {
                delBudgetStmt.setInt(1, budgetId);
                delBudgetStmt.executeUpdate();
            }
    
            conn.commit();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    

    private Budget mapResultSetToBudget(ResultSet rs) throws SQLException {
        Budget budget = new Budget();
        budget.setBudgetID(rs.getInt("budget_id"));
        budget.setUserID(rs.getInt("user_id"));
        budget.setBudgetName(rs.getString("budget_name"));
        budget.setLimit(rs.getDouble("budget_limit"));
        budget.setSpent(rs.getDouble("spent"));
        budget.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        return budget;
    }

}
