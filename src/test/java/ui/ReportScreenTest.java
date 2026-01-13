package ui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;

import model.Budget;
import model.BudgetItem;
import model.Scenario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link ReportScreen}.
 * <p>
 * Ελέγχει την ορθή εμφάνιση της οθόνης αναφορών (reports).
 * Καλύπτει δύο βασικά σενάρια:
 * <ul>
 * <li>Άνοιγμα της οθόνης χωρίς δεδομένα (null scenario), όπου πρέπει να εμφανίζεται κατάλληλο μήνυμα.</li>
 * <li>Άνοιγμα με έγκυρο σενάριο, όπου πρέπει να εμφανίζονται η σύνοψη κειμένου και τα γραφήματα.</li>
 * </ul>
 * </p>
 */
class ReportScreenTest {

    private AppController controller;

    /**
     * Αρχικοποίηση δεδομένων πριν από κάθε test.
     * <p>
     * Δημιουργεί τον ελεγκτή (σε κρυφή κατάσταση) για να είναι έτοιμος για έλεγχο.
     * </p>
     */
    @BeforeEach
    void setUp() {
        controller = new AppController();
        controller.setVisible(false);
    }

    /**
     * Ελέγχει τη συμπεριφορά της οθόνης όταν δεν υπάρχει σενάριο (null).
     * <p>
     * Επιβεβαιώνει ότι:
     * 1. Η οθόνη δημιουργείται χωρίς να κρασάρει.
     * 2. Η περιοχή κειμένου περιέχει το μήνυμα "Δεν υπάρχει σενάριο".
     * 3. Το κουμπί επιστροφής λειτουργεί.
     * </p>
     */
    @Test
    void testReportScreenWithNullScenario() {
        ReportScreen screen = new ReportScreen(controller, null);

        assertNotNull(screen);
        assertTrue(screen.getLayout() instanceof BorderLayout);

        JTextArea area = findComponent(screen, JTextArea.class);
        JButton backButton = findButtonByText(screen, "Back");

        assertNotNull(area);
        assertTrue(area.getText().contains("Δεν υπάρχει σενάριο"));
        assertFalse(area.isEditable());

        assertNotNull(backButton);
        assertDoesNotThrow(() -> backButton.doClick());
    }

    /**
     * Ελέγχει τη συμπεριφορά της οθόνης με έγκυρο σενάριο.
     * <p>
     * Δημιουργεί εικονικά δεδομένα (base vs modified budget), τα αναθέτει σε ένα σενάριο
     * και επιβεβαιώνει ότι:
     * 1. Η περιοχή κειμένου δείχνει τη σύνοψη του σεναρίου.
     * 2. Υπάρχει το γραφικό συστατικό {@link ChartPanel} για τα διαγράμματα.
     * 3. Το κουμπί επιστροφής λειτουργεί.
     * </p>
     */
    @Test
    void testReportScreenWithScenario() {
        // Setup δεδομένων
        BudgetItem item1 = new BudgetItem("A1", "Item A1", "Income", 1000);
        BudgetItem item2 = new BudgetItem("B1", "Item B1", "Income", 2000);

        Budget base = new Budget(2025, List.of(item1));
        Budget modified = new Budget(2025, List.of(item2));

        Scenario scenario = new Scenario(base, "Test Scenario");
        scenario.setModifiedBudget(modified);

        // Δημιουργία οθόνης
        ReportScreen screen = new ReportScreen(controller, scenario);

        assertNotNull(screen);

        // Εύρεση συστατικών
        JTextArea area = findComponent(screen, JTextArea.class);
        JButton backButton = findButtonByText(screen, "Back");
        ChartPanel chart = findComponent(screen, ChartPanel.class);

        // Assertions
        assertNotNull(area);
        assertEquals(scenario.getSummary(), area.getText()); // Το κείμενο πρέπει να είναι η σύνοψη του σεναρίου

        assertNotNull(chart, "Πρέπει να υπάρχει το ChartPanel");
        assertNotNull(backButton);
        assertDoesNotThrow(() -> backButton.doClick());
    }


    /* =======================
       Helper methods (Βοηθητικές μέθοδοι)
       ======================= */

    /**
     * Βοηθητική μέθοδος για την αναδρομική εύρεση συστατικού βάσει τύπου κλάσης.
     *
     * @param root Το αρχικό δοχείο αναζήτησης.
     * @param type Η κλάση του στοιχείου που ψάχνουμε.
     * @param <T>  Ο γενικός τύπος του στοιχείου.
     * @return Το στοιχείο αν βρεθεί, αλλιώς null.
     */
    @SuppressWarnings("unchecked")
    private <T extends JComponent> T findComponent(Container root, Class<T> type) {
        for (Component c : root.getComponents()) {
            if (type.isInstance(c)) {
                return (T) c;
            }
            if (c instanceof Container) {
                T found = findComponent((Container) c, type);
                if (found != null) return found;
            }
        }
        return null;
    }

    /**
     * Βοηθητική μέθοδος για την αναδρομική εύρεση κουμπιού βάσει κειμένου.
     *
     * @param root Το αρχικό δοχείο αναζήτησης.
     * @param text Το κείμενο του κουμπιού.
     * @return Το κουμπί αν βρεθεί, αλλιώς null.
     */
    private JButton findButtonByText(Container root, String text) {
        for (Component c : root.getComponents()) {
            if (c instanceof JButton) {
                JButton button = (JButton) c;
                if (text.equals(button.getText())) {
                    return button;
                }
            }
            if (c instanceof Container) {
                JButton found = findButtonByText((Container) c, text);
                if (found != null) return found;
            }
        }
        return null;
    }
}
