package logic;

import model.BudgetChange;
import model.Scenario;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReportGenerator {

    public String generateSummary(Scenario scenario, List<BudgetChange> changes) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== ΑΝΑΦΟΡΑ ΣΕΝΑΡΙΟΥ: ").append(scenario.getitemName()).append(" ===\n");
        sb.append("------------------------------------------------------------\n");

        long totalRevenueDiff = 0;
        long totalExpenditureDiff = 0;

        if (changes != null) {
            for (BudgetChange change : changes) {
                String type = change.getType();
                
                if (type != null && type.equals("REVENUE")) {
                    totalRevenueDiff += change.getDifference();
                } else if (type != null && type.equals("EXPENDITURE")) {
                    totalExpenditureDiff += change.getDifference();
                }
            }
        }

        sb.append("ΣΥΝΟΛΙΚΗ ΕΙΚΟΝΑ:\n");
        sb.append(String.format("• Μεταβολή Εσόδων:   %+d €\n", totalRevenueDiff));
        sb.append(String.format("• Μεταβολή Εξόδων:   %+d €\n", totalExpenditureDiff));
        
        long netImpact = totalRevenueDiff - totalExpenditureDiff;
        sb.append(String.format("• Καθαρή Επίπτωση:   %+d €\n", netImpact));
        sb.append("\n");

        sb.append("ΑΝΑΛΥΤΙΚΕΣ ΑΛΛΑΓΕΣ:\n");
        if (changes == null || changes.isEmpty()) {
            sb.append(" - Δεν βρέθηκαν αλλαγές στο σενάριο.\n");
        } else {
            for (BudgetChange change : changes) {
                sb.append(String.format(" - [%s] %s (%s):\n", 
                        change.getType(), change.getItemName(), change.getItemCode()));
                
                sb.append(String.format("     Παλιό: %d  ->  Νέο: %d\n", 
                        change.getOldValue(), change.getNewValue()));
                
                sb.append(String.format("     Διαφορά: %+d  (%.2f%%)\n", 
                        change.getDifference(), change.getPercentageChange()));
                sb.append("------------------------------------------------------------\n");
            }
        }

        return sb.toString();
    }

    public void exportToPDF(Scenario scenario, String content) {
        String filename = "Report_" + scenario.getitemName().replaceAll("\\s+", "_") + ".txt";

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
            System.out.println("Η αναφορά αποθηκεύτηκε στο αρχείο: " + filename);
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την αποθήκευση της αναφοράς: " + e.getMessage());
        }
    }
}
