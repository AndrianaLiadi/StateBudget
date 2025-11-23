import.java.util.Scanner;
public class Budget{
    public static void listMaker(){
        List<BudgetItem> allItems = budget.getItems();
    }

    public static int getYear() {
        scanner Scanner = new Scanner
        System.out.println("Enter the Budjet Year")
        int year = scanner.nextInt();
        return year 
    }

    public static void totalRevenue(){
        public int trevenue = 0;
        List<BudgetItem> revenue = new ArrayList<>();
        for (BudgetItem item : allItems){
            if (item.getType() == BudgetItem.Type.REVENUE){
                revenues.add(item);
                trevenue = trevenue + item;
            }
        }
    }

    public static void totalExpenditure(){
        public int texpenditure = 0;
        List<BudgetItem> expenditure = new ArrayList<>();
        for (BudgetItem item : allItems){
            if (item.getType() == BudgetItem.Type.EXPENDITURE){
                expeditures.add(item);
                texpenditure = texpenditure + item;
            }
        }
    }

    public static int deficitFinder(int trevenue, int texpenditure){
        int deficit = trevenue - texpenditure;
        return deficit;
    }
    
    public static double getItembyCode(){
        scanner Scanner = new Scanner
        System.out.println("Enter the Code of the Item")
    }
}