package src.main.java.main;

import src.main.java.data.BudgetDataLoader;
import src.main.java.ui.MainApp;
import src.main.java.model.Budget;
import src.main.java.model.BudgetItem;
import src.main.java.model.BudgetChange;
import src.main.java.model.Scenario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner; 
import javax.swing.SwingUtilities; 


public class Main {

    public static void main(String[] args) {
        
    System.out.println("Enter Budget Year:");
        int year = scanner.nextInt();
        scanner.nextLine(); 

    
// fortwsh apo  CSV
        BudgetDataLoader loader = new BudgetDataLoader(); // dhmioyrgia adikeimenou ths BudgetdataLoader
        Budget budget = loader.loadFromCSV(filePath, year);

        if (budget == null) {
            System.out.println(" Αποτυχία φόρτωσης από CSV");
            return;
        }


        Budget budget = new Budget(); // dhmioygia antikeimenou budget gia thn klish ths klashs budget//
    
        System.out.println( "Επεξεργάζεστε την προϋπολογισμό του έτους:" + year);

        int totalRev = budget.totalRevenue();
        System.out.println( "Εδώ παρατίθενται τα συνολικά έσοδα:" + totalRev);

        int totalExp = budget.totalExpenditure();
        System.out.println( "Εδώ παρατίθενται τα συνολικά έξοδα:" + totalExp);

        
BudgetItem item1 = new BudgetItem(csvFilePath, jsonOutputFilePath, jsonInputFilePath, totalExp);
    BudgetItem item2 = new BudgetItem(csvFilePath, jsonOutputFilePath, jsonInputFilePath, totalExp);   

    List<BudgetItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        Budget baseBudget = new Budget(items);




    }
} 