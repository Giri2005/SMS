package com.sportsmanagement.team;

import com.sportsmanagement.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TeamManagement extends JFrame {

    public TeamManagement() {
        setTitle("Team Management - Admin View");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JTextArea teamArea = new JTextArea();
        teamArea.setEditable(false);
        teamArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String teamQuery = "SELECT id, team_name, captain_name, vice_captain_name FROM coaches";
            Statement teamStmt = conn.createStatement();
            ResultSet teamRs = teamStmt.executeQuery(teamQuery);

            while (teamRs.next()) {
                int coachId = teamRs.getInt("id");
                String teamName = teamRs.getString("team_name");
                String captain = teamRs.getString("captain_name");
                String viceCaptain = teamRs.getString("vice_captain_name");

                teamArea.append("🏏 Team Name       : " + teamName + "\n");
                teamArea.append("🧢 Captain         : " + captain + "\n");
                teamArea.append("🎩 Vice-Captain    : " + viceCaptain + "\n");

                // Fetch Players from players table
                String playerQuery = "SELECT player_name FROM players WHERE coach_id = ?";
                try (PreparedStatement playerStmt = conn.prepareStatement(playerQuery)) {
                    playerStmt.setInt(1, coachId);
                    ResultSet playerRs = playerStmt.executeQuery();

                    int count = 1;
                    teamArea.append("👥 Players:\n");
                    while (playerRs.next()) {
                        String player = playerRs.getString("player_name");
                        teamArea.append(String.format("   %2d. %s\n", count++, player));
                    }
                }

                teamArea.append("-----------------------------------------------------\n\n");
            }

        } catch (SQLException e) {
            teamArea.setText("❌ Error fetching team data: " + e.getMessage());
        }

        add(new JScrollPane(teamArea), BorderLayout.CENTER);
        setVisible(true);
    }
}
