package util;

import model.Budget;
import model.BudgetItem;

public class BudgetTablePrinter {

    public void printBudgetTable(Budget budget) {
        System.out.println("=== Κρατικός Προϋπολογισμός " + budget.getYear() + " ===");

        System.out.println("\n--- ΕΣΟΔΑ ---");
        System.out.printf("%-10s %-40s %15s%n", "Κωδικός", "Ονομασία", "Ποσό");
        for (BudgetItem item : budget.getItems()) {
            if (item.getType() == BudgetItem.Type.REVENUE) {
                System.out.printf("%-10s %-40s %,15d%n", item.getCode(), item.getName(), item.getAmount());
            }
        }

        System.out.println("\n--- ΕΞΟΔΑ ---");
        System.out.printf("%-10s %-40s %15s%n", "Κωδικός", "Ονομασία", "Ποσό");
        for (BudgetItem item : budget.getItems()) {
            if (item.getType() == BudgetItem.Type.EXPENDITURE) {
                System.out.printf("%-10s %-40s %,15d%n", item.getCode(), item.getName(), item.getAmount());
            }
        }

        System.out.println("\nΣύνολο Εσόδων: " + budget.getTotalRevenue());
        System.out.println("Σύνολο Εξόδων: " + budget.getTotalExpenditure());
        System.out.println("Έλλειμμα/Πλεόνασμα: " + budget.getDeficitOrSurplus());
    }
}
