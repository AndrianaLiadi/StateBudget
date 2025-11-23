package logic;

import data.BudgetDataLoader;
import model.Budget;
import model.BudgetItem;
import model.BudgetChange;
import model.Scenario;
import util.ReportGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BudgetService {
    private final BudgetDataLoader objloader;

    public BudgetService(BudgetDataLoader objloader) {
        this.objloader = objloader;
    }

    public Budget loadBudget(int year) {
        String filename = "budget_" + year + ".json"; 
        //edw xrhsimopoiw th BudgetDataLoader gia ton xrono pou thelw
        Budget budget = objloader.loadFromJSON(filename); //analoga pali
        return budget;
    }

    public long calculateDeficit(Budget budgetnow) {
        return budgetnow.getDeficitOrSurplus();
        //xrhsimopoiw methodo apo to model
    }

    public List<BudgetChange> compareBudgets(Budget budgetA, Budget budgetB) {
        List<BudgetChange> differences = new ArrayList<>;
        List<String> processedCodes = new ArrayList<>; //wste na mhn ginetai dyo fores
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
                double amountA = 0L;
                double amountB = itemB.getAmount();
                //edw pali prepei na dw ton kwdika tou BudgetChange
                BudgetChange change = new BudgetChange(code, itemA.getName(), amountA, amountB);
                differences.add(change);
            }

        }
        return differences;
    }

    private final ReportGenerator reportGenerator;
    public String analyzeImpact(Budget base, Budget modified, String description) {
        Scenario tempScenario = new Scenario(base, description); //prepei na dw ton kwdika kai ton kataskevasth tou Scenario
        List<BudgetChange> changes = compareBudgets(base, modified);
        return reportGenerator.generateSummary(tempScenario, changes);
    }
}
