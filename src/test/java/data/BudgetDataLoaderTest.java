package data;

import model.Budget;
import model.BudgetItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link BudgetDataLoader}.
 * <p>
 * Ελέγχει τη διαδικασία φόρτωσης δεδομένων από αρχεία CSV. Δημιουργεί προσωρινά αρχεία
 * για να επαληθεύσει ότι:
 * <ul>
 * <li>Το αρχείο διαβάζεται σωστά.</li>
 * <li>Τα δεδομένα (κωδικοί, ποσά, τύποι) αναλύονται (parse) σωστά.</li>
 * <li>Γίνεται σωστός χειρισμός ειδικών χαρακτήρων και μορφοποίησης.</li>
 * <li>Αντιμετωπίζονται σωστά περιπτώσεις ανύπαρκτων αρχείων.</li>
 * </ul>
 * </p>
 */
public class BudgetDataLoaderTest {
    private BudgetDataLoader loader;
    private File tempFile;

    /**
     * Εκτελείται πριν από κάθε μέθοδο ελέγχου (@Test).
     * <p>
     * Προετοιμάζει το περιβάλλον:
     * <ol>
     * <li>Αρχικοποιεί τον loader.</li>
     * <li>Δημιουργεί ένα προσωρινό αρχείο CSV.</li>
     * <li>Γράφει εικονικά δεδομένα (mock data) στο αρχείο, συμπεριλαμβανομένων
     * ειδικών περιπτώσεων όπως σύμβολα στα ποσά και κενές γραμμές.</li>
     * </ol>
     * </p>
     */
    @BeforeEach
    void setUp() throws IOException {
        loader = new BudgetDataLoader();
        
        // Δημιουργία προσωρινού αρχείου CSV
        tempFile = File.createTempFile("test_budget", ".csv");
        
        // Εγγραφή δεδομένων που προσομοιώνουν τη δομή των πραγματικών αρχείων
        try (FileWriter writer = new FileWriter(tempFile)) {
            // Header Εσόδων
            writer.write("1. ΕΣΟΔΑ,,,Ευρώ,1.000.000\n");
            // Κανονική εγγραφή
            writer.write("11.,Φόροι,,,10.000\n");
            // Εγγραφή με "σκουπίδια" στο ποσό (»)
            writer.write("14.,Πωλήσεις με σύμβολα,,,»2.000\n");
            // Εγγραφή με δεκαδικά
            writer.write("19.,Με δεκαδικά,,,5.500,00\n");
            // Κενή γραμμή
            writer.write("\n");
            // Header Εξόδων
            writer.write("2. ΕΞΟΔΑ,,,Ευρώ,500.000\n");
            // Κανονική εγγραφή εξόδου
            writer.write("21.,Παροχές σε εργαζομένους,,,4.000\n"); 
            // Προβληματική γραμμή
            writer.write("BadLine,Χωρίς Ποσό,,,\n");
        }
    }

    /**
     * Εκτελείται μετά από κάθε μέθοδο ελέγχου.
     * <p>
     * Καθαρίζει το σύστημα διαγράφοντας το προσωρινό αρχείο που δημιουργήθηκε,
     * ώστε να μην μένουν υπολείμματα στον δίσκο.
     * </p>
     */
    @AfterEach
    void tearDown() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    /**
     * Ελέγχει τη βασική επιτυχή φόρτωση ενός αρχείου.
     * <p>
     * Επιβεβαιώνει ότι επιστρέφεται αντικείμενο Budget, το έτος είναι σωστό
     * και ο αριθμός των έγκυρων εγγραφών είναι ο αναμενόμενος (4 εγγραφές
     * βάσει των δεδομένων του setUp).
     * </p>
     */
    @Test
    void testLoadFromCSV_SuccessfulLoad() {
        Budget budget = loader.loadFromCSV(tempFile.getAbsolutePath(), 2025);
        
        assertNotNull(budget, "Το budget δεν πρέπει να είναι null");
        assertEquals(2025, budget.getYear());
        
        List<BudgetItem> items = budget.getItems();
        assertFalse(items.isEmpty(), "Η λίστα δεν πρέπει να είναι άδεια");
        
        // Αναμένουμε 4 έγκυρα items (11, 14, 19, 21). Οι τίτλοι και οι προβληματικές γραμμές αγνοούνται.
        assertEquals(4, items.size());
    }

    /**
     * Ελέγχει τη λογική ανάλυσης (parsing) των δεδομένων.
     * <p>
     * Επαληθεύει ότι:
     * <ul>
     * <li>Οι κωδικοί (π.χ. "11") καθαρίζονται από τελείες.</li>
     * <li>Ο τύπος (REVENUE/EXPENDITURE) ανατίθεται σωστά ανάλογα με την ενότητα.</li>
     * <li>Τα ποσά μετατρέπονται σωστά σε αριθμούς, αφαιρώντας διαχωριστικά και σύμβολα.</li>
     * </ul>
     * </p>
     */
    @Test
    void testLoadFromCSV_ParsingLogic() {
        Budget budget = loader.loadFromCSV(tempFile.getAbsolutePath(), 2025);
        List<BudgetItem> items = budget.getItems();

        // Έλεγχος κανονικής εγγραφής εσόδων
        BudgetItem item11 = findItemByCode(items, "11");
        assertNotNull(item11);
        assertEquals("Φόροι", item11.getName());
        assertEquals("REVENUE", item11.getType());
        assertEquals(10000L, item11.getAmount());

        // Έλεγχος εγγραφής με σύμβολα (»2.000 -> 2000)
        BudgetItem item14 = findItemByCode(items, "14");
        assertNotNull(item14);
        assertEquals(2000L, item14.getAmount());

        // Έλεγχος εγγραφής εξόδων
        BudgetItem item21 = findItemByCode(items, "21");
        assertNotNull(item21);
        assertEquals("EXPENDITURE", item21.getType());
        assertEquals(4000L, item21.getAmount());
    }

    /**
     * Ελέγχει τη συμπεριφορά όταν το αρχείο δεν υπάρχει.
     * <p>
     * Αναμένουμε η μέθοδος να επιστρέψει {@code null} και να μην καταρρεύσει η εφαρμογή.
     * </p>
     */
    @Test
    void testLoadFromCSV_FileNotFound() {
        Budget budget = loader.loadFromCSV("C:/fake/path/does_not_exist.csv", 2025);
        assertNull(budget, "Αν δεν βρεθεί αρχείο, πρέπει να επιστρέφει null");
    }

    /**
     * Βοηθητική μέθοδος για την εύρεση αντικειμένου στη λίστα βάσει κωδικού.
     *
     * @param items Η λίστα των κονδυλίων.
     * @param code  Ο κωδικός προς αναζήτηση.
     * @return Το αντικείμενο BudgetItem αν βρεθεί, αλλιώς null.
     */
    private BudgetItem findItemByCode(List<BudgetItem> items, String code) {
        for (BudgetItem item : items) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}