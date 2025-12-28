package ui;

import model.Budget;
import model.BudgetItem;
import java.util.Comparator;
import java.util.List;

public class BudgetTablePrinter {


// ektiposi tou pinaka
    public void printBudget(Budget budget) {

        System.out.println("---------ΚΡΑΤΙΚΟΣ ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ---------");

        printSection("ΕΣΟΔΑ", budget.getItemsByType("REVENUE"));
        printSection("ΕΞΟΔΑ", budget.getItemsByType("EXPENDITURE"));

        System.out.println("    ΣΥΝΟΛΙΚΑ   ");
        System.out.println("Έσοδα: " + budget.totalRevenue());
        System.out.println("Έξοδα: " + budget.totalExpenditure());
        System.out.println("Ισοζύγιο: " + budget.surplusdeficitFinder(budget.totalRevenue(), budget.totalExpenditure()));
        System.out.println(  );
    }

    public void printSection(String title, List<BudgetItem> items) {

        System.out.println( );
        System.out.println("Κώδικας | Ονομασία | Ποσό");
        System.out.println( );

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
