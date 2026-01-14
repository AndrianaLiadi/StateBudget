package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link Scenario}.
 * <p>
 * Ελέγχει τη διαχείριση σεναρίων, συμπεριλαμβανομένης της αρχικοποίησης,
 * της εφαρμογής αλλαγών στον προϋπολογισμό (τροποποίηση υπαρχόντων ή προσθήκη νέων)
 * και της παραγωγής κειμενικών συνόψεων.
 * </p>
 */
public class ScenarioTest {

    /**
     * Ελέγχει την ορθή αρχικοποίηση ενός σεναρίου.
     * <p>
     * Επιβεβαιώνει ότι το όνομα αποθηκεύεται σωστά και ότι η λίστα των αλλαγών
     * αρχικοποιείται κενή (αλλά όχι null), ώστε να είναι έτοιμη για προσθήκες.
     * </p>
     */
    @Test
    public void checkInitialState() {
        List<BudgetItem> items = new ArrayList<>();
        items.add(new BudgetItem("CODE1", "Item 1", "Type A", (long)100.0));
        Budget b = new Budget(2024, items);
        
        Scenario s = new Scenario(b, "First Scenario");

        assertEquals("First Scenario", s.getitemName());
        assertNotNull(s.getChanges());
        assertEquals(0, s.getChanges().size());
    }

    /**
     * Ελέγχει τη λογική εφαρμογής αλλαγών σε υπάρχοντα κονδύλια.
     * <p>
     * Δημιουργεί ένα σενάριο αλλαγής ποσού (από 500 σε 600) και επιβεβαιώνει
     * ότι μετά την κλήση της {@code applyChanges}, ο τροποποιημένος προϋπολογισμός
     * περιέχει τη νέα τιμή.
     * </p>
     */
    @Test
    public void checkApplyChangesLogic() {
        List<BudgetItem> items = new ArrayList<>();
        items.add(new BudgetItem("10", "Salary", "Expense", (long)500.0));
        Budget base = new Budget(2024, items);
        Scenario scen = new Scenario(base, "Test");

        List<BudgetChange> changeList = new ArrayList<>();
        changeList.add(new BudgetChange("10", "Salary", (long)500.0, (long)600.0, "expense"));
        scen.setChanges(changeList);

        scen.applyChanges();

        Budget mod = scen.getModifiedBudget();
        assertEquals(1, mod.getItems().size());
        assertEquals(600.0, mod.getItems().get(0).getAmount());
    }

    /**
     * Ελέγχει την προσθήκη νέου κονδυλίου μέσω σεναρίου.
     * <p>
     * Επιβεβαιώνει ότι αν ορίσουμε μια αλλαγή για κωδικό που δεν υπάρχει
     * στον βασικό προϋπολογισμό, το σύστημα τον προσθέτει ως νέο κονδύλιο
     * στον τροποποιημένο προϋπολογισμό.
     * </p>
     */
    @Test
    public void testAdditionOfNewItem() {
        Budget base = new Budget(2024, new ArrayList<>());
        Scenario sc = new Scenario(base, "EmptyBase");

        List<BudgetChange> ch = new ArrayList<>();
        ch.add(new BudgetChange("NEW", "Extra", 0, 200, "Income"));
        sc.setChanges(ch);

        sc.applyChanges();

        List<BudgetItem> res = sc.getModifiedBudget().getItems();
        assertEquals(1, res.size());
        assertEquals("NEW", res.get(0).getCode());
    }

    /**
     * Ελέγχει τη λειτουργία παραγωγής σύνοψης (Summary).
     * <p>
     * Εξετάζει δύο περιπτώσεις:
     * 1. Όταν δεν υπάρχουν αλλαγές (εμφάνιση σχετικού μηνύματος).
     * 2. Όταν υπάρχουν αλλαγές (επιβεβαίωση ότι το κείμενο παράγεται και περιέχει το όνομα του σεναρίου).
     * </p>
     */
    @Test
    public void summaryOutputTest() {
        Budget b = new Budget(2024, new ArrayList<>());
        Scenario s = new Scenario(b, "S1");

        // Case 1: Χωρίς αλλαγές
        s.generateSummary();
        assertTrue(s.getSummary().contains("Δεν υπάρχει καμία αλλαγή"));

        // Case 2: Με αλλαγές
        List<BudgetChange> list = new ArrayList<>();
        list.add(new BudgetChange("1", "TestItem", 20, 10, "T1"));
        s.setChanges(list);
        
        s.generateSummary();
        assertNotNull(s.getSummary());
        assertTrue(s.getSummary().contains("S1"));
    }
}