package ui;

import model.Budget;
import model.BudgetItem;

import javax.swing.*;
import java.awt.*;
import java.util.List;
//this class creates charts
public class ChartPanel extends JPanel {

    private final Budget base;
    private final Budget modified;

    public ChartPanel(Budget base, Budget modified) {
        this.base = base;
        this.modified = modified;
        setPreferredSize(new Dimension(500, 350));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (base == null || modified == null) {
            g.drawString("No data to display.", 20, 20);
            return;
        }

        List<BudgetItem> baseItems = base.getItems();
        List<BudgetItem> modItems = modified.getItems();

        if (baseItems == null || modItems == null || baseItems.isEmpty()) {
            g.drawString("No items to display.", 20, 20);
            return;
        }

      
        int paddingLeft = 40;
        int paddingTop = 30;
        int paddingBottom = 50;
        int paddingRight = 20;

        int w = getWidth() - paddingLeft - paddingRight;
        int h = getHeight() - paddingTop - paddingBottom;

        long max = 1;
        int n = Math.min(baseItems.size(), modItems.size());
        for (int i = 0; i < n; i++) {
            max = Math.max(max, baseItems.get(i).getAmount());
            max = Math.max(max, modItems.get(i).getAmount());
        }

      
        g.drawLine(paddingLeft, paddingTop, paddingLeft, paddingTop + h);
        g.drawLine(paddingLeft, paddingTop + h, paddingLeft + w, paddingTop + h);


        int groupWidth = Math.max(40, w / n);
        int barWidth = Math.max(10, (groupWidth - 12) / 2);
        int x = paddingLeft + 10;

        for (int i = 0; i < n; i++) {
            long baseVal = baseItems.get(i).getAmount();
            long modVal = modItems.get(i).getAmount();

            int baseBarH = (int) ((double) baseVal / max * h);
            int modBarH  = (int) ((double) modVal / max * h);

            int yBase = paddingTop + h - baseBarH;
            int yMod  = paddingTop + h - modBarH;

            
            g.setColor(Color.GRAY);
            g.fillRect(x, yBase, barWidth, baseBarH);

            
            g.setColor(new Color(60, 120, 220));
            g.fillRect(x + barWidth + 6, yMod, barWidth, modBarH);

           
            g.setColor(Color.DARK_GRAY);
            String label = baseItems.get(i).getCode();
            g.drawString(label, x, paddingTop + h + 15);

            x += groupWidth;
        }

       
        g.setColor(Color.GRAY);
        g.fillRect(paddingLeft + 10, 10, 12, 12);
        g.setColor(Color.BLACK);
        g.drawString("Base", paddingLeft + 26, 20);

        g.setColor(new Color(60, 120, 220));
        g.fillRect(paddingLeft + 90, 10, 12, 12);
        g.setColor(Color.BLACK);
        g.drawString("Scenario", paddingLeft + 106, 20);
    }
}
