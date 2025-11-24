package model;

public class BudgetChange {

    private String itemCode;
    private String itemName; 
    private long oldValue;
    private long newValue;

    public BudgetChange(String itemCode, String itemName, long oldValue, long newValue) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    