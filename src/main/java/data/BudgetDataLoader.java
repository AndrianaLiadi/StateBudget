package data;

import model.Budget;
import model.BudgetItem;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Η κλάση BudgetDataLoader είναι υπεύθυνη για τη φόρτωση δεδομένων προϋπολογισμού από αρχεία CSV.
 * <p>
 * Αυτή η έκδοση είναι ενισχυμένη για να διαχειρίζεται:
 * - BOM (Byte Order Mark) στην αρχή των αρχείων.
 * - Ελληνικούς χαρακτήρες (UTF-8).
 * - Διαφορετικές μορφές CSV (με κενές στήλες ή μετατοπισμένα δεδομένα).
 * </p>
 */
public class BudgetDataLoader {

    /**
     * Καθαρίζει και μετατρέπει το string του ποσού σε αριθμό.
     */
    private long cleanAndParseAmount(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return 0;
        }

        // Καθαρισμός: Αφαιρούμε τελείες (διαχωριστικά χιλιάδων), σύμβολα » και εισαγωγικά
        String cleaned = amountStr.replaceAll("\\.", "")
                                  .replaceAll("»", "")
                                  .replaceAll("\"", "")
                                  .trim();
        
        // Αν υπάρχει κόμμα (υποδιαστολή), κρατάμε μόνο το ακέραιο μέρος
        if (cleaned.contains(",")) {
            cleaned = cleaned.substring(0, cleaned.indexOf(','));
        }

        if (cleaned.isEmpty()) {
            return 0;
        }

        try {
            // Προσπαθούμε να το κάνουμε αριθμό
            return Long.parseLong(cleaned);
        } catch (NumberFormatException e) {
            // Αν αποτύχει (π.χ. είναι γράμματα), επιστρέφουμε 0
            return 0;
        }
    }

    /**
     * Εξυπνη ανάγνωση γραμμής CSV που σέβεται τα εισαγωγικά.
     */
    private List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '\"') {
                inQuotes = !inQuotes; // Εναλλαγή κατάστασης εντός/εκτός εισαγωγικών
            } else if (c == ',' && !inQuotes) {
                // Βρήκαμε κόμμα και ΔΕΝ είμαστε σε εισαγωγικά -> Τέλος πεδίου
                result.add(currentField.toString());
                currentField.setLength(0); // Καθαρισμός για το επόμενο
            } else {
                currentField.append(c);
            }
        }
        result.add(currentField.toString()); // Προσθήκη του τελευταίου πεδίου
        return result;
    }

    /**
     * Η κύρια μέθοδος φόρτωσης.
     */
    public Budget loadFromCSV(String filePath, int year) {
        List<BudgetItem> items = new ArrayList<>();
        String currentType = null; // Κρατάει αν διαβάζουμε ΕΣΟΔΑ ή ΕΞΟΔΑ

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 1. Αφαίρεση BOM (Αόρατος χαρακτήρας στην αρχή)
                if (line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }

                if (line.trim().isEmpty()) continue;

                // 2. Έλεγχος για αλλαγή κατηγορίας (ΕΣΟΔΑ / ΕΞΟΔΑ)
                // Το ψάχνουμε σε όλη τη γραμμή για σιγουριά
                if (line.contains("ΕΣΟΔΑ")) {
                    currentType = "REVENUE";
                    continue; // Πάμε στην επόμενη γραμμή
                }
                if (line.contains("ΕΞΟΔΑ")) {
                    currentType = "EXPENDITURE";
                    continue;
                }

                // 3. Ανάλυση της γραμμής σε στήλες
                List<String> data = parseCsvLine(line);
                
                // Πρέπει να έχουμε τουλάχιστον 2 στήλες για να έχει νόημα
                if (data.size() < 2) {
                    continue;
                }

                // 4. Εύρεση Κωδικού και Ονόματος (Ευέλικτη Λογική)
                String codePart = data.get(0).replaceAll("\"", "").trim();
                String name = "";
                
                // Αν η 1η στήλη είναι κενή (συχνό στο budget-2024.csv), ψάχνουμε στη 2η
                if (codePart.isEmpty() && data.size() > 1) {
                    codePart = data.get(1).replaceAll("\"", "").trim();
                    // Αν ο κωδικός ήταν στη 2η, το όνομα θα είναι στην 3η (αν υπάρχει)
                    if (data.size() > 2) {
                        name = data.get(2).replaceAll("\"", "").trim();
                    }
                } else {
                    // Κανονική περίπτωση: Κωδικός στην 1η, Όνομα στη 2η
                    if (data.size() > 1) {
                        name = data.get(1).replaceAll("\"", "").trim();
                    }
                }

                // Καθαρισμός κωδικού από τελείες στο τέλος (π.χ. "11.")
                codePart = codePart.replaceAll("\\.$", "").trim();

                // Αν ακόμα δεν βρήκαμε τίποτα ουσιαστικό, αγνοούμε τη γραμμή
                if (name.isEmpty() && codePart.isEmpty()) continue;

                // 5. Εύρεση Ποσού (Πάντα στην τελευταία γεμάτη στήλη)
                String amountStr = data.get(data.size() - 1);
                long amount = cleanAndParseAmount(amountStr);

                // 6. Δημιουργία Αντικειμένου
                // Φτιάχνουμε το αντικείμενο μόνο αν έχουμε ενεργό τύπο (REVENUE/EXPENDITURE) και ποσό > 0
                if (currentType != null && amount > 0) {
                    // Αν λείπει ο κωδικός αλλά έχουμε τα άλλα, φτιάχνουμε έναν προσωρινό
                    if (codePart.isEmpty()) {
                        codePart = "GEN_" + items.size(); 
                    }
                    
                    BudgetItem item = new BudgetItem(codePart, name, currentType, amount);
                    items.add(item);
                }
            }
            // Τέλος ανάγνωσης αρχείου
            return new Budget(year, items);

        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την ανάγνωση του αρχείου CSV: " + e.getMessage());
            // Επιστροφή άδειου προϋπολογισμού αντί για crash
            return new Budget(year, new ArrayList<>());
        }
    }
}
