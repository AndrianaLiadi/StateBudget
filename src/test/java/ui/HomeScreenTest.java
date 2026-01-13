package ui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link HomeScreen}.
 * <p>
 * Ελέγχει την ορθή αρχικοποίηση και τη δομή της αρχικής οθόνης της εφαρμογής.
 * Επιβεβαιώνει ότι χρησιμοποιείται το σωστό Layout Manager (GridLayout) και ότι
 * υπάρχουν τα απαραίτητα κουμπιά πλοήγησης ("Δες Προϋπολογισμό", "Back") και ο τίτλος.
 * </p>
 */
class HomeScreenTest {

    private HomeScreen homeScreen;
    private AppController controller;

    /**
     * Αρχικοποίηση δεδομένων πριν από κάθε test.
     * <p>
     * Δημιουργεί τον ελεγκτή και την αρχική οθόνη για να είναι έτοιμα για έλεγχο.
     * </p>
     */
    @BeforeEach
    void setUp() {
        controller = new AppController();
        controller.setVisible(false);

        homeScreen = new HomeScreen(controller);
    }

    /**
     * Ελέγχει τη σωστή αρχικοποίηση της οθόνης.
     * <p>
     * Επιβεβαιώνει ότι:
     * 1. Το αντικείμενο δεν είναι null.
     * 2. Χρησιμοποιείται {@link GridLayout}.
     * 3. Το πλέγμα έχει 3 γραμμές και 1 στήλη (όπως ορίστηκε στον σχεδιασμό).
     * </p>
     */
    @Test
    void testHomeScreenInitialization() {
        assertNotNull(homeScreen);
        assertTrue(homeScreen.getLayout() instanceof GridLayout);

        GridLayout layout = (GridLayout) homeScreen.getLayout();
        assertEquals(3, layout.getRows());
        assertEquals(1, layout.getColumns());
    }

    /**
     * Ελέγχει την ύπαρξη και το περιεχόμενο της ετικέτας τίτλου.
     * <p>
     * Αναζητά ένα {@link JLabel} και επιβεβαιώνει ότι το κείμενό του είναι
     * "Καλώς ήρθες στο Home" και είναι κεντραρισμένο.
     * </p>
     */
    @Test
    void testContainsTitleLabel() {
        JLabel label = findComponent(homeScreen, JLabel.class);

        assertNotNull(label);
        assertEquals("Καλώς ήρθες στο Home", label.getText());
        assertEquals(SwingConstants.CENTER, label.getHorizontalAlignment());
    }

    /**
     * Ελέγχει την ύπαρξη του κουμπιού "Δες Προϋπολογισμό".
     */
    @Test
    void testBudgetButtonExists() {
        JButton budgetButton = findButtonByText(homeScreen, "Δες Προϋπολογισμό");
        assertNotNull(budgetButton);
    }

    /**
     * Ελέγχει την ύπαρξη του κουμπιού "Back".
     */
    @Test
    void testBackButtonExists() {
        JButton backButton = findButtonByText(homeScreen, "Back");
        assertNotNull(backButton);
    }

    /**
     * Ελέγχει τη λειτουργικότητα του κουμπιού "Δες Προϋπολογισμό".
     * <p>
     * Προσομοιώνει το πάτημα (click) και βεβαιώνει ότι δεν προκαλείται εξαίρεση.
     * </p>
     */
    @Test
    void testBudgetButtonActionDoesNotThrow() {
        JButton budgetButton = findButtonByText(homeScreen, "Δες Προϋπολογισμό");

        assertNotNull(budgetButton);
        assertDoesNotThrow(() -> budgetButton.doClick());
    }

    /**
     * Ελέγχει τη λειτουργικότητα του κουμπιού "Back".
     */
    @Test
    void testBackButtonActionDoesNotThrow() {
        JButton backButton = findButtonByText(homeScreen, "Back");

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
