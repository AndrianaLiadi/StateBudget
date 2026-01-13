package logic;

import model.Budget;
import model.BudgetChange;
import model.BudgetItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link BudgetService}.
 * <p>
 * Ελέγχει την επιχειρησιακή λογική της εφαρμογής, όπως τον υπολογισμό του
 * δημοσιονομικού αποτελέσματος (πλεόνασμα/έλλειμμα), τη σύγκριση μεταξύ
 * δύο προϋπολογισμών (π.χ. διαφορετικών ετών) και την παραγωγή αναφορών επιπτώσεων.
 * </p>
 */
public class BudgetServiceTest {
    private BudgetService budgetService;
    private ReportGenerator reportGenerator;

    /**
     * Αρχικοποίηση πριν από κάθε test.
     * <p>
     * Δημιουργεί ένα instance του {@link ReportGenerator} και του {@link BudgetService}
     * ώστε να είναι έτοιμα για χρήση στις μεθόδους ελέγχου.
     * </p>
     */
    @BeforeEach
    void setUp() {
        reportGenerator = new ReportGenerator();
        // Περνάμε null στον loader γιατί δεν θα χρειαστεί φόρτωση αρχείων σε αυτά τα tests
        budgetService = new BudgetService(null, reportGenerator);
    }
    
    /**
     * Ελέγχει τον υπολογισμό του πλεονάσματος/ελλείμματος.
     * <p>
     * Δημιουργεί έναν εικονικό προϋπολογισμό με γνωστά έσοδα και έξοδα και
     * επαληθεύει ότι το αποτέλεσμα της αφαίρεσης (Έσοδα - Έξοδα) είναι το αναμενόμενο.
     * </p>
     */
    @Test
    void testCalculateSurplusOrDeficit() {
        List<BudgetItem> items = new ArrayList<>();
        items.add(new BudgetItem("11", "Φόροι", "REVENUE", 56_597_000_000L));
        items.add(new BudgetItem("12", "Κοινωνικές εισφορές", "REVENUE", 56_000_000L));
        items.add(new BudgetItem("21", "Παροχές σε εργαζομένους", "EXPENDITURE", 14_849_625_000L));

        Budget budget = new Budget(2024, items);

        double result = budgetService.calculateSurplusOrDeficit(budget);

        // Αναμενόμενο: (56.597.000.000 + 56.000.000) - 14.849.625.000 = 41.803.375.000
        assertEquals(41_803_375_000.0, result, 0.001, "Ο υπολογισμός πλεονάσματος δεν είναι σωστός");
    }

    /**
     * Ελέγχει τη σύγκριση δύο προϋπολογισμών χρησιμοποιώντας δεδομένα που μοιάζουν με τα πραγματικά.
     * <p>
     * Δημιουργεί δύο αντικείμενα {@link Budget} (για το 2024 και το 2025) και ελέγχει αν η μέθοδος
     * {@code compareBudgets} εντοπίζει σωστά τις διαφορές για κάθε κονδύλιο.
     * </p>
     */
    @Test
    void testCompareBudgets_RealData_2024_vs_2025() {
        // Setup 2024 Budget
        List<BudgetItem> items2024 = new ArrayList<>();
        items2024.add(new BudgetItem("11", "Φόροι", "REVENUE", 56_597_000_000L));
        items2024.add(new BudgetItem("12", "Κοινωνικές εισφορές", "REVENUE", 56_000_000L));
        items2024.add(new BudgetItem("21", "Παροχές σε εργαζομένους", "EXPENDITURE", 14_849_625_000L));
        Budget budget2024 = new Budget(2024, items2024);

        // Setup 2025 Budget
        List<BudgetItem> items2025 = new ArrayList<>();
        items2025.add(new BudgetItem("11", "Φόροι", "REVENUE", 62_055_000_000L));
        items2025.add(new BudgetItem("12", "Κοινωνικές εισφορές", "REVENUE", 60_000_000L));
        items2025.add(new BudgetItem("21", "Παροχές σε εργαζομένους", "EXPENDITURE", 14_889_199_000L));
        Budget budget2025 = new Budget(2025, items2025);

        // Εκτέλεση σύγκρισης
        List<BudgetChange> changes = budgetService.compareBudgets(budget2024, budget2025);

        // Assertions (Έλεγχοι)
        assertEquals(3, changes.size(), "Πρέπει να βρεθούν αλλαγές και στα 3 κονδύλια");
        
        // Έλεγχος Φόρων (Taxes)
        BudgetChange taxChange = findChangeByCode(changes, "11");
        assertNotNull(taxChange);
        assertEquals(56_597_000_000L, taxChange.getOldValue());
        assertEquals(62_055_000_000L, taxChange.getNewValue());
        assertEquals(5_458_000_000L, taxChange.getDifference(), "Η αύξηση στους φόρους πρέπει να είναι 5.458 δις");
        
        // Έλεγχος Κοινωνικών Εισφορών (Social)
        BudgetChange socialChange = findChangeByCode(changes, "12");
        assertNotNull(socialChange);
        assertEquals(56_000_000L, socialChange.getOldValue());
        assertEquals(60_000_000L, socialChange.getNewValue());
        assertEquals(4_000_000L, socialChange.getDifference());
        
        // Έλεγχος Παροχών (Benefits)
        BudgetChange benefitsChange = findChangeByCode(changes, "21");
        assertNotNull(benefitsChange);
        assertEquals(14_849_625_000L, benefitsChange.getOldValue());
        assertEquals(14_889_199_000L, benefitsChange.getNewValue());
        assertEquals(39_574_000L, benefitsChange.getDifference());
    }

    /**
     * Ελέγχει την παραγωγή της αναφοράς επιπτώσεων (Impact Analysis).
     * <p>
     * Επαληθεύει ότι η μέθοδος {@code analyzeImpact} επιστρέφει ένα κείμενο που δεν είναι null
     * και περιέχει βασικές λέξεις-κλειδιά όπως το όνομα του σεναρίου και τα ονόματα των κονδυλίων.
     * </p>
     */
    @Test
    void testAnalyzeImpact() {
        List<BudgetItem> items = new ArrayList<>();
        items.add(new BudgetItem("11", "Φόροι", "REVENUE", 56_597_000_000L));
        Budget base = new Budget(2024, items);
        
        List<BudgetItem> modifiedItems = new ArrayList<>();
        modifiedItems.add(new BudgetItem("11", "Φόροι", "REVENUE", 60_000_000_000L));
        Budget modified = new Budget(2025, modifiedItems);
        
        String report = budgetService.analyzeImpact(base, modified, "Σενάριο Αύξησης Φόρων");
        
        System.out.println(report); // Προαιρετική εκτύπωση για έλεγχο
        
        assertNotNull(report);
        assertTrue(report.contains("Σενάριο Αύξησης Φόρων"));
        assertTrue(report.contains("Φόροι"));
    }

    /**
     * Βοηθητική μέθοδος για την εύρεση μιας αλλαγής στη λίστα βάσει κωδικού.
     *
     * @param changes Η λίστα των αλλαγών.
     * @param code    Ο κωδικός του κονδυλίου προς αναζήτηση.
     * @return Το αντικείμενο BudgetChange αν βρεθεί, αλλιώς null.
     */
    private BudgetChange findChangeByCode(List<BudgetChange> changes, String code) {
        for (BudgetChange change : changes) {
            if (change.getItemCode().equals(code)) {
                return change;
            }
        }
        return null;
    }
}