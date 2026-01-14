package ui;

import data.BudgetDataLoader;
import model.Budget;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import javax.swing.*;
import java.awt.*;

/**
 * Η κλάση BudgetScreen υλοποιεί την οθόνη προβολής του Κρατικού Προϋπολογισμού.
 * <p>
 * Παρέχει στον χρήστη τη δυνατότητα να επιλέξει ένα οικονομικό έτος από μια λίστα
 * και να δει τα αναλυτικά στοιχεία εσόδων και εξόδων σε μια περιοχή κειμένου.
 * Χρησιμοποιεί τον {@link BudgetDataLoader} για τη φόρτωση των αρχείων.
 * </p>
 */
public class BudgetScreen extends JPanel {

    private JTextArea area;
    private JComboBox<Integer> yearCombo;
    private final BudgetDataLoader loader = new BudgetDataLoader();

    /**
     * Κατασκευαστής της οθόνης προβολής προϋπολογισμού.
     * <p>
     * Αρχικοποιεί τα γραφικά συστατικά (τίτλους, επιλογέα έτους, περιοχή κειμένου)
     * και ορίζει τις ενέργειες (listeners) για την αλλαγή έτους και την επιστροφή.
     * </p>
     *
     * @param controller Ο ελεγκτής της εφαρμογής για την πλοήγηση.
     */
    public BudgetScreen(AppController controller) {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Προβολή Κρατικού Προϋπολογισμού", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));

        // Year selector panel
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

        // Προσθήκη listener για αλλαγή έτους
        yearCombo.addActionListener(e -> loadAndShowSelectedYear());
        
        // Αρχική φόρτωση
        loadAndShowSelectedYear();
    }

    /**
     * Φορτώνει και εμφανίζει τα δεδομένα του προϋπολογισμού για το επιλεγμένο έτος.
     * <p>
     * Διαβάζει το έτος από το {@code yearCombo}, κατασκευάζει το όνομα του αρχείου CSV,
     * φορτώνει το αντικείμενο {@link Budget} και ενημερώνει την περιοχή κειμένου.
     * </p>
     */
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

    /**
     * Βοηθητική μέθοδος που καταγράφει την έξοδο της {@link BudgetTablePrinter}.
     * <p>
     * Επειδή η κλάση {@code BudgetTablePrinter} είναι σχεδιασμένη να τυπώνει απευθείας
     * στο {@code System.out} (κονσόλα), αυτή η μέθοδος ανακατευθύνει προσωρινά την έξοδο
     * σε μια ροή μνήμης ({@link ByteArrayOutputStream}). Έτσι, μπορούμε να πάρουμε το
     * αποτέλεσμα ως String και να το εμφανίσουμε στο GUI αντί για το τερματικό.
     * </p>
     *
     * @param budget Ο προϋπολογισμός προς εκτύπωση.
     * @return Το κείμενο του προϋπολογισμού μορφοποιημένο.
     */
    private String captureBudgetPrinterOutput(Budget budget) {
        BudgetTablePrinter printer = new BudgetTablePrinter();

        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8)) {
            // Ανακατεύθυνση του System.out στο δικό μας PrintStream
            System.setOut(ps);
            printer.printBudget(budget);
        } finally {
            // Επαναφορά του αρχικού System.out
            System.setOut(oldOut);
        }

        return baos.toString(StandardCharsets.UTF_8);
    }
}