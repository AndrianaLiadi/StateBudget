package ui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link AboutScreen}.
 * <p>
 * Ελέγχει τη σωστή αρχικοποίηση και δομή της οθόνης "About".
 * Επιβεβαιώνει ότι τα απαραίτητα γραφικά στοιχεία (όπως η περιοχή κειμένου και το κουμπί επιστροφής)
 * υπάρχουν, έχουν τις σωστές ιδιότητες και λειτουργούν χωρίς να προκαλούν σφάλματα.
 * </p>
 */
class AboutScreenTest {

    private AboutScreen aboutScreen;
    private AppController controller;

    /**
     * Αρχικοποίηση πριν από κάθε test.
     * <p>
     * Δημιουργεί έναν εικονικό AppController (κρυφό) και την οθόνη AboutScreen,
     * ώστε να είναι έτοιμα για έλεγχο.
     * </p>
     */
    @BeforeEach
    void setUp() {
        controller = new AppController();
        controller.setVisible(false); // Δεν χρειάζεται να εμφανιστεί το παράθυρο κατά το test

        aboutScreen = new AboutScreen(controller);
    }

    /**
     * Ελέγχει τη βασική αρχικοποίηση της οθόνης.
     * <p>
     * Επιβεβαιώνει ότι το αντικείμενο δεν είναι null και ότι χρησιμοποιεί
     * τη σωστή διάταξη (BorderLayout), όπως αναμένεται από τον σχεδιασμό.
     * </p>
     */
    @Test
    void testAboutScreenInitialization() {
        assertNotNull(aboutScreen);
        assertTrue(aboutScreen.getLayout() instanceof java.awt.BorderLayout);
    }

    /**
     * Ελέγχει την ύπαρξη και τις ιδιότητες της περιοχής κειμένου.
     * <p>
     * Ψάχνει μέσα στα συστατικά της οθόνης για να βρει ένα {@link JScrollPane}
     * που περιέχει ένα {@link JTextArea}. Ελέγχει επίσης ότι το κείμενο:
     * <ul>
     * <li>Δεν είναι επεξεργάσιμο (read-only).</li>
     * <li>Δεν είναι κενό.</li>
     * </ul>
     * </p>
     */
    @Test
    void testContainsTextAreaInsideScrollPane() {
        boolean foundScrollPane = false;

        for (var component : aboutScreen.getComponents()) {
            if (component instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) component;
                if (scroll.getViewport().getView() instanceof JTextArea) {
                    JTextArea textArea = (JTextArea) scroll.getViewport().getView();
                    
                    assertFalse(textArea.isEditable(), "Το κείμενο δεν πρέπει να είναι επεξεργάσιμο");
                    assertTrue(textArea.getText().length() > 0, "Το κείμενο δεν πρέπει να είναι κενό");

                    foundScrollPane = true;
                }
            }
        }

        assertTrue(foundScrollPane, "Δεν βρέθηκε JScrollPane με JTextArea");
    }

    /**
     * Ελέγχει την ύπαρξη του κουμπιού επιστροφής ("Back").
     * <p>
     * Σαρώνει τα συστατικά της οθόνης αναζητώντας ένα κουμπί με το κείμενο "Back".
     * </p>
     */
    @Test
    void testBackButtonExists() {
        boolean foundButton = false;

        for (var component : aboutScreen.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if ("Back".equals(button.getText())) {
                    foundButton = true;
                }
            }
        }

        assertTrue(foundButton, "Δεν βρέθηκε κουμπί Back");
    }

    /**
     * Ελέγχει τη λειτουργικότητα του κουμπιού επιστροφής.
     * <p>
     * Εντοπίζει το κουμπί και προσομοιώνει ένα κλικ (doClick).
     * Επιβεβαιώνει ότι η ενέργεια εκτελείται χωρίς να πετάξει κάποιο Exception.
     * </p>
     */
    @Test
    void testBackButtonActionDoesNotThrow() {
        JButton backButton = null;

        for (var component : aboutScreen.getComponents()) {
            if (component instanceof JButton) {
                backButton = (JButton) component;
                break;
            }
        }

        assertNotNull(backButton, "Το κουμπί πρέπει να υπάρχει για να πατηθεί");

        JButton finalBackButton = backButton;
        assertDoesNotThrow(() -> finalBackButton.doClick());
    }
}