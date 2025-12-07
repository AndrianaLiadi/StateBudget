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

        System.out.println( "Εδώ παρατίθενται τα συνολικά έσοδα:" +  budget.totalRevenue());

        
        System.out.println( "Εδώ παρατίθενται τα συνολικά έξοδα:" + budget.totalExpenditure());



        System.out.println("Εισάγετε κωδικό");
        BudgetItem item = budget.getItembyCode();

        if (item != null) {
            System.out.println("Επεξεργάζεστε: " + item.getName() + " — " + item.getAmount());
        } else {
            System.out.println("Δεν βρέθηκε στοιχείο για επεξεργασία!");
        }
    
   




    }
} 