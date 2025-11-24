package model;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    private String name;
    private Budget baseBudget;
    private List<BudgetChange> changes;
    private Budget modifiedBudget;
    private String summary;

    public Scenario(Budget baseBudget, String name) {
        this.baseBudget = baseBudget;
        this.name = name;
        this.changes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Budget getBaseBudget() {
        return baseBudget;
    }

    public List<BudgetChange> getChanges() {
        return changes;
    }
