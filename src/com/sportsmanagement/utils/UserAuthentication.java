package com.sportsmanagement.utils;

import java.sql.*;

import com.sportsmanagement.database.DatabaseConnection;

public class UserAuthentication {
    public static boolean isAdmin(String username, String password) {
        String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean isCoach(String username, String password) {
        String query = "SELECT * FROM coaches WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static int getCoachId(String username) {
        String query = "SELECT id FROM coaches WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("id") : -1;
        } catch (SQLException e) {
            return -1;
        }
    }

    public static String getTeamName(int coachId) {
        String query = "SELECT team_name FROM coaches WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, coachId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString("team_name") : "";
        } catch (SQLException e) {
            return "";
        }
    }
}
