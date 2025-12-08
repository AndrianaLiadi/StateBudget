package main;

import data.BudgetDataLoader;
import model.Budget;
import model.BudgetChange;
import model.BudgetItem;
import model.Scenario;


import java.util.Scanner; 



public class Main {

    public static void main(String[] args) {
    
   Scanner scanner = new Scanner(System.in);

    System.out.println("Enter Budget Year:");
        int year = scanner.nextInt();
        scanner.nextLine(); 

    
// fortwsh apo  CSV
        BudgetDataLoader loader = new BudgetDataLoader(); // dhmioyrgia adikeimenou ths BudgetdataLoader
        System.out.println("Εισάγετε το path του αρχείου CSV");
        String filePath = scanner.nextLine();
        
        Budget budget = loader.loadFromCSV(filePath, year);

        if (budget == null) {
            System.out.println(" Αποτυχία φόρτωσης από CSV");
            scanner.close();
            return; }
        

    
        System.out.println( "Επεξεργάζεστε την προϋπολογισμό του έτους:" + year);

        System.out.println( "Εδώ παρατίθενται τα συνολικά έσοδα:" +  budget.totalRevenue());

        
        System.out.println( "Εδώ παρατίθενται τα συνολικά έξοδα:" + budget.totalExpenditure());

        
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
                System.out.println("Το στοιχείο δεν βρέθηκε! Προσπάθείστε ξανά!");
                continue;
            }

            System.out.println(" Εισάγετε ποσό " + item.getName() + " (" + item.getCode() + "): " + item.getAmount());
            System.out.println("εισάγετε καινούριο ποσό");
            long newAmount = scanner.nextLong();
            scanner.nextLine();
        


        BudgetChange change = new BudgetChange(
                    item.getCode(),
                    item.getName(),
                    item.getAmount(),
                    newAmount
            );
            scenario.getChanges().add(change);

            System.out.println("Θέλετε να προσθέσετε άλλη αλλαγή; (y/n)");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (!answer.equals("y")) addMore = false;
        

        // egarmogh allagvn kai sunopsi
        scenario.applyChanges();
        scenario.generateSummary();

        System.out.println("\n=== Σύνοψη ===");
        System.out.println(scenario.getSummary());

        }
   

         

    scanner.close();
    }
}
