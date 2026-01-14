package ui;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Container;

import javax.swing.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link ContactScreen}.
 * <p>
 * Ελέγχει την ορθή εμφάνιση της οθόνης επικοινωνίας.
 * Βεβαιώνει ότι οι πληροφορίες επικοινωνίας εμφανίζονται σε μη επεξεργάσιμη περιοχή κειμένου
 * και ότι παρέχεται λειτουργικότητα επιστροφής στην προηγούμενη οθόνη.
 * </p>
 */
class ContactScreenTest {

    private ContactScreen contactScreen;
    private AppController controller;

    /**
     * Αρχικοποίηση πριν από κάθε test.
     * <p>
     * Δημιουργεί τον ελεγκτή (σε κρυφή κατάσταση) και την οθόνη επικοινωνίας.
     * </p>
     */
    @BeforeEach
    void setUp() {
        controller = new AppController();
        controller.setVisible(false);

        contactScreen = new ContactScreen(controller);
    }

    /**
     * Ελέγχει τη σωστή αρχικοποίηση της οθόνης.
     * <p>
     * Επιβεβαιώνει ότι το αντικείμενο δημιουργήθηκε και έχει τη σωστή διάταξη (BorderLayout).
     * </p>
     */
    @Test
    void testContactScreenInitialization() {
        assertNotNull(contactScreen);
        assertTrue(contactScreen.getLayout() instanceof java.awt.BorderLayout);
    }

    /**
     * Ελέγχει την περιοχή κειμένου με τα στοιχεία επικοινωνίας.
     * <p>
     * Επαληθεύει ότι:
     * 1. Υπάρχει {@link JScrollPane} (για κύλιση κειμένου).
     * 2. Μέσα του υπάρχει {@link JTextArea}.
     * 3. Το κείμενο δεν είναι επεξεργάσιμο (read-only).
     * 4. Το κείμενο περιέχει τη λέξη "Email" (ένδειξη ότι φορτώθηκε το σωστό περιεχόμενο).
     * </p>
     */
    @Test
    void testContainsTextAreaInsideScrollPane() {
        boolean foundScrollPane = false;

        for (var component : contactScreen.getComponents()) {
            if (component instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) component;
                if (scroll.getViewport().getView() instanceof JTextArea) {
                    
                    JTextArea textArea = (JTextArea) scroll.getViewport().getView();
                    assertFalse(textArea.isEditable(), "Το κείμενο δεν πρέπει να αλλάζει από τον χρήστη");
                    assertTrue(textArea.getText().contains("Email"), "Πρέπει να περιέχει πληροφορίες email");

                    foundScrollPane = true;
                }
            }
        }

        assertTrue(foundScrollPane, "Δεν βρέθηκε JScrollPane με JTextArea");
    }

    /**
     * Ελέγχει την ύπαρξη του κουμπιού επιστροφής ("Back").
     * <p>
     * Χρησιμοποιεί την αναδρομική μέθοδο {@code findButtonByText} για να εντοπίσει το κουμπί.
     * </p>
     */
    @Test
    void testBackButtonExists() {
        JButton backButton = findButtonByText((Container)contactScreen, "Back");
        assertNotNull(backButton);
    }

    /**
     * Ελέγχει τη λειτουργικότητα του κουμπιού επιστροφής.
     * <p>
     * Προσομοιώνει το πάτημα (click) και επιβεβαιώνει ότι δεν προκαλείται εξαίρεση.
     * </p>
     */
    @Test
    void testBackButtonActionDoesNotThrow() {
        JButton backButton = findButtonByText(contactScreen, "Back");

        assertNotNull(backButton);
        assertDoesNotThrow(() -> backButton.doClick());
    }


    /**
     * Βοηθητική μέθοδος για την εύρεση κουμπιού με βάση το κείμενο.
     * <p>
     * Ψάχνει αναδρομικά μέσα σε όλα τα υπο-δοχεία (containers) της οθόνης.
     * </p>
     *
     * @param root Το στοιχείο (Container) από το οποίο ξεκινά η αναζήτηση.
     * @param text Το κείμενο του κουμπιού που ψάχνουμε.
     * @return Το κουμπί αν βρεθεί, αλλιώς null.
     */
    private JButton findButtonByText(Container root, String text) {
        for (var component : root.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (text.equals(button.getText())) {
                    return button;
                }
            }
            if (component instanceof Container) {
                JButton found = findButtonByText((Container) component, text);
                if (found != null) return found;
            }
        }
        return null;
    }
}
