package ui;

import javax.swing.*;
import java.awt.*;

public class HomeScreen extends JPanel {

    public HomeScreen(AppController controller) {

        setLayout(new GridLayout(3, 1, 10, 10));

        JLabel title = new JLabel("Καλώς ήρθες στο Home", SwingConstants.CENTER);

        JButton budgetBtn = new JButton("Δες Προϋπολογισμό");
        budgetBtn.addActionListener(e -> controller.showScreen(AppController.BUDGET));

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> controller.showScreen(AppController.LANDING));

        add(title);
        add(budgetBtn);
        add(backBtn);
    }
}
