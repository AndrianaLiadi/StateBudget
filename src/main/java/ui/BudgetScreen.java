package ui;

import data.BudgetDataLoader;
import model.Budget;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import javax.swing.*;
import java.awt.*;
//this class shows the budget of the goverment 
public class BudgetScreen extends JPanel {

    private JTextArea area;
    private JComboBox<Integer> yearCombo;
    private final BudgetDataLoader loader = new BudgetDataLoader();
    public BudgetScreen(AppController controller) {


       setLayout(new BorderLayout());

JLabel title = new JLabel("Προβολή Κρατικού Προϋπολογισμού", SwingConstants.CENTER);
title.setFont(new Font("Arial", Font.BOLD, 16));

//  Year selector
JPanel top = new JPanel();
top.add(new JLabel("Έτος:"));

Integer[] years = {2019, 2020, 2021, 2022, 2023, 2024, 2025};
yearCombo = new JComboBox<>(years);
yearCombo.setSelectedItem(2025);
top.add(yearCombo);

JPanel header = new JPanel();
header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
title.setAlignmentX(Component.CENTER_ALIGNMENT);
top.setAlignmentX(Component.CENTER_ALIGNMENT);
header.add(title);
header.add(top);

area = new JTextArea();
area.setEditable(false);
area.setFont(new Font("Consolas", Font.PLAIN, 14));
area.setCaretPosition(0);

add(header, BorderLayout.NORTH);
add(new JScrollPane(area), BorderLayout.CENTER);

JButton back = new JButton("Back");
back.addActionListener(e -> controller.showScreen(AppController.HOME));
add(back, BorderLayout.SOUTH);

yearCombo.addActionListener(e -> loadAndShowSelectedYear());
loadAndShowSelectedYear();

    }
    private void loadAndShowSelectedYear() {
    Integer year = (Integer) yearCombo.getSelectedItem();
    if (year == null) return;

    String filePath = "budget-" + year + ".csv";

    Budget budget = loader.loadFromCSV(filePath, year);

    if (budget == null) {
        area.setText("Σφάλμα φόρτωσης αρχείου: " + filePath);
        return;
    }

    String output = captureBudgetPrinterOutput(budget);
    area.setText(output);
    area.setCaretPosition(0);
}



private String captureBudgetPrinterOutput(Budget budget) {
    BudgetTablePrinter printer = new BudgetTablePrinter();

    PrintStream oldOut = System.out;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try (PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8)) {
        System.setOut(ps);
        printer.printBudget(budget);
    } finally {
        System.setOut(oldOut);
    }

    return baos.toString(StandardCharsets.UTF_8);
}

}
