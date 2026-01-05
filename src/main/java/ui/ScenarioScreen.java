package ui;

import model.Budget;
import model.BudgetChange;
import model.BudgetItem;
import model.Scenario;

import javax.swing.*;
import java.awt.*;
//this class shows the scenarios
public class ScenarioScreen extends JPanel {

    private final Scenario scenario;

    public ScenarioScreen(AppController controller, Budget baseBudget) {
        this.scenario = new Scenario(baseBudget, "Σενάριο Χρήστη");

        setLayout(new BorderLayout());

       
        JLabel title = new JLabel("Scenario Simulation", SwingConstants.CENTER);

        JComboBox<BudgetItem> itemBox = new JComboBox<>(
                baseBudget.getItems().toArray(new BudgetItem[0])
        );

        JTextField newValueField = new JTextField(10);

        JButton addChange = new JButton("Προσθήκη αλλαγής");
        JButton runScenario = new JButton("Εκτέλεση σεναρίου");
        JButton back = new JButton("Back");

        DefaultListModel<String> changesModel = new DefaultListModel<>();
        JList<String> changesList = new JList<>(changesModel);
        changesList.setVisibleRowCount(8);

        
        addChange.addActionListener(ev -> {
            try {
                BudgetItem item = (BudgetItem) itemBox.getSelectedItem();
                if (item == null) return;

                long newValue = Long.parseLong(newValueField.getText().trim());
                long oldValue = item.getAmount();

                BudgetChange change = new BudgetChange(
                        item.getCode(),
                        item.getName(),
                        oldValue,
                        newValue,
                        item.getType()
                );

                scenario.getChanges().add(change);

                
                changesModel.addElement(
                        item.getName() + " (" + item.getCode() + "): " + oldValue + " → " + newValue
                );
                newValueField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Βάλε αριθμό (π.χ. 1200).",
                        "Λάθος τιμή",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        runScenario.addActionListener(ev -> {
            scenario.applyChanges();
            scenario.generateSummary();
            controller.showReportScreen(scenario); 
        });

        back.addActionListener(ev -> controller.showScreen(AppController.HOME));

       
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        top.add(title);

        JPanel center = new JPanel(new FlowLayout());
        center.add(new JLabel("Item:"));
        center.add(itemBox);
        center.add(new JLabel("Νέα τιμή:"));
        center.add(newValueField);
        center.add(addChange);
        center.add(runScenario);

        JPanel right = new JPanel(new BorderLayout());
        right.add(new JLabel("Αλλαγές που προστέθηκαν:", SwingConstants.CENTER), BorderLayout.NORTH);
        right.add(new JScrollPane(changesList), BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(right, BorderLayout.EAST);
        add(back, BorderLayout.SOUTH);
    }
}

