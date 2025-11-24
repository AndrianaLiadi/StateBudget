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

    public String getCode(){
        return code;
    }

    public String getName(){
        return code;
    }
     
    public String getType(){
        return type;
    }

    public double getAmount(){
        return amount;
    }

    
}