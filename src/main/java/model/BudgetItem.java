package src.main.java.model;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class BudgetItem implements Cloneable {

    private String code;
    private String name;
    private String type;
    private long amount;
    private BudgetItem parentCategory;
    private List<BudgetItem> subItems;

    public BudgetItem(String code, String name, String type, double amount) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.subItems = new ArrayList<>();
    }


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public long getAmount() {  
        return amount;
    }

     public BudgetItem getParentCategory() {
        return parentCategory;
    }

    public List<BudgetItem> getSubItems() {
        return subItems;
    }

    public void updateAmount(double newValue) {
        this.amount = newValue;
    }

    public void addSubItem(BudgetItem newItem) {
        newItem.parentCategory = this;
        subItems.add(newItem);
    }

    public double getTotal() {
        double total = this.amount;
        for (BudgetItem i: subItems){
            total = total + i.getTotal();
        }
        return total;
    }

    @Override
    public BudgetItem clone(){
        try {
            BudgetItem cloned = (BudgetItem) super.clone();
            cloned.subItems = new ArrayList<>();
            for (BudgetItem sub:this.subItems) {
                BudgetItem subClone = sub.clone();
                subClone.parentCategory = cloned;
                cloned.subItems.add(subClone);
            }
            return cloned;
        }  catch (CloneNotSupportedException e){
            System.out.println("Clone Error");
            return null;
        }
    }




}