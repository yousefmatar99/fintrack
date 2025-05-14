package com.example.fintrack.dao;

import com.example.fintrack.model.User;
import com.example.fintrack.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class UserDao {

    /*
     * API:
     * createUser
     * getUserById
     * getUserByUsername
     * getAllUsers
     * updateUser
     * deleteUser
     * mapResultSetToUser
     */

    public void createUser(User user) {
        String sql = """
            INSERT INTO users (
                name,
                password,
                email,
                age,
                created_at
            )
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setInt(4, user.getAge());
            pstmt.setTimestamp(5, Timestamp.valueOf(user.getCreatedAt()));

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setUserID(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void updateUserPassword(User user, String newPassword) {
        String sql = """
            UPDATE users
            SET
                password = ?
            WHERE
                user_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setInt(2, user.getUserID());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int userId) {
        String selectBudgetsSql = "SELECT budget_id FROM budgets WHERE user_id = ?";
        String deleteExpensesSql = "DELETE FROM expenses WHERE budget_id = ?";
        String deleteBudgetsSql = "DELETE FROM budgets WHERE budget_id = ?";
        String deleteUserSql = "DELETE FROM users WHERE user_id = ?";
    
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
    
            try (PreparedStatement selectBudgetsStmt = conn.prepareStatement(selectBudgetsSql)) {
                selectBudgetsStmt.setInt(1, userId);
    
                try (ResultSet rs = selectBudgetsStmt.executeQuery()) {
                    while (rs.next()) {
                        int budgetId = rs.getInt("budget_id");
    
                        try (PreparedStatement delExpensesStmt = conn.prepareStatement(deleteExpensesSql)) {
                            delExpensesStmt.setInt(1, budgetId);
                            delExpensesStmt.executeUpdate();
                        }
    
                        try (PreparedStatement delBudgetStmt = conn.prepareStatement(deleteBudgetsSql)) {
                            delBudgetStmt.setInt(1, budgetId);
                            delBudgetStmt.executeUpdate();
                        }
                    }
                }
            }
    
            try (PreparedStatement deleteUserStmt = conn.prepareStatement(deleteUserSql)) {
                deleteUserStmt.setInt(1, userId);
                deleteUserStmt.executeUpdate();
            }
    
            conn.commit();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserID(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setAge(rs.getInt("age"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            user.setCreatedAt(ts.toLocalDateTime());
        }

        return user;
    }

}
