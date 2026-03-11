package com.sportsmanagement.team;

import com.sportsmanagement.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamViewer extends JFrame {

    public TeamViewer(int coachId) {
        setTitle("Your Team Players");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JTextArea teamInfoArea = new JTextArea();
        teamInfoArea.setEditable(false);
        teamInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        try (Connection conn = DatabaseConnection.getConnection()) {
            String teamQuery = "SELECT team_name, captain_name, vice_captain_name FROM coaches WHERE id = ?";
            try (PreparedStatement teamStmt = conn.prepareStatement(teamQuery)) {
                teamStmt.setInt(1, coachId);
                ResultSet teamRs = teamStmt.executeQuery();

                if (teamRs.next()) {
                    String teamName = teamRs.getString("team_name");
                    String captain = teamRs.getString("captain_name");
                    String viceCaptain = teamRs.getString("vice_captain_name");

                    teamInfoArea.append("🏏 Team Name       : " + teamName + "\n");
                    teamInfoArea.append("🧢 Captain         : " + captain + "\n");
                    teamInfoArea.append("🎩 Vice-Captain    : " + viceCaptain + "\n");
                    teamInfoArea.append("\n👥 Players:\n");

                    // Now get player names from players table
                    String playerQuery = "SELECT player_name FROM players WHERE coach_id = ?";
                    try (PreparedStatement playerStmt = conn.prepareStatement(playerQuery)) {
                        playerStmt.setInt(1, coachId);
                        ResultSet playerRs = playerStmt.executeQuery();

                        int count = 1;
                        while (playerRs.next()) {
                            String playerName = playerRs.getString("player_name");
                            teamInfoArea.append(String.format("   %2d. %s\n", count++, playerName));
                        }
                    }
                } else {
                    teamInfoArea.setText("❌ Team not found.");
                }
            }

        } catch (SQLException e) {
            teamInfoArea.setText("❌ Error fetching team info: " + e.getMessage());
        }

        add(new JScrollPane(teamInfoArea), BorderLayout.CENTER);
        setVisible(true);
    }
}
