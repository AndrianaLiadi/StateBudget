package util;

import model.Budget;
import model.BudgetChange;
import model.BudgetItem;

import java.util.List;

public class BudgetComparisonPrinter {

    private static final String ANSI_YELLOW = "\u001B[43m"; // Κίτρινο background
    private static final String ANSI_RESET = "\u001B[0m";

    public void printBudgetWithChanges(Budget base, List<BudgetChange> changes) {
        System.out.println("=== Κρατικός Προϋπολογισμός με Αλλαγές " + base.getYear() + " ===");

        System.out.println("\n--- ΕΣΟΔΑ ---");
        System.out.printf("%-10s %-40s %15s%n", "Κωδικός", "Ονομασία", "Ποσό");
        for (BudgetItem item : base.getItems()) {
            if (item.getType() == BudgetItem.Type.REVENUE) {
                boolean changed = isChanged(item.getCode(), changes);
                String display = String.format("%,15d", item.getAmount());
                if (changed) display = ANSI_YELLOW + display + ANSI_RESET;
                System.out.printf("%-10s %-40s %s%n", item.getCode(), item.getName(), display);
            }
        }

        System.out.println("\n--- ΕΞΟΔΑ ---");
        System.out.printf("%-10s %-40s %15s%n", "Κωδικός", "Ονομασία", "Ποσό");
        for (BudgetItem item : base.getItems()) {
            if (item.getType() == BudgetItem.Type.EXPENDITURE) {
                boolean changed = isChanged(item.getCode(), changes);
                String display = String.format("%,15d", item.getAmount());
                if (changed) display = ANSI_YELLOW + display + ANSI_RESET;
                System.out.printf("%-10s %-40s %s%n", item.getCode(), item.getName(), display);
            }
        }

        System.out.println("\n--- Συγκρίσεις ---");
        for (BudgetChange change : changes) {
            System.out.printf("%s | %s | Από: %,d -> Σε: %,d%n",
                    change.getCode(), change.getName(), change.getAmountA(), change.getAmountB());
        }

        System.out.println("\nΣύνολο αλλαγών: " + changes.size());
    }

    private boolean isChanged(String code, List<BudgetChange> changes) {
        return changes.stream().anyMatch(c -> c.getCode().equals(code));
    }
}
