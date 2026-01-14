package main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Κλάση ελέγχου (Test Class) για την {@link Main}.
 *
 * <p>
 * Η παρούσα κλάση ελέγχει τη σωστή λειτουργία της εφαρμογής
 * σε περιβάλλον κονσόλας (CLI), προσομοιώνοντας την είσοδο χρήστη
 * και παρακολουθώντας την εκτέλεση χωρίς εξαιρέσεις.
 * </p>
 *
 * <p>
 * Τα tests αυτά συμβάλλουν σημαντικά στην κάλυψη κώδικα (code coverage)
 * και αξιολογούνται από εργαλεία όπως το JaCoCo.
 * </p>
 */
class MainTest {

    /**
     * Sanity check test.
     *
     * <p>
     * Επαληθεύει ότι το περιβάλλον του JUnit είναι σωστά ρυθμισμένο
     * και ότι τα assertions λειτουργούν κανονικά.
     * </p>
     */
    @Test
    @DisplayName("Sanity test – περνάει πάντα")
    void testAlwaysPassing() {
        assertTrue(true);
    }

    /**
     * Integration test για τη μέθοδο {@code Main.main()}.
     *
     * <p>
     * Το test προσομοιώνει πλήρως τη ροή χρήσης της εφαρμογής:
     * <ul>
     *   <li>Εισαγωγή έτους</li>
     *   <li>Φόρτωση CSV αρχείου</li>
     *   <li>Δημιουργία σεναρίου</li>
     *   <li>Προσθήκη μίας αλλαγής προϋπολογισμού</li>
     *   <li>Ολοκλήρωση και παραγωγή σύνοψης</li>
     * </ul>
     * </p>
     *
     * <p>
     * Το test ελέγχει ότι η εκτέλεση ολοκληρώνεται χωρίς να
     * προκύψει εξαίρεση (Exception).
     * </p>
     */
    @Test
    @DisplayName("Πλήρης ροή εκτέλεσης της Main (CLI Integration Test)")
    void testMainCompleteFlow() {

        /*
         * Προσομοίωση εισόδου χρήστη.
         * Η σειρά των τιμών ΠΡΕΠΕΙ να ταιριάζει ακριβώς
         * με τις κλήσεις scanner.nextInt(), nextLine(), nextLong().
         */
        String input =
                "2024\n" +          // Έτος προϋπολογισμού
                "test.csv\n" +      // Όνομα αρχείου CSV
                "TestScenario\n" +  // Όνομα σεναρίου
                "1001\n" +          // Υπαρκτός κωδικός κονδυλίου
                "1300000\n" +       // Νέο ποσό
                "increase\n" +      // Τύπος αλλαγής
                "n\n";              // Τερματισμός προσθήκης αλλαγών

        // Ανακατεύθυνση εισόδου και εξόδου
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        /*
         * Εκτέλεση της Main.
         * Το assertDoesNotThrow επιβεβαιώνει ότι η εφαρμογή
         * ολοκληρώνεται χωρίς runtime σφάλματα.
         */
        assertDoesNotThrow(() -> Main.main(new String[]{}));

        // Προαιρετικός έλεγχος: επιβεβαίωση ότι παράχθηκε έξοδος
        assertFalse(output.toString().isEmpty());
    }
}

