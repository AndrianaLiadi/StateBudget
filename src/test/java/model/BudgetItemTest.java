package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link BudgetItem}.
 * <p>
 * Ελέγχει τη συμπεριφορά των αντικειμένων του προϋπολογισμού (κονδύλια).
 * Καλύπτει τη δημιουργία, τη διαχείριση της ιεραρχίας (γονέας-παιδί),
 * τον υπολογισμό των συνολικών ποσών και τη διαδικασία κλωνοποίησης (Deep Copy).
 * </p>
 */
class BudgetItemTest {       

    private BudgetItem parent;
    private BudgetItem child1;
    private BudgetItem child2;

    /**
     * Αρχικοποίηση δεδομένων πριν από κάθε έλεγχο.
     * <p>
     * Δημιουργεί ένα γονικό κονδύλιο και δύο παιδικά κονδύλια με προκαθορισμένες
     * τιμές, ώστε να χρησιμοποιηθούν στα tests.
     * </p>
     */
    @BeforeEach
    void setUp() {
        parent = new BudgetItem("P01", "Parent", "EXPENDITURE", 1000);
        child1 = new BudgetItem("C01", "Child 1", "EXPENDITURE", 500);
        child2 = new BudgetItem("C02", "Child 2", "EXPENDITURE", 300);
    }

    /**
     * Ελέγχει τον κατασκευαστή και τις μεθόδους πρόσβασης (Getters).
     * <p>
     * Επιβεβαιώνει ότι το αντικείμενο αρχικοποιείται σωστά με τα ορίσματα που δίνονται
     * και ότι η λίστα υπο-στοιχείων είναι αρχικά κενή.
     * </p>
     */
    @Test
    void testConstructorAndGetters() {
        assertEquals("P01", parent.getCode());
        assertEquals("Parent", parent.getName());
        assertEquals("EXPENDITURE", parent.getType());
        assertEquals(1000, parent.getAmount());
        assertTrue(parent.getSubItems().isEmpty());
    }

    /**
     * Ελέγχει την προσθήκη υποκατηγορίας (Sub-item).
     * <p>
     * Επιβεβαιώνει ότι:
     * 1. Το παιδί προστίθεται επιτυχώς στη λίστα του γονέα.
     * 2. Ορίζεται σωστά η αναφορά του γονέα μέσα στο αντικείμενο του παιδιού.
     * </p>
     */
    @Test
    void testAddSubItem() {
        parent.addSubItem(child1);
        assertEquals(1, parent.getSubItems().size());
        assertEquals(parent, child1.getParentCategory());
    }

    /**
     * Ελέγχει τη δυνατότητα ενημέρωσης του ποσού.
     */
    @Test
    void testUpdateAmount() {
        parent.updateAmount(2000);
        assertEquals(2000, parent.getAmount());
    }

    /**
     * Ελέγχει τον υπολογισμό του συνολικού ποσού (getTotal).
     * <p>
     * Η μέθοδος getTotal αναμένεται να αθροίζει το ποσό του ίδιου του κονδυλίου
     * συν τα ποσά όλων των άμεσων υποκατηγοριών του.
     * (Εδώ: 1000 + 500 + 300 = 1800).
     * </p>
     */
    @Test
    void testGetTotal() {
        parent.addSubItem(child1);
        parent.addSubItem(child2);
        assertEquals(1800.0, parent.getTotal());
    }

    /**
     * Ελέγχει τον υπολογισμό του συνόλου όταν δεν υπάρχουν υποκατηγορίες.
     * <p>
     * Σε αυτή την περίπτωση, το getTotal πρέπει να είναι ίσο με το getAmount.
     * </p>
     */
    @Test
    void testGetTotalNoChildren() {
        assertEquals(1000.0, parent.getTotal());
    }

    /**
     * Ελέγχει τη μέθοδο κλωνοποίησης (clone).
     * <p>
     * Αυτό είναι ένα κρίσιμο test για το Deep Copy. Επιβεβαιώνει ότι:
     * <ul>
     * <li>Δημιουργείται νέο αντικείμενο στη μνήμη (όχι απλή αναφορά).</li>
     * <li>Τα δεδομένα (ποσά) αντιγράφονται σωστά.</li>
     * <li>Η λίστα των παιδιών είναι νέα λίστα (όχι αναφορά στην παλιά).</li>
     * <li>Τα ίδια τα παιδιά έχουν κλωνοποιηθεί αναδρομικά και δείχνουν στον νέο γονέα.</li>
     * </ul>
     * </p>
     */
    @Test
    void testClone() {
        parent.addSubItem(child1);
        BudgetItem cloned = parent.clone();

        assertNotNull(cloned);
        assertNotSame(parent, cloned); // Διαφορετικά αντικείμενα στη μνήμη
        assertNotSame(parent.getSubItems(), cloned.getSubItems()); // Διαφορετικές λίστες
        assertEquals(parent.getAmount(), cloned.getAmount()); // Ίδιες τιμές
        assertEquals(parent.getSubItems().size(), cloned.getSubItems().size());
        
        // Έλεγχος ότι και το παιδί κλωνοποιήθηκε σωστά (Deep Copy)
        BudgetItem clonedChild = cloned.getSubItems().get(0);
        assertNotSame(child1, clonedChild);
        assertEquals(cloned, clonedChild.getParentCategory()); // Ο γονέας του κλωνοποιημένου παιδιού είναι ο κλωνοποιημένος γονέας
    }
}