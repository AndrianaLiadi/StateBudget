package main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests για την κλάση {@link Main}.
 */
class MainTest {

    @TempDir
    Path tempDir;

    /**
     * Test για πλήρη ροή εκτέλεσης της Main με σωστό CSV format.
     */
    @Test
    @DisplayName("Πλήρης ροή εκτέλεσης με σωστό CSV format")
    void testMainCompleteFlowWithCorrectCsv() throws Exception {
        // 1. Δημιουργία CSV με τη ΣΩΣΤΗ δομή που αναμένει ο BudgetDataLoader
        File csvFile = tempDir.resolve("budget.csv").toFile();
        
        try (FileWriter writer = new FileWriter(csvFile)) {
            // Σωστή επικεφαλίδα που αναμένει ο BudgetDataLoader
            writer.write("code,name,type,amount,revenue_or_expenditure\n");
            
            // Ετικέτα για έσοδα
            writer.write("\"ΕΣΟΔΑ\",\"\",\"\",\"\",\"\"\n");
            
            // Ένα item εσόδων με κωδικό 1001
            writer.write("\"1001\",\"ΦΠΑ\",\"INCOME\",\"25000000\",\"revenue\"\n");
            writer.write("\"1002\",\"Φόρος Εισοδήματος\",\"INCOME\",\"15000000\",\"revenue\"\n");
            
            // Ετικέτα για έξοδα
            writer.write("\"ΕΞΟΔΑ\",\"\",\"\",\"\",\"\"\n");
            
            // Δύο items εξόδων
            writer.write("\"2001\",\"Μισθοί Δημόσιου\",\"EXPENSE\",\"12000000\",\"expenditure\"\n");
            writer.write("\"2002\",\"Συντάξεις\",\"EXPENSE\",\"8000000\",\"expenditure\"\n");
        }
        
        // 2. Προσομοίωση χρηστικής εισόδου
        String input = String.join("\n",
            "2024",                      // Έτος
            csvFile.getAbsolutePath(),   // Path προς το CSV
            "TestScenario",              // Όνομα σεναρίου
            "1001",                      // Κωδικός προϋπολογισμού (υπάρχει στο CSV)
            "30000000",                  // Νέο ποσό (αύξηση από 25.000.000 σε 30.000.000)
            "increase",                  // Τύπος αλλαγής
            "n"                          // Τερματισμός
        );
        
        // 3. Αποθήκευση αρχικών System streams
        java.io.InputStream originalIn = System.in;
        java.io.PrintStream originalOut = System.out;
        
        try {
            // 4. Ανακατεύθυνση I/O για testing
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));
            
            // 5. Εκτέλεση και έλεγχος ότι δεν πετάει exceptions
            assertDoesNotThrow(() -> Main.main(new String[]{}),
                "Η Main.main() θα έπρεπε να εκτελείται χωρίς exceptions με σωστό CSV");
            
            // 6. Έλεγχος ότι παράχθηκε έξοδος
            String consoleOutput = output.toString();
            assertFalse(consoleOutput.isEmpty(),
                "Η Main.main() θα έπρεπε να παράγει έξοδο");
            
            // 7. Έλεγχος για βασικά μηνύματα
            assertTrue(consoleOutput.contains("Προϋπολογισμού") || 
                      consoleOutput.contains("Προυπολογισμού") ||
                      consoleOutput.contains("συνολικά") ||
                      consoleOutput.contains("Σύνοψη"),
                "Η έξοδος πρέπει να περιέχει βασικά μηνύματα της εφαρμογής");
                      
        } finally {
            // 8. Επαναφορά αρχικών streams
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }
    
    /**
     * Απλό sanity test.
     */
    @Test
    @DisplayName("Sanity test")
    void testAlwaysPassing() {
        assertTrue(true);
    }
}