package logic;

import model.Budget;
import model.BudgetChange;
import model.BudgetItem;
import ui.ReportGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BudgetServiceTest {
    private BudgetService budgetService;
    private ReportGenerator reportGenerator;

    @BeforeEach
    void setUp() {
        reportGenerator = new ReportGenerator();
        budgetService = new BudgetService(null, reportGenerator);
    }

    @Test
    void testCalculateSurplusOrDeficit() {
        List<BudgetItem> items = new ArrayList<>();
        
        //Adding revenue data based on csv file for 2025
        items.add(new BudgetItem("11", "Φόροι", "REVENUE", 60_000_000_000L)); 
        items.add(new BudgetItem("14", "Πωλήσεις αγαθών και υπηρεσιών", "REVENUE", 2_000_000_000L));
        
        //Adding expenditure data
        items.add(new BudgetItem("21", "Παροχές σε εργαζομένους", "EXPENDITURE", 15_000_000_000L));
        items.add(new BudgetItem("22", "Κοινωνικές παροχές", "EXPENDITURE", 40_000_000_000L));
        Budget budget = new Budget(2025, items);
        double result = budgetService.calculateSurplusOrDeficit(budget);
        assertEquals(7_000_000_000.0, result, 0.001, "Το πλεόνασμα πρέπει να είναι 7 δισεκατομμύρια");
    }
    
}
