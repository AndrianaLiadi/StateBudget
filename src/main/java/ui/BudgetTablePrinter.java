package src.main.java.ui;

import src.main.java.model.Budget;
import src.main.java.model.BudgetItem;

import java.util.Comparator;
import java.util.List;

public class BudgetTablePrinter {

    private final UIStyle style = new UIStyle();

    public void printBudget(Budget budget) {

        style.printHeader("ΚΡΑΤΙΚΟΣ ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ");

        printSection("ΕΣΟΔΑ", budget.getItemsByType(BudgetItem.Type.REVENUE));
        printSection("ΕΞΟΔΑ", budget.getItemsByType(BudgetItem.Type.EXPENDITURE));

        style.printSubHeader("ΣΥΝΟΛΙΚΑ");
        System.out.println("Έσοδα: " + budget.getTotalRevenue());
        System.out.println("Έξοδα: " + budget.getTotalExpenditure());
        System.out.println("Ισοζύγιο: " + budget.getBalance());
        style.printLine();
    }

    private void printSection(String title, List<BudgetItem> items) {

        style.printSubHeader(title);
        System.out.println("Κώδικας | Ονομασία | Ποσό");
        style.printLine();

        items.stream()
                .sorted(Comparator.comparing(BudgetItem::getName))
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
