package ui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link RegistrationScreen}.
 * <p>
 * Ελέγχει την ορθή αρχικοποίηση και τη δομή της οθόνης εγγραφής χρηστών.
 * Επιβεβαιώνει ότι υπάρχουν όλα τα απαραίτητα πεδία εισαγωγής (Όνομα, Επίθετο, Email)
 * και το κουμπί εγγραφής, καθώς και ότι η διάταξη (Layout) είναι η αναμενόμενη.
 * </p>
 */
class RegistrationScreenTest {

    private RegistrationScreen registrationScreen;
    private AppController controller;

    /**
     * Αρχικοποίηση δεδομένων πριν από κάθε test.
     * <p>
     * Δημιουργεί τον ελεγκτή και την οθόνη εγγραφής για να είναι έτοιμα για έλεγχο.
     * </p>
     */
    @BeforeEach
    void setUp() {
        controller = new AppController();
        controller.setVisible(false);

        registrationScreen = new RegistrationScreen(controller);
    }

    /**
     * Ελέγχει τη σωστή αρχικοποίηση της οθόνης.
     * <p>
     * Επιβεβαιώνει ότι:
     * 1. Το αντικείμενο δημιουργήθηκε.
     * 2. Χρησιμοποιείται {@link GridLayout}.
     * 3. Το πλέγμα έχει 4 γραμμές (3 πεδία + 1 κουμπί) και 1 στήλη.
     * </p>
     */
    @Test
    void testRegistrationScreenInitialization() {
        assertNotNull(registrationScreen);
        assertTrue(registrationScreen.getLayout() instanceof GridLayout);

        GridLayout layout = (GridLayout) registrationScreen.getLayout();
        assertEquals(4, layout.getRows());
        assertEquals(1, layout.getColumns());
    }

    /**
     * Ελέγχει την ύπαρξη των πεδίων κειμένου (TextFields).
     * <p>
     * Αναζητά τα πεδία με βάση το αρχικό τους κείμενο ("Name", "Surname", "Email")
     * για να βεβαιωθεί ότι έχουν προστεθεί στην οθόνη.
     * </p>
     */
    @Test
    void testTextFieldsExist() {
        JTextField nameField = findTextFieldByText(registrationScreen, "Name");
        JTextField surnameField = findTextFieldByText(registrationScreen, "Surname");
        JTextField emailField = findTextFieldByText(registrationScreen, "Email");

        assertNotNull(nameField);
        assertNotNull(surnameField);
        assertNotNull(emailField);
    }

    /**
     * Ελέγχει την ύπαρξη του κουμπιού "Register".
     */
    @Test
    void testRegisterButtonExists() {
        JButton registerButton = findButtonByText(registrationScreen, "Register");
        assertNotNull(registerButton);
    }

    /**
     * Ελέγχει τη λειτουργικότητα του κουμπιού "Register".
     * <p>
     * Προσομοιώνει το πάτημα (click) και βεβαιώνει ότι δεν προκαλείται εξαίρεση.
     * </p>
     */
    @Test
    void testRegisterButtonActionDoesNotThrow() {
        JButton registerButton = findButtonByText(registrationScreen, "Register");

        assertNotNull(registerButton);
        assertDoesNotThrow(() -> registerButton.doClick());
    }


    /* =======================
       Helper methods (Βοηθητικές μέθοδοι)
       ======================= */

    /**
     * Βοηθητική μέθοδος για την αναδρομική εύρεση πεδίου κειμένου βάσει περιεχομένου.
     *
     * @param root Το αρχικό δοχείο αναζήτησης.
     * @param text Το κείμενο που περιέχει το πεδίο.
     * @return Το JTextField αν βρεθεί, αλλιώς null.
     */
    private JTextField findTextFieldByText(Container root, String text) {
        for (Component c : root.getComponents()) {
            if (c instanceof JTextField) {
                JTextField tf = (JTextField) c;
                if (text.equals(tf.getText())) {
                    return tf;
                }
            }
            if (c instanceof Container) {
                JTextField found = findTextFieldByText((Container) c, text);
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