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

        if (cleaned.isEmpty()) {
            return 0;
        }

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

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
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