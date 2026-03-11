package com.sportsmanagement.main;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends JFrame {
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JProgressBar progressBar;
    private Timer timer;
    private int progress = 0;

    public SplashScreen() {
        setTitle("Welcome");
        setSize(650, 400);
        setLocationRelativeTo(null);
        setUndecorated(true);

        // Background panel
        JPanel backgroundPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Gradient background
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(0, 102, 204);
                Color color2 = new Color(255, 255, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // Title
        titleLabel = new JLabel("Sports Tournament Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);

        // Subtitle
        subtitleLabel = new JLabel("", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        subtitleLabel.setForeground(Color.DARK_GRAY);
        backgroundPanel.add(subtitleLabel, BorderLayout.CENTER);

        // Progress Bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(0, 204, 102));
        progressBar.setBackground(Color.WHITE);
        backgroundPanel.add(progressBar, BorderLayout.SOUTH);

        // Typing animation
        String welcomeText = "Welcome to the Future of Tournament Management...";
        Timer typingTimer = new Timer();
        typingTimer.scheduleAtFixedRate(new TimerTask() {
            int i = 0;
            public void run() {
                if (i < welcomeText.length()) {
                    subtitleLabel.setText(subtitleLabel.getText() + welcomeText.charAt(i));
                    i++;
                } else {
                    typingTimer.cancel();
                }
            }
        }, 100, 50);

        // Progress animation
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                progress++;
                progressBar.setValue(progress);
                if (progress >= 100) {
                    timer.cancel();
                    dispose();
                    new LoginPage(); // Redirect to LoginPage
                }
            }
        }, 100, 40);

        setVisible(true);
    }

    public static void main(String[] args) {
        new SplashScreen();
    }
}
