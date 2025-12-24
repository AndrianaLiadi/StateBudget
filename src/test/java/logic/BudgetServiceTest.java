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
        items.add(new BudgetItem("11", "Φόροι", "REVENUE", 56_597_000_000L));
        items.add(new BudgetItem("12", "Κοινωνικές εισφορές", "REVENUE", 56_000_000L));
        items.add(new BudgetItem("21", "Παροχές σε εργαζομένους", "EXPENDITURE", 14_849_625_000L));

        Budget budget = new Budget(2024, items);

        double result = budgetService.calculateSurplusOrDeficit(budget);

        assertEquals(41_803_375_000.0, result, 0.001, "Ο υπολογισμός πλεονάσματος δεν είναι σωστός");
    }
}
