package logic;

import model.Budget;
import model.BudgetChange;
import model.BudgetItem;
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

    @Test
    void testCompareBudgets_RealData_2024_vs_2025() {
        //2024
        List<BudgetItem> items2024 = new ArrayList<>();
        items2024.add(new BudgetItem("11", "Φόροι", "REVENUE", 56_597_000_000L));
        items2024.add(new BudgetItem("12", "Κοινωνικές εισφορές", "REVENUE", 56_000_000L));
        items2024.add(new BudgetItem("21", "Παροχές σε εργαζομένους", "EXPENDITURE", 14_849_625_000L));
        Budget budget2024 = new Budget(2024, items2024);

        //2025
        List<BudgetItem> items2025 = new ArrayList<>();
        items2025.add(new BudgetItem("11", "Φόροι", "REVENUE", 62_055_000_000L));
        items2025.add(new BudgetItem("12", "Κοινωνικές εισφορές", "REVENUE", 60_000_000L));
        items2025.add(new BudgetItem("21", "Παροχές σε εργαζομένους", "EXPENDITURE", 14_889_199_000L));
        Budget budget2025 = new Budget(2025, items2025);

        //compare
        List<BudgetChange> changes = budgetService.compareBudgets(budget2024, budget2025);

        //assert
        assertEquals(3, changes.size(), "Πρέπει να βρεθούν αλλαγές και στα 3 κονδύλια");
        //Taxes
        BudgetChange taxChange = findChangeByCode(changes, "11");
        assertNotNull(taxChange);
        assertEquals(56_597_000_000L, taxChange.getOldValue());
        assertEquals(62_055_000_000L, taxChange.getNewValue());
        assertEquals(5_458_000_000L, taxChange.getDifference(), "Η αύξηση στους φόρους πρέπει να είναι 5.458 δις");
        //Social
        BudgetChange socialChange = findChangeByCode(changes, "12");
        assertNotNull(socialChange);
        assertEquals(56_000_000L, socialChange.getOldValue());
        assertEquals(60_000_000L, socialChange.getNewValue());
        assertEquals(4_000_000L, socialChange.getDifference());
        //Benefits
        BudgetChange benefitsChange = findChangeByCode(changes, "21");
        assertNotNull(benefitsChange);
        assertEquals(14_849_625_000L, benefitsChange.getOldValue());
        assertEquals(14_889_199_000L, benefitsChange.getNewValue());
        assertEquals(39_574_000L, benefitsChange.getDifference());
    }

    @Test
    void testAnalyzeImpact() {
        List<BudgetItem> items = new ArrayList<>();
        items.add(new BudgetItem("11", "Φόροι", "REVENUE", 56_597_000_000L));
        Budget base = new Budget(2024, items);
        List<BudgetItem> modifiedItems = new ArrayList<>();
        modifiedItems.add(new BudgetItem("11", "Φόροι", "REVENUE", 60_000_000_000L));
        Budget modified = new Budget(2025, modifiedItems);
        String report = budgetService.analyzeImpact(base, modified, "Σενάριο Αύξησης Φόρων");
        System.out.println(report);
        assertNotNull(report);
        assertTrue(report.contains("Σενάριο Αύξησης Φόρων"));
        assertTrue(report.contains("Φόροι"));
    }

    private BudgetChange findChangeByCode(List<BudgetChange> changes, String code) {
        for (BudgetChange change : changes) {
            if (change.getItemCode().equals(code)) {
                return change;
            }
        }
        return null;
    }
}



