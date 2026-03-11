package com.sportsmanagement.admin;

import com.sportsmanagement.match.MatchManagement;
import com.sportsmanagement.notification.NotificationManagement;
import com.sportsmanagement.match.ResultManagement;
import com.sportsmanagement.match.ResultViewer;
import com.sportsmanagement.match.MatchViewer;
import com.sportsmanagement.team.TeamManagement;
import com.sportsmanagement.venue.VenueManagement;
import com.sportsmanagement.main.LoginPage;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard - Sports Tournament Management");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 1, 10, 10));

        JButton showTeamsButton = new JButton("Show Teams");
        JButton addVenueButton = new JButton("Add Venue");
        JButton showVenuesButton = new JButton("Show Venues");
        JButton scheduleMatchButton = new JButton("Schedule Match");
        JButton showMatchesButton = new JButton("Show Matches");
        JButton updateResultButton = new JButton("Update Results");
        JButton viewResultButton = new JButton("View Overall Results");
        JButton sendNotificationButton = new JButton("Send Notifications");
        JButton logoutButton = new JButton("Logout");

        add(showTeamsButton);
        add(addVenueButton);
        add(showVenuesButton);
        add(scheduleMatchButton);
        add(showMatchesButton);
        add(updateResultButton);
        add(viewResultButton);
        add(sendNotificationButton);
        add(logoutButton);

        showTeamsButton.addActionListener(e -> new TeamManagement());
        addVenueButton.addActionListener(e -> new VenueManagement(true));
        showVenuesButton.addActionListener(e -> new VenueManagement(false));
        scheduleMatchButton.addActionListener(e -> new MatchManagement(false));
        showMatchesButton.addActionListener(e -> new MatchViewer(-1, false));        updateResultButton.addActionListener(e -> new ResultManagement());
        viewResultButton.addActionListener(e -> new ResultViewer());
        sendNotificationButton.addActionListener(e -> new NotificationManagement());

        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logging out...");
            dispose();
            new LoginPage();
        });

        setVisible(true);
    }
}
