package model;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Budget implements Cloneable{

    private List<BudgetItem> items;
    private int year;

    public Budget(int year, List<BudgetItem> items){
        this.items = items;
        this.year = year;
    }

    public List<BudgetItem> getItems() {
        return this.items;
    }

    public List<BudgetItem> listMaker() {
        return this.getItems();
    }

    public int getYear() {
        return year; 
    }

    public long totalRevenue(){
        long trevenue = 0;
        List<BudgetItem> revenue = new ArrayList<>();
        for (BudgetItem item : this.items) {
            if (item.getType().equals("REVENUE")) {
                revenue.add(item);
                trevenue = trevenue + item.getAmount();
            }
        }
        return trevenue;
    }

    public double totalExpenditure(){
        long texpenditure = 0;
        List<BudgetItem> expenditure = new ArrayList<>();
        for (BudgetItem item : this.items){
            if (item.getType().equals("EXPENDITURE")){
                expenditure.add(item);
                texpenditure = texpenditure + item.getAmount();
            }
        }
        return texpenditure;
    }

    public double surplusdeficitFinder(double trevenue, double texpenditure){
        if (trevenue > texpenditure){
            double surplus = trevenue - texpenditure;
            System.out.println("The surplus is" + surplus);
            return surplus;
        } else {
            double deficit = texpenditure - trevenue;
            System.out.println("The deficit is " + deficit);
            return deficit;
        }
    }
    
    public BudgetItem getItembyCode(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Code of the Item");
        String code = scanner.nextLine();
        BudgetItem f = null;
        for (BudgetItem item : this.getItems()){
            if (item.getCode().equals(code)) {
                f = item;
                break;
            }
        }
        scanner.close();
        return f;
    }

    public List<BudgetItem> getItemsByType(String type) {
        List<BudgetItem> filteredItems = new ArrayList<>();
        for (BudgetItem item : items) {
            if (item.getType() != null && item.getType().equals(type)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    @Override
    public Budget clone() {
        try {
            Budget cloned = (Budget) super.clone();

            cloned.items = new ArrayList<>();
            for (BudgetItem item : this.items) {
                cloned.items.add(item.clone());
            }

            return cloned;

        } catch (CloneNotSupportedException e) {
            System.out.println("Clone Error");
            return null;
        }
    }
}
