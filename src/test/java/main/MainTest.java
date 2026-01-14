package main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Κλάση ελέγχου για την Main.
 * Δημιουργεί προσωρινά το test.csv, τρέχει την εφαρμογή και μετά το σβήνει.
 */
class MainTest {

    private final String CSV_FILENAME = "test.csv";

    @BeforeEach
    void setupCSV() throws IOException {
        // 1. Δημιουργία του αρχείου test.csv με τα δεδομένα που ζήτησες
        // Χρησιμοποιούμε UTF-8 για να φαίνονται σωστά τα Ελληνικά
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(CSV_FILENAME), StandardCharsets.UTF_8))) {
            writer.println("id,description,amount,category");
            writer.println("1001,Μισθοί Δημόσιων Υπαλλήλων,1200000,Μισθοί");
            writer.println("1002,Υγειονομική Περίθαλψη,800000,Υγεία");
            writer.println("1003,Εκπαίδευση,500000,Εκπαίδευση");
            writer.println("1004,Μεταφορές,300000,Υποδομές");
            writer.println("1005,Άμυνα,400000,Ασφάλεια");
        }
    }

    @AfterEach
    void cleanupCSV() {
        // 2. Διαγραφή του αρχείου μετά το τέλος του test
        File file = new File(CSV_FILENAME);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @DisplayName("Πλήρης έλεγχος Main με πραγματικό CSV")
    void testMainWithRealFile() {
        /*
         * ΠΡΟΣΟΜΟΙΩΣΗ ΕΙΣΟΔΟΥ ΧΡΗΣΤΗ
         * Η σειρά πρέπει να ταιριάζει ΑΚΡΙΒΩΣ με τα scanner της Main:
         */
        String input = 
            "2024" + System.lineSeparator() +      // 1. Έτος (nextInt)
            CSV_FILENAME + System.lineSeparator() + // 2. Όνομα αρχείου (nextLine)
            "MyScenario" + System.lineSeparator() + // 3. Όνομα Σεναρίου (nextLine)
            "1001" + System.lineSeparator() +       // 4. Κωδικός που ΥΠΑΡΧΕΙ στο CSV (nextLine)
            "1500000" + System.lineSeparator() +    // 5. Νέο ποσό (nextLong)
            "increase" + System.lineSeparator() +   // 6. Τύπος αλλαγής (nextLine μετά το buffer)
            "n" + System.lineSeparator();           // 7. Τερματισμός (nextLine)

        // Ρύθμιση εισόδου/εξόδου
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Προαιρετικά: Καταγραφή εξόδου για να δούμε αν τυπώθηκαν τα σωστά
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        // ΕΚΤΕΛΕΣΗ
        assertDoesNotThrow(() -> Main.main(new String[]{}));

        // ΕΛΕΓΧΟΣ: Επιβεβαίωση ότι διάβασε το CSV και βρήκε το "Μισθοί"
        String output = out.toString();
        // Ελέγχουμε αν εμφανίστηκε το όνομα του κονδυλίου στην κονσόλα
        // (σημαίνει ότι το βρήκε και ζήτησε ποσό)
        assertTrue(output.contains("Μισθοί Δημόσιων Υπαλλήλων"), 
            "Η εφαρμογή δεν βρήκε το κονδύλιο 1001 από το CSV!");
    }
}