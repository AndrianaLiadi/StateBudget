package ui;

import javax.swing.*;
import java.awt.*;

public class ReportScreen extends JPanel {

    public ReportScreen(AppController controller) {

        setLayout(new BorderLayout());

        JLabel label = new JLabel("Reports Page", SwingConstants.CENTER);

        JButton back = new JButton("Back");
        back.addActionListener(e -> controller.showScreen(AppController.HOME));

        add(label, BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
    }
}
