package ui;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Container;

import javax.swing.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link BudgetScreen}.
 * <p>
 * Ελέγχει την ορθή αρχικοποίηση και τη λειτουργικότητα των γραφικών στοιχείων της οθόνης
 * προβολής προϋπολογισμού. Επαληθεύει την ύπαρξη των απαραίτητων συστατικών (λίστα ετών,
 * περιοχή κειμένου, κουμπιά) και τη σωστή συμπεριφορά τους κατά την αλληλεπίδραση.
 * </p>
 */
class BudgetScreenTest {

    private BudgetScreen budgetScreen;
    private AppController controller;

    /**
     * Αρχικοποίηση πριν από κάθε test.
     * <p>
     * Δημιουργεί τον ελεγκτή και την οθόνη BudgetScreen για να είναι έτοιμα για έλεγχο.
     * </p>
     */
    @BeforeEach
    void setUp() {
        controller = new AppController();
        controller.setVisible(false);

        budgetScreen = new BudgetScreen(controller);
    }

    /**
     * Ελέγχει τη σωστή αρχικοποίηση της οθόνης.
     * <p>
     * Επιβεβαιώνει ότι το αντικείμενο δεν είναι null και έχει το σωστό Layout (BorderLayout).
     * </p>
     */
    @Test
    void testBudgetScreenInitialization() {
        assertNotNull(budgetScreen);
        assertTrue(budgetScreen.getLayout() instanceof java.awt.BorderLayout);
    }

    /**
     * Ελέγχει την ύπαρξη και τα περιεχόμενα του ComboBox επιλογής έτους.
     * <p>
     * Επιβεβαιώνει ότι:
     * 1. Υπάρχει ένα JComboBox.
     * 2. Περιέχει 7 επιλογές (έτη 2019-2025).
     * 3. Η προεπιλεγμένη τιμή είναι το 2025.
     * </p>
     */
    @Test
    void testYearComboBoxExistsAndHasYears() {
        JComboBox<?> combo = findComponent(budgetScreen, JComboBox.class);

        assertNotNull(combo);
        assertEquals(7, combo.getItemCount());
        assertEquals(2025, combo.getSelectedItem());
    }

    /**
     * Ελέγχει την περιοχή κειμένου (TextArea) όπου εμφανίζεται ο προϋπολογισμός.
     * <p>
     * Επιβεβαιώνει ότι υπάρχει και δεν είναι επεξεργάσιμη (read-only), ώστε ο χρήστης
     * να μην μπορεί να αλλοιώσει τα δεδομένα προβολής.
     * </p>
     */
    @Test
    void testTextAreaExistsAndIsNotEditable() {
        JTextArea area = findComponent(budgetScreen, JTextArea.class);

        assertNotNull(area);
        assertFalse(area.isEditable());
    }

    /**
     * Ελέγχει την ύπαρξη του κουμπιού επιστροφής ("Back").
     */
    @Test
    void testBackButtonExists() {
        JButton backButton = findButtonByText(budgetScreen, "Back");

        assertNotNull(backButton);
    }

    /**
     * Ελέγχει τη λειτουργικότητα του κουμπιού επιστροφής.
     * <p>
     * Προσομοιώνει το πάτημα (click) και βεβαιώνει ότι δεν προκαλείται σφάλμα.
     * </p>
     */
    @Test
    void testBackButtonActionDoesNotThrow() {
        JButton backButton = findButtonByText((Container)budgetScreen, "Back");

        assertNotNull(backButton);
        assertDoesNotThrow(() -> backButton.doClick());
    }

    /**
     * Ελέγχει τη λειτουργικότητα αλλαγής έτους.
     * <p>
     * Προσομοιώνει την επιλογή διαφορετικών ετών στο ComboBox για να βεβαιωθεί ότι
     * η φόρτωση δεδομένων (που συμβαίνει στον listener) δεν "κρασάρει" την εφαρμογή.
     * </p>
     */
    @Test
    void testChangingYearDoesNotThrow() {
        JComboBox<Integer> combo = findComponent(budgetScreen, JComboBox.class);

        assertNotNull(combo);

        assertDoesNotThrow(() -> {
            combo.setSelectedItem(2019);
            combo.setSelectedItem(2023);
            combo.setSelectedItem(2025);
        });
    }

    /* =======================
       Helper methods (Βοηθητικές μέθοδοι εύρεσης συστατικών)
       ======================= */

    /**
     * Βοηθητική μέθοδος που αναζητά αναδρομικά ένα γραφικό στοιχείο συγκεκριμένου τύπου.
     * <p>
     * Ψάχνει μέσα σε όλα τα παιδιά (components) του δοχείου (container) και των υπο-δοχείων του.
     * </p>
     *
     * @param root Το αρχικό δοχείο αναζήτησης (π.χ. το Panel).
     * @param type Η κλάση του στοιχείου που ψάχνουμε (π.χ. JComboBox.class).
     * @param <T>  Ο τύπος του στοιχείου.
     * @return Το στοιχείο αν βρεθεί, αλλιώς null.
     */
    @SuppressWarnings("unchecked")
    private <T extends JComponent> T findComponent(Container root, Class<T> type) {
        for (java.awt.Component c : root.getComponents()) {
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
     * Βοηθητική μέθοδος που αναζητά αναδρομικά ένα κουμπί με βάση το κείμενό του.
     *
     * @param root Το αρχικό δοχείο αναζήτησης.
     * @param text Το κείμενο που πρέπει να γράφει το κουμπί.
     * @return Το κουμπί αν βρεθεί, αλλιώς null.
     */
    private JButton findButtonByText(Container root, String text) {
        for (java.awt.Component c : root.getComponents()) {
            if (c instanceof JButton) {
                JButton b = (JButton) c;
                if (text.equals(b.getText())) {
                    return b;
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
