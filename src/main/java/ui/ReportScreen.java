package ui;

import javax.swing.*;

import model.Budget;
import model.Scenario;

import java.awt.*;

public class ReportScreen extends JPanel {

     public ReportScreen(AppController controller, Scenario scenario) {

        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setText(scenario.getSummary());

        Budget base = scenario.getBaseBudget();
        Budget modified = scenario.getModifiedBudget();

        ChartPanel chart = new ChartPanel(base, modified);

        JButton back = new JButton("Back");
        back.addActionListener(e ->
            controller.showScreen(AppController.HOME)
        );

        add(new JScrollPane(area), BorderLayout.WEST);
        add(chart, BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
    }
}


