package ui;

import model.Budget;
import model.BudgetChange;
import model.BudgetItem;
import model.Scenario;

import javax.swing.*;
import java.awt.*;

/**
 * Η κλάση ScenarioScreen παρέχει τη διεπαφή χρήστη για τη δημιουργία και προσομοίωση σεναρίων.
 * <p>
 * Σε αυτή την οθόνη, ο χρήστης μπορεί:
 * <ul>
 * <li>Να επιλέξει ένα κονδύλιο από τον βασικό προϋπολογισμό.</li>
 * <li>Να ορίσει μια νέα τιμή για το συγκεκριμένο κονδύλιο.</li>
 * <li>Να δει μια λίστα με τις αλλαγές που έχει προγραμματίσει.</li>
 * <li>Να εκτελέσει το σενάριο για να δει τα αποτελέσματα (μετάβαση στο ReportScreen).</li>
 * </ul>
 * </p>
 */
public class ScenarioScreen extends JPanel {

    private final Scenario scenario;

    /**
     * Κατασκευαστής της οθόνης σεναρίων.
     * <p>
     * Αρχικοποιεί ένα νέο αντικείμενο {@link Scenario} βασισμένο στον προϋπολογισμό (baseBudget).
     * Στήνει τα γραφικά στοιχεία (Dropdowns, TextFields, Buttons) και ορίζει τη λογική
     * για την προσθήκη αλλαγών και την εκτέλεση της προσομοίωσης.
     * </p>
     *
     * @param controller Ο κεντρικός ελεγκτής της εφαρμογής για την πλοήγηση.
     * @param baseBudget Ο βασικός προϋπολογισμός πάνω στον οποίο θα γίνουν οι αλλαγές.
     */
    public ScenarioScreen(AppController controller, Budget baseBudget) {
        // Δημιουργία νέου σεναρίου κατά την είσοδο στην οθόνη
        this.scenario = new Scenario(baseBudget, "Σενάριο Χρήστη");

        setLayout(new BorderLayout());

        
        JLabel title = new JLabel("Scenario Simulation", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));

        // Dropdown με τα διαθέσιμα κονδύλια
        JComboBox<BudgetItem> itemBox = new JComboBox<>(
                baseBudget.getItems().toArray(new BudgetItem[0])
        );

        JTextField newValueField = new JTextField(10);

        JButton addChange = new JButton("Προσθήκη αλλαγής");
        JButton runScenario = new JButton("Εκτέλεση σεναρίου");
        JButton back = new JButton("Back");

        // Μοντέλο λίστας για την εμφάνιση των αλλαγών στο UI
        DefaultListModel<String> changesModel = new DefaultListModel<>();
        JList<String> changesList = new JList<>(changesModel);
        changesList.setVisibleRowCount(8);

        
        // Λογική κουμπιού "Προσθήκη αλλαγής"
        addChange.addActionListener(ev -> {
            try {
                BudgetItem item = (BudgetItem) itemBox.getSelectedItem();
                if (item == null) return;

                // Ανάγνωση και έλεγχος της τιμής
                long newValue = Long.parseLong(newValueField.getText().trim());
                long oldValue = item.getAmount();

                // Δημιουργία αντικειμένου αλλαγής
                BudgetChange change = new BudgetChange(
                        item.getCode(),
                        item.getName(),
                        oldValue,
                        newValue,
                        item.getType()
                );

                // Προσθήκη στο σενάριο
                scenario.getChanges().add(change);

                // Ενημέρωση της λίστας στην οθόνη
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

        // Λογική κουμπιού "Εκτέλεση σεναρίου"
        runScenario.addActionListener(ev -> {
            scenario.applyChanges();     // Εφαρμογή αλλαγών
            scenario.generateSummary();  // Δημιουργία κειμένου σύνοψης
            controller.showReportScreen(scenario); // Μετάβαση στην οθόνη αποτελεσμάτων
        });

        back.addActionListener(ev -> controller.showScreen(AppController.HOME));

        // --- Στήσιμο Layout (Εμφάνιση) ---
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