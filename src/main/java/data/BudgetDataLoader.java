package data;

import model.Budget;
import model.BudgetItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy, -1);
                //gia na mh vlepw kenes h mh xrhsimes grammes:
                if (data.length < 2) {
                    continue;
                }
                String codePart = data[0].trim();
                String name = data[1].trim();
                //elegxw gia na eimaste sigouroi oti to poso einai sth sthlh 5
                String amountStr = (data.length > 4) ? data[4] : null;
                //orizw currentType analoga me to arxeio
                if (codePart.contains("ΕΣΟΔΑ")) {
                    currentType = "REVENUE";
                    continue; 
                }
                if (codePart.contains("ΕΞΟΔΑ")) {
                    currentType = "EXPENDITURE";
                    continue;
                }
                //ftiaxnw to budgetitem
                if (currentType != null && !codePart.isEmpty() && amountStr != null && !amountStr.trim().isEmpty()) {
                    try {
                        //vgazw teleies gia eukolia
                        String cleanCode = codePart.replaceAll("\\.", "").trim();
                        //metatrepw se long
                        long amount = cleanAndParseAmount(amountStr);
                        //ftiaxnw twra to antikeimeno
                        BudgetItem item = new BudgetItem(cleanCode, name, currentType, amount);
                        items.add(item);
                    } catch (NumberFormatException e) {
                        System.err.println("Προσοχή: Δεν μπόρεσε να διαβαστεί το ποσό στη γραμμή: " + line);
                    }
                }
            }
            //return to budget!
            return new Budget(year, items);
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την ανάγνωση του αρχείου: " + e.getMessage());
            return null;
        }
    }
}