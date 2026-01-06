package ui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LandingScreenTest {

    private LandingScreen landingScreen;
    private AppController controller;

    @BeforeEach
    void setUp() {
        controller = new AppController();
        controller.setVisible(false);

        landingScreen = new LandingScreen(controller);
    }

    @Test
    void testLandingScreenInitialization() {
        assertNotNull(landingScreen);
        assertTrue(landingScreen.getLayout() instanceof GridLayout);

        GridLayout layout = (GridLayout) landingScreen.getLayout();
        assertEquals(4, layout.getRows());
        assertEquals(1, layout.getColumns());
    }

    @Test
    void testWelcomeLabelExists() {
        JLabel label = findComponent(landingScreen, JLabel.class);

        assertNotNull(label);
        assertEquals("Καλώς ήρθες στην εφαρμογή!", label.getText());
        assertEquals(SwingConstants.CENTER, label.getHorizontalAlignment());
    }

    @Test
    void testHomeButtonExists() {
        JButton homeButton = findButtonByText(landingScreen, "Home");
        assertNotNull(homeButton);
    }

    @Test
    void testAboutButtonExists() {
        JButton aboutButton = findButtonByText(landingScreen, "About");
        assertNotNull(aboutButton);
    }

    @Test
    void testContactButtonExists() {
        JButton contactButton = findButtonByText(landingScreen, "Contact");
        assertNotNull(contactButton);
    }

    @Test
    void testHomeButtonActionDoesNotThrow() {
        JButton homeButton = findButtonByText(landingScreen, "Home");

        assertNotNull(homeButton);
        assertDoesNotThrow(homeButton::doClick);
    }

    @Test
    void testAboutButtonActionDoesNotThrow() {
        JButton aboutButton = findButtonByText(landingScreen, "About");

        assertNotNull(aboutButton);
        assertDoesNotThrow(aboutButton::doClick);
    }

    @Test
    void testContactButtonActionDoesNotThrow() {
        JButton contactButton = findButtonByText(landingScreen, "Contact");

        assertNotNull(contactButton);
        assertDoesNotThrow(contactButton::doClick);
    }

    /* =======================
       Helper methods
       ======================= */

    @SuppressWarnings("unchecked")
    private <T extends JComponent> T findComponent(Container root, Class<T> type) {
        for (Component c : root.getComponents()) {
            if (type.isInstance(c)) {
                return (T) c;
            }
            if (c instanceof Container) {
                T found = findComponent((Container) c, type);
                if (found != null) return found;
            }
        }
        return null;
    }

    private JButton findButtonByText(Container root, String text) {
        for (Component c : root.getComponents()) {
            if (c instanceof JButton) {
                JButton button = (JButton) c;
                if (text.equals(button.getText())) {
                    return button;
                }
            }
            if (c instanceof Container) {
                JButton found = findButtonByText((Container) c, text);
                if (found != null) return found;
            }
        }
        return null;
    }
}
