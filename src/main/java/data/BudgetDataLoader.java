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
 * Η κλάση BudgetDataLoader είναι υπεύθυνη για τη φόρτωση δεδομένων προϋπολογισμού από αρχεία CSV.
 * <p>
 * UNIVERSAL VERSION:
 * - Διαβάζει αρχεία με κενή πρώτη στήλη (π.χ. 2024).
 * - Διαβάζει αρχεία με κολλημένα Κωδικό+Όνομα (π.χ. 2019 "11.Φόροι").
 * - Διαβάζει κανονικά αρχεία (π.χ. 2023).
 * - Αγνοεί σκουπίδια BOM και χαρακτήρες όπως "»".
 * </p>
 */
public class BudgetDataLoader {

    private long cleanAndParseAmount(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return 0;
        }

        String cleaned = amountStr.replaceAll("\\.", "")
                                  .replaceAll("»", "")
                                  .replaceAll("\"", "")
                                  .trim();
        
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

                // Ανίχνευση Τύπου
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
                
                // --- ΛΟΓΙΚΗ ΓΙΑ 2019 (Κολλημένα) ---
                Matcher matcher = mergedPattern.matcher(codePart);
                if (matcher.find()) {
                    // Βρήκαμε κολλημένα! (π.χ. Group 1: "11.", Group 2: "Φόροι")
                    codePart = matcher.group(1).trim();
                    name = matcher.group(2).trim();
                } else {
                    // --- ΛΟΓΙΚΗ ΓΙΑ 2024 (Κενή 1η στήλη) & 2023 (Κανονικό) ---
                    if (codePart.isEmpty() && data.size() > 1) {
                        codePart = data.get(1).replaceAll("\"", "").trim();
                        if (data.size() > 2) name = data.get(2).replaceAll("\"", "").trim();
                    } else if (data.size() > 1) {
                        name = data.get(1).replaceAll("\"", "").trim();
                    }
                }
                
                // Καθαρισμός ονόματος από σκουπίδια τύπου "»"
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
                
                // Τελικός έλεγχος
                if (name.isEmpty() && codePart.isEmpty()) continue;

                // Ποσό (Πάντα στο τέλος)
                String amountStr = data.get(data.size() - 1);
                long amount = cleanAndParseAmount(amountStr);

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
