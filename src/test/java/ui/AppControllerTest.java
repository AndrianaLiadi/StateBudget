package ui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link AppController}.
 * <p>
 * Ελέγχει τον κεντρικό ελεγκτή της διεπαφής χρήστη.
 * Επειδή το AppController είναι κλάση Swing (κληρονομεί από JFrame), όλα τα tests
 * πρέπει να τρέχουν εντός του Swing Event Dispatch Thread (EDT) για να αποφευχθούν
 * προβλήματα συγχρονισμού (concurrency issues). Γι' αυτό χρησιμοποιείται η
 * {@code SwingUtilities.invokeAndWait}.
 * </p>
 */
class AppControllerTest {

    private AppController app;

    /**
     * Αρχικοποίηση πριν από κάθε test.
     * <p>
     * Δημιουργεί ένα νέο στιγμιότυπο του {@link AppController} μέσα στο EDT.
     * Το παράθυρο ορίζεται ως μη ορατό (setVisible(false)) για να μην πετάγονται
     * παράθυρα στην οθόνη κατά την εκτέλεση των tests.
     * </p>
     */
    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            app = new AppController();
            app.setVisible(false);
        });
    }

    /**
     * Καθαρισμός μετά από κάθε test.
     * <p>
     * Καλεί την {@code dispose()} για να καταστρέψει το παράθυρο και να ελευθερώσει
     * τους πόρους του συστήματος.
     * </p>
     */
    @AfterEach
    void tearDown() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            if (app != null) {
                app.dispose();
            }
        });
    }

    /**
     * Ελέγχει ότι ο AppController αρχικοποιείται σωστά.
     * <p>
     * Επιβεβαιώνει ότι το αντικείμενο δεν είναι null και ότι οι βασικές ιδιότητες
     * του παραθύρου (τίτλος, διαστάσεις) έχουν τις αναμενόμενες τιμές.
     * </p>
     */
    @Test
    void testAppControllerInitialization() {
        assertNotNull(app);
        assertEquals("State Budget App", app.getTitle());
        assertEquals(700, app.getWidth());
        assertEquals(500, app.getHeight());
    }

    /**
     * Ελέγχει την πλοήγηση στην αρχική οθόνη (HOME).
     * <p>
     * Επιβεβαιώνει ότι η μέθοδος {@code showScreen} εκτελείται χωρίς σφάλματα.
     * </p>
     */
    @Test
    void testShowHomeScreenDoesNotThrow() {
        assertDoesNotThrow(() -> {
            SwingUtilities.invokeAndWait(() -> {
                app.showScreen(AppController.HOME);
            });
        });
    }

    /**
     * Ελέγχει την πλοήγηση στην οθόνη του προϋπολογισμού (BUDGET).
     */
    @Test
    void testShowBudgetScreenDoesNotThrow() {
        assertDoesNotThrow(() -> {
            SwingUtilities.invokeAndWait(() -> {
                app.showScreen(AppController.BUDGET);
            });
        });
    }

    /**
     * Ελέγχει την πλοήγηση στην οθόνη των σεναρίων (SCENARIO).
     */
    @Test
    void testShowScenarioScreenDoesNotThrow() {
        assertDoesNotThrow(() -> {
            SwingUtilities.invokeAndWait(() -> {
                app.showScreen(AppController.SCENARIO);
            });
        });
    }

    /**
     * Ελέγχει την εμφάνιση της οθόνης αναφορών (ReportScreen) όταν δεν υπάρχει σενάριο.
     * <p>
     * Περνάμε {@code null} ως σενάριο για να βεβαιωθούμε ότι η εφαρμογή το διαχειρίζεται
     * σωστά και δεν καταρρέει (αναμένεται να εμφανίσει κατάλληλο μήνυμα στο UI).
     * </p>
     */
    @Test
    void testShowReportScreenWithNullScenario() {
        assertDoesNotThrow(() -> {
            SwingUtilities.invokeAndWait(() -> {
                app.showReportScreen(null);
            });
        });
    }
}