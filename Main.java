Import java.util.Scanner;
Import 
public class Main {
    public static void main(String[] args) {
        Budget budget = new Budget(); // dhmioygia antikeimenou budget gia thn klish ths klashs budget//
        int year = budget.getYear(); 
        System.out.println( "Επεξεργάζεστε την προϋπολογισμό του έτους:" + year);

        int totalRev = budget.totalRevenue();
        System.out.println( "Εδώ παρατίθενται τα συνολικά έσοδα:" + totalRev);

        int totalExp = budget.totalExpenditure();
        System.out.println( "Εδώ παρατίθενται τα συνολικά έξοδα:" + totalExp);



    }
} 