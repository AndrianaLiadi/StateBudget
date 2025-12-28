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

    @Test
    @DisplayName("Test δημιουργίας Scenario με budget")
    void testScenarioCreation() {
        Scenario scenario = new Scenario(testBudget, "Test Scenario");
        
        assertNotNull(scenario);
        assertEquals("Test Scenario", scenario.getName());
        assertEquals(testBudget, scenario.getOriginalBudget());
        assertTrue(scenario.getChanges().isEmpty());
    }
    @Test
    @DisplayName("Test προσθήκης αλλαγής στο scenario")
    void testAddChangeToScenario() {
        Scenario scenario = new Scenario(testBudget, "Test Scenario");
        BudgetItem item = testItems.get(0);
        
        BudgetChange change = new BudgetChange(
            item.getCode(),
            item.getName(),
            item.getAmount(),
            1200000L,
            "increase"
        );
        
        scenario.getChanges().add(change);
        
        assertEquals(1, scenario.getChanges().size());
        assertEquals(item.getCode(), scenario.getChanges().get(0).getCode());
    }
}