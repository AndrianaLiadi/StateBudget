package src.main.java.logic;

import src.main.java.data.BudgetDataLoader;
import src.main.java.model.Budget;
import src.main.java.model.BudgetItem;
import src.main.java.model.BudgetChange;
import src.main.java.model.Scenario;
import src.main.java.ui.ReportGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BudgetService {
    private final BudgetDataLoader objloader;
    private final ReportGenerator reportGenerator;

    public BudgetService(BudgetDataLoader objloader, ReportGenerator reportGenerator) {
        this.objloader = objloader;
        this.reportGenerator = reportGenerator;
    }

    public Budget loadBudget(int year) {
        String filename = "budget_" + year + ".json"; 
        //edw xrhsimopoiw th BudgetDataLoader gia ton xrono pou thelw
         try {
            return objloader.loadFromJSON(filename);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load budget file: " + filename, e);
        }
    }

    public long calculateDeficit(Budget budgetnow) {
        return budgetnow.getDeficitOrSurplus();
        //xrhsimopoiw methodo apo to model
    }

    public List<BudgetChange> compareBudgets(Budget budgetA, Budget budgetB) {
        List<BudgetChange> differences = new ArrayList<>();
        List<String> processedCodes = new ArrayList<>(); //wste na mhn ginetai dyo fores
        for (BudgetItem itemA : budgetA.getItems()) {
            String code = itemA.getCode();
            processedCodes.add(code); //exei xrhsimopoihthei
            BudgetItem itemB = budgetB.getItemByCode(code);
            long amountA = itemA.getAmount();
            long amountB;
            if (itemB != null) {
                amountB = itemB.getAmount();
            } else {
                amountB = 0L;
            }
            //edw prepei na dw ton kwdika tou BudgetChange
            if (amountA != amountB) {
                BudgetChange change = new BudgetChange(code, itemA.getName(), amountA, amountB);
                differences.add(change);
            }
        }
        for (BudgetItem itemB : budgetB.getItems()) {
            String code = itemB.getCode();
            //elegxw thn periptwsh sto budgetB na yparxei kati pou den yparxei sto A
            if (!processedCodes.contains(code)) {
                long amountA = 0L;
                long amountB = itemB.getAmount();
                //edw pali prepei na dw ton kwdika tou BudgetChange
                BudgetChange change = new BudgetChange(code, itemB.getName(), amountA, amountB);
                differences.add(change);
            }

        }
        return differences;
    }


    public String analyzeImpact(Budget base, Budget modified, String name) {
        Scenario tempScenario = new Scenario(base, name); //prepei na dw ton kwdika kai ton kataskevasth tou Scenario
        List<BudgetChange> changes = compareBudgets(base, modified);
        return reportGenerator.generateSummary(tempScenario, changes);
    }
}
