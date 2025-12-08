package src.main.java.ui;

import javax.swing.*;
import java.awt.*;

public class LandingScreen extends JPanel {

    public LandingScreen(AppController controller) {

        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel welcome = new JLabel("Καλώς ήρθες στην εφαρμογή!", SwingConstants.CENTER);

        JButton homeBtn = new JButton("Home");
        JButton aboutBtn = new JButton("About");
        JButton contactBtn = new JButton("Contact");

        homeBtn.addActionListener(e -> controller.showScreen(AppController.HOME));
        aboutBtn.addActionListener(e -> controller.showScreen(AppController.ABOUT));
        contactBtn.addActionListener(e -> controller.showScreen(AppController.CONTACT));

        add(welcome);
        add(homeBtn);
        add(aboutBtn);
        add(contactBtn);
    }
}
