package data;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Budget{

    private List<BudgetItem> items;

    public Budget() {
        this.items = new ArrayList<>();
    }
    
    public List<BudgetItem> getItems() {
        return this.items;
    }

    public List<BudgetItem> listMaker(){
        return this.getItems();
    }

    public static int getYearFromUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Budget Year");
        int year = scanner.nextInt();
        return year;
    }

    public double totalRevenue(){
        double trevenue = 0;
        List<BudgetItem> revenue = new ArrayList<>();
        for (BudgetItem item : this.items){
            if (item.getType().equals("REVENUE")){
                revenue.add(item);
                trevenue = trevenue + item.getAmount();
            }
        }
        return trevenue;
    }

    public double totalExpenditure(){
        double texpenditure = 0;
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
        if (trevenue < texpenditure) {
            double deficit = texpenditure - trevenue;
            System.out.println("The deficit is " + deficit);
            return deficit;
        } else {
            double surplus = trevenue - texpenditure;
            System.out.println("The surplus is " + surplus);
            return surplus;
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
        return f;
    }
}