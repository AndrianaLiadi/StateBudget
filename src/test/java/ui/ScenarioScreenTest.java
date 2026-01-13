package ui;

import static org.junit.jupiter.api.Assertions.*;

import model.Budget;
import model.BudgetItem;
import model.Scenario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link ScenarioScreen}.
 * <p>
 * Ελέγχει τη διεπαφή χρήστη για τη διαχείριση και προσομοίωση σεναρίων.
 * Επιβεβαιώνει την ύπαρξη των απαραίτητων συστατικών (ComboBox, TextField, Λίστα, Κουμπιά)
 * και δοκιμάζει τη λειτουργικότητα της προσθήκης αλλαγών στη λίστα του σεναρίου.
 * </p>
 */
class ScenarioScreenTest {

    private AppController controller;
    private Budget baseBudget;
    private ScenarioScreen scenarioScreen;

    /**
     * Αρχικοποίηση δεδομένων πριν από κάθε test.
     * <p>
     * Δημιουργεί έναν ελεγκτή και έναν βασικό προϋπολογισμό με εικονικά δεδομένα,
     * ώστε να μπορεί να δημιουργηθεί η οθόνη σεναρίου.
     * </p>
     */
    @BeforeEach
    void setUp() {
        controller = new AppController();
        controller.setVisible(false);

        // Δημιουργία mock δεδομένων για το Dropdown
        BudgetItem item1 = new BudgetItem("A1", "Item A1", "Income", 1000);
        BudgetItem item2 = new BudgetItem("B1", "Item B1", "Income", 2000);
        baseBudget = new Budget(2025, List.of(item1, item2));

        scenarioScreen = new ScenarioScreen(controller, baseBudget);
    }

    /**
     * Ελέγχει τη σωστή αρχικοποίηση της οθόνης.
     */
    @Test
    void testScenarioScreenInitialization() {
        assertNotNull(scenarioScreen);
        assertTrue(scenarioScreen.getLayout() instanceof BorderLayout);
    }

    /**
     * Ελέγχει την ύπαρξη όλων των απαραίτητων γραφικών στοιχείων.
     * <p>
     * Επιβεβαιώνει ότι υπάρχουν:
     * <ul>
     * <li>Dropdown επιλογής κονδυλίου (JComboBox).</li>
     * <li>Πεδίο εισαγωγής τιμής (JTextField).</li>
     * <li>Κουμπιά "Προσθήκη", "Εκτέλεση" και "Back".</li>
     * <li>Λίστα εμφάνισης αλλαγών (JList).</li>
     * </ul>
     * </p>
     */
    @Test
    void testComponentsExist() {
        JComboBox<?> comboBox = findComponent(scenarioScreen, JComboBox.class);
        JTextField textField = findComponent(scenarioScreen, JTextField.class);
        JButton addButton = findButtonByText(scenarioScreen, "Προσθήκη αλλαγής");
        JButton runButton = findButtonByText(scenarioScreen, "Εκτέλεση σεναρίου");
        JButton backButton = findButtonByText(scenarioScreen, "Back");
        JList<?> changesList = findComponent(scenarioScreen, JList.class);

        assertNotNull(comboBox);
        assertNotNull(textField);
        assertNotNull(addButton);
        assertNotNull(runButton);
        assertNotNull(backButton);
        assertNotNull(changesList);
    }

    /**
     * Ελέγχει τη λειτουργικότητα προσθήκης μιας αλλαγής.
     * <p>
     * Προσομοιώνει τα βήματα του χρήστη:
     * 1. Επιλογή στοιχείου από το ComboBox.
     * 2. Πληκτρολόγηση τιμής στο TextField.
     * 3. Πάτημα του κουμπιού "Προσθήκη αλλαγής".
     * <br>
     * Στη συνέχεια ελέγχει αν το πεδίο καθάρισε και αν η λίστα ενημερώθηκε με 1 εγγραφή.
     * </p>
     */
    @Test
    void testAddChangeActionDoesNotThrow() {
        JComboBox<BudgetItem> comboBox = findComponent(scenarioScreen, JComboBox.class);
        JTextField textField = findComponent(scenarioScreen, JTextField.class);
        JButton addButton = findButtonByText(scenarioScreen, "Προσθήκη αλλαγής");
        JList<?> changesList = findComponent(scenarioScreen, JList.class);

        assertNotNull(comboBox);
        assertNotNull(textField);
        assertNotNull(addButton);
        assertNotNull(changesList);

        // Προσομοίωση χρήστη
        comboBox.setSelectedIndex(0);
        textField.setText("1500");

        // Πάτημα κουμπιού
        assertDoesNotThrow(() -> addButton.doClick());

        // Έλεγχοι αποτελεσμάτων
        assertEquals("", textField.getText()); // Το πεδίο πρέπει να αδειάσει

        DefaultListModel<?> model = (DefaultListModel<?>) changesList.getModel();
        assertEquals(1, model.getSize()); // Η λίστα πρέπει να έχει 1 στοιχείο
        assertTrue(model.getElementAt(0).toString().contains("A1"));
    }

    /**
     * Ελέγχει τη λειτουργικότητα των κουμπιών εκτέλεσης και επιστροφής.
     */
    @Test
    void testRunScenarioAndBackActionsDoNotThrow() {
        JButton runButton = findButtonByText(scenarioScreen, "Εκτέλεση σεναρίου");
        JButton backButton = findButtonByText(scenarioScreen, "Back");

        assertNotNull(runButton);
        assertNotNull(backButton);

        assertDoesNotThrow(() -> runButton.doClick());
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
