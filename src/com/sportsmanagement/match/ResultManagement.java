package com.sportsmanagement.match;

import com.sportsmanagement.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ResultManagement extends JFrame {
    private JComboBox<String> matchDropdown;
    private JTextField score1Field;
    private JTextField score2Field;

    public ResultManagement() {
        setTitle("Update Match Result");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        matchDropdown = new JComboBox<>();
        score1Field = new JTextField();
        score2Field = new JTextField();
        JButton updateBtn = new JButton("Update Result");

        add(new JLabel("Select Match:"));
        add(matchDropdown);
        add(new JLabel("Enter Score for Team 1:"));
        add(score1Field);
        add(new JLabel("Enter Score for Team 2:"));
        add(score2Field);
        add(updateBtn);

        loadMatches();

        updateBtn.addActionListener(e -> updateResult());

        setVisible(true);
    }

    private void loadMatches() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT m.id, c1.team_name AS team1, c2.team_name AS team2 " +
                             "FROM matches m JOIN coaches c1 ON m.team1_id = c1.id " +
                             "JOIN coaches c2 ON m.team2_id = c2.id " +
                             "WHERE m.id NOT IN (SELECT match_id FROM results)"
             );
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int matchId = rs.getInt("id");
                String team1 = rs.getString("team1");
                String team2 = rs.getString("team2");
                matchDropdown.addItem(matchId + " - " + team1 + " vs " + team2);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Failed to load matches: " + e.getMessage());
        }
    }

    private void updateResult() {
        String selected = (String) matchDropdown.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a match.");
            return;
        }

        int matchId = Integer.parseInt(selected.split(" - ")[0]);
        int score1, score2;
        try {
            score1 = Integer.parseInt(score1Field.getText().trim());
            score2 = Integer.parseInt(score2Field.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid scores.");
            return;
        }

        String team1 = selected.split(" - ")[1].split(" vs ")[0];
        String team2 = selected.split(" vs ")[1];
        String result;

        if (score1 > score2) {
            result = team1 + " won";
        } else if (score2 > score1) {
            result = team2 + " won";
        } else {
            result = "Match Draw";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO results (match_id, team1_score, team2_score, result) VALUES (?, ?, ?, ?)"
             )) {
            stmt.setInt(1, matchId);
            stmt.setInt(2, score1);
            stmt.setInt(3, score2);
            stmt.setString(4, result);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "✅ Result Updated: " + result);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to update result.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
        }
    }
}
