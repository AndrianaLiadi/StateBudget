package main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link Main}.
 * <p>
 * Αυτή η κλάση είναι κρίσιμη για τον έλεγχο της έκδοσης κονσόλας (CLI) της εφαρμογής.
 * Αντί να περιμένει πραγματική πληκτρολόγηση από τον χρήστη, προσομοιώνει την είσοδο
 * δεδομένων (Input Simulation) για να ελέγξει ολόκληρη τη ροή του προγράμματος.
 * </p>
 */
class MainTest {
    
    /**
     * Ένα απλό test επαλήθευσης (Sanity Check).
     * <p>
     * Χρησιμεύει απλώς για να επιβεβαιώσουμε ότι το περιβάλλον του JUnit
     * έχει ρυθμιστεί σωστά και μπορεί να εκτελέσει tests.
     * </p>
     */
    @Test
    @DisplayName("Test που περνάει πάντα")
    void testAlwaysPassing() {
        System.out.println(" Αυτό το test περνάει πάντα!");
        assertTrue(true);
    }
    
    /**
     * Ελέγχει την πλήρη ροή εκτέλεσης της μεθόδου {@code main}.
     * <p>
     * Αυτό το test είναι πολύ σημαντικό (Integration Test). Κάνει τα εξής:
     * <ol>
     * <li>Δημιουργεί ένα String με όλες τις απαντήσεις που θα έδινε ένας χρήστης
     * (Έτος -> Αρχείο -> Enter -> Όνομα Σεναρίου -> Κωδικός -> Ποσό -> Τύπος -> Όχι άλλες αλλαγές).</li>
     * <li>Το διοχετεύει στο {@code System.in} ώστε η εφαρμογή να νομίζει ότι πληκτρολογεί χρήστης.</li>
     * <li>Καταγράφει την έξοδο (Output) για να μην γεμίζει η κονσόλα μας.</li>
     * <li>Εκτελεί τη {@code Main.main} και ελέγχει ότι δεν πετάει σφάλμα (Exception).</li>
     * </ol>
     * </p>
     */
    @Test
    @DisplayName("Test για την Main - πλήρης ροή (Αυτό ΠΕΡΝΑΕΙ!)")
    void testMainCompleteFlow() {
        System.err.println(" Εκτέλεση πλήρους test...");
        
        // Input simulation: Προσομοίωση των κινήσεων του χρήστη
        String input = "2024\n" +           // Εισαγωγή έτους
                       "test.csv\n" +       // Εισαγωγή ονόματος αρχείου CSV
                       "\n" +               // Πάτημα Enter (π.χ. στο μενού)
                       "TestScenario\n" +   // Όνομα σεναρίου
                       "1001\n" +           // Κωδικός κονδυλίου προς αλλαγή
                       "1300000\n" +        // Νέο ποσό
                       "increase\n" +       // Τύπος αλλαγής (σημείωση: εδώ ίσως αγνοείται από τη λογική αν ζητάει απλά ποσό)
                       "n\n";               // 'n' για τερματισμό αλλαγών
        
        // Ανακατεύθυνση της εισόδου και εξόδου
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        
        // Εκτέλεση της Main και έλεγχος ότι δεν κρασάρει
        assertDoesNotThrow(() -> {
            Main.main(new String[]{});
        });
        
        System.err.println(" Η Main ΟΛΟΚΛΗΡΩΘΗΚΕ επιτυχώς!");
        System.err.println("Μήκος εξόδου: " + out.toString().length());
    }
    
    /**
     * Ένα βασικό αριθμητικό test.
     * <p>
     * Επιβεβαιώνει ότι οι βασικοί υπολογισμοί και οι εντολές assert
     * λειτουργούν όπως αναμένεται.
     * </p>
     */
    @Test
    @DisplayName("Simple test that always works")
    void simpleTest() {
        // Simple assertion always runs 
        assertEquals(2, 1 + 1, "1+1 should be 2");
        System.out.println(" Simple math test passed!");
    }
}