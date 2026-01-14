package main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
        
        // Χρήση UTF-8 για να διαβαστούν σωστά τα Ελληνικά από τον Loader
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(csvFile), StandardCharsets.UTF_8))) {
            // ΚΕΦΑΛΙΔΑ ΕΣΟΔΩΝ (Trigger για τον Loader)
            writer.println("1. ΕΣΟΔΑ,,,");
            
            // Items: Κωδικός, Όνομα, ..., ΠΟΣΟ (Το ποσό πρέπει να είναι ΠΑΝΤΑ τελευταίο)
            writer.println("\"1001\",\"ΦΠΑ\",,\"25000000\"");
            writer.println("\"1002\",\"Φόρος Εισοδήματος\",,\"15000000\"");
            
            // ΚΕΦΑΛΙΔΑ ΕΞΟΔΩΝ
            writer.println("2. ΕΞΟΔΑ,,,");
            
            // Items Εξόδων
            writer.println("\"2001\",\"Μισθοί Δημόσιου\",,\"12000000\"");
            writer.println("\"2002\",\"Συντάξεις\",,\"8000000\"");
        }
        
        // 2. Προσομοίωση χρηστικής εισόδου (User Input)
        String input = String.join(System.lineSeparator(),
            "2024",                      // Έτος
            csvFile.getAbsolutePath(),   // Path προς το CSV
            "TestScenario",              // Όνομα σεναρίου
            "1001",                      // Κωδικός (Υπάρχει στο CSV)
            "30000000",                  // Νέο ποσό
            "increase",                  // Τύπος αλλαγής
            "n"                          // Τερματισμός
        ) + System.lineSeparator();
        
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
            assertFalse(consoleOutput.isEmpty(), "Η Main.main() θα έπρεπε να παράγει έξοδο");
            
            // 7. Έλεγχος ότι βρέθηκε το κονδύλιο (σημαντικό!)
            // Αν ο Loader αποτύχει, θα λέει "Το στοιχείο δεν βρέθηκε"
            boolean itemFound = consoleOutput.contains("εισάγετε καινούριο ποσό") || 
                                consoleOutput.contains("amount");
            assertTrue(itemFound, "Το Test απέτυχε να βρει το κονδύλιο 1001. Ελέγξτε τον Loader.");

            // 8. Έλεγχος για βασικά μηνύματα ολοκλήρωσης
            assertTrue(consoleOutput.contains("Σύνοψη") || consoleOutput.contains("Summary"),
                "Η έξοδος πρέπει να περιέχει τη Σύνοψη στο τέλος");
                      
        } finally {
            // 9. Επαναφορά αρχικών streams
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }
    
    @Test
    @DisplayName("Sanity test")
    void testAlwaysPassing() {
        assertTrue(true);
    }
}
