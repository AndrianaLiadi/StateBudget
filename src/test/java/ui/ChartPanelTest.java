package ui;

import static org.junit.jupiter.api.Assertions.*;

import model.Budget;
import model.BudgetItem;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

class ChartPanelTest {

    @Test
    void testChartPanelInitialization() {
        Budget base = new Budget(2025, List.of());
        Budget modified = new Budget(2025, List.of());

        ChartPanel panel = new ChartPanel(base, modified);

        assertNotNull(panel);
        assertEquals(Color.WHITE, panel.getBackground());
        assertEquals(new Dimension(650, 420), panel.getPreferredSize());
    }

    @Test
    void testPaintComponentWithNullBudgetsDoesNotThrow() {
        ChartPanel panel = new ChartPanel(null, null);

        assertDoesNotThrow(() -> paint(panel));
    }

    @Test
    void testPaintComponentWithEmptyBudgetsDoesNotThrow() {
        Budget base = new Budget(2025, List.of());
        Budget modified = new Budget(2025, List.of());

        ChartPanel panel = new ChartPanel(base, modified);

        assertDoesNotThrow(() -> paint(panel));
    }

    @Test
    void testPaintComponentWithDataDoesNotThrow() {
        BudgetItem b1 = new BudgetItem("A1", "Test A1", 1_000);
        BudgetItem b2 = new BudgetItem("B1", "Test B1", 2_500);

        Budget base = new Budget(2025, List.of(b1));
        Budget modified = new Budget(2025, List.of(b2));

        ChartPanel panel = new ChartPanel(base, modified);

        assertDoesNotThrow(() -> paint(panel));
    }

    @Test
    void testPaintComponentWithDuplicateCodesDoesNotThrow() {
        BudgetItem b1 = new BudgetItem("X", "Item 1", 100);
        BudgetItem b2 = new BudgetItem("X", "Item 2", 200);

        Budget base = new Budget(2025, List.of(b1, b2));
        Budget modified = new Budget(2025, List.of(b1));

        ChartPanel panel = new ChartPanel(base, modified);

        assertDoesNotThrow(() -> paint(panel));
    }



    private void paint(JPanel panel) {
        panel.setSize(700, 500);

        BufferedImage image = new BufferedImage(
                panel.getWidth(),
                panel.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = image.createGraphics();
        panel.paint(g2);
        g2.dispose();
    }
}
