package src.main.java.ui;



import src.main.java.model.BudgetChange;

import src.main.java.model.Scenario;



import java.util.List;


public class ReportGenerator {

    public String generateSummary(Scenario scenario, List<BudgetChange> changes) {

        StringBuilder sb = new StringBuilder();



        sb.append("ΑΝΑΦΟΡΑ ΣΕΝΑΡΙΟΥ\n");

        sb.append("────────────────────────────\n\n");



        sb.append("Σενάριο: ").append(scenario.getName()).append("\n");

        sb.append("Βασικός Προϋπολογισμός: ").append(scenario.getBase().getYear()).append("\n\n");



        sb.append("Μεταβολές:\n");

        sb.append("────────────────────────────\n");



        if (changes.isEmpty()) {

            sb.append("Δεν υπάρχουν αλλαγές.\n");

        } else {

            for (BudgetChange change : changes) {

                sb.append("Κωδικός: ").append(change.getCode()).append("\n");

                sb.append("Κατηγορία: ").append(change.getName()).append("\n");

                sb.append("Παλαιό ποσό: ").append(change.getOldAmount()).append(" €\n");

                sb.append("Νέο ποσό: ").append(change.getNewAmount()).append(" €\n");

                sb.append("Διαφορά: ").append(change.getDifference()).append(" €\n");

                sb.append("-----------------------------\n");

            }

        }



        sb.append("\nΤέλος αναφοράς.\n");



        return sb.toString();

    }

}