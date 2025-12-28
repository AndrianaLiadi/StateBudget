package main;

import model.Budget;
import model.BudgetItem;
import model.BudgetChange;
import model.Scenario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestMain {
    
    private Budget testBudget;
    private List<BudgetItem> testItems;
    
    @BeforeEach
    void setUp() {
        // Δημιουργία test budget
        testItems = Arrays.asList(
            new BudgetItem("1001", "Φόροι Εισοδήματος", "Έσοδα", 1000000L),
            new BudgetItem("2001", "Μισθοδοσία", "Έξοδα", 500000L),
            new BudgetItem("2002", "Υποδομές", "Έξοδα", 300000L)
        );
        
        testBudget = new Budget(2024);
        for (BudgetItem item : testItems) {
            testBudget.addItem(item);
        }
    }
}