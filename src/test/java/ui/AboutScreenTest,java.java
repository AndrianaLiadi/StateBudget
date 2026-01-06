package ui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AboutScreenTest {

    private AboutScreen aboutScreen;
    private AppController controller;

    @BeforeEach
    void setUp() {
        
        controller = new AppController();
        controller.setVisible(false);

        aboutScreen = new AboutScreen(controller);
    }

    @Test
    void testAboutScreenInitialization() {
        assertNotNull(aboutScreen);
        assertTrue(aboutScreen.getLayout() instanceof java.awt.BorderLayout);
    }

    @Test
    void testContainsTextAreaInsideScrollPane() {
        boolean foundScrollPane = false;

        for (var component : aboutScreen.getComponents()) {
            if (component instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) component;
                assertTrue(scroll.getViewport().getView() instanceof JTextArea);

                JTextArea textArea = (JTextArea) scroll.getViewport().getView();
                assertFalse(textArea.isEditable());
                assertTrue(textArea.getText().length() > 0);

                foundScrollPane = true;
            }
        }

        assertTrue(foundScrollPane, "Δεν βρέθηκε JScrollPane με JTextArea");
    }

    @Test
    void testBackButtonExists() {
        boolean foundButton = false;

        for (var component : aboutScreen.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                assertEquals("Back", button.getText());
                foundButton = true;
            }
        }

        assertTrue(foundButton, "Δεν βρέθηκε κουμπί Back");
    }

    @Test
    void testBackButtonActionDoesNotThrow() {
        JButton backButton = null;

        for (var component : aboutScreen.getComponents()) {
            if (component instanceof JButton) {
                backButton = (JButton) component;
                break;
            }
        }

        assertNotNull(backButton);

        JButton finalBackButton = backButton;
        assertDoesNotThrow(finalBackButton::doClick);
    }
}
