package ui;

import model.Budget;
import model.BudgetItem;
import data.BudgetDataLoader;

import java.util.Comparator;
import java.util.List;

public class BudgetTablePrinter {



    public void printBudget(Budget budget) {

        System.out.println("---------ΚΡΑΤΙΚΟΣ ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ-------");

        printSection("ΕΣΟΔΑ", budget.getItemsByType("REVENUE"));
        printSection("ΕΞΟΔΑ", budget.getItemsByType("EXPENDITURE"));

        System.out.println("    ΣΥΝΟΛΙΚΑ   ");
        System.out.println("Έσοδα: " + budget.getTotalRevenue());
        System.out.println("Έξοδα: " + budget.getTotalExpenditure());
        System.out.println("Ισοζύγιο: " + budget.getBalance());
        System.out.println(  );
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
