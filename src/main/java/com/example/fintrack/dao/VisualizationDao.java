package com.example.fintrack.dao;

import com.example.fintrack.model.Visualization;
import com.example.fintrack.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.sql.Timestamp;
import java.util.ArrayList;

public class VisualizationDao {

    /*
     * API:
     * createVisualization
     * getVisualizationById
     * getAllVisualizations
     * getVisualizationsByUser
     * updateVisualization
     * deleteVisualization
     * mapRowToVisualization
     */

    public void createVisualization(Visualization viz) {
        String sql = """
            INSERT INTO visualizations (
                user_id,
                report_type,
                vis_data,
                generated_at
            )
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, viz.getUserID());
            pstmt.setString(2, viz.getReportType());
            pstmt.setString(3, viz.getData());
            pstmt.setTimestamp(4, Timestamp.valueOf(viz.getGeneratedAt()));

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    viz.setReportID(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Visualization getVisualizationById(int reportId) {
        String sql = "SELECT * FROM visualizations WHERE report_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reportId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToVisualization(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Visualization> getAllVisualizations() {
        ArrayList<Visualization> list = new ArrayList<>();
        String sql = "SELECT * FROM visualizations";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapRowToVisualization(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Visualization> getVisualizationsByUser(int userId) {
        ArrayList<Visualization> list = new ArrayList<>();
        String sql = "SELECT * FROM visualizations WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToVisualization(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateVisualization(Visualization viz) {
        String sql = """
            UPDATE visualizations
            SET
                user_id = ?,
                report_type = ?,
                data = ?,
                generated_at = ?
            WHERE
                report_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, viz.getUserID());
            pstmt.setString(2, viz.getReportType());
            pstmt.setString(3, viz.getData());
            pstmt.setTimestamp(4, Timestamp.valueOf(viz.getGeneratedAt()));
            pstmt.setInt(5, viz.getReportID());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteVisualization(int reportId) {
        String sql = "DELETE FROM visualizations WHERE report_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reportId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Visualization mapRowToVisualization(ResultSet rs) throws SQLException {
        Visualization viz = new Visualization();
        viz.setReportID(rs.getInt("report_id"));
        viz.setUserID(rs.getInt("user_id"));
        viz.setReportType(rs.getString("report_type"));
        viz.setData(rs.getString("vis_data"));
        Timestamp ts = rs.getTimestamp("generated_at");
        if (ts != null) {
            viz.setGeneratedAt(ts.toLocalDateTime());
        }

        return viz;
    }
    
}
