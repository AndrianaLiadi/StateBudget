package ui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link LandingScreen}.
 * <p>
 * Ελέγχει την ορθή αρχικοποίηση και τη δομή της οθόνης υποδοχής.
 * Επιβεβαιώνει ότι η διάταξη είναι σωστή (GridLayout 4 γραμμών) και ότι
 * όλα τα απαραίτητα στοιχεία πλοήγησης (κουμπιά Home, About, Contact) και
 * το μήνυμα καλωσορίσματος υπάρχουν και λειτουργούν.
 * </p>
 */
class LandingScreenTest {

    private LandingScreen landingScreen;
    private AppController controller;

    /**
     * Αρχικοποίηση δεδομένων πριν από κάθε test.
     * <p>
     * Δημιουργεί τον ελεγκτή και την οθόνη υποδοχής για να είναι έτοιμα για έλεγχο.
     * </p>
     */
    @BeforeEach
    void setUp() {
        controller = new AppController();
        controller.setVisible(false);

        landingScreen = new LandingScreen(controller);
    }

    /**
     * Ελέγχει τη σωστή αρχικοποίηση της οθόνης.
     * <p>
     * Επιβεβαιώνει ότι:
     * 1. Το αντικείμενο δεν είναι null.
     * 2. Χρησιμοποιείται {@link GridLayout}.
     * 3. Το πλέγμα έχει 4 γραμμές και 1 στήλη (Τίτλος + 3 Κουμπιά).
     * </p>
     */
    @Test
    void testLandingScreenInitialization() {
        assertNotNull(landingScreen);
        assertTrue(landingScreen.getLayout() instanceof GridLayout);

        GridLayout layout = (GridLayout) landingScreen.getLayout();
        assertEquals(4, layout.getRows());
        assertEquals(1, layout.getColumns());
    }

    /**
     * Ελέγχει την ύπαρξη της ετικέτας καλωσορίσματος.
     * <p>
     * Αναζητά ένα {@link JLabel} και επιβεβαιώνει το κείμενο "Καλώς ήρθες στην εφαρμογή!".
     * </p>
     */
    @Test
    void testWelcomeLabelExists() {
        JLabel label = findComponent(landingScreen, JLabel.class);

        assertNotNull(label);
        assertEquals("Καλώς ήρθες στην εφαρμογή!", label.getText());
        assertEquals(SwingConstants.CENTER, label.getHorizontalAlignment());
    }

    /**
     * Ελέγχει την ύπαρξη του κουμπιού "Home".
     */
    @Test
    void testHomeButtonExists() {
        JButton homeButton = findButtonByText(landingScreen, "Home");
        assertNotNull(homeButton);
    }

    /**
     * Ελέγχει την ύπαρξη του κουμπιού "About".
     */
    @Test
    void testAboutButtonExists() {
        JButton aboutButton = findButtonByText(landingScreen, "About");
        assertNotNull(aboutButton);
    }

    /**
     * Ελέγχει την ύπαρξη του κουμπιού "Contact".
     */
    @Test
    void testContactButtonExists() {
        JButton contactButton = findButtonByText(landingScreen, "Contact");
        assertNotNull(contactButton);
    }

    /**
     * Ελέγχει τη λειτουργικότητα του κουμπιού "Home".
     * <p>
     * Προσομοιώνει το πάτημα (click) και βεβαιώνει ότι δεν προκαλείται εξαίρεση.
     * </p>
     */
    @Test
    void testHomeButtonActionDoesNotThrow() {
        JButton homeButton = findButtonByText(landingScreen, "Home");

        assertNotNull(homeButton);
        assertDoesNotThrow(() -> homeButton.doClick());
    }

    /**
     * Ελέγχει τη λειτουργικότητα του κουμπιού "About".
     */
    @Test
    void testAboutButtonActionDoesNotThrow() {
        JButton aboutButton = findButtonByText(landingScreen, "About");

        assertNotNull(aboutButton);
        assertDoesNotThrow(() -> aboutButton.doClick());
    }

    /**
     * Ελέγχει τη λειτουργικότητα του κουμπιού "Contact".
     */
    @Test
    void testContactButtonActionDoesNotThrow() {
        JButton contactButton = findButtonByText(landingScreen, "Contact");

        assertNotNull(contactButton);
        assertDoesNotThrow(() -> contactButton.doClick());
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
