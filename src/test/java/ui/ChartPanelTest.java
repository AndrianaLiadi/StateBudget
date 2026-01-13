package ui;

import static org.junit.jupiter.api.Assertions.*;

import model.Budget;
import model.BudgetItem;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link ChartPanel}.
 * <p>
 * Ελέγχει την ορθή λειτουργία του γραφικού συστατικού των διαγραμμάτων.
 * Επειδή η μέθοδος {@code paintComponent} είναι πολύπλοκη, τα tests εστιάζουν
 * στο να βεβαιώσουν ότι η διαδικασία σχεδίασης ολοκληρώνεται χωρίς σφάλματα (Exceptions)
 * για διάφορα σενάρια δεδομένων.
 * </p>
 */
class ChartPanelTest {

    /**
     * Ελέγχει την αρχικοποίηση του Panel.
     * <p>
     * Επιβεβαιώνει ότι το αντικείμενο δημιουργείται σωστά, έχει λευκό φόντο
     * και τις καθορισμένες διαστάσεις (PreferredSize).
     * </p>
     */
    @Test
    void testChartPanelInitialization() {
        Budget base = new Budget(2025, List.of());
        Budget modified = new Budget(2025, List.of());

        ChartPanel panel = new ChartPanel(base, modified);

        assertNotNull(panel);
        assertEquals(Color.WHITE, panel.getBackground());
        // Σημείωση: Βεβαιώσου ότι αυτές οι διαστάσεις ταιριάζουν με αυτές στην κλάση ChartPanel
        assertEquals(new Dimension(650, 420), panel.getPreferredSize());
    }

    /**
     * Ελέγχει τη συμπεριφορά σχεδίασης όταν οι προϋπολογισμοί είναι null.
     * <p>
     * Αναμένεται να μην κρασάρει η εφαρμογή, αλλά να εμφανίσει μήνυμα (εσωτερικά στη paint).
     * </p>
     */
    @Test
    void testPaintComponentWithNullBudgetsDoesNotThrow() {
        ChartPanel panel = new ChartPanel(null, null);

        assertDoesNotThrow(() -> paint(panel));
    }

    /**
     * Ελέγχει τη συμπεριφορά σχεδίασης όταν οι προϋπολογισμοί είναι άδειοι (χωρίς κονδύλια).
     */
    @Test
    void testPaintComponentWithEmptyBudgetsDoesNotThrow() {
        Budget base = new Budget(2025, List.of());
        Budget modified = new Budget(2025, List.of());

        ChartPanel panel = new ChartPanel(base, modified);

        assertDoesNotThrow(() -> paint(panel));
    }

    /**
     * Ελέγχει τη συμπεριφορά σχεδίασης με κανονικά δεδομένα.
     * <p>
     * Δημιουργεί εικονικά δεδομένα εσόδων και εξόδων και επιβεβαιώνει ότι
     * η μέθοδος paint εκτελείται επιτυχώς.
     * </p>
     */
    @Test
    void testPaintComponentWithDataDoesNotThrow() {
        BudgetItem b1 = new BudgetItem("A1", "Test A1", "Income", 1_000);
        BudgetItem b2 = new BudgetItem("B1", "Test B1", "Expense", 2_500);

        Budget base = new Budget(2025, List.of(b1));
        Budget modified = new Budget(2025, List.of(b2));

        ChartPanel panel = new ChartPanel(base, modified);

        assertDoesNotThrow(() -> paint(panel));
    }

    /**
     * Ελέγχει τη συμπεριφορά όταν υπάρχουν διπλότυποι κωδικοί (Aggregation logic).
     * <p>
     * Η κλάση ChartPanel θα πρέπει να αθροίσει τα ποσά για τον ίδιο κωδικό
     * και να μην προκαλέσει σφάλμα κατά τη σχεδίαση.
     * </p>
     */
    @Test
    void testPaintComponentWithDuplicateCodesDoesNotThrow() {
        BudgetItem b1 = new BudgetItem("X", "Item 1", "Icome", 100);
        BudgetItem b2 = new BudgetItem("X", "Item 2", "Income", 200);

        Budget base = new Budget(2025, List.of(b1, b2));
        Budget modified = new Budget(2025, List.of(b1));

        ChartPanel panel = new ChartPanel(base, modified);

        assertDoesNotThrow(() -> paint(panel));
    }


    /**
     * Βοηθητική μέθοδος που προσομοιώνει τη διαδικασία σχεδίασης (Painting).
     * <p>
     * Επειδή τα tests τρέχουν χωρίς οθόνη (headless), δημιουργούμε μια εικονική
     * εικόνα στη μνήμη (BufferedImage) και ζητάμε από το panel να ζωγραφίσει πάνω της.
     * Αν υπάρχει λάθος στον κώδικα σχεδίασης (π.χ. NullPointerException), θα φανεί εδώ.
     * </p>
     *
     * @param panel Το panel που θα τεσταριστεί.
     */
    private void paint(JPanel panel) {
        panel.setSize(700, 500); // Ορισμός μεγέθους για να έχει χώρο να ζωγραφίσει

        BufferedImage image = new BufferedImage(
                panel.getWidth(),
                panel.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = image.createGraphics();
        panel.paint(g2); // Κλήση της μεθόδου ζωγραφικής
        g2.dispose();
    }
}