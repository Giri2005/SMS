package com.sportsmanagement.main;

import com.sportsmanagement.admin.AdminDashboard;
import com.sportsmanagement.coach.CoachDashboard;
import com.sportsmanagement.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPage extends JFrame {

    public LoginPage() {
        setTitle("Sports Tournament Management System");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 🎯 Left panel: Information
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setFont(new Font("Serif", Font.PLAIN, 14));
        infoArea.setText("""
🏏 Welcome to Sports Tournament System

Join us in celebrating the spirit of competition and community.
At our System, we organize thrilling sports events for all ages and skill levels.
---------------------------------------------------------------------------------------------------------------------
📜 RULES & REGULATIONS

✅ Each team must consist of 11 players with 1 Captain and 1 Vice-Captain clearly designated.
📝 Team registration must be completed by the Coach before the registration deadline.
🏟 Matches will be played at assigned venues only; no rescheduling is allowed.
⏰ Punctuality is mandatory. Report 30 minutes before match time.
🏏 All matches are 20-over format (or Admin defined).
📋 Scores are updated by Admin after verification.
❌ Unregistered players = Disqualification.
🔄 Submitted results are final unless reverified.
📣 Misconduct leads to suspension.
🏆 Winners are ranked by match wins, NRR & final standings.
---------------------------------------------------------------------------------------------------------------------
Kindly Login is Existing User to get into your Dashboard or Register For New User to Explore our Services!!!
---------------------------------------------------------------------------------------------------------------------
@Sports Tournament Management Association
KIOT, Salem, Tamil Nadu
        """);

        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("📜 About & Rules"));

        // 🎯 Right panel: Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        JButton adminLoginBtn = new JButton("Admin Login");
        JButton coachLoginBtn = new JButton("Coach Login");
        JButton coachRegisterBtn = new JButton("Coach Registration");
        JButton exitBtn = new JButton("Exit");

        buttonPanel.add(adminLoginBtn);
        buttonPanel.add(coachLoginBtn);
        buttonPanel.add(coachRegisterBtn);
        buttonPanel.add(exitBtn);

        // 🧩 SplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, buttonPanel);
        splitPane.setDividerLocation(600);
        splitPane.setResizeWeight(0.7);
        add(splitPane);

        // ➕ Actions
        adminLoginBtn.addActionListener(e -> adminLogin());
        coachLoginBtn.addActionListener(e -> coachLogin());
        coachRegisterBtn.addActionListener(e -> coachRegister());
        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Exit application?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void adminLogin() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        Object[] message = { "Username:", usernameField, "Password:", passwordField };

        int option = JOptionPane.showConfirmDialog(this, message, "Admin Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM admin WHERE username=? AND password=?")) {
                stmt.setString(1, user);
                stmt.setString(2, pass);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "✅ Welcome Admin!");
                    new AdminDashboard();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Invalid admin credentials!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
            }
        }
    }

    private void coachLogin() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        Object[] message = { "Username:", usernameField, "Password:", passwordField };

        int option = JOptionPane.showConfirmDialog(this, message, "Coach Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM coaches WHERE username=? AND password=?")) {
                stmt.setString(1, user);
                stmt.setString(2, pass);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int coachId = rs.getInt("id");
                    String teamName = rs.getString("team_name");
                    JOptionPane.showMessageDialog(this, "✅ Coach Login Successful!");
                    new CoachDashboard(coachId, teamName);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Invalid coach credentials! Please register first.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
            }
        }
    }

    private void coachRegister() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField teamField = new JTextField();
        JTextField captainField = new JTextField();
        JTextField viceCaptainField = new JTextField();

        Object[] fields = {
                "Username:", usernameField,
                "Password:", passwordField,
                "Team Name:", teamField,
                "Captain Name:", captainField,
                "Vice-Captain Name:", viceCaptainField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Coach Registration", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String team = teamField.getText();
            String captain = captainField.getText();
            String viceCaptain = viceCaptainField.getText();

            if (username.isEmpty() || password.isEmpty() || team.isEmpty() || captain.isEmpty() || viceCaptain.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ All fields are required.");
                return;
            }

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO coaches (username, password, team_name, captain_name, vice_captain_name) VALUES (?, ?, ?, ?, ?)")) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, team);
                stmt.setString(4, captain);
                stmt.setString(5, viceCaptain);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "✅ Coach registered successfully! Please log in.");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Registration failed.");
                }
            } catch (SQLIntegrityConstraintViolationException dup) {
                JOptionPane.showMessageDialog(this, "❌ Username or Team already exists.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
