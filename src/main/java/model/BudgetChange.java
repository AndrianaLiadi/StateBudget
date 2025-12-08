package model;

public class BudgetChange {

    private String itemCode;
    private String itemName; 
    private String type;
    private long oldValue;
    private long newValue;

    public BudgetChange(String itemCode, String itemName, long oldValue, long newValue, String type) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.type = type;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public long getOldValue() {
        return oldValue;
    }

    public long getNewValue() {
        return newValue;
    }

    public long getDifference() {
        return newValue - oldValue;
    }

    public String getType() {
        return type;
    }
    //dinetai i posostiaia metavoli
    public double getPercentageChange() {
        if (oldValue == 0) {
            return newValue != 0 ? 100.0 : 0.0;
        }
        return ((double)(newValue - oldValue) / oldValue) * 100.0;
    }

    public void setNewValue(long newValue) {
        this.newValue = newValue;
    }
}
