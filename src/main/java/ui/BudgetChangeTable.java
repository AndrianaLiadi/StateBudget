package ui; 


import model.BudgetChange;
import java.util.List;


public class BudgetChangeTable {


List<BudgetChange> changes; 

public BudgetChangeTable(List<BudgetChange> changes) {
this.changes = changes;
}
public void printTable() {
System.out.println(" ΑΛΛΑΓΜΕΝΟΣ ΠΙΝΑΚΑΣ ");
for (BudgetChange ch : changes) {
System.out.println(ch.getItemCode() + " : " + ch.getItemName() + " Από " + ch.getOldValue() + " -> " + ch.getNewValue());
}
System.out.println("Σύνολο αλλαγών: " + changes.size());
}
}