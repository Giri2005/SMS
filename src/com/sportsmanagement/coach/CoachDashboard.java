package com.sportsmanagement.coach;

import com.sportsmanagement.match.MatchViewer;
import com.sportsmanagement.match.ResultViewer;
import com.sportsmanagement.notification.NotificationViewer;
import com.sportsmanagement.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoachDashboard extends JFrame {
    private final int coachId;
    private final String teamName;

    public CoachDashboard(int coachId, String teamName) {
        this.coachId = coachId;
        this.teamName = teamName;

        setTitle("Coach Dashboard - " + teamName);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 10, 10));

        JButton addPlayersButton = new JButton("Add / Update Team Players");
        JButton viewMatchesButton = new JButton("View Assigned Matches");
        JButton viewResultsButton = new JButton("View Overall Results");
        JButton viewNotificationsButton = new JButton("View Notifications");
        JButton logoutButton = new JButton("Logout");

        add(addPlayersButton);
        add(viewMatchesButton);
        add(viewResultsButton);
        add(viewNotificationsButton);
        add(logoutButton);

        addPlayersButton.addActionListener(e -> showPlayerForm());
        viewMatchesButton.addActionListener(e -> new MatchViewer(coachId, false));
        viewResultsButton.addActionListener(e -> new ResultViewer());
        viewNotificationsButton.addActionListener(e -> new NotificationViewer());
        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logging Out...");
            dispose();
            new com.sportsmanagement.main.LoginPage();
        });

        setVisible(true);
    }

    private void showPlayerForm() {
        JPanel panel = new JPanel(new GridLayout(12, 2, 5, 5));
        List<JTextField> playerFields = new ArrayList<>();

        for (int i = 1; i <= 11; i++) {
            JLabel label = new JLabel("Player " + i + ":");
            JTextField field = new JTextField();
            playerFields.add(field);
            panel.add(label);
            panel.add(field);
        }

        int result = JOptionPane.showConfirmDialog(this, panel, "Enter Player Names", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            List<String> playerNames = new ArrayList<>();
            for (JTextField field : playerFields) {
                String name = field.getText().trim();
                if (!name.isEmpty()) {
                    playerNames.add(name);
                }
            }

            if (playerNames.size() < 11) {
                JOptionPane.showMessageDialog(this, "⚠️ Please enter all 11 player names.");
                return;
            }

            savePlayers(coachId, playerNames);
        }
    }

    private void savePlayers(int coachId, List<String> players) {
        String deleteOld = "DELETE FROM players WHERE coach_id = ?";
        String insertNew = "INSERT INTO players (coach_id, player_name) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Delete existing players
            try (PreparedStatement delStmt = conn.prepareStatement(deleteOld)) {
                delStmt.setInt(1, coachId);
                delStmt.executeUpdate();
            }

            // Insert new players
            try (PreparedStatement insertStmt = conn.prepareStatement(insertNew)) {
                for (String player : players) {
                    insertStmt.setInt(1, coachId);
                    insertStmt.setString(2, player);
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }

            JOptionPane.showMessageDialog(this, "✅ Players saved successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error saving players: " + e.getMessage());
        }
    }
}
