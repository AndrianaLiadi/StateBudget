import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.io.IOException;

public class ReportGenerator {

public String generateSummary(Scenario scenario){
    StringBuilder sb = new StringBuilder();
    List<BudgetChange> changes = scenario.getChanges();
    sb.append("ΑΝΑΦΟΡΑ ΣΕΝΑΡΙΟΥ:"+scenario.getNAme()+ "\n");
    sb.append("--------------\n");

    if (changes == null|| changes.isEmpty()){
        sb.append("Δεν έχουν γίνει αλλαγές")
        return sb.toString();
    }
    sb.append("Σύνολο αλλαγών:"+ changes.size()+ "\n\n");

    for (BudgetChange change: changes){
        sb.append("Κωδικός:"+ change.getCode()+"\n");
        sb.append("Όνομα:"+ change.getName()+ "\n");

        sb.append("Παλιό Ποσό:"+ change.getOldAmount()+"\n");
        sb.append("Νέο Πόσο:"+ change.getNewAmount()+ "\n");
        sb.append("Διαφορά: "+ change.getDifference()+ "\n");
        sb.append("Ποσοστό: "+ change.getPercentageChange()+ "%\n");

        sb.append("-------------\n");
    }
    return sb.toString();
}
public void exportToTextFile(Scenario scenario, String filename){
    try(FileWriter writer = new FileWriter(filename)){
        writer.write(generateSummary(scenario));
        System.out.println("Αποθηκεύτηκε στο αρχείο:"+ filename);
    }catch (IOException e) {
        System.out.println("Σφάλμα κατά την εγγραφή του αρχείου.")
        e.printStackTrace();//Εκτύπωση του εγγράγου
    }
}


