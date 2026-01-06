package ui;

import model.Budget;
import model.BudgetItem;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class ChartPanel extends JPanel {

    private final Budget base;
    private final Budget modified;

    public ChartPanel(Budget base, Budget modified) {
        this.base = base;
        this.modified = modified;
        setPreferredSize(new Dimension(650, 420));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        try {

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (base == null || modified == null) {
                drawCenteredText(g2, "No data to display.", getWidth(), getHeight());
                return;
            }

            List<BudgetItem> baseItems = base.getItems();
            List<BudgetItem> modItems = modified.getItems();

            if (baseItems == null) baseItems = Collections.emptyList();
            if (modItems == null) modItems = Collections.emptyList();

            if (baseItems.isEmpty() && modItems.isEmpty()) {
                drawCenteredText(g2, "No items to display.", getWidth(), getHeight());
                return;
            }


            Map<String, Long> baseMap = toMapByCode(baseItems);
            Map<String, Long> modMap  = toMapByCode(modItems);


            List<String> codes = new ArrayList<>();
            for (String c : baseMap.keySet()) codes.add(c);
            for (String c : modMap.keySet()) if (!baseMap.containsKey(c)) codes.add(c);


            long rawMax = 0;
            for (String code : codes) {
                rawMax = Math.max(rawMax, baseMap.getOrDefault(code, 0L));
                rawMax = Math.max(rawMax, modMap.getOrDefault(code, 0L));
            }
            if (rawMax <= 0) rawMax = 1;


            int ticks = 5;
            long niceMax = niceCeil(rawMax);
            long tickStep = Math.max(1, niceMax / ticks);


            int paddingLeft = 70;
            int paddingTop = 28;
            int paddingBottom = 80;
            int paddingRight = 24;

            int w = getWidth() - paddingLeft - paddingRight;
            int h = getHeight() - paddingTop - paddingBottom;


            Color baseFill = new Color(90, 140, 255);
            Color baseBorder = new Color(55, 95, 190);

            Color scenFill = new Color(255, 145, 85);
            Color scenBorder = new Color(190, 90, 45);

            Color axisColor = new Color(60, 60, 60);
            Color gridColor = new Color(230, 230, 230);
            Color labelColor = new Color(40, 40, 40);


            g2.setFont(getFont().deriveFont(Font.PLAIN, 12f));
            g2.setColor(axisColor);


            int x0 = paddingLeft;
            int y0 = paddingTop + h;


            NumberFormat nf = NumberFormat.getInstance();
            for (int i = 0; i <= ticks; i++) {
                int y = paddingTop + (int) Math.round(h - (h * (i / (double) ticks)));
                long v = i * tickStep;

                g2.setColor(gridColor);
                g2.drawLine(paddingLeft, y, paddingLeft + w, y);

                g2.setColor(labelColor);
                String s = nf.format(v);
                int sw = g2.getFontMetrics().stringWidth(s);
                g2.drawString(s, paddingLeft - sw - 10, y + 4);
            }

            g2.setColor(axisColor);
            g2.drawLine(x0, paddingTop, x0, y0);      
            g2.drawLine(x0, y0, x0 + w, y0);          


            int n = Math.max(1, codes.size());
            int groupWidth = Math.max(46, w / n);
            int barWidth = Math.max(10, (groupWidth - 14) / 2);
            int gap = 6;

 
            int x = paddingLeft + Math.max(6, (w - (groupWidth * n)) / 2);

            Font valueFont = getFont().deriveFont(Font.BOLD, 11f);
            Font codeFont = getFont().deriveFont(Font.PLAIN, 12f);

            for (int i = 0; i < n; i++) {
                String code = codes.get(i);

                long baseVal = baseMap.getOrDefault(code, 0L);
                long modVal = modMap.getOrDefault(code, 0L);

                int baseBarH = (int) Math.round((baseVal / (double) niceMax) * h);
                int modBarH  = (int) Math.round((modVal  / (double) niceMax) * h);

                int yBase = y0 - baseBarH;
                int yMod  = y0 - modBarH;

                int xBase = x + 6;
                int xMod  = xBase + barWidth + gap;

    
                drawRoundedBar(g2, xBase, yBase, barWidth, baseBarH, baseFill, baseBorder);


                drawRoundedBar(g2, xMod, yMod, barWidth, modBarH, scenFill, scenBorder);

                g2.setFont(valueFont);
                g2.setColor(labelColor);

                String baseTxt = nf.format(baseVal);
                String modTxt  = nf.format(modVal);

                int baseTW = g2.getFontMetrics().stringWidth(baseTxt);
                int modTW  = g2.getFontMetrics().stringWidth(modTxt);

                g2.drawString(baseTxt, xBase + (barWidth - baseTW) / 2, Math.max(paddingTop + 12, yBase - 6));
                g2.drawString(modTxt,  xMod  + (barWidth - modTW) / 2,  Math.max(paddingTop + 12, yMod - 6));


                g2.setFont(codeFont);
                g2.setColor(labelColor);

                String label = code == null ? "" : code;
                int lw = g2.getFontMetrics().stringWidth(label);
                int lx = x + (groupWidth - lw) / 2;
                int ly = y0 + 22;


                if (lw > groupWidth - 6) {
                    Graphics2D gg = (Graphics2D) g2.create();
                    gg.translate(x + groupWidth / 2.0, y0 + 30);
                    gg.rotate(-Math.PI / 6); // -30 degrees
                    gg.drawString(label, -lw / 2f, 0);
                    gg.dispose();
                } else {
                    g2.drawString(label, lx, ly);
                }

                x += groupWidth;
            }


            int legendY = 10;
            int legendX = paddingLeft;

            g2.setFont(getFont().deriveFont(Font.BOLD, 12f));
            g2.setColor(labelColor);
            g2.drawString("Legend:", legendX, legendY + 12);

            int lx = legendX + 65;


            g2.setColor(baseFill);
            g2.fillRoundRect(lx, legendY + 2, 14, 14, 6, 6);
            g2.setColor(baseBorder);
            g2.drawRoundRect(lx, legendY + 2, 14, 14, 6, 6);
            g2.setColor(labelColor);
            g2.drawString("Base", lx + 20, legendY + 14);


            lx += 90;
            g2.setColor(scenFill);
            g2.fillRoundRect(lx, legendY + 2, 14, 14, 6, 6);
            g2.setColor(scenBorder);
            g2.drawRoundRect(lx, legendY + 2, 14, 14, 6, 6);
            g2.setColor(labelColor);
            g2.drawString("Scenario", lx + 20, legendY + 14);

        } finally {
            g2.dispose();
        }
    }

    private static Map<String, Long> toMapByCode(List<BudgetItem> items) {
        Map<String, Long> map = new LinkedHashMap<>();
        for (BudgetItem it : items) {
            if (it == null) continue;
            String code = it.getCode();
            long amount = it.getAmount();
            if (code == null) code = "";
            // If duplicate codes exist, sum them (safer)
            map.put(code, map.getOrDefault(code, 0L) + amount);
        }
        return map;
    }

    private static void drawRoundedBar(Graphics2D g2, int x, int y, int w, int h,
                                       Color fill, Color border) {
        if (h < 0) h = 0;
        int arc = Math.min(12, Math.min(w, h));
        g2.setColor(fill);
        g2.fillRoundRect(x, y, w, h, arc, arc);
        g2.setColor(border);
        g2.drawRoundRect(x, y, w, h, arc, arc);
    }

    private static void drawCenteredText(Graphics2D g2, String text, int width, int height) {
        FontMetrics fm = g2.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();
        g2.setColor(Color.DARK_GRAY);
        g2.drawString(text, x, y);
    }


    private static long niceCeil(long v) {
        if (v <= 0) return 1;
        long pow10 = 1;
        while (pow10 * 10 <= v) pow10 *= 10;

        long d = v / pow10;
        long nice;
        if (d <= 1) nice = 1;
        else if (d <= 2) nice = 2;
        else if (d <= 5) nice = 5;
        else nice = 10;

        long res = nice * pow10;
        if (res < v) res *= 2;
        return res;
    }
}
