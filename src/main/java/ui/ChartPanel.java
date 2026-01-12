package ui;

import model.Budget;
import model.BudgetItem;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class ChartPanel extends JPanel {

    private final Budget base;
    private final Budget modified;

    public ChartPanel(Budget base, Budget modified) {
        this.base = base;
        this.modified = modified;
        setPreferredSize(new Dimension(900, 700));
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

            List<BudgetItem> baseItems = safeList(base.getItems());
            List<BudgetItem> modItems  = safeList(modified.getItems());

            if (baseItems.isEmpty() && modItems.isEmpty()) {
                drawCenteredText(g2, "No items to display.", getWidth(), getHeight());
                return;
            }


            Map<String, Long> baseMap = toAmountByCode(baseItems);
            Map<String, Long> modMap  = toAmountByCode(modItems);

            Map<String, String> baseLabelMap = toLabelByCode(baseItems);
            Map<String, String> modLabelMap  = toLabelByCode(modItems);


            List<String> codes = new ArrayList<>();
            for (String c : baseMap.keySet()) codes.add(c);
            for (String c : modMap.keySet()) if (!baseMap.containsKey(c)) codes.add(c);


            if (codes.isEmpty()) {
                drawCenteredText(g2, "No items to display.", getWidth(), getHeight());
                return;
            }


            codes.sort((a, b) -> {
                long am = modMap.getOrDefault(a, 0L);
                long bm = modMap.getOrDefault(b, 0L);
                if (am != bm) return Long.compare(bm, am);
                long ab = baseMap.getOrDefault(a, 0L);
                long bb = baseMap.getOrDefault(b, 0L);
                return Long.compare(bb, ab);
            });


            int pad = 18;
            int topTitleH = 52;
            int legendH = 34;


            int fullW = getWidth();
            int fullH = getHeight();

            int barAreaX = pad;
            int barAreaY = pad + topTitleH;
            int barAreaW = fullW - 2 * pad;
            int barAreaH = (int) Math.round((fullH - 2 * pad - topTitleH - legendH) * 0.55);

            int pieAreaX = pad;
            int pieAreaY = barAreaY + barAreaH + legendH;
            int pieAreaW = fullW - 2 * pad;
            int pieAreaH = fullH - pad - pieAreaY;


            drawHeader(g2, pad, pad, fullW - 2 * pad);


            drawBarChart(
                    g2,
                    barAreaX, barAreaY, barAreaW, barAreaH,
                    codes,
                    baseMap, modMap,
                    baseLabelMap, modLabelMap
            );


            drawPieSection(
                    g2,
                    pieAreaX, pieAreaY, pieAreaW, pieAreaH,
                    codes,
                    baseMap, modMap,
                    baseLabelMap, modLabelMap
            );

        } finally {
            g2.dispose();
        }
    }


    private void drawBarChart(Graphics2D g2,
                              int x, int y, int w, int h,
                              List<String> codes,
                              Map<String, Long> baseMap,
                              Map<String, Long> modMap,
                              Map<String, String> baseLabelMap,
                              Map<String, String> modLabelMap) {

        Color axisColor = new Color(60, 60, 60);
        Color gridColor = new Color(230, 230, 230);
        Color textColor = new Color(35, 35, 35);

        Color baseFill = new Color(90, 140, 255);
        Color baseBorder = new Color(55, 95, 190);

        Color scenFill = new Color(255, 145, 85);
        Color scenBorder = new Color(190, 90, 45);

        Font titleFont = getFont().deriveFont(Font.BOLD, 15f);
        Font axisFont = getFont().deriveFont(Font.PLAIN, 12f);
        Font valueFont = getFont().deriveFont(Font.BOLD, 12f);
        Font diffFont = getFont().deriveFont(Font.PLAIN, 11f);

        int padLeft = 90;
        int padRight = 20;
        int padTop = 18;
        int padBottom = 95;

        int plotX = x + padLeft;
        int plotY = y + padTop;
        int plotW = Math.max(10, w - padLeft - padRight);
        int plotH = Math.max(10, h - padTop - padBottom);


        g2.setFont(titleFont);
        g2.setColor(textColor);
        g2.drawString("Σύγκριση ανά κατηγορία (Base vs Scenario)", x + 6, y + 16);


        long rawMax = 0;
        for (String code : codes) {
            rawMax = Math.max(rawMax, baseMap.getOrDefault(code, 0L));
            rawMax = Math.max(rawMax, modMap.getOrDefault(code, 0L));
        }
        if (rawMax <= 0) rawMax = 1;

        long niceMax = niceCeil(rawMax);
        int ticks = 6;

        NumberFormat nf = NumberFormat.getInstance();


        g2.setFont(axisFont);
        for (int i = 0; i <= ticks; i++) {
            double t = i / (double) ticks;
            int yy = plotY + (int) Math.round(plotH - t * plotH);
            long val = Math.round(t * niceMax);

            g2.setColor(gridColor);
            g2.drawLine(plotX, yy, plotX + plotW, yy);

            g2.setColor(textColor);
            String s = nf.format(val);
            int sw = g2.getFontMetrics().stringWidth(s);
            g2.drawString(s, plotX - sw - 12, yy + 4);
        }


        g2.setColor(axisColor);
        g2.drawLine(plotX, plotY, plotX, plotY + plotH);
        g2.drawLine(plotX, plotY + plotH, plotX + plotW, plotY + plotH);


        g2.setFont(getFont().deriveFont(Font.BOLD, 12f));
        g2.setColor(textColor);
        g2.drawString("Ποσό", x + 10, plotY + 12);
        g2.drawString("Κατηγορίες", plotX + plotW - 90, plotY + plotH + 86);

        int n = codes.size();
        int groupW = Math.max(56, plotW / n);
        int barW = Math.max(12, (groupW - 16) / 2);
        int gap = 6;


        int totalGroupsW = groupW * n;
        int startX = plotX + Math.max(0, (plotW - totalGroupsW) / 2);

        for (int i = 0; i < n; i++) {
            String code = codes.get(i);

            long baseVal = baseMap.getOrDefault(code, 0L);
            long modVal  = modMap.getOrDefault(code, 0L);
            long diff = modVal - baseVal;

            int baseH = (int) Math.round((baseVal / (double) niceMax) * plotH);
            int modH  = (int) Math.round((modVal  / (double) niceMax) * plotH);

            int xGroup = startX + i * groupW;
            int xBase = xGroup + 6;
            int xMod  = xBase + barW + gap;

            int yBase = plotY + plotH - baseH;
            int yMod  = plotY + plotH - modH;


            drawRoundedBar(g2, xBase, yBase, barW, baseH, baseFill, baseBorder);
            drawRoundedBar(g2, xMod,  yMod,  barW, modH,  scenFill, scenBorder);


            g2.setFont(valueFont);
            g2.setColor(textColor);

            String bTxt = nf.format(baseVal);
            String mTxt = nf.format(modVal);

            int bw = g2.getFontMetrics().stringWidth(bTxt);
            int mw = g2.getFontMetrics().stringWidth(mTxt);

            g2.drawString(bTxt, xBase + (barW - bw) / 2, Math.max(plotY + 14, yBase - 6));
            g2.drawString(mTxt, xMod  + (barW - mw) / 2, Math.max(plotY + 14, yMod - 6));


            g2.setFont(diffFont);
            String dTxt = (diff >= 0 ? "Δ +" : "Δ ") + nf.format(diff);
            int dw = g2.getFontMetrics().stringWidth(dTxt);
            int dy = plotY + plotH + 34;
            g2.setColor(new Color(70, 70, 70));
            g2.drawString(dTxt, xGroup + (groupW - dw) / 2, dy);


            String label = pickLabel(code, baseLabelMap, modLabelMap);
            g2.setFont(axisFont);
            g2.setColor(textColor);

            int lw = g2.getFontMetrics().stringWidth(label);
            int labelBaseY = plotY + plotH + 70;

            if (lw > groupW + 18) {
                Graphics2D gg = (Graphics2D) g2.create();
                gg.translate(xGroup + groupW / 2.0, labelBaseY);
                gg.rotate(-Math.PI / 5.0);
                gg.drawString(label, -lw / 2f, 0);
                gg.dispose();
            } else {
                int lx = xGroup + (groupW - lw) / 2;
                g2.drawString(label, lx, labelBaseY);
            }
        }


        drawLegend(g2, x + 8, y + h - 24);
    }

    private void drawLegend(Graphics2D g2, int x, int y) {
        Color textColor = new Color(35, 35, 35);

        Color baseFill = new Color(90, 140, 255);
        Color baseBorder = new Color(55, 95, 190);

        Color scenFill = new Color(255, 145, 85);
        Color scenBorder = new Color(190, 90, 45);

        g2.setFont(getFont().deriveFont(Font.PLAIN, 12f));
        g2.setColor(textColor);
        g2.drawString("Legend:", x, y);

        int bx = x + 60;

        g2.setColor(baseFill);
        g2.fillRoundRect(bx, y - 12, 14, 14, 6, 6);
        g2.setColor(baseBorder);
        g2.drawRoundRect(bx, y - 12, 14, 14, 6, 6);
        g2.setColor(textColor);
        g2.drawString("Base", bx + 20, y);

        bx += 90;

        g2.setColor(scenFill);
        g2.fillRoundRect(bx, y - 12, 14, 14, 6, 6);
        g2.setColor(scenBorder);
        g2.drawRoundRect(bx, y - 12, 14, 14, 6, 6);
        g2.setColor(textColor);
        g2.drawString("Scenario", bx + 20, y);
    }


    private void drawPieSection(Graphics2D g2,
                                int x, int y, int w, int h,
                                List<String> codes,
                                Map<String, Long> baseMap,
                                Map<String, Long> modMap,
                                Map<String, String> baseLabelMap,
                                Map<String, String> modLabelMap) {

        Color textColor = new Color(35, 35, 35);
        Color boxBorder = new Color(220, 220, 220);

        Font sectionTitle = getFont().deriveFont(Font.BOLD, 15f);
        Font small = getFont().deriveFont(Font.PLAIN, 12f);


        g2.setFont(sectionTitle);
        g2.setColor(textColor);
        g2.drawString("Κατανομή (Pie) — ίδιοι χρωματισμοί ανά κατηγορία", x + 6, y + 18);

        int innerY = y + 28;
        int innerH = Math.max(10, h - 34);


        int gap = 18;
        int pieW = (w - gap) / 2;

        Rectangle left = new Rectangle(x, innerY, pieW, innerH);
        Rectangle right = new Rectangle(x + pieW + gap, innerY, pieW, innerH);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(left.x, left.y, left.width, left.height, 12, 12);
        g2.fillRoundRect(right.x, right.y, right.width, right.height, 12, 12);

        g2.setColor(boxBorder);
        g2.drawRoundRect(left.x, left.y, left.width, left.height, 12, 12);
        g2.drawRoundRect(right.x, right.y, right.width, right.height, 12, 12);


        g2.setFont(getFont().deriveFont(Font.BOLD, 13f));
        g2.setColor(textColor);
        g2.drawString("Base", left.x + 12, left.y + 20);
        g2.drawString("Scenario", right.x + 12, right.y + 20);


        int pieSize = Math.min(left.width, left.height) - 90;
        pieSize = Math.max(140, pieSize);

        Rectangle pieLeft = new Rectangle(left.x + 18, left.y + 34, pieSize, pieSize);
        Rectangle pieRight = new Rectangle(right.x + 18, right.y + 34, pieSize, pieSize);


        long baseTotal = sumPositive(baseMap, codes);
        long modTotal = sumPositive(modMap, codes);


        drawPie(g2, pieLeft, codes, baseMap, baseTotal);
        drawPie(g2, pieRight, codes, modMap, modTotal);


        g2.setFont(small);
        drawPieLegend(g2, left.x + pieLeft.width + 28, left.y + 44,
                codes, baseMap, baseTotal, baseLabelMap, modLabelMap);

        drawPieLegend(g2, right.x + pieRight.width + 28, right.y + 44,
                codes, modMap, modTotal, baseLabelMap, modLabelMap);


        NumberFormat nf = NumberFormat.getInstance();
        g2.setFont(getFont().deriveFont(Font.BOLD, 12f));
        g2.setColor(textColor);
        g2.drawString("Σύνολο: " + nf.format(baseTotal), left.x + 12, left.y + left.height - 14);
        g2.drawString("Σύνολο: " + nf.format(modTotal), right.x + 12, right.y + right.height - 14);
    }

    private void drawPie(Graphics2D g2, Rectangle area, List<String> codes,
                         Map<String, Long> map, long total) {

 
        g2.setColor(new Color(245, 245, 245));
        g2.fillOval(area.x, area.y, area.width, area.height);

        if (total <= 0) {
            g2.setColor(new Color(120, 120, 120));
            g2.setFont(getFont().deriveFont(Font.PLAIN, 12f));
            g2.drawString("No data", area.x + 10, area.y + 18);
            return;
        }

        double start = 90.0;
        for (String code : codes) {
            long val = Math.max(0, map.getOrDefault(code, 0L));
            if (val == 0) continue;

            double angle = (val / (double) total) * 360.0;
            Color c = colorForCode(code);

            g2.setColor(c);
            Arc2D.Double arc = new Arc2D.Double(area.x, area.y, area.width, area.height,
                    start, -angle, Arc2D.PIE);
            g2.fill(arc);

            start -= angle;
        }


        g2.setColor(new Color(200, 200, 200));
        g2.drawOval(area.x, area.y, area.width, area.height);
    }

    private void drawPieLegend(Graphics2D g2, int x, int y,
                               List<String> codes, Map<String, Long> map, long total,
                               Map<String, String> baseLabelMap,
                               Map<String, String> modLabelMap) {


        List<String> sorted = new ArrayList<>(codes);
        sorted.sort((a, b) -> Long.compare(map.getOrDefault(b, 0L), map.getOrDefault(a, 0L)));

        int shown = 0;
        int box = 12;
        int lineH = 18;

        NumberFormat nf = NumberFormat.getInstance();
        g2.setColor(new Color(35, 35, 35));

        for (String code : sorted) {
            long val = Math.max(0, map.getOrDefault(code, 0L));
            if (val == 0) continue;

            String label = pickLabel(code, baseLabelMap, modLabelMap);
            double pct = total > 0 ? (val * 100.0 / total) : 0.0;


            g2.setColor(colorForCode(code));
            g2.fillRoundRect(x, y + shown * lineH - 10, box, box, 4, 4);
            g2.setColor(new Color(120, 120, 120));
            g2.drawRoundRect(x, y + shown * lineH - 10, box, box, 4, 4);


            g2.setColor(new Color(35, 35, 35));
            String txt = String.format("%s — %s (%.1f%%)", shorten(label, 32), nf.format(val), pct);
            g2.drawString(txt, x + box + 8, y + shown * lineH);

            shown++;
            if (shown >= 8) break;
        }

        if (shown == 0) {
            g2.setColor(new Color(120, 120, 120));
            g2.drawString("No data", x, y);
        }
    }

    private void drawHeader(Graphics2D g2, int x, int y, int w) {
        Color textColor = new Color(25, 25, 25);
        Color subColor = new Color(90, 90, 90);

        g2.setFont(getFont().deriveFont(Font.BOLD, 18f));
        g2.setColor(textColor);
        g2.drawString("StateBudget — Charts", x + 6, y + 22);

        g2.setFont(getFont().deriveFont(Font.PLAIN, 12f));
        g2.setColor(subColor);
        g2.drawString("Σύγκριση Base προϋπολογισμού με Scenario (με ποσά & διαφορές ανά κατηγορία)", x + 6, y + 40);

        g2.setColor(new Color(230, 230, 230));
        g2.drawLine(x, y + 48, x + w, y + 48);
    }

    private static List<BudgetItem> safeList(List<BudgetItem> items) {
        return items == null ? Collections.emptyList() : items;
    }

    private static Map<String, Long> toAmountByCode(List<BudgetItem> items) {
        Map<String, Long> map = new LinkedHashMap<>();
        for (BudgetItem it : items) {
            if (it == null) continue;

            String code = it.getCode();
            if (code == null) code = "";

            long amount = (long) Math.round(it.getTotal());

            map.put(code, map.getOrDefault(code, 0L) + amount);
        }
        return map;
    }

    private static Map<String, String> toLabelByCode(List<BudgetItem> items) {
        Map<String, String> map = new LinkedHashMap<>();
        for (BudgetItem it : items) {
            if (it == null) continue;

            String code = it.getCode();
            if (code == null) code = "";

            String name = it.getName();

            if (name != null && !name.isBlank()) {
                map.putIfAbsent(code, name + " (" + code + ")");
            } else {
                map.putIfAbsent(code, code);
            }
        }
        return map;
    }

    private static String pickLabel(String code,
                                    Map<String, String> baseLabelMap,
                                    Map<String, String> modLabelMap) {
        if (code == null) return "";
        String s = baseLabelMap.get(code);
        if (s != null) return s;
        s = modLabelMap.get(code);
        return s != null ? s : code;
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

    private static long sumPositive(Map<String, Long> map, List<String> codes) {
        long s = 0;
        for (String c : codes) {
            s += Math.max(0, map.getOrDefault(c, 0L));
        }
        return s;
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


    private static Color colorForCode(String code) {
        int h = (code == null ? 0 : code.hashCode());

        int r = 80 + Math.abs(h * 31) % 140;
        int g = 80 + Math.abs(h * 17) % 140;
        int b = 80 + Math.abs(h * 13) % 140;
        return new Color(r, g, b);
    }

    private static String shorten(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, Math.max(0, max - 1)) + "…";
    }
}

