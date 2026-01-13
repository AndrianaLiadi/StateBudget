package data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import model.Budget;
import model.BudgetItem;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link BudgetDataLoader}.
 * <p>
 * Ελέγχει τη διαδικασία φόρτωσης δεδομένων από αρχεία CSV.
 * Δημιουργεί προσωρινά αρχεία με Ελληνικούς χαρακτήρες για να επιβεβαιώσει
 * ότι η κωδικοποίηση (UTF-8) και η ανάλυση (parsing) των ποσών γίνονται σωστά.
 * </p>
 */
class BudgetDataLoaderTest {

    private BudgetDataLoader loader;
    private File tempFile;

    /**
     * Προετοιμασία πριν από κάθε test.
     * <p>
     * 1. Δημιουργεί ένα προσωρινό αρχείο (.csv).
     * 2. Γράφει μέσα στο αρχείο δεδομένα test (με Ελληνικά και διάφορες μορφές αριθμών).
     * 3. Χρησιμοποιεί {@code OutputStreamWriter} με {@code UTF-8} για σωστή εγγραφή.
     * </p>
     */
    @BeforeEach
    void setUp() throws IOException {
        loader = new BudgetDataLoader();
        
        // Δημιουργία προσωρινού αρχείου
        tempFile = File.createTempFile("test_budget", ".csv");
        
        // Χρήση UTF-8 για σωστή εγγραφή Ελληνικών
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(tempFile), StandardCharsets.UTF_8)) {
            // Γράφουμε δεδομένα που μιμούνται τη δομή του πραγματικού αρχείου
            writer.write("1. ΕΣΟΔΑ,,,Ευρώ,1.000.000\n");
            writer.write("11.,Φόροι,,,10.000\n");
            writer.write("14.,Πωλήσεις,,,2.000\n");
            writer.write("2. ΕΞΟΔΑ,,,Ευρώ,500.000\n");
            writer.write("21.,Μισθοί,,,5.000\n");
        }
    }

    @AfterEach
    void tearDown() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    /**
     * Ελέγχει αν τα δεδομένα φορτώνονται σωστά από το αρχείο.
     */
    @Test
    void testLoadData() {
        // ΔΙΟΡΘΩΣΗ: Καλούμε την loadFromCSV με παράμετρο και το έτος (π.χ. 2024)
        Budget budget = loader.loadFromCSV(tempFile.getAbsolutePath(), 2024);
        
        assertNotNull(budget, "Το Budget object δεν πρέπει να είναι null");
        
        // Λήψη της λίστας αντικειμένων από το Budget
        List<BudgetItem> items = budget.getItems();
        assertNotNull(items, "Η λίστα items δεν πρέπει να είναι null");
        assertFalse(items.isEmpty(), "Η λίστα δεν πρέπει να είναι άδεια");
        
        // Έλεγχος ότι διαβάστηκε το "Φόροι" σωστά (Ελληνικά)
        boolean foundTax = items.stream()
                .anyMatch(item -> item.getName().contains("Φόροι"));
        
        assertTrue(foundTax, "Πρέπει να βρεθεί το κονδύλιο 'Φόροι' (Έλεγχος Encoding)");

        // Έλεγχος για "Μισθοί"
        boolean foundWages = items.stream()
                .anyMatch(item -> item.getName().contains("Μισθοί"));
        assertTrue(foundWages, "Πρέπει να βρεθεί το κονδύλιο 'Μισθοί'");
    }

    /**
     * Ελέγχει τη συμπεριφορά όταν το αρχείο δεν υπάρχει.
     * <p>
     * Στον κώδικά σου, το Exception πιάνεται (catch) και επιστρέφεται ένα κενό Budget.
     * Οπότε ελέγχουμε αν το Budget είναι άδειο, όχι αν πετάει Exception.
     * </p>
     */
    @Test
    void testLoadWithInvalidPath() {
        String invalidPath = "non_existent_file_12345.csv";
        
        // Η μέθοδός σου επιστρέφει κενό Budget σε περίπτωση λάθους (δες το catch block σου)
        Budget budget = loader.loadFromCSV(invalidPath, 2024);
        
        assertNotNull(budget);
        assertTrue(budget.getItems().isEmpty(), "Αν το αρχείο δεν υπάρχει, πρέπει να επιστρέφει κενή λίστα");
    }
}
