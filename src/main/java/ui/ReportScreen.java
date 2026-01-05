package ui;

import javax.swing.*;

import model.Budget;
import model.Scenario;

import java.awt.*;

public class ReportScreen extends JPanel {

     public ReportScreen(AppController controller, Scenario scenario) {

        setLayout(new BorderLayout());

        JButton back = new JButton("Back");
        back.addActionListener(e -> controller.showScreen(AppController.HOME));

        JTextArea area = new JTextArea();
        area.setEditable(false);
        
        if (scenario == null) {
            area.setText("Δεν υπάρχει σενάριο ακόμη. Πήγαινε στο Scenarios και δημιούργησε ένα.");
            add(new JScrollPane(area), BorderLayout.CENTER);
            add(back, BorderLayout.SOUTH);
            return;
        }
        area.setText(scenario.getSummary());
        Budget base = scenario.getBaseBudget();
        Budget modified = scenario.getModifiedBudget();

        ChartPanel chart = new ChartPanel(base, modified);

       
        back.addActionListener(e ->
            controller.showScreen(AppController.HOME)
        );

        add(new JScrollPane(area), BorderLayout.WEST);
        add(chart, BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
    }
}


