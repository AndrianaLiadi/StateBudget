package data;

import model.Budget;
import model.BudgetItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BudgetDataLoaderTest {
    private BudgetDataLoader loader;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        loader = new BudgetDataLoader();
        
        //Make temporary csv file
        tempFile = File.createTempFile("test_budget", ".csv");
        
        //adding data like they are in our csv files
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("1. ΕΣΟΔΑ,,,Ευρώ,1.000.000\n");
            writer.write("11.,Φόροι,,,10.000\n");
            writer.write("14.,Πωλήσεις με σύμβολα,,,»2.000\n");
            writer.write("19.,Με δεκαδικά,,,5.500,00\n");
            writer.write("\n");
            writer.write("2. ΕΞΟΔΑ,,,Ευρώ,500.000\n");
            writer.write("21.,Παροχές σε εργαζομένους,,,4.000\n"); 
            writer.write("BadLine,Χωρίς Ποσό,,,\n");
        }
    }

    @AfterEach
    void tearDown() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    void testLoadFromCSV_SuccessfulLoad() {
        Budget budget = loader.loadFromCSV(tempFile.getAbsolutePath(), 2025);
        assertNotNull(budget, "Το budget δεν πρέπει να είναι null");
        assertEquals(2025, budget.getYear());
        
        List<BudgetItem> items = budget.getItems();
        assertFalse(items.isEmpty(), "Η λίστα δεν πρέπει να είναι άδεια");
        //the lines have to be 4
        assertEquals(4, items.size());
    }

    @Test
    void testLoadFromCSV_ParsingLogic() {
        Budget budget = loader.loadFromCSV(tempFile.getAbsolutePath(), 2025);
        List<BudgetItem> items = budget.getItems();
        BudgetItem item11 = findItemByCode(items, "11");
        assertNotNull(item11);
        assertEquals("Φόροι", item11.getName());
        assertEquals("REVENUE", item11.getType());
        assertEquals(10000L, item11.getAmount());
        BudgetItem item14 = findItemByCode(items, "14");
        assertNotNull(item14);
        assertEquals(2000L, item14.getAmount());
        BudgetItem item21 = findItemByCode(items, "21");
        assertNotNull(item21);
        assertEquals("EXPENDITURE", item21.getType());
        assertEquals(4000L, item21.getAmount());
    }
}