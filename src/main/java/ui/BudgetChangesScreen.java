package ui;

import model.BudgetChange;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BudgetChangesScreen extends JPanel {

    public BudgetChangesScreen(AppController controller, List<BudgetChange> changes) {

        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append("ΑΛΛΑΓΜΕΝΟΣ ΠΙΝΑΚΑΣ\n\n");

        for (BudgetChange ch : changes) {
            sb.append(ch.getItemCode()).append(" : ")
              .append(ch.getItemName())
              .append(" Από ").append(ch.getOldValue())
              .append(" -> ").append(ch.getNewValue())
              .append("\n");
        }

        sb.append("\nΣύνολο αλλαγών: ").append(changes.size());

        area.setText(sb.toString());

        add(new JScrollPane(area), BorderLayout.CENTER);

        JButton back = new JButton("Back");
        back.addActionListener(e -> controller.showScreen(AppController.HOME));

        add(back, BorderLayout.SOUTH);
    }
}
