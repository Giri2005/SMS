package com.sportsmanagement.team;

import com.sportsmanagement.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerManager extends JFrame {
    private final int coachId;
    private final JTextField[] playerFields = new JTextField[11];

    public PlayerManager(int coachId) {
        this.coachId = coachId;

        setTitle("Add / Update Players");
        setSize(400, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel inputPanel = new JPanel(new GridLayout(12, 1, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter 11 Player Names"));

        for (int i = 0; i < 11; i++) {
            playerFields[i] = new JTextField();
            inputPanel.add(new JLabel("Player " + (i + 1) + ":"));
            inputPanel.add(playerFields[i]);
        }

        JButton saveButton = new JButton("Save Players");
        saveButton.addActionListener(e -> savePlayers());

        add(inputPanel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void savePlayers() {
        StringBuilder sb = new StringBuilder();
        for (JTextField field : playerFields) {
            String name = field.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All player names must be filled.");
                return;
            }
            sb.append(name).append(",");
        }

        String players = sb.substring(0, sb.length() - 1); // Remove last comma

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE coaches SET players = ? WHERE id = ?")) {
            stmt.setString(1, players);
            stmt.setInt(2, coachId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "✅ Players updated successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to update players.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
        }
    }
}
