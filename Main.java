import data.BudgetDataLoader;
import java.io.IOException;
import java.util.Scanner; 
import javax.swing.SwingUtilities; 


public class Main {

    public static void main(String[] args) {
        
    BudgetDataLoader dataloader = new BudgetDataLoader(); 
        String csvFilePath = "budget_data.csv"; 
        String jsonOutputFilePath = "budget_output.json";
        String jsonInputFilePath = "budget_output.json";

        int year = 2024; // etos proypologismou//

        // fortosh apo CSV//
        Budget loadedBudget = null;
        try {
            loadedBudget = dataLoader.loadFromCSV(csvFilePath, year);
        } catch (IOException e) {
            e.printStackTrace();
            return; // stamataei an den fortothei to archeio//
        }

        // Apothikefsfh se jason//
        if (loadedBudget != null) {
                 try {
                dataLoader.saveToJSON(loadedBudget, jsonOutputFilePath);
            
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

            SwingUtilities.invokeLater(() -> {
            MainApp app = new MainApp();
            app.setVisible(true);
        });

        Budget budget = new Budget(); // dhmioygia antikeimenou budget gia thn klish ths klashs budget//
        int year = budget.getYear(); 
        System.out.println( "Επεξεργάζεστε την προϋπολογισμό του έτους:" + year);

        int totalRev = budget.totalRevenue();
        System.out.println( "Εδώ παρατίθενται τα συνολικά έσοδα:" + totalRev);

        int totalExp = budget.totalExpenditure();
        System.out.println( "Εδώ παρατίθενται τα συνολικά έξοδα:" + totalExp);

        




    }
} 