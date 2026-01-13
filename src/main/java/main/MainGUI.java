package main;

import ui.AppController;
import javax.swing.SwingUtilities;

/**
 * Η κλάση {@code MainGUI} αποτελεί το σημείο εισόδου (entry point) για την έκδοση της εφαρμογής
 * με Γραφική Διεπαφή Χρήστη (GUI).
 * <p>
 * Είναι υπεύθυνη για την ασφαλή εκκίνηση του νήματος της διεπαφής (Swing Event Dispatch Thread)
 * και την αρχικοποίηση του κεντρικού ελεγκτή της εφαρμογής.
 * </p>
 */
public class MainGUI {

    /**
     * Η κύρια μέθοδος που εκκινεί το γραφικό περιβάλλον της εφαρμογής.
     * <p>
     * Χρησιμοποιεί την {@link SwingUtilities#invokeLater(Runnable)} για να διασφαλίσει
     * ότι η δημιουργία και η ενημέρωση των στοιχείων του Swing γίνονται στο
     * Event Dispatch Thread (EDT), αποτρέποντας προβλήματα συγχρονισμού νημάτων.
     * </p>
     *
     * @param args Τα ορίσματα γραμμής εντολών (δεν χρησιμοποιούνται στην παρούσα έκδοση).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppController app = new AppController();
            app.setVisible(true);
        });
    }
}