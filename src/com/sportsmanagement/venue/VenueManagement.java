package com.sportsmanagement.venue;

import com.sportsmanagement.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class VenueManagement extends JFrame {
    public VenueManagement(boolean isAddMode) {
        setTitle(isAddMode ? "Add Venue" : "View Venues");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        if (isAddMode) {
            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
            JTextField venueNameField = new JTextField();
            JTextField locationField = new JTextField();
            JButton addButton = new JButton("Add Venue");

            panel.add(new JLabel("Venue Name:"));
            panel.add(venueNameField);
            panel.add(new JLabel("Location:"));
            panel.add(locationField);
            panel.add(new JLabel());
            panel.add(addButton);

            addButton.addActionListener(e -> {
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement("INSERT INTO venues (venue_name, location) VALUES (?, ?)")) {
                    stmt.setString(1, venueNameField.getText());
                    stmt.setString(2, locationField.getText());
                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Venue Added Successfully!");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            });

            add(panel, BorderLayout.CENTER);
        } else {
            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);

            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM venues")) {
                while (rs.next()) {
                    textArea.append("Venue: " + rs.getString("venue_name") + " | Location: " + rs.getString("location") + "\n");
                }
            } catch (SQLException e) {
                textArea.setText("Error: " + e.getMessage());
            }

            add(new JScrollPane(textArea));
        }

        setVisible(true);
    }
}
