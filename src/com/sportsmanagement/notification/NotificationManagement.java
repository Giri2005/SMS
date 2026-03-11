package com.sportsmanagement.notification;

import com.sportsmanagement.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class NotificationManagement extends JFrame {
    public NotificationManagement() {
        setTitle("Send Notification");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextArea messageArea = new JTextArea();
        JButton sendButton = new JButton("Send Notification");

        add(new JScrollPane(messageArea), BorderLayout.CENTER);
        add(sendButton, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> {
            String message = messageArea.getText().trim();
            if (!message.isEmpty()) {
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement("INSERT INTO notifications (message) VALUES (?)")) {
                    stmt.setString(1, message);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "✅ Notification Sent Successfully!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Message cannot be empty!");
            }
        });

        setVisible(true);
    }
}
