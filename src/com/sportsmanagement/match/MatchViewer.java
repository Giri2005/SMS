package com.sportsmanagement.match;

import com.sportsmanagement.database.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MatchViewer extends JFrame {
    public MatchViewer(int coachId, boolean showOnlyResults) {
        setTitle(showOnlyResults ? "Match Results" : "Assigned Matches");
        setSize(800, 400);
        setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("Match ID");
        model.addColumn("Team 1");
        model.addColumn("Team 2");
        model.addColumn("Date");
        model.addColumn("Venue");
        model.addColumn("Incharge");
        model.addColumn("Status");

        String sql = """
                SELECT m.id, t1.team_name AS team1, t2.team_name AS team2, m.match_date, 
                       v.venue_name, m.match_incharge, 
                       IFNULL(r.result, 'Scheduled') AS status
                FROM matches m
                JOIN coaches t1 ON m.team1_id = t1.id
                JOIN coaches t2 ON m.team2_id = t2.id
                JOIN venues v ON m.venue_id = v.id
                LEFT JOIN results r ON r.match_id = m.id
                """ + (coachId != -1 ? " WHERE m.team1_id = ? OR m.team2_id = ?" : "");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (coachId != -1) {
                stmt.setInt(1, coachId);
                stmt.setInt(2, coachId);
            }

            ResultSet rs = stmt.executeQuery();

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

        add(new JScrollPane(table));
        setVisible(true);
    }
}
