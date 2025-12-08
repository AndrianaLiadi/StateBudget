package ui;

import javax.swing.*;
import java.awt.*;

public class AppController extends JFrame {

    private CardLayout layout;
    private JPanel container;

    public static final String REGISTRATION = "registration";
    public static final String LANDING = "landing";
    public static final String HOME = "home";
    public static final String BUDGET = "budget";
    public static final String SCENARIO = "scenario";
    public static final String REPORTS = "reports";
    public static final String ABOUT = "about";
    public static final String CONTACT = "contact";

    public AppController() {
        setTitle("State Budget App");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layout = new CardLayout();
        container = new JPanel(layout);

        container.add(new RegistrationScreen(this), REGISTRATION);
        container.add(new LandingScreen(this), LANDING);
        container.add(new HomeScreen(this), HOME);
        container.add(new BudgetScreen(this), BUDGET);
        container.add(new ScenarioScreen(this), SCENARIO);
        container.add(new ReportScreen(this), REPORTS);
        container.add(new AboutScreen(this), ABOUT);
        container.add(new ContactScreen(this), CONTACT);

        add(container);

        setupMenuBar();
        showScreen(REGISTRATION);
    }

    public void showScreen(String screenName) {
        layout.show(container, screenName);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem homeItem = new JMenuItem("Home");
        homeItem.addActionListener(e -> showScreen(HOME));

        JMenuItem budgetItem = new JMenuItem("Budget");
        budgetItem.addActionListener(e -> showScreen(BUDGET));

        JMenuItem scenarioItem = new JMenuItem("Scenarios");
        scenarioItem.addActionListener(e -> showScreen(SCENARIO));

        JMenuItem reportsItem = new JMenuItem("Reports");
        reportsItem.addActionListener(e -> showScreen(REPORTS));

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(homeItem);
        fileMenu.add(budgetItem);
        fileMenu.add(scenarioItem);
        fileMenu.add(reportsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Help");

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showScreen(ABOUT));

        JMenuItem contactItem = new JMenuItem("Contact");
        contactItem.addActionListener(e -> showScreen(CONTACT));

        helpMenu.add(aboutItem);
        helpMenu.add(contactItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
}
