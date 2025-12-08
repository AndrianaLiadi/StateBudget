package src.main.java.ui;

import controller.AppController;

import javax.swing.*;
import java.awt.*;

public class AboutScreen extends JPanel {

    public AboutScreen(AppController controller) {

        setLayout(new BorderLayout());

        JLabel label = new JLabel("Εδώ θα γράψω αργότερα για το About.", SwingConstants.CENTER);

        JButton back = new JButton("Back");
        back.addActionListener(e -> controller.showScreen(AppController.LANDING));

        add(label, BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
    }
}
