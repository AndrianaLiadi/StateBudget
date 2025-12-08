package logic;

import data.BudgetDataLoader;
import model.Budget;
import model.BudgetItem;
import model.BudgetChange;
import model.Scenario;
import ui.ReportGenerator;
import java.util.ArrayList;
import java.util.List;
public class BudgetService {
    private final BudgetDataLoader objloader;
    private final ReportGenerator reportGenerator;

    public BudgetService(BudgetDataLoader objloader, ReportGenerator reportGenerator) {
        this.objloader = objloader;
        this.reportGenerator = reportGenerator;
    }
    public Budget loadBudget(int year, String filePath) {
        try {
            return objloader.loadFromCSV(filePath, year);
        } catch (Exception e) {
            throw new RuntimeException("Σφάλμα κατά τη φόρτωση του προϋπολογισμού (" + filePath + ")", e);
        }
    }
    public double calculateSurplusOrDeficit(Budget budget) {
        return budget.totalRevenue() - budget.totalExpenditure();
    }
    public List<BudgetChange> compareBudgets(Budget budgetA, Budget budgetB) {
        List<BudgetChange> differences = new ArrayList<>();
        List<String> processedCodes = new ArrayList<>(); 
        for (BudgetItem itemA : budgetA.getItems()) {
            String code = itemA.getCode();
            processedCodes.add(code);
            BudgetItem itemB = budgetB.getItembyCode(code); 
            long amountA = itemA.getAmount();
            //gia an diagrafhke to kondylio
            long amountB = (itemB != null) ? itemB.getAmount() : 0L;
            if (amountA != amountB) {
                BudgetChange change = new BudgetChange(code, itemA.getName(), amountA, amountB);
                differences.add(change);
            }
        }
        for (BudgetItem itemB : budgetB.getItems()) {
            String code = itemB.getCode();
            //an yparxei stous elegxous
            if (!processedCodes.contains(code)) {
                long amountA = 0L;
                long amountB = itemB.getAmount();
                BudgetChange change = new BudgetChange(code, itemB.getName(), amountA, amountB);
                differences.add(change);
            }
        }
        return differences;
    }
    public String analyzeImpact(Budget base, Budget modified, String scenarioName) {
        //ftiaxnw senario
        Scenario scenario = new Scenario(base, scenarioName);
        //vriskw diafores
        List<BudgetChange> changes = compareBudgets(base, modified);
        //tis settarw
        scenario.setChanges(changes);
        scenario.generateSummary(); 
        //return report
        return reportGenerator.generateSummary(scenario);
    }
}