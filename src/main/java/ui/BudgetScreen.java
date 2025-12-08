package ui;

import src.main.java.model.BudgetItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BudgetScreen extends JPanel {

    public BudgetScreen(AppController controller) {

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Προβολή Κρατικού Προϋπολογισμού", SwingConstants.CENTER);

        String[] cols = {"Code", "Name", "Type", "Amount", "Total"};

        DefaultTableModel model = new DefaultTableModel(cols, 0);

        BudgetItem i1 = new BudgetItem("001", "Salary Tax", "REVENUE", 2000000L);
        BudgetItem i2 = new BudgetItem("002", "Education Spending", "EXPENDITURE", 1200000L);

        model.addRow(new Object[]{i1.getCode(), i1.getName(), i1.getType(), i1.getAmount(), i1.getTotal()});
        model.addRow(new Object[]{i2.getCode(), i2.getName(), i2.getType(), i2.getAmount(), i2.getTotal()});

        JTable table = new JTable(model);

        JButton back = new JButton("Back");
        back.addActionListener(e -> controller.showScreen(AppController.HOME));

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
    }
}
