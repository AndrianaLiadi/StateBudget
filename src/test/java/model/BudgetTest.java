package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link Budget}.
 * <p>
 * Ελέγχει τη λειτουργικότητα του αντικειμένου Budget, συμπεριλαμβανομένων:
 * <ul>
 * <li>Του υπολογισμού συνολικών εσόδων και εξόδων.</li>
 * <li>Του υπολογισμού του δημοσιονομικού αποτελέσματος (surplus/deficit).</li>
 * <li>Της αναζήτησης και φιλτραρίσματος κονδυλίων.</li>
 * <li>Της σωστής κλωνοποίησης (Deep Copy) του προϋπολογισμού.</li>
 * </ul>
 * </p>
 */
class BudgetTest {

    private Budget budget;
    private List<BudgetItem> items;

    /**
     * Αρχικοποίηση δεδομένων πριν από κάθε έλεγχο.
     * <p>
     * Δημιουργεί μια λίστα με 4 κονδύλια (2 έσοδα, 2 έξοδα) και έναν προϋπολογισμό
     * για το έτος 2023, ώστε να υπάρχουν γνωστά δεδομένα για τους υπολογισμούς.
     * </p>
     */
    @BeforeEach
    void setUp() {
        items = new ArrayList<>();
        items.add(new BudgetItem("R01", "Income 1", "REVENUE", 1000));
        items.add(new BudgetItem("R02", "Income 2", "REVENUE", 500));
        items.add(new BudgetItem("E01", "Expense 1", "EXPENDITURE", 800));
        items.add(new BudgetItem("E02", "Expense 2", "EXPENDITURE", 200));

        budget = new Budget(2023, items);
    }

    /**
     * Ελέγχει τον υπολογισμό του συνόλου των εσόδων (Total Revenue).
     * <p>
     * Αναμένεται: 1000 + 500 = 1500.
     * </p>
     */
    @Test
    void testTotalRevenue() {
        assertEquals(1500, budget.totalRevenue());
    }

    /**
     * Ελέγχει τον υπολογισμό του συνόλου των εξόδων (Total Expenditure).
     * <p>
     * Αναμένεται: 800 + 200 = 1000.
     * </p>
     */
    @Test
    void testTotalExpenditure() {
        assertEquals(1000, budget.totalExpenditure());
    }

    /**
     * Ελέγχει τη μέθοδο υπολογισμού του αποτελέσματος (Πλεόνασμα/Έλλειμμα).
     * <p>
     * Επαληθεύει ότι η μέθοδος επιστρέφει τη σωστή διαφορά μεταξύ εσόδων και εξόδων.
     * </p>
     */
    @Test
    void testSurplusdeficitFinder() {
        double rev = budget.totalRevenue();
        double exp = budget.totalExpenditure();
        
        // 1500 - 1000 = 500
        assertEquals(500, budget.surplusdeficitFinder(rev, exp));
        
        // Έλεγχος με hardcoded τιμές
        assertEquals(500, budget.surplusdeficitFinder(1000, 1500));
    }

    /**
     * Ελέγχει τη λειτουργία φιλτραρίσματος των κονδυλίων ανά τύπο.
     * <p>
     * Επιβεβαιώνει ότι επιστρέφονται μόνο τα κονδύλια του ζητούμενου τύπου
     * και ότι επιστρέφεται κενή λίστα αν ο τύπος δεν υπάρχει.
     * </p>
     */
    @Test
    void testGetItemsByType() {
        List<BudgetItem> revenues = budget.getItemsByType("REVENUE");
        assertEquals(2, revenues.size());
        
        List<BudgetItem> none = budget.getItemsByType("NON_EXISTENT");
        assertTrue(none.isEmpty());
    }

    /**
     * Ελέγχει την αναζήτηση συγκεκριμένου κονδυλίου με βάση τον κωδικό του.
     * <p>
     * Επιβεβαιώνει ότι επιστρέφεται το σωστό αντικείμενο αν ο κωδικός υπάρχει,
     * και null αν ο κωδικός δεν βρεθεί.
     * </p>
     */
    @Test
    void testGetItemByCode() {
        BudgetItem item = budget.getItemByCode("R01");
        assertNotNull(item);
        assertEquals("REVENUE", item.getType());
        
        assertNull(budget.getItemByCode("UNKNOWN"));
    }

    /**
     * Ελέγχει τη μέθοδο κλωνοποίησης (clone).
     * <p>
     * Βεβαιώνει ότι δημιουργείται ένα πλήρως ανεξάρτητο αντίγραφο (Deep Copy)
     * του προϋπολογισμού και των κονδυλίων του, ώστε αλλαγές στον κλώνο
     * να μην επηρεάζουν το πρωτότυπο.
     * </p>
     */
    @Test
    void testClone() {
        Budget clonedBudget = budget.clone();

        assertNotNull(clonedBudget);
        assertNotSame(budget, clonedBudget); // Όχι το ίδιο object reference
        assertEquals(budget.getYear(), clonedBudget.getYear());
        assertEquals(budget.getItems().size(), clonedBudget.getItems().size());
        
        // Έλεγχος ότι και τα items μέσα στη λίστα είναι διαφορετικά objects (Deep Copy)
        assertNotSame(budget.getItems().get(0), clonedBudget.getItems().get(0));
    }
}