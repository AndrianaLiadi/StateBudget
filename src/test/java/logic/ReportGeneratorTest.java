package logic;

import model.BudgetChange;
import model.Scenario;
import model.Budget;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link ReportGenerator}.
 * <p>
 * Ελέγχει τη λειτουργικότητα παραγωγής αναφορών, επιβεβαιώνοντας ότι:
 * <ul>
 * <li>Δημιουργούνται σωστά τα κείμενα σύνοψης (summary) για κενά και γεμάτα σενάρια.</li>
 * <li>Η διαδικασία εξαγωγής σε αρχείο (Export) ολοκληρώνεται χωρίς σφάλματα και παράγει το αρχείο.</li>
 * </ul>
 * </p>
 */
class ReportGeneratorTest {

    private ReportGenerator generator;
    private Scenario scenario;

    /**
     * Αρχικοποίηση πριν από κάθε test.
     * <p>
     * Δημιουργεί ένα νέο instance του generator και ένα βασικό σενάριο "Test Scenario"
     * με κενό προϋπολογισμό, ώστε να είναι έτοιμα για χρήση.
     * </p>
     */
    @BeforeEach
    void setUp() {
        generator = new ReportGenerator();
        Budget base = new Budget(2025, List.of());
        scenario = new Scenario(base, "Test Scenario");
    }

    /**
     * Ελέγχει τη δημιουργία σύνοψης όταν δεν υπάρχουν αλλαγές στο σενάριο.
     * <p>
     * Αναμένουμε το κείμενο να μην είναι null και να περιέχει το μήνυμα
     * "Δεν βρέθηκαν αλλαγές".
     * </p>
     */
    @Test
    void testGenerateSummaryWithNoChanges() {
        String summary = generator.generateSummary(scenario, List.of());

        assertNotNull(summary);
        assertTrue(summary.contains("Δεν βρέθηκαν αλλαγές"));
        assertTrue(summary.contains("Καθαρή Επίπτωση"));
    }

    /**
     * Ελέγχει τη δημιουργία σύνοψης όταν υπάρχουν συγκεκριμένες αλλαγές.
     * <p>
     * Δημιουργεί εικονικές αλλαγές (Μια αύξηση Εσόδων και μια μείωση Εξόδων) και
     * επαληθεύει ότι τα ονόματα των κονδυλίων και τα ποσά των διαφορών (+500, -200)
     * εμφανίζονται σωστά στο παραγόμενο κείμενο.
     * </p>
     */
    @Test
    void testGenerateSummaryWithChanges() {
        BudgetChange revChange = new BudgetChange("R1", "Revenue Item", 1000, 1500, "REVENUE");
        BudgetChange expChange = new BudgetChange("E1", "Expenditure Item", 2000, 1800, "EXPENDITURE");

        List<BudgetChange> changes = List.of(revChange, expChange);

        String summary = generator.generateSummary(scenario, changes);

        assertNotNull(summary);
        assertTrue(summary.contains("Revenue Item"));
        assertTrue(summary.contains("Expenditure Item"));
        assertTrue(summary.contains("Καθαρή Επίπτωση"));
        // 1500 - 1000 = +500
        assertTrue(summary.contains("+500"));
        // 1800 - 2000 = -200
        assertTrue(summary.contains("-200"));
    }

    /**
     * Ελέγχει τη λειτουργία εξαγωγής αρχείου (Export).
     * <p>
     * Επιβεβαιώνει ότι η μέθοδος {@code exportToPDF} δεν προκαλεί εξαίρεση (Exception)
     * και ότι το αρχείο δημιουργείται επιτυχώς στον δίσκο (ως .txt σε αυτή την υλοποίηση).
     * Στο τέλος, διαγράφει το αρχείο για να καθαρίσει το περιβάλλον.
     * </p>
     */
    @Test
    void testExportToPDFDoesNotThrow() {
        String content = "Test report content";

        // Έλεγχος ότι δεν "σκάει" η μέθοδος
        assertDoesNotThrow(() -> {
            generator.exportToPDF(scenario, content);
        });

        // Έλεγχος ότι το αρχείο δημιουργήθηκε πράγματι
        File file = new File("Report_" + scenario.getitemName().replaceAll("\\s+", "_") + ".txt");
        assertTrue(file.exists());

        // Διαγραφή του αρχείου test (Cleanup)
        file.delete();
    }
}