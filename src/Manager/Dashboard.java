/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Wlhoe
 */
package Manager;

import Class.Manager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Dashboard extends JPanel {

    private String apptFilter = "Today";
    private String avgFilter = "Doctor";
    private String revenueFilter = "Monthly";
    private final DefaultTableModel model;
    private final Manager managerActions = new Manager();
    private final RevenueChartPanel chartPanel;

    public Dashboard() {

        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);

        // ─── compute our “axes” and data up front ─────────────────────────
        int anchorYear = LocalDate.now().getYear();

        // 12 months
        String[] monthLabels = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };
        double[] monthlyData = managerActions.returnMonthlyRevenue(anchorYear);

        // last 10 years (oldest first)
        String[] yearLabels = new String[10];
        for (int i = 0; i < 10; i++) {
            yearLabels[i] = String.valueOf(anchorYear - (9 - i));
        }
        double[] yearlyData = managerActions.returnYearsRevenue(anchorYear);

        chartPanel = new RevenueChartPanel(monthLabels, monthlyData);
        
        // ─── 1) Revenue Chart panel with filter ─────────────────────────
        JPanel revenuePanel = new JPanel(new BorderLayout());
        revenuePanel.setBackground(Color.WHITE);
        // a) filter row pinned to east
        JPanel revenueFilterRow = new JPanel(new BorderLayout());
        revenueFilterRow.setBackground(Color.WHITE);
        revenueFilterRow.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
        revenueFilterRow.add(
                createFilterButton("Revenue", new String[]{"Monthly", "Yearly"}, sel -> {
                    // swap the data inside chartPanel:
                    if ("Monthly".equals(sel)) {
                        chartPanel.setData(monthLabels, managerActions.returnMonthlyRevenue(anchorYear));
                    } else {
                        chartPanel.setData(yearLabels, managerActions.returnYearsRevenue(anchorYear));
                    }
                }),
                BorderLayout.EAST
        );
        
        // 3) Put them together
        revenuePanel.setBackground(Color.WHITE);
        revenuePanel.add(revenueFilterRow, BorderLayout.NORTH);
        revenuePanel.add(chartPanel, BorderLayout.CENTER);

        add(revenuePanel, BorderLayout.NORTH);

        // ─── 2) Middle row: two cards side by side ─────────────────────
        JPanel middleRow = new JPanel(new BorderLayout(20, 0));
        middleRow.setBackground(Color.WHITE);

        // a) Appointment card
        JPanel apptCard = new JPanel(new BorderLayout(0, 4));
        apptCard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        apptCard.setBackground(Color.WHITE);

        // Initialise lblCountNum
        JLabel lblCountNum = new JLabel(
                String.valueOf(managerActions.returnTotalAppointment(apptFilter)),
                SwingConstants.CENTER
        );

        // header: title centered + filter right
        JPanel apptHeader = new JPanel(new BorderLayout());
        apptHeader.setBackground(Color.WHITE);
        apptHeader.add(Box.createHorizontalStrut(1), BorderLayout.WEST);
        apptHeader.add(
                createFilterButton(
                        "Appointments",
                        new String[]{"Today", "This Week", "This Month", "This Year"},
                        sel -> {
                            apptFilter = sel;
                            int total = managerActions.returnTotalAppointment(apptFilter);
                            lblCountNum.setText(String.valueOf(total));
                        }
                ),
                BorderLayout.EAST
        );
        JLabel lblCountTitle = new JLabel("Appointments Count", SwingConstants.CENTER);
        lblCountTitle.setFont(lblCountTitle.getFont().deriveFont(Font.BOLD, 15f));
        apptHeader.add(lblCountTitle, BorderLayout.CENTER);
        apptCard.add(apptHeader, BorderLayout.NORTH);

        // big number
        lblCountNum.setFont(lblCountNum.getFont().deriveFont(Font.BOLD, 25f));
        apptCard.add(lblCountNum, BorderLayout.CENTER);

        // range label
        JLabel lblCountRange = new JLabel("This Month", SwingConstants.CENTER);
        lblCountRange.setFont(lblCountRange.getFont().deriveFont(Font.PLAIN, 15f));
        apptCard.add(lblCountRange, BorderLayout.SOUTH);

        middleRow.add(apptCard, BorderLayout.WEST);

        // b) Avg Rating card
        String[] docCols = {"Doctor ID", "Doctor Name", "Avg Rating"};

        // build table model & table
        model = new DefaultTableModel(docCols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tbl = new JTable(model);
        tbl.setShowGrid(false);
        tbl.setTableHeader(null);
        tbl.setIntercellSpacing(new Dimension(0, 0));
        tbl.setRowHeight(24);
        tbl.setFillsViewportHeight(true);
        tbl.setBackground(Color.WHITE);

        // strip built-in header
        JScrollPane tblScroll = new JScrollPane(tbl,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        tblScroll.setColumnHeaderView(null);
        tblScroll.setBorder(BorderFactory.createEmptyBorder());

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tbl.setDefaultRenderer(Object.class, centerRenderer);

        // custom header row
        JPanel customHeader = new JPanel(new GridLayout(1, docCols.length));
        customHeader.setBackground(Color.WHITE);
        for (String h : docCols) {
            JLabel lbl = new JLabel(h, SwingConstants.CENTER);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
            customHeader.add(lbl);
        }

        // feedback card container
        JPanel fbCard = new JPanel(new BorderLayout(0, 4));
        fbCard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        fbCard.setBackground(Color.WHITE);

        // header row: customHeader in CENTER, filter button in EAST
        JPanel fbHeader = new JPanel(new BorderLayout());
        fbHeader.setBackground(Color.WHITE);
        fbHeader.add(customHeader, BorderLayout.CENTER);
        fbHeader.add(
                createFilterButton(
                        "Avg Rating",
                        new String[]{"Doctor", "Staff"},
                        sel -> {
                            avgFilter = sel;
                            refreshTable();
                        }
                ),
                BorderLayout.EAST
        );
        fbCard.add(fbHeader, BorderLayout.NORTH);

        // table below
        fbCard.add(tblScroll, BorderLayout.CENTER);

        middleRow.add(fbCard, BorderLayout.CENTER);

        add(middleRow, BorderLayout.CENTER);

        refreshTable();
    }

    private JButton createFilterButton(String title, String[] options, Consumer<String> onSelect) {
        JButton btn = new JButton();
        btn.setBackground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("Filter " + title);
        // load & scale your icon
        ImageIcon ic = new ImageIcon(
                new ImageIcon(getClass().getResource("/image/filter.png"))
                        .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
        );
        btn.setIcon(ic);

        btn.addActionListener(e -> {
            // temporarily override UI defaults
            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("Button.background", Color.WHITE);
            UIManager.put("ComboBox.background", Color.WHITE);
            UIManager.put("ComboBox.foreground", Color.BLACK);

            JComboBox<String> combo = new JComboBox<>(options);
            combo.setBackground(Color.WHITE);
            combo.setOpaque(true);

            JPanel panel = new JPanel(new BorderLayout(0, 8));
            panel.setBackground(Color.WHITE);
            panel.add(new JLabel("Select " + title + " Filter:"), BorderLayout.NORTH);
            panel.add(combo, BorderLayout.CENTER);

            int res = JOptionPane.showConfirmDialog(
                    SwingUtilities.getWindowAncestor(this),
                    panel,
                    title + " Filter",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );
            if (res == JOptionPane.OK_OPTION) {
                String sel = (String) combo.getSelectedItem();
                onSelect.accept(sel);
            }

            // restore defaults
            UIManager.put("OptionPane.background", null);
            UIManager.put("Panel.background", null);
            UIManager.put("Button.background", null);
            UIManager.put("ComboBox.background", null);
            UIManager.put("ComboBox.foreground", null);
        });

        return btn;
    }

    private static JPanel makeCountBlock(String title, int count) {
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(140, 60));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setFont(t.getFont().deriveFont(Font.PLAIN, 12f));
        JLabel c = new JLabel(String.valueOf(count), SwingConstants.CENTER);
        c.setFont(c.getFont().deriveFont(Font.BOLD, 20f));
        p.add(t, BorderLayout.NORTH);
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    private static class RevenueChartPanel extends JPanel {

        private String[] labels;
        private double[] values;

        public RevenueChartPanel(String[] labels, double[] values) {
            if (labels.length != values.length) {
                throw new IllegalArgumentException("labels and values length must match");
            }
            this.labels = labels;
            this.values = values;
            setPreferredSize(new Dimension(800, 250));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createTitledBorder("Revenue"));
        }

        public void setData(String[] labels, double[] values) {
            if (labels.length != values.length) {
                throw new IllegalArgumentException("labels and values length must match");
            }
            this.values = values;
             this.labels = labels;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int margin = 40;
            int chartW = w - margin * 2;
            int chartH = h - margin * 2;

            // find max value
            double max = 0;
            for (double v : values) {
                max = Math.max(max, v);
            }
            if (max == 0) {
                max = 1;                       // avoid zero-division
            }
            max = ((max + 9) / 10) * 10;                   // round up

            // draw axes
            g2.setColor(Color.BLACK);
            g2.drawLine(margin, margin, margin, margin + chartH);
            g2.drawLine(margin, margin + chartH, margin + chartW, margin + chartH);

            int ticks = 5;
            for (int i = 0; i <= ticks; i++) {
                int y = margin + chartH - chartH * i / ticks;
                double val = max * i / ticks;
                g2.drawLine(margin - 5, y, margin, y);
                g2.drawString(String.format("%.0f", val), 5, y + 4);
            }

            // draw bars
            int n = values.length;
            int barW = chartW / (n * 2);
            for (int i = 0; i < n; i++) {
                int x = margin + (2 * i + 1) * barW;
                int barH = (int) (values[i] / max * chartH);
                int y = margin + chartH - barH;
                g2.setColor(new Color(100, 150, 240));
                g2.fillRect(x, y, barW, barH);
                g2.setColor(Color.DARK_GRAY);
                g2.drawRect(x, y, barW, barH);

                // label
                String lbl = labels[i];
                int strW = g2.getFontMetrics().stringWidth(lbl);
                g2.drawString(lbl, x + (barW - strW) / 2, margin + chartH + 15);
            }
            g2.dispose();
        }
    }

