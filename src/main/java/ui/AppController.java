package ui;

import model.Scenario;
import model.Budget;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Map;
import java.util.HashMap;

import data.BudgetDataLoader;

import java.awt.*;
// this class is the backbone of ui
public class AppController extends JFrame {

    private CardLayout layout;
    private JPanel container;
    private Scenario currentScenario;
    private Budget baseBudget;
    private Map<Integer, Budget> budgetsByYear = new HashMap<>();

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

        BudgetDataLoader loader = new BudgetDataLoader();

        for (int year = 2019; year <= 2025; year++) {
        String path = "budget-" + year + ".csv";
        Budget budget = loader.loadFromCSV(path, year);

        baseBudget = loader.loadFromCSV(path, 2025);



        if (baseBudget == null) {
            JOptionPane.showMessageDialog(this,
            "Αποτυχία φόρτωσης προϋπολογισμού (baseBudget). Έλεγξε το path του CSV.",
            "Error", JOptionPane.ERROR_MESSAGE);
            baseBudget = new Budget(2025, new ArrayList<>());
        }


        container.add(new RegistrationScreen(this), REGISTRATION);
        container.add(new LandingScreen(this), LANDING);
        container.add(new HomeScreen(this), HOME);
        container.add(new BudgetScreen(this), BUDGET);
        container.add(new ScenarioScreen(this, baseBudget), SCENARIO);
        container.add(new ReportScreen(this, null), REPORTS);
        container.add(new AboutScreen(this), ABOUT);
        container.add(new ContactScreen(this), CONTACT);

        add(container);


        setupMenuBar();
        showScreen(REGISTRATION);}
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
    public void showReportScreen(Scenario scenario) {
    this.currentScenario = scenario;

    container.add(new ReportScreen(this, currentScenario), REPORTS);
    showScreen(REPORTS);

    container.revalidate();
    container.repaint();
}
    }
