package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link BudgetChange}.
 * <p>
 * Ελέγχει την ορθή αποθήκευση των δεδομένων μιας αλλαγής και την ακρίβεια των
 * υπολογισμών που προκύπτουν από αυτήν (διαφορά ποσού, ποσοστιαία μεταβολή).
 * </p>
 */
public class BudgetChangeTest {

    /**
     * Ελέγχει αν ο κατασκευαστής (constructor) αρχικοποιεί σωστά όλα τα πεδία
     * και αν οι getters επιστρέφουν τις αναμενόμενες τιμές.
     */
    @Test
    public void testBasicFields() {
        BudgetChange bc = new BudgetChange("A1", "Test Item", 100, 150, "Expense");
        
        assertEquals("A1", bc.getItemCode());
        assertEquals("Test Item", bc.getItemName());
        assertEquals("Expense", bc.getType());
        assertEquals(100, bc.getOldValue());
        assertEquals(150, bc.getNewValue());
    }

    /**
     * Ελέγχει τον υπολογισμό της απόλυτης διαφοράς (New Value - Old Value).
     * <p>
     * Εξετάζει δύο περιπτώσεις:
     * 1. Αύξηση ποσού (θετική διαφορά).
     * 2. Μείωση ποσού (αρνητική διαφορά).
     * </p>
     */
    @Test
    public void testCalcDifference() {
        BudgetChange bc1 = new BudgetChange("C1", "Item", 200, 300, "Type");
        assertEquals(100, bc1.getDifference());

        BudgetChange bc2 = new BudgetChange("C2", "Item", 500, 200, "Type");
        assertEquals(-300, bc2.getDifference());
    }

    /**
     * Ελέγχει τον υπολογισμό της ποσοστιαίας μεταβολής.
     * <p>
     * Βεβαιώνει ότι το αποτέλεσμα είναι σωστό τόσο για θετικές όσο και για
     * αρνητικές αλλαγές (π.χ. αύξηση 50%, μείωση 50%).
     * </p>
     */
    @Test
    public void testPercents() {
        BudgetChange bc = new BudgetChange("P1", "Item", 100, 150, "T");
        assertEquals(50.0, bc.getPercentageChange());

        BudgetChange bc2 = new BudgetChange("P2", "Item", 100, 50, "T");
        assertEquals(-50.0, bc2.getPercentageChange());
    }

    /**
     * Ελέγχει ακραίες περιπτώσεις (Edge Cases) όπου η αρχική τιμή είναι μηδέν.
     * <p>
     * Αυτό είναι κρίσιμο για την αποφυγή σφαλμάτων διαίρεσης με το μηδέν (ArithmeticException)
     * και για να διασφαλιστεί ότι η λογική του προγράμματος επιστρέφει λογικά αποτελέσματα
     * (π.χ. 100% αύξηση αν πάμε από 0 σε 50).
     * </p>
     */
    @Test
    public void testZeroValueCases() {
        BudgetChange zeroOld = new BudgetChange("Z1", "Item", 0, 50, "T");
        assertEquals(100.0, zeroOld.getPercentageChange());

        BudgetChange bothZero = new BudgetChange("Z2", "Item", 0, 0, "T");
        assertEquals(0.0, bothZero.getPercentageChange());
    }

    /**
     * Ελέγχει τη λειτουργία ενημέρωσης της νέας τιμής (setter).
     * <p>
     * Επιβεβαιώνει ότι όταν αλλάζουμε τη "νέα τιμή" μέσω του {@code setNewValue},
     * οι υπολογισμοί της διαφοράς και του ποσοστού ενημερώνονται δυναμικά
     * και αντικατοπτρίζουν τη νέα κατάσταση.
     * </p>
     */
    @Test
    public void testUpdateValue() {
        BudgetChange bc = new BudgetChange("U1", "Item", 100, 150, "T");
        bc.setNewValue(200);
        
        assertEquals(200, bc.getNewValue());
        assertEquals(100, bc.getDifference());
        assertEquals(100.0, bc.getPercentageChange());
    }
}