package ui;

import javax.swing.*;
import java.awt.*;

public class ScenarioScreen extends JPanel {

    public ScenarioScreen(AppController controller) {

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Scenario Simulation", SwingConstants.CENTER);

        JButton back = new JButton("Back");
        back.addActionListener(e -> controller.showScreen(AppController.HOME));

        add(title, BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
    }
}
