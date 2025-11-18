public class BudgetService {
    private final BudgetDataLoader objloader;

    public BudgetService(BudgetDataLoader objloader) {
        this.objloader = objloader;
    }

    public Budget loadBudget(int year) {
        String filename = 'budget_' + year + '.json'; //analoga pws tha ta paroume
        //edw xrhsimopoiw th BudgetDataLoader gia ton xrono pou thelw
        Budget budget = objloader.loadfromJSON(filename); //analoga pali
        return budget;
    }

    public double calculateDeficit(Budget budgetnow) {
        return budget.getDeficitOrSurplus();
        //xrhsimopoiw methodo apo to 1 
    }

    public List<BudgetChange> compareBudgets(Budget budgetA, Budget budgetB) {
        List<BudgetChange> differences = new ArrayList<>;
        List<String> processedCodes = new ArrayList<>; //wste na mhn ginetai dyo fores
        for (BudgetItem itemA : budgetA.getItems()) {
            String code = itemA.getCode();
            processedCodes.add(code); //exei xrhsimopoihthei
            BudgetItem itemB = budgetB.getItemByCode(code);
            double amountA = itemA.getAmount();
            double amountB;
            if (itemB != null) {
                amountB = itemB.getAmount();
            } else {
                amountB = 0.0;
            }
            //edw prepei na dw ton kwdika tou BudgetChange
            if (amountA != amountB) {
                BudgetChange change = new BudgetChange(code, amountA, amountB);
                differences.add(change);
            }
        }
        for (BudgetItem itemB : budgetB.getItems()) {
            String code = itemB.getCode();
            //elegxw thn periptwsh sto budgetB na yparxei kati pou den yparxei sto A
            if (!processedCodes.contains(code)) {
                double amountA = 0.0
                double amountB = itemB.getAmount();
                //edw pali prepei na dw ton kwdika tou BudgetChange
                BudgetChange change = new BudgetChange(code, amountA, amountB);
                differences.add(change);
            }

        }
        return differences;
    }

    private final ReportGenerator reportGenerator;
    public String analyzeImpact(Budget base, Budget modified) {
        Scenario tempScenario = new Scenario; //prepei na dw ton kwdika kai ton kataskevasth tou Scenario
        tempScenario.setModifiedBudget(modified);
        String summary = reportGenerator.generateSummary(tempScenario);
        return summary;
    }
}
