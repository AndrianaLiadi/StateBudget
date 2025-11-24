package data;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Budget implements Cloneable{

    private List<BudgetItem> items;

    public Budget() {
        this.items = new ArrayList<>();
    }
    
    public List<BudgetItem> getItems() {
        return this.items;
    }

<<<<<<< HEAD
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
    }
=======
    public static int getYear() {
        (scanner Scanner = new Scanner)
        System.out.println("Enter the Budjet Year");
        int year = scanner.nextInt();
        return year; 
    }

    public static void totalRevenue(){
        int trevenue = 0;
        List<BudgetItem> revenue = new ArrayList<>();
         for (BudgetItem item : allItems) {
            if (item.getType() == BudgetItem.Type.REVENUE) {
                revenues.add(item);
                trevenue = trevenue + item;
>>>>>>> cdd375ae7c28a13f9ee227b643a20bf23b7c1c33
            }
        }
        return trevenue;
    }

    public double totalExpenditure(){
        double texpenditure = 0;
        List<BudgetItem> expenditure = new ArrayList<>();
<<<<<<< HEAD
        for (BudgetItem item : this.items){
            if (item.getType().equals("EXPENDITURE")){
                expenditure.add(item);
                texpenditure = texpenditure + item.getAmount();
            }
        }
=======
        for (BudgetItem item : allItems) {
            if (item.getType() == BudgetItem.Type.EXPENDITURE){
                expeditures.add(item);
                texpenditure = texpenditure + item;
>>>>>>> cdd375ae7c28a13f9ee227b643a20bf23b7c1c33
            }
        }
        return texpenditure;
    }
<<<<<<< HEAD
    public double surplusdeficitFinder(double trevenue, double texpenditure){
        if (trevenue < texpenditure) {
            double deficit = texpenditure - trevenue;
            System.out.println("The deficit is " + deficit);
        }
    }
=======

    public static int deficitFinder(int trevenue, int texpenditure){
        if (trevenue < texpenditure){
            int deficit = trevenue - texpenditure;
            System.out.println("The deficit is", deficit)
>>>>>>> cdd375ae7c28a13f9ee227b643a20bf23b7c1c33
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