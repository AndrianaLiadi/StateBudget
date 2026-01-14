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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Η κλάση {@code BudgetDataLoader} είναι υπεύθυνη για τη φόρτωση δεδομένων προϋπολογισμού από αρχεία CSV.
 * <p>
 * Πρόκειται για μια <b>Universal</b> έκδοση που έχει σχεδιαστεί για να διαχειρίζεται
 * διάφορες ιδιομορφίες των αρχείων προϋπολογισμού, όπως:
 * <ul>
 * <li>Αρχεία με κενή την πρώτη στήλη (π.χ. δεδομένα του 2024).</li>
 * <li>Αρχεία όπου ο Κωδικός και το Όνομα έχουν συγχωνευθεί στο ίδιο κελί (π.χ. "11.Φόροι").</li>
 * <li>Αφαίρεση ειδικών χαρακτήρων όπως "»" και διαχωριστικών χιλιάδων.</li>
 * <li>Αυτόματη αφαίρεση του BOM (Byte Order Mark) για σωστή ανάγνωση UTF-8.</li>
 * </ul>
 * </p>
 */
public class BudgetDataLoader {

    /**
     * Καθαρίζει και μετατρέπει μια συμβολοσειρά ποσού σε ακέραιο αριθμό (long).
     * <p>
     * Αφαιρεί τελείες (διαχωριστικά χιλιάδων), εισαγωγικά και ειδικά σύμβολα (»).
     * Αν το ποσό περιέχει υποδιαστολή (κόμμα), κρατάει μόνο το ακέραιο μέρος.
     * </p>
     *
     * @param amountStr Η αρχική συμβολοσειρά του ποσού.
     * @return Το ποσό ως {@code long}. Επιστρέφει 0 αν η είσοδος είναι κενή, null ή μη έγκυρη.
     */
    private long cleanAndParseAmount(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return 0;
        }

        String cleaned = amountStr.replaceAll("\\.", "")
                                  .replaceAll("»", "")
                                  .replaceAll("\"", "")
                                  .trim();
        
        // Αφαίρεση δεκαδικών ψηφίων αν υπάρχουν
        if (cleaned.contains(",")) {
            cleaned = cleaned.substring(0, cleaned.indexOf(','));
        }

        if (cleaned.isEmpty()) return 0;

        try {
            return Long.parseLong(cleaned);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Αναλύει μια γραμμή CSV σε λίστα πεδίων, λαμβάνοντας υπόψη τα εισαγωγικά.
     * <p>
     * Διαχειρίζεται σωστά περιπτώσεις όπου ένα πεδίο περιέχει κόμμα, αρκεί να βρίσκεται
     * εντός εισαγωγικών (π.χ. "Επίδομα, Ειδικό").
     * </p>
     *
     * @param line Η γραμμή CSV προς ανάλυση.
     * @return Μια λίστα με τα επιμέρους πεδία (Strings).
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
     * Φορτώνει τα δεδομένα του προϋπολογισμού από το καθορισμένο αρχείο CSV.
     * <p>
     * Η μέθοδος εφαρμόζει ευρετική λογική (heuristics) για να εντοπίσει σωστά
     * τον Κωδικό, το Όνομα και το Ποσό, ανεξάρτητα από μικρές διαφοροποιήσεις στη δομή του αρχείου.
     * Αναγνωρίζει αυτόματα τις ενότητες "ΕΣΟΔΑ" και "ΕΞΟΔΑ".
     * </p>
     *
     * @param filePath Η διαδρομή του αρχείου CSV.
     * @param year Το οικονομικό έτος του προϋπολογισμού.
     * @return Ένα αντικείμενο {@link Budget} με τα φορτωμένα δεδομένα. Σε περίπτωση σφάλματος, επιστρέφει ένα κενό Budget.
     */
    public Budget loadFromCSV(String filePath, int year) {
        List<BudgetItem> items = new ArrayList<>();
        String currentType = null;
        
        // Regex για διαχωρισμό κολλημένων κωδικών (π.χ. "11.Φόροι" -> "11." και "Φόροι")
        // Ψάχνει για ψηφία/τελείες στην αρχή, και μετά οτιδήποτε άλλο
        Pattern mergedPattern = Pattern.compile("^([0-9.]+)([^0-9.].*)$");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1); // BOM fix
                if (line.trim().isEmpty()) continue;

                // Ανίχνευση Τύπου (ΕΣΟΔΑ / ΕΞΟΔΑ)
                if (line.contains("ΕΣΟΔΑ")) {
                    currentType = "REVENUE";
                    continue; 
                }
                if (line.contains("ΕΞΟΔΑ")) {
                    currentType = "EXPENDITURE";
                    continue;
                }

                List<String> data = parseCsvLine(line);
                if (data.size() < 2) continue;

                // Αρχικοποίηση μεταβλητών
                String codePart = data.get(0).replaceAll("\"", "").trim();
                String name = "";
                
                // Έλεγχος για συγχωνευμένα κελιά (περίπτωση 2019)
                Matcher matcher = mergedPattern.matcher(codePart);
                if (matcher.find()) {
                    codePart = matcher.group(1).trim();
                    name = matcher.group(2).trim();
                } else {
                    // Λογική για μετατοπισμένες στήλες (περίπτωση 2024 με κενή 1η στήλη)
                    if (codePart.isEmpty() && data.size() > 1) {
                        codePart = data.get(1).replaceAll("\"", "").trim();
                        if (data.size() > 2) name = data.get(2).replaceAll("\"", "").trim();
                    } else if (data.size() > 1) {
                        name = data.get(1).replaceAll("\"", "").trim();
                    }
                }
                
                // Καθαρισμός ονόματος από σκουπίδια τύπου "»" ή ">"
                if (name.equals("»") || name.equals(">")) {
                    // Αν το όνομα είναι σύμβολο, προσπαθούμε να βρούμε το επόμενο κελί
                    if (data.size() > 2 && !data.get(2).matches(".*\\d.*")) { 
                        // Αν το επόμενο κελί δεν είναι αριθμός, μάλλον είναι το όνομα
                        name = data.get(2).replaceAll("\"", "").trim();
                    } else {
                        name = ""; // Δεν βρέθηκε έγκυρο όνομα
                    }
                }

                codePart = codePart.replaceAll("\\.$", "").trim();
                
                // Τελικός έλεγχος εγκυρότητας γραμμής
                if (name.isEmpty() && codePart.isEmpty()) continue;

                // Ποσό (Πάντα στο τέλος της γραμμής)
                String amountStr = data.get(data.size() - 1);
                long amount = cleanAndParseAmount(amountStr);

                // Δημιουργία αντικειμένου μόνο αν έχουμε ενεργό τύπο και θετικό ποσό
                if (currentType != null && amount > 0) {
                    if (codePart.isEmpty()) codePart = "UNKNOWN_" + items.size();
                    if (name.isEmpty()) name = "Άγνωστο Κονδύλιο";
                    
                    items.add(new BudgetItem(codePart, name, currentType, amount));
                }
            }
            return new Budget(year, items);
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
            return new Budget(year, new ArrayList<>());
        }
    }
}

