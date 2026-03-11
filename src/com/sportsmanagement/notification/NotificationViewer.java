package com.sportsmanagement.notification;

import com.sportsmanagement.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class NotificationViewer extends JFrame {
    public NotificationViewer() {
        setTitle("📢 Notifications");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JTextArea notificationArea = new JTextArea();
        notificationArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(notificationArea);
        add(scrollPane, BorderLayout.CENTER);

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT message, created_at FROM notifications ORDER BY created_at DESC")) {

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("📅 ").append(rs.getTimestamp("created_at")).append("\n")
                  .append("🔔 ").append(rs.getString("message")).append("\n\n");
            }

            if (sb.length() == 0) {
                sb.append("No notifications yet.");
            }

            notificationArea.setText(sb.toString());
        } catch (SQLException e) {
            notificationArea.setText("❌ Error loading notifications:\n" + e.getMessage());
        }

        setVisible(true);
    }
}