//    private static class RevenueChartPanel extends JPanel {
//
//        // sample revenue per month
//        private final String[] months = {
//            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
//        };
//        private final double[] revenue;
//
//        public RevenueChartPanel(double[] revenue) {
//            this.revenue = revenue;
//            setPreferredSize(new Dimension(800, 250));
//            setBackground(Color.WHITE);
//            setBorder(BorderFactory.createTitledBorder("Revenue"));
//        }
//
//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            Graphics2D g2 = (Graphics2D) g.create();
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                    RenderingHints.VALUE_ANTIALIAS_ON);
//
//            int w = getWidth(), h = getHeight();
//            int margin = 40;
//            int chartW = w - margin * 2;
//            int chartH = h - margin * 2;
//
//            // find max revenue
//            double max = 0;
//            for (double v : revenue) {
//                max = Math.max(max, v);
//            }
//            max = ((max + 9) / 10) * 10; // round up to nice tick
//
//            // draw Y-axis and ticks
//            g2.setColor(Color.BLACK);
//            g2.drawLine(margin, margin, margin, margin + chartH);
//            int ticks = 5;
//            for (int i = 0; i <= ticks; i++) {
//                int y = margin + chartH - (chartH * i / ticks);
//                double val = max * i / ticks;
//                g2.drawLine(margin - 5, y, margin, y);
//                g2.drawString(String.valueOf(val), 5, y + 4);
//            }
//
//            // draw X-axis
//            g2.drawLine(margin, margin + chartH, margin + chartW, margin + chartH);
//
//            // draw bars
//            int barCount = months.length;
//            int barW = chartW / (barCount * 2);
//            for (int i = 0; i < barCount; i++) {
//                int x = margin + (2 * i + 1) * barW;
//                int barH = (int) ((double) revenue[i] / max * chartH);
//                int y = margin + chartH - barH;
//                g2.setColor(new Color(100, 150, 240));
//                g2.fillRect(x, y, barW, barH);
//                g2.setColor(Color.DARK_GRAY);
//                g2.drawRect(x, y, barW, barH);
//
//                // month label
//                String m = months[i];
//                int strW = g2.getFontMetrics().stringWidth(m);
//                g2.drawString(m, x + (barW - strW) / 2, margin + chartH + 15);
//            }
//
//            g2.dispose();
//        }
//    }
    private void refreshTable() {
        model.setRowCount(0);// Remove old records
        List<String[]> rows = managerActions
                .returnAverageRatingList(avgFilter);// Retrive new records
        for (String[] r : rows) {
            model.addRow(r);
        }
    }
}
