package ui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppControllerTest {

    private AppController app;

    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            app = new AppController();
            app.setVisible(false);
        });
    }

    @AfterEach
    void tearDown() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            if (app != null) {
                app.dispose();
            }
        });
    }

    @Test
    void testAppControllerInitialization() {
        assertNotNull(app);
        assertEquals("State Budget App", app.getTitle());
        assertEquals(700, app.getWidth());
        assertEquals(500, app.getHeight());
    }

    @Test
    void testShowHomeScreenDoesNotThrow() {
        assertDoesNotThrow(() -> {
            SwingUtilities.invokeAndWait(() -> {
                app.showScreen(AppController.HOME);
            });
        });
    }

    @Test
    void testShowBudgetScreenDoesNotThrow() {
        assertDoesNotThrow(() -> {
            SwingUtilities.invokeAndWait(() -> {
                app.showScreen(AppController.BUDGET);
            });
        });
    }

    @Test
    void testShowScenarioScreenDoesNotThrow() {
        assertDoesNotThrow(() -> {
            SwingUtilities.invokeAndWait(() -> {
                app.showScreen(AppController.SCENARIO);
            });
        });
    }

    @Test
    void testShowReportScreenWithNullScenario() {
        assertDoesNotThrow(() -> {
            SwingUtilities.invokeAndWait(() -> {
                app.showReportScreen(null);
            });
        });
    }
}
