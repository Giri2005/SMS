package com.sportsmanagement.match;

import com.sportsmanagement.database.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MatchManagement extends JFrame {

    public MatchManagement(boolean showOnly) {
        setTitle(showOnly ? "Match Schedule Viewer" : "Schedule New Match");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        if (showOnly) {
            showMatchesTable();
        } else {
            JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

            JTextField team1IdField = new JTextField();
            JTextField team2IdField = new JTextField();
            JTextField matchDateField = new JTextField();
            JTextField venueIdField = new JTextField();
            JTextField inchargeField = new JTextField(); // NEW

            panel.add(new JLabel("Team 1 ID:"));
            panel.add(team1IdField);
            panel.add(new JLabel("Team 2 ID:"));
            panel.add(team2IdField);
            panel.add(new JLabel("Match Date (YYYY-MM-DD):"));
            panel.add(matchDateField);
            panel.add(new JLabel("Venue ID:"));
            panel.add(venueIdField);
            panel.add(new JLabel("Match Incharge Name:"));
            panel.add(inchargeField); // NEW

            JButton scheduleButton = new JButton("Schedule Match");
            scheduleButton.addActionListener(e -> {
                try {
                    int t1 = Integer.parseInt(team1IdField.getText().trim());
                    int t2 = Integer.parseInt(team2IdField.getText().trim());
                    String date = matchDateField.getText().trim();
                    int venueId = Integer.parseInt(venueIdField.getText().trim());
                    String incharge = inchargeField.getText().trim();

                    scheduleMatch(t1, t2, date, venueId, incharge);
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
                }
            });

            add(panel, BorderLayout.CENTER);
            add(scheduleButton, BorderLayout.SOUTH);
        }

        setVisible(true);
    }

    private void scheduleMatch(int team1Id, int team2Id, String date, int venueId, String incharge) {
        String sql = "INSERT INTO matches (team1_id, team2_id, match_date, venue_id, match_incharge) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, team1Id);
            stmt.setInt(2, team2Id);
            stmt.setDate(3, Date.valueOf(date));
            stmt.setInt(4, venueId);
            stmt.setString(5, incharge);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Match scheduled successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
        }
    }

    private void showMatchesTable() {
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        model.addColumn("Match ID");
        model.addColumn("Team 1");
        model.addColumn("Team 2");
        model.addColumn("Date");
        model.addColumn("Venue");
        model.addColumn("Incharge");
        model.addColumn("Status");

        String sql = "SELECT m.id, c1.team_name AS team1, c2.team_name AS team2, m.match_date, v.venue_name, m.match_incharge, m.status " +
                     "FROM matches m " +
                     "JOIN coaches c1 ON m.team1_id = c1.id " +
                     "JOIN coaches c2 ON m.team2_id = c2.id " +
                     "LEFT JOIN venues v ON m.venue_id = v.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("team1"),
                        rs.getString("team2"),
                        rs.getDate("match_date"),
                        rs.getString("venue_name"),
                        rs.getString("match_incharge"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Failed to load matches: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
}
