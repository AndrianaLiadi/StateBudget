import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BudgetDataLoader {
    private static final String CSV_DELIMITER = ",";

    public Budget loadFromCSV(String filePath, int year) throws IOException {
        Budget budget = new Budget(year);
        BudgetItem.Type currentType = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.contains("1. ΕΣΟΔΑ")) {
                    currentType = BudgetItem.Type.REVENUE;
                    continue;
                }

                if (line.contains("2. ΕΞΟΔΑ")) {
                    currentType = BudgetItem.Type.EXPENDITURE;
                    continue;
                }

                if (currentType == null) continue;

                String[] columns = line.split(CSV_DELIMITER, -1);
                String code = columns[0].trim().replace("\"", "");
                String name = columns.length > 1 ? columns[1].trim().replace("\"", "") : "";

                if (name.isEmpty() || name.contains("Ονομασία") || name.length < 3) continue;

                String amountString = "";

                if (currentType == BudgetItem.Type.REVENUE) {
                    //esoda 5h sthlh
                    amountString = columns.length > 4 ? columns[4].trim().replace("\"", "") : "";
                } else {
                    //exoda 3h sthlh
                    amountString = columns.length > 2 ? columns[2].trim().replace("\"", "") : "";
                }

                long amount = parseAmount(amountString);
                if (amount == 0) continue;

                String finalCode = code.isEmpty() ? name.substring(0, Math.min(name.length, 10)) : code;
                BudgetItem item = new BudgetItem(finalCode, name, currentType, amount);
                budget.addItem(item);
            }
        }
        budget.calculateTotals();
        return budget;
    }
    private long parseAmount(String amountString) {
        if (amountString == null || amountString.isEmpty()) {
            return 0;
        }
        
        String cleaned = raw.replace(".", "").replace(",", "");


        try {
            if (cleaned.endsWith("»")) {
                cleaned = cleaned.substring(0, cleaned.length() - 1);
            }
            return Long.parseLong(cleaned);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void saveToJSON(Budget budget, String outputFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(writer, budget);
        }
    }


    public Budget loadFromJSON(String inputFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(inputFilePath), Budget.class);
    }
}
