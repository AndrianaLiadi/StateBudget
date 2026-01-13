package main;

import ui.BudgetTablePrinter;
import ui.BudgetChangeTable;
import data.BudgetDataLoader;
import model.Budget;
import model.BudgetChange;
import model.BudgetItem;
import model.Scenario;

import java.util.Scanner; 

/**
 * Η κλάση {@code Main} αποτελεί το σημείο εισόδου (entry point) της εφαρμογής κονσόλας (CLI).
 * <p>
 * Συντονίζει τη συνολική ροή εκτέλεσης του προγράμματος:
 * <ol>
 * <li>Ζητάει δεδομένα εισόδου από τον χρήστη (έτος, αρχείο CSV).</li>
 * <li>Φορτώνει και παρουσιάζει τον αρχικό προϋπολογισμό.</li>
 * <li>Επιτρέπει τη διαδραστική δημιουργία σεναρίων με προσθήκη αλλαγών.</li>
 * <li>Υπολογίζει και παρουσιάζει τα αποτελέσματα και τις διαφορές του σεναρίου.</li>
 * </ol>
 * </p>
 */
public class Main {

    /**
     * Η κύρια μέθοδος που εκτελείται κατά την εκκίνηση της εφαρμογής.
     * * @param args Τα ορίσματα γραμμής εντολών (δεν χρησιμοποιούνται στην παρούσα έκδοση).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Εισαγωγή βασικών στοιχείων
        System.out.println("Εισάγετε το έτος του κρατικού προυπολογισμού:");
        int year = scanner.nextInt();
        scanner.nextLine(); // buffer consumption

        // 2. Φόρτωση δεδομένων από CSV
        BudgetDataLoader loader = new BudgetDataLoader();
        System.out.println("Εισάγετε το path του αρχείου CSV");
        String filePath = scanner.nextLine();
        
        Budget budget = loader.loadFromCSV(filePath, year);
        if (budget == null) {
            System.out.println("Αποτυχία φόρτωσης από CSV");
            scanner.close();
            return;
        }
        
        // 3. Παρουσίαση αρχικού πίνακα
        System.out.println("Εδώ παρέχεται ο πίνακας του Κρατικού Προϋπολογισμού");
        BudgetTablePrinter printer = new BudgetTablePrinter();
        printer.printBudget(budget);
    
        // 4. Εμφάνιση συγκεντρωτικών στοιχείων
        System.out.println("Επεξεργάζεστε την προϋπολογισμό του έτους:" + year);
        System.out.println("Εδώ παρατίθενται τα συνολικά έσοδα:" + budget.totalRevenue());
        System.out.println("Εδώ παρατίθενται τα συνολικά έξοδα:" + budget.totalExpenditure());

        // 5. Δημιουργία Σεναρίου
        System.out.println("\nΔώστε το σεναριό σας!");
        String scenarioName = scanner.nextLine();
        Scenario scenario = new Scenario(budget, scenarioName);

        boolean addMore = true;

        // 6. Βρόχος προσθήκης αλλαγών
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
            scanner.nextLine(); 
            
            System.out.println("Εισάγετε τύπο αλλαγής (increase/decrease):");
            String changeType = scanner.nextLine(); 

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

        // 7. Εφαρμογή αλλαγών και παραγωγή αναφοράς
        scenario.applyChanges();
        scenario.generateSummary();

        // 8. Εκτύπωση αποτελεσμάτων
        BudgetChangeTable table = new BudgetChangeTable(scenario.getChanges());
        table.printTable();

        System.out.println("\n=== Σύνοψη ===");
        System.out.println(scenario.getSummary());

        scanner.close();
    }
}