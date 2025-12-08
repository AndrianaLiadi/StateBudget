package src.main.java.ui;

import javax.swing.*;
import java.awt.*;

public class HomeScreen extends JPanel {

    public HomeScreen(AppController controller) {

        setLayout(new BorderLayout());

        JLabel label = new JLabel("Home Page", SwingConstants.CENTER);

        JButton back = new JButton("Back");
        back.addActionListener(e -> controller.showScreen(AppController.LANDING));

        add(label, BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
    }
}
