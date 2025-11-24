package model;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    private String name;
    private Budget baseBudget;
    private List<BudgetChange> changes;
    private Budget modifiedBudget;
    private String summary;

    public Scenario(Budget baseBudget, String name) {
        this.baseBudget = baseBudget;
        this.name = name;
        this.changes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Budget getBaseBudget() {
        return baseBudget;
    }

    public List<BudgetChange> getChanges() {
        return changes;
    }
    
    public Budget getModifiedBudget() {
        return modifiedBudget;
    }

    public String getSummary() {
        return summary;
    }

    public void setChanges(List<BudgetChange> changes) {
        this.changes = changes;
    }
    //efarmozei tis allages pou poragmatopoiise o xrhsths
    public void applyChanges() {
        List<BudgetItem> newItems = new ArrayList<>();

        for (BudgetItem item : baseBudget.getItems()) {
            BudgetItem newItem = new BudgetItem(item.getCode(), item.getName(), item.getAmount());
            newItems.add(newItem);
        }
        for (BudgetChange change : changes) {

            BudgetItem targetItem = null;
            for (BudgetItem it : newItems) {
                if (it.getCode().equals(change.getCode())) {
                    targetItem = it;
                    break;
                }
            }