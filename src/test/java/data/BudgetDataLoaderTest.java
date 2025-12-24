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
}