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
 * Η κλάση BudgetDataLoader είναι υπεύθυνη για τη φόρτωση δεδομένων προϋπολογισμού από αρχεία.
 * <p>
 * Αναλαμβάνει το άνοιγμα αρχείων CSV, την ανάλυση (parsing) των γραμμών τους,
 * τον καθαρισμό των δεδομένων (π.χ. αφαίρεση ειδικών χαρακτήρων από τα ποσά)
 * και τη δημιουργία αντικειμένων {@link Budget}.
 * </p>
 */
public class BudgetDataLoader {

    /**
     * Καθαρίζει μια συμβολοσειρά ποσού και τη μετατρέπει σε ακέραιο αριθμό (long).
     * <p>
     * Αφαιρεί τελείες, εισαγωγικά και ειδικούς χαρακτήρες. Αν το ποσό περιέχει
     * δεκαδικό μέρος (υποδιαστολή με κόμμα), αυτό αποκόπτεται.
     * </p>
     *
     * @param amountStr Η αρχική συμβολοσειρά που περιέχει το ποσό.
     * @return Το ποσό ως {@code long}. Επιστρέφει 0 αν η είσοδος είναι κενή ή μη έγκυρη.
     */
    private long cleanAndParseAmount(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return 0;
        }

        String cleaned = amountStr.replaceAll("\\.", "")
                                  .replaceAll("»", "")
                                  .replaceAll("\"", "")
                                  .trim();
        
        // Αφαίρεση δεκαδικών αν υπάρχουν
        if (cleaned.contains(",")) {
            cleaned = cleaned.substring(0, cleaned.indexOf(','));
        }

        if (cleaned.isEmpty()) {
            return 0;
        }

        try {
            return Long.parseLong(cleaned);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Αναλύει μια γραμμή CSV λαμβάνοντας υπόψη πεδία που βρίσκονται εντός εισαγωγικών.
     * <p>
     * Αυτή η μέθοδος είναι απαραίτητη γιατί απλό {@code split(",")} θα αποτύγχανε
     * αν ένα πεδίο (π.χ. όνομα κατηγορίας) περιείχε κόμμα μέσα του.
     * </p>
     *
     * @param line Η γραμμή κειμένου από το αρχείο CSV.
     * @return Μια λίστα με τα επιμέρους πεδία της γραμμής.
     */
    private List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '\"') {
                inQuotes = !inQuotes; 
            } else if (c == ',' && !inQuotes) {
                result.add(currentField.toString());
                currentField.setLength(0); 
            } else {
                currentField.append(c);
            }
        }
        result.add(currentField.toString()); 
        return result;
    }

    /**
     * Φορτώνει τα δεδομένα του προϋπολογισμού από ένα αρχείο CSV.
     * <p>
     * Η μέθοδος διαβάζει το αρχείο, αναγνωρίζει αν πρόκειται για ΕΣΟΔΑ ή ΕΞΟΔΑ
     * με βάση το περιεχόμενο, και δημιουργεί τη λίστα με τα αντικείμενα {@link BudgetItem}.
     * </p>
     *
     * @param filePath Η διαδρομή του αρχείου CSV στο δίσκο.
     * @param year     Το οικονομικό έτος στο οποίο αναφέρεται ο προϋπολογισμός.
     * @return Ένα αντικείμενο {@link Budget} που περιέχει όλα τα φορτωμένα στοιχεία.
     */
    public Budget loadFromCSV(String filePath, int year) {
        List<BudgetItem> items = new ArrayList<>();
        String currentType = null;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Αφαίρεση του BOM (Byte Order Mark) αν υπάρχει στην αρχή του αρχείου
                if (line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }

                List<String> data = parseCsvLine(line);

                if (data.size() < 2) {
                    continue;
                }

                String codePart = data.get(0).replaceAll("\"", "").trim();
                String name = data.get(1).replaceAll("\"", "").trim();
                String amountStr = data.get(data.size() - 1);

                // Ανίχνευση αλλαγής τύπου (Έσοδα/Έξοδα)
                if (codePart.contains("ΕΣΟΔΑ")) {
                    currentType = "REVENUE";
                    continue;
                }
                if (codePart.contains("ΕΞΟΔΑ")) {
                    currentType = "EXPENDITURE";
                    continue;
                }

                if (name.isEmpty()) {
                    continue; 
                }

                if (currentType != null && !codePart.isEmpty()) {
                    long amount = cleanAndParseAmount(amountStr);
                    String cleanCode = codePart.replaceAll("\\.", "").trim();
                    BudgetItem item = new BudgetItem(cleanCode, name, currentType, amount);
                    items.add(item);
                }
            }
            return new Budget(year, items);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return new Budget(year, new ArrayList<>());
        }
    }
}