package logic;

import model.BudgetChange;
import model.Scenario;
import model.Budget;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportGeneratorTest {

    private ReportGenerator generator;
    private Scenario scenario;

    @BeforeEach
    void setUp() {
        generator = new ReportGenerator();


        Budget base = new Budget(2025, List.of());
        scenario = new Scenario(base, "Test Scenario");
    }

    @Test
    void testGenerateSummaryWithNoChanges() {
        String summary = generator.generateSummary(scenario, List.of());

        assertNotNull(summary);
        assertTrue(summary.contains("Δεν βρέθηκαν αλλαγές"));
        assertTrue(summary.contains("Καθαρή Επίπτωση"));
    }

    @Test
    void testGenerateSummaryWithChanges() {
        BudgetChange revChange = new BudgetChange("R1", "Revenue Item", 1000, 1500, "REVENUE");
        BudgetChange expChange = new BudgetChange("E1", "Expenditure Item", 2000, 1800, "EXPENDITURE");

        List<BudgetChange> changes = List.of(revChange, expChange);

        String summary = generator.generateSummary(scenario, changes);

        assertNotNull(summary);
        assertTrue(summary.contains("Revenue Item"));
        assertTrue(summary.contains("Expenditure Item"));
        assertTrue(summary.contains("Καθαρή Επίπτωση"));
        assertTrue(summary.contains("+500"));
        assertTrue(summary.contains("-200"));
    }

    @Test
    void testExportToPDFDoesNotThrow() {
        String content = "Test report content";

        assertDoesNotThrow(() -> {
            generator.exportToPDF(scenario, content);
        });


        File file = new File("Report_" + scenario.getitemName().replaceAll("\\s+", "_") + ".txt");
        assertTrue(file.exists());

  
        file.delete();
    }
}
