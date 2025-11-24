public class BudgetItem{
    
    private String code;
    private String name;
    private String type;
    private double amount;

    public BudgetItem(String code, String name, String type, double amount){
        this.code = code;
        this.name = name;
        this.type = type;
        this.amount = amount;
    }

    public static String getCode(){
        return code;
    }

    public static String getName(){
        return code;
    }
     
    public static String getType(){
        return type;
    }

    public static double getAmount(){
        return amount;
    }

    
}