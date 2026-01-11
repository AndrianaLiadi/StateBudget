package ui;

import model.Budget;
import model.BudgetItem;
import java.util.List;

public class BudgetTablePrinter {

    public void printBudget(Budget budget) {
        System.out.println("---------ΚΡΑΤΙΚΟΣ ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ---------");

        printSection("ΕΣΟΔΑ", budget.getItemsByType("REVENUE"));
        printSection("ΕΞΟΔΑ", budget.getItemsByType("EXPENDITURE"));

        System.out.println("    ΣΥΝΟΛΙΚΑ    ");
        System.out.println("Έσοδα: " + budget.totalRevenue());
        System.out.println("Έξοδα: " + budget.totalExpenditure());
        System.out.println("Ισοζύγιο: " + budget.surplusdeficitFinder(budget.totalRevenue(), budget.totalExpenditure()));
        System.out.println();
    }

    public void printSection(String title, List<BudgetItem> items) {
        System.out.println(title);
        System.out.println("Κώδικας | Ονομασία | Ποσό");
        System.out.println("------------------------------------------------");

        items.stream()
                .forEach(item -> {
                    System.out.println(
                        item.getCode() + " | " +
                        item.getName() + " | " +
                        item.getAmount()
                    );
                });

        System.out.println();
    }
}