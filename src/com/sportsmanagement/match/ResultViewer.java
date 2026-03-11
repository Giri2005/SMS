package com.sportsmanagement.match;

import com.sportsmanagement.database.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ResultViewer extends JFrame {
    public ResultViewer() {
        setTitle("Match Results");
        setSize(800, 400);
        setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("Match ID");
        model.addColumn("Team 1");
        model.addColumn("Team 2");
        model.addColumn("Score 1");
        model.addColumn("Score 2");
        model.addColumn("Result");

        String sql = """
                SELECT m.id, t1.team_name AS team1, t2.team_name AS team2,
                       r.team1_score, r.team2_score, r.result
                FROM results r
                JOIN matches m ON r.match_id = m.id
                JOIN coaches t1 ON m.team1_id = t1.id
                JOIN coaches t2 ON m.team2_id = t2.id
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("team1"),
                        rs.getString("team2"),
                        rs.getInt("team1_score"),
                        rs.getInt("team2_score"),
                        rs.getString("result")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Failed to load results: " + e.getMessage());
        }

        add(new JScrollPane(table));
        setVisible(true);
    }
}
