package src.main.java.ui;

import controller.AppController;

import javax.swing.*;
import java.awt.*;

public class ContactScreen extends JPanel {

    public ContactScreen(AppController controller) {

        setLayout(new BorderLayout());

        JLabel label = new JLabel("Εδώ θα γράψω αργότερα για το Contact.", SwingConstants.CENTER);

        JButton back = new JButton("Back");
        back.addActionListener(e -> controller.showScreen(AppController.LANDING));

        add(label, BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
    }
}