package main;

import ui.BudgetTablePrinter;
import ui.BudgetChangeTable;
import data.BudgetDataLoader;
import model.Budget;
import model.BudgetChange;
import model.BudgetItem;
import model.Scenario;


import java.util.Scanner; 



public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Εισάγετε το έτος του κρατικού προυπολογισμού:");
        int year = scanner.nextInt();
        scanner.nextLine(); // Καθαρισμός buffer

        // φόρτωση από CSV
        BudgetDataLoader loader = new BudgetDataLoader();
        System.out.println("Εισάγετε το path του αρχείου CSV");
        String filePath = scanner.nextLine();
        
        Budget budget = loader.loadFromCSV(filePath, year);
        if (budget == null) {
            System.out.println("Αποτυχία φόρτωσης από CSV");
            scanner.close();
            return;
        }
        
        // εμφάνιση πίνακα
        System.out.println("Εδώ παρέχεται ο πίνακας του Κρατικού Προϋπολογισμού");
        BudgetTablePrinter printer = new BudgetTablePrinter();
        printer.printBudget(budget);
    
        // βασικές πληροφορίες
        System.out.println("Επεξεργάζεστε την προϋπολογισμό του έτους:" + year);
        System.out.println("Εδώ παρατίθενται τα συνολικά έσοδα:" + budget.totalRevenue());
        System.out.println("Εδώ παρατίθενται τα συνολικά έξοδα:" + budget.totalExpenditure());

        System.out.println("\nΔώστε το σεναριό σας!");
        String scenarioName = scanner.nextLine();
        Scenario scenario = new Scenario(budget, scenarioName);

        boolean addMore = true;

        while (addMore) {
            System.out.println("\nΕισάγετε κωδικό σεναρίου αλλαγής!");
            String code = scanner.nextLine();

            BudgetItem item = null;
            for (BudgetItem bItem : budget.getItems()) {
                if (bItem.getCode().equals(code)) {
                    item = bItem;
                    break;
                }
            }

            if (item == null) {
                System.out.println("Το στοιχείο δεν βρέθηκε! Προσπάθειστε ξανά!");
                continue;
            }

            System.out.println("Εισάγετε ποσό " + item.getName() + " (" + item.getCode() + "): " + item.getAmount());
            System.out.println("εισάγετε καινούριο ποσό");
            long newAmount = scanner.nextLong();
            scanner.nextLine(); // ΜΟΝΟ 1 φορά εδώ!
            
            System.out.println("Εισάγετε τύπο αλλαγής (increase/decrease):");
            String changeType = scanner.nextLine(); // Αυτό λείπει από το test σου!

            BudgetChange change = new BudgetChange(
                item.getCode(),
                item.getName(),
                item.getAmount(),
                newAmount, 
                changeType);
            
            scenario.getChanges().add(change);

            System.out.println("Θέλετε να προσθέσετε άλλη αλλαγή; (y/n)");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (!answer.equals("y")) {
                addMore = false;
            }
        }

        // εφαρμογή αλλαγών και σύνοψη
        scenario.applyChanges();
        scenario.generateSummary();

        BudgetChangeTable table = new BudgetChangeTable(scenario.getChanges());
        table.printTable();

        System.out.println("\n=== Σύνοψη ===");
        System.out.println(scenario.getSummary());

        scanner.close();
    }
}

