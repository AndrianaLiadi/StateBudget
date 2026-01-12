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
        setPreferredSize(new Dimension(1050, 780));
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
            List<BudgetItem> modItems = safeList(modified.getItems());

            if (baseItems.isEmpty() && modItems.isEmpty()) {
                drawCenteredText(g2, "No items to display.", getWidth(), getHeight());
                return;
            }

            Map<String, Long> baseMap = toAmountByCode(baseItems);
            Map<String, Long> modMap = toAmountByCode(modItems);

            Map<String, String> baseLabelMap = toLabelByCode(baseItems);
            Map<String, String> modLabelMap = toLabelByCode(modItems);

            List<String> allCodes = unionCodes(baseMap, modMap);
            if (allCodes.isEmpty()) {
                drawCenteredText(g2, "No items to display.", getWidth(), getHeight());
                return;
            }

            int pad = 18;
            int headerH = 54;

            int fullW = getWidth();
            int fullH = getHeight();

            int barX = pad;
            int barY = pad + headerH;
            int barW = fullW - 2 * pad;
            int barH = (int) Math.round((fullH - 2 * pad - headerH) * 0.38);

            int changesX = pad;
            int changesY = barY + barH + 14;
            int changesW = fullW - 2 * pad;
            int changesH = (int) Math.round((fullH - 2 * pad - headerH) * 0.30);

            int pieX = pad;
            int pieY = changesY + changesH + 14;
            int pieW = fullW - 2 * pad;
            int pieH = fullH - pad - pieY;

            drawHeader(g2, pad, pad, fullW - 2 * pad);

            AggregatedData bars = buildTopWithOthers(allCodes, baseMap, modMap, baseLabelMap, modLabelMap, 10);
            drawBarChart(g2, barX, barY, barW, barH, bars);

            ChangeData changes = buildChanges(allCodes, baseMap, modMap, baseLabelMap, modLabelMap, 18);
            drawChangesTable(g2, changesX, changesY, changesW, changesH, changes);

            drawPieSection(g2, pieX, pieY, pieW, pieH, allCodes, baseMap, modMap, baseLabelMap, modLabelMap);

        } finally {
            g2.dispose();
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
        g2.drawString("Σύγκριση Base προϋπολογισμού με Scenario — ξεκάθαρη απεικόνιση αλλαγών χρήστη", x + 6, y + 40);

        g2.setColor(new Color(230, 230, 230));
        g2.drawLine(x, y + 48, x + w, y + 48);
    }

    private void drawBarChart(Graphics2D g2, int x, int y, int w, int h, AggregatedData d) {
        Color axisColor = new Color(60, 60, 60);
        Color gridColor = new Color(234, 234, 234);
        Color textColor = new Color(35, 35, 35);

        Color baseFill = new Color(90, 140, 255);
        Color baseBorder = new Color(55, 95, 190);

        Color scenFill = new Color(255, 145, 85);
        Color scenBorder = new Color(190, 90, 45);

        Font titleFont = getFont().deriveFont(Font.BOLD, 15f);
        Font axisFont = getFont().deriveFont(Font.PLAIN, 12f);
        Font labelFont = getFont().deriveFont(Font.PLAIN, 11f);

        int padLeft = 92;
        int padRight = 18;
        int padTop = 18;
        int padBottom = 56;

        int plotX = x + padLeft;
        int plotY = y + padTop;
        int plotW = Math.max(10, w - padLeft - padRight);
        int plotH = Math.max(10, h - padTop - padBottom);

        g2.setFont(titleFont);
        g2.setColor(textColor);
        g2.drawString("Top κατηγορίες (Base vs Scenario) + Λοιπά", x + 6, y + 16);

        long rawMax = 0;
        for (String code : d.codes) {
            rawMax = Math.max(rawMax, d.baseMap.getOrDefault(code, 0L));
            rawMax = Math.max(rawMax, d.modMap.getOrDefault(code, 0L));
        }
        if (rawMax <= 0) rawMax = 1;

        long niceMax = niceCeil(rawMax);
        int ticks = 6;

        g2.setFont(axisFont);
        for (int i = 0; i <= ticks; i++) {
            double t = i / (double) ticks;
            int yy = plotY + (int) Math.round(plotH - t * plotH);
            long val = Math.round(t * niceMax);

            g2.setColor(gridColor);
            g2.drawLine(plotX, yy, plotX + plotW, yy);

            g2.setColor(textColor);
            String s = fmtCompact(val);
            int sw = g2.getFontMetrics().stringWidth(s);
            g2.drawString(s, plotX - sw - 12, yy + 4);
        }

        g2.setColor(axisColor);
        g2.drawLine(plotX, plotY, plotX, plotY + plotH);
        g2.drawLine(plotX, plotY + plotH, plotX + plotW, plotY + plotH);

        g2.setFont(getFont().deriveFont(Font.BOLD, 12f));
        g2.setColor(textColor);
        g2.drawString("Ποσό", x + 10, plotY + 12);

        int n = d.codes.size();
        int groupW = Math.max(80, plotW / Math.max(1, n));
        int barW = Math.max(14, (groupW - 18) / 2);
        int gap = 6;

        int totalGroupsW = groupW * n;
        int startX = plotX + Math.max(0, (plotW - totalGroupsW) / 2);

        for (int i = 0; i < n; i++) {
            String code = d.codes.get(i);

            long baseVal = d.baseMap.getOrDefault(code, 0L);
            long modVal = d.modMap.getOrDefault(code, 0L);

            int baseH = (int) Math.round((baseVal / (double) niceMax) * plotH);
            int modH = (int) Math.round((modVal / (double) niceMax) * plotH);

            int xGroup = startX + i * groupW;
            int xBase = xGroup + 8;
            int xMod = xBase + barW + gap;

            int yBase = plotY + plotH - baseH;
            int yMod = plotY + plotH - modH;

            drawRoundedBar(g2, xBase, yBase, barW, baseH, baseFill, baseBorder);
            drawRoundedBar(g2, xMod, yMod, barW, modH, scenFill, scenBorder);

            String label = d.labelMap.getOrDefault(code, code);
            String line1 = shorten(label, 22);
            String line2 = "";
            if (label.length() > 22) line2 = shorten(label.substring(22), 22);

            g2.setFont(labelFont);
            g2.setColor(textColor);

            int lw1 = g2.getFontMetrics().stringWidth(line1);
            int lx1 = xGroup + (groupW - lw1) / 2;
            int ly1 = plotY + plotH + 16;
            g2.drawString(line1, lx1, ly1);

            if (!line2.isEmpty()) {
                int lw2 = g2.getFontMetrics().stringWidth(line2);
                int lx2 = xGroup + (groupW - lw2) / 2;
                int ly2 = ly1 + 14;
                g2.drawString(line2, lx2, ly2);
            }
        }

        drawLegend(g2, x + 8, y + h - 14);
    }

    private void drawChangesTable(Graphics2D g2, int x, int y, int w, int h, ChangeData cd) {
        Color textColor = new Color(35, 35, 35);
        Color border = new Color(220, 220, 220);
        Color headerBg = new Color(248, 248, 248);
        Color grid = new Color(238, 238, 238);

        Color posFill = new Color(255, 145, 85);
        Color posBorder = new Color(190, 90, 45);
        Color negFill = new Color(90, 140, 255);
        Color negBorder = new Color(55, 95, 190);

        Font titleFont = getFont().deriveFont(Font.BOLD, 15f);
        Font headerFont = getFont().deriveFont(Font.BOLD, 12f);
        Font rowFont = getFont().deriveFont(Font.PLAIN, 12f);
        Font valueFont = getFont().deriveFont(Font.BOLD, 12f);

        g2.setFont(titleFont);
        g2.setColor(textColor);
        g2.drawString("Αλλαγές χρήστη στο Scenario (μόνο ό,τι άλλαξε)", x + 6, y + 16);

        int boxY = y + 22;
        int boxH = h - 26;

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, boxY, w, boxH, 12, 12);
        g2.setColor(border);
        g2.drawRoundRect(x, boxY, w, boxH, 12, 12);

        if (cd.rows.isEmpty()) {
            g2.setFont(rowFont);
            g2.setColor(new Color(120, 120, 120));
            g2.drawString("Δεν υπάρχουν αλλαγές (Scenario = Base σε όλες τις κατηγορίες).", x + 12, boxY + 34);
            return;
        }

        int headerH = 28;
        int tableX = x + 10;
        int tableY = boxY + 10;
        int tableW = w - 20;
        int tableH = boxH - 20;

        int nameW = Math.min(420, (int) (tableW * 0.44));
        int baseW = Math.min(150, (int) (tableW * 0.16));
        int scenW = Math.min(150, (int) (tableW * 0.16));
        int deltaW = tableW - nameW - baseW - scenW;

        int nameX = tableX;
        int baseX = nameX + nameW;
        int scenX = baseX + baseW;
        int deltaX = scenX + scenW;

        g2.setColor(headerBg);
        g2.fillRoundRect(tableX, tableY, tableW, headerH, 10, 10);
        g2.setColor(border);
        g2.drawRoundRect(tableX, tableY, tableW, headerH, 10, 10);

        g2.setFont(headerFont);
        g2.setColor(textColor);
        g2.drawString("Κατηγορία", nameX + 8, tableY + 18);
        g2.drawString("Base", baseX + 8, tableY + 18);
        g2.drawString("Scenario", scenX + 8, tableY + 18);
        g2.drawString("Δ (Scenario - Base)", deltaX + 8, tableY + 18);

        int rowsAreaY = tableY + headerH;
        int rowsAreaH = tableH - headerH;

        int maxRowsVisible = Math.max(4, rowsAreaH / 28);
        List<ChangeRow> rows = cd.rows;
        if (rows.size() > maxRowsVisible) {
            rows = rows.subList(0, maxRowsVisible);
        }

        long maxAbs = 1;
        for (ChangeRow r : rows) maxAbs = Math.max(maxAbs, Math.abs(r.delta));
        long niceMax = niceCeil(maxAbs);

        int rowH = 28;

        for (int i = 0; i < rows.size(); i++) {
            int ry = rowsAreaY + i * rowH;
            g2.setColor(i % 2 == 0 ? Color.WHITE : new Color(252, 252, 252));
            g2.fillRect(tableX, ry, tableW, rowH);

            g2.setColor(grid);
            g2.drawLine(tableX, ry + rowH, tableX + tableW, ry + rowH);

            ChangeRow r = rows.get(i);

            g2.setFont(rowFont);
            g2.setColor(textColor);

            String name = shorten(r.label, 46);
            g2.drawString(name, nameX + 8, ry + 19);

            String b = fmtFull(r.baseValue);
            String m = fmtFull(r.modValue);

            g2.drawString(b, baseX + 8, ry + 19);
            g2.drawString(m, scenX + 8, ry + 19);

            int barPad = 10;
            int barAreaX = deltaX + barPad;
            int barAreaW = deltaW - 2 * barPad;
            int barCY = ry + rowH / 2;

            int zeroX = barAreaX + barAreaW / 2;

            g2.setColor(new Color(220, 220, 220));
            g2.drawLine(zeroX, ry + 6, zeroX, ry + rowH - 6);

            double frac = r.delta / (double) niceMax;
            int len = (int) Math.round(Math.min(1.0, Math.abs(frac)) * (barAreaW / 2.0));
            int bh = 14;
            int by = barCY - bh / 2;

            if (r.delta >= 0) {
                drawRoundedBar(g2, zeroX, by, len, bh, posFill, posBorder);
            } else {
                drawRoundedBar(g2, zeroX - len, by, len, bh, negFill, negBorder);
            }

            g2.setFont(valueFont);
            g2.setColor(textColor);
            String dv = (r.delta >= 0 ? "+" : "") + fmtFull(r.delta);
            int dvw = g2.getFontMetrics().stringWidth(dv);
            int tx = (r.delta >= 0) ? (zeroX + len + 8) : (zeroX - len - dvw - 8);
            tx = Math.max(barAreaX, Math.min(barAreaX + barAreaW - dvw, tx));
            g2.drawString(dv, tx, ry + 19);
        }

        if (cd.hiddenCount > 0) {
            g2.setFont(getFont().deriveFont(Font.PLAIN, 12f));
            g2.setColor(new Color(110, 110, 110));
            g2.drawString("…και " + cd.hiddenCount + " ακόμη αλλαγές (δεν χωράνε στο παράθυρο).", x + 12, boxY + boxH - 10);
        }
    }

    private void drawPieSection(Graphics2D g2, int x, int y, int w, int h,
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
        g2.drawString("Κατανομή (Pie) — Base vs Scenario", x + 6, y + 18);

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
        pieSize = Math.max(150, pieSize);

        Rectangle pieLeft = new Rectangle(left.x + 18, left.y + 34, pieSize, pieSize);
        Rectangle pieRight = new Rectangle(right.x + 18, right.y + 34, pieSize, pieSize);

        long baseTotal = sumPositive(baseMap, codes);
        long modTotal = sumPositive(modMap, codes);

        drawPie(g2, pieLeft, codes, baseMap, baseTotal);
        drawPie(g2, pieRight, codes, modMap, modTotal);

        g2.setFont(small);
        drawPieLegend(g2, left.x + pieLeft.width + 28, left.y + 44, codes, baseMap, baseTotal, baseLabelMap, modLabelMap);
        drawPieLegend(g2, right.x + pieRight.width + 28, right.y + 44, codes, modMap, modTotal, baseLabelMap, modLabelMap);

        g2.setFont(getFont().deriveFont(Font.BOLD, 12f));
        g2.setColor(textColor);
        g2.drawString("Σύνολο: " + fmtFull(baseTotal), left.x + 12, left.y + left.height - 14);
        g2.drawString("Σύνολο: " + fmtFull(modTotal), right.x + 12, right.y + right.height - 14);
    }

    private void drawPie(Graphics2D g2, Rectangle area, List<String> codes, Map<String, Long> map, long total) {
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
            Arc2D.Double arc = new Arc2D.Double(area.x, area.y, area.width, area.height, start, -angle, Arc2D.PIE);
            g2.fill(arc);

            start -= angle;
        }

        g2.setColor(new Color(200, 200, 200));
        g2.drawOval(area.x, area.y, area.width, area.height);
    }

    private void drawPieLegend(Graphics2D g2, int x, int y,
                               List<String> codes, Map<String, Long> map, long total,
                               Map<String, String> baseLabelMap, Map<String, String> modLabelMap) {

        List<String> sorted = new ArrayList<>(codes);
        sorted.sort((a, b) -> Long.compare(map.getOrDefault(b, 0L), map.getOrDefault(a, 0L)));

        int shown = 0;
        int box = 12;
        int lineH = 18;

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
            String txt = String.format("%s — %s (%.1f%%)", shorten(label, 28), fmtFull(val), pct);
            g2.drawString(txt, x + box + 8, y + shown * lineH);

            shown++;
            if (shown >= 8) break;
        }

        if (shown == 0) {
            g2.setColor(new Color(120, 120, 120));
            g2.drawString("No data", x, y);
        }
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
            if (name != null && !name.isBlank()) map.putIfAbsent(code, name + " (" + code + ")");
            else map.putIfAbsent(code, code);
        }
        return map;
    }

    private static List<String> unionCodes(Map<String, Long> baseMap, Map<String, Long> modMap) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        set.addAll(baseMap.keySet());
        set.addAll(modMap.keySet());
        return new ArrayList<>(set);
    }

    private static String pickLabel(String code, Map<String, String> baseLabelMap, Map<String, String> modLabelMap) {
        if (code == null) return "";
        String s = baseLabelMap.get(code);
        if (s != null) return s;
        s = modLabelMap.get(code);
        return s != null ? s : code;
    }

    private static void drawRoundedBar(Graphics2D g2, int x, int y, int w, int h, Color fill, Color border) {
        if (w < 0) w = 0;
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
        for (String c : codes) s += Math.max(0, map.getOrDefault(c, 0L));
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

    private static String fmtFull(long v) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(true);
        return nf.format(v);
    }

    private static String fmtCompact(long v) {
        long av = Math.abs(v);
        String sign = v < 0 ? "-" : "";
        if (av >= 1_000_000_000_000L) return sign + String.format(Locale.US, "%.2fT", av / 1_000_000_000_000.0);
        if (av >= 1_000_000_000L) return sign + String.format(Locale.US, "%.2fB", av / 1_000_000_000.0);
        if (av >= 1_000_000L) return sign + String.format(Locale.US, "%.2fM", av / 1_000_000.0);
        if (av >= 1_000L) return sign + String.format(Locale.US, "%.2fK", av / 1_000.0);
        return sign + fmtFull(av);
    }

    private static AggregatedData buildTopWithOthers(List<String> allCodes,
                                                     Map<String, Long> baseMap,
                                                     Map<String, Long> modMap,
                                                     Map<String, String> baseLabelMap,
                                                     Map<String, String> modLabelMap,
                                                     int topN) {

        List<String> sorted = new ArrayList<>(allCodes);
        sorted.sort((a, b) -> {
            long aMax = Math.max(baseMap.getOrDefault(a, 0L), modMap.getOrDefault(a, 0L));
            long bMax = Math.max(baseMap.getOrDefault(b, 0L), modMap.getOrDefault(b, 0L));
            return Long.compare(bMax, aMax);
        });

        LinkedHashMap<String, Long> baseOut = new LinkedHashMap<>();
        LinkedHashMap<String, Long> modOut = new LinkedHashMap<>();
        LinkedHashMap<String, String> labelOut = new LinkedHashMap<>();
        List<String> codesOut = new ArrayList<>();

        long baseOther = 0;
        long modOther = 0;

        for (int i = 0; i < sorted.size(); i++) {
            String code = sorted.get(i);
            long b = baseMap.getOrDefault(code, 0L);
            long m = modMap.getOrDefault(code, 0L);

            if (i < topN) {
                codesOut.add(code);
                baseOut.put(code, b);
                modOut.put(code, m);
                labelOut.put(code, pickLabel(code, baseLabelMap, modLabelMap));
            } else {
                baseOther += b;
                modOther += m;
            }
        }

        if (sorted.size() > topN) {
            String otherCode = "__OTHER__";
            codesOut.add(otherCode);
            baseOut.put(otherCode, baseOther);
            modOut.put(otherCode, modOther);
            labelOut.put(otherCode, "Λοιπά");
        }

        return new AggregatedData(codesOut, baseOut, modOut, labelOut);
    }

    private static ChangeData buildChanges(List<String> allCodes,
                                          Map<String, Long> baseMap,
                                          Map<String, Long> modMap,
                                          Map<String, String> baseLabelMap,
                                          Map<String, String> modLabelMap,
                                          int maxRows) {

        List<ChangeRow> rows = new ArrayList<>();
        for (String code : allCodes) {
            long b = baseMap.getOrDefault(code, 0L);
            long m = modMap.getOrDefault(code, 0L);
            long d = m - b;
            if (d != 0) {
                rows.add(new ChangeRow(code, pickLabel(code, baseLabelMap, modLabelMap), b, m, d));
            }
        }

        rows.sort((r1, r2) -> Long.compare(Math.abs(r2.delta), Math.abs(r1.delta)));

        int hidden = 0;
        if (rows.size() > maxRows) {
            hidden = rows.size() - maxRows;
            rows = new ArrayList<>(rows.subList(0, maxRows));
        }

        return new ChangeData(rows, hidden);
    }

    private static class AggregatedData {
        final List<String> codes;
        final Map<String, Long> baseMap;
        final Map<String, Long> modMap;
        final Map<String, String> labelMap;

        AggregatedData(List<String> codes, Map<String, Long> baseMap, Map<String, Long> modMap, Map<String, String> labelMap) {
            this.codes = codes;
            this.baseMap = baseMap;
            this.modMap = modMap;
            this.labelMap = labelMap;
        }
    }

    private static class ChangeData {
        final List<ChangeRow> rows;
        final int hiddenCount;

        ChangeData(List<ChangeRow> rows, int hiddenCount) {
            this.rows = rows;
            this.hiddenCount = hiddenCount;
        }
    }

    private static class ChangeRow {
        final String code;
        final String label;
        final long baseValue;
        final long modValue;
        final long delta;

        ChangeRow(String code, String label, long baseValue, long modValue, long delta) {
            this.code = code;
            this.label = label;
            this.baseValue = baseValue;
            this.modValue = modValue;
            this.delta = delta;
        }
    }
}


