package data;

import model.Budget;
import model.BudgetItem;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.Charset;
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
        String cvsSplitBy = ",";
        String currentType = null;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), Charset.forName("Windows-1253")))) {

            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }

                String[] data = line.split(cvsSplitBy, -1);

                if (data.length < 2) {
                    continue;
                }

                String codePart = data[0].trim();
                String name = data[1].trim();
                String amountStr = (data.length > 4) ? data[4] : null;

                if (codePart.contains("ΕΣΟΔΑ")) {
                    currentType = "REVENUE";
                    continue;
                }
                if (codePart.contains("ΕΞΟΔΑ")) {
                    currentType = "EXPENDITURE";
                    continue;
                }

                if (currentType != null && !codePart.isEmpty() && amountStr != null && !amountStr.trim().isEmpty()) {
                    try {
                        String cleanCode = codePart.replaceAll("\\.", "").trim();
                        long amount = cleanAndParseAmount(amountStr);
                        
                        BudgetItem item = new BudgetItem(cleanCode, name, currentType, amount);
                        items.add(item);
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
