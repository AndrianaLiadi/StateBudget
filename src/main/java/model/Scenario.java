package model;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    private String itemName;
    private Budget baseBudget;
    private List<BudgetChange> changes;
    private Budget modifiedBudget;
    private String summary;

    public Scenario(Budget baseBudget, String itemName) {
        this.baseBudget = baseBudget;
        this.itemName = itemName;
        this.changes = new ArrayList<>();
    }

    public String getitemName() {
        return itemName;
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
            BudgetItem newItem = new BudgetItem(item.getCode(), item.getName(), item.getType(), item.getAmount());
            newItems.add(newItem);
        }

        for (BudgetChange change : changes) {

            BudgetItem targetItem = null;
            for (BudgetItem it : newItems) {
                if (it.getCode().equals(it.getCode())) {
                    targetItem = it;
                    break;
                }
            }

             if (targetItem != null) {
                targetItem.updateAmount(change.getNewValue());
            } else {
                BudgetItem y = new BudgetItem(
                        change.getItemCode(),
                        change.getItemName(),
                        change.getType(),
                        change.getNewValue());
                
                newItems.add(y);
            }
        }

        this.modifiedBudget = new Budget(Budget.getYear(), newItems);
    }
    //prosthiki syntomis perilipsis sxetika me tis allages pou pragmatopoiithikan
    public void generateSummary() {
        if (changes == null || changes.isEmpty()) {
            this.summary = "Δεν υπάρχει καμία αλλαγή στο συγκεκριμένο σενάριο";
            return;
        }

         StringBuilder sb = new StringBuilder();
        sb.append("Σενάριο: ").append(itemName).append("\n");
        sb.append("Αλλαγές που εφαρμόστηκαν:\n\n");

        for (BudgetChange change : changes) {
            sb.append("- ")
              .append(change.getItemName()).append(" (")
              .append(change.getItemCode()).append(")")
              .append(": από ")
              .append(change.getOldValue())
              .append(" σε ")
              .append(change.getNewValue())
              .append("\n");
        }

        this.summary = sb.toString();
    }
}
