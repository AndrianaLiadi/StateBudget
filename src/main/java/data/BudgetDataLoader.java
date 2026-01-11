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

public class BudgetDataLoader {
    private long cleanAndParseAmount(String amountStr) throws NumberFormatException {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return 0;
        }

        // afairesh perittwn
        String cleaned = amountStr.replaceAll("\\.", "").replaceAll("»", "").trim();
        if (cleaned.contains(",")) {
            cleaned = cleaned.substring(0, cleaned.indexOf(','));
        }

        //ta kanw long
        return Long.parseLong(cleaned);
    }

    //pairnei to arxeio kai ftiaxnei to budget
    public Budget loadFromCSV(String filePath, int year) {
        List<BudgetItem> items = new ArrayList<>();
        String line;
        String csvSplitBy = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"; 
        String currentType = null;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }

                String[] data = line.split(csvSplitBy, -1);

                if (data.length < 2) {
                    continue;
                }

                String codePart = data[0].replaceAll("\"", "").trim();
                String name = data[1].replaceAll("\"", "").trim();
                
                String amountStr = null;
                if (data.length > 4) {
                    amountStr = data[4];
                } else if (data.length > 2) {
                    amountStr = data[data.length - 1]; 
                }

                if (codePart.contains("ΕΣΟΔΑ")) {
                    currentType = "REVENUE";
                    continue;
                }
                if (codePart.contains("ΕΞΟΔΑ")) {
                    currentType = "EXPENDITURE";
                    continue;
                }

                if (currentType != null && !codePart.isEmpty()) {
                    try {
                        String cleanCode = codePart.replaceAll("\\.", "").trim();
                        long amount = cleanAndParseAmount(amountStr);

                        items.add(new BudgetItem(cleanCode, name, currentType, amount));
                    } catch (NumberFormatException e) {
                    }
                }
            }
            return new Budget(year, items);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
