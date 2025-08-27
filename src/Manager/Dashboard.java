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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Dashboard extends JPanel {

    private String apptFilter = "Today";
    private String avgFilter = "Doctor";
    private String revenueFilter = "Monthly";
    private final DefaultTableModel model;
    private final Manager managerActions = new Manager();
    private final RevenueChartPanel chartPanel;
    private final String[] monthLabels = {
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };
    private final String[] ratingCols = {"Person ID", "Person Name", "Avg Rating"};
    private final String[] paymentCols = {"Customer ID","Customer Name","Amount"};
    private final int anchorYear = LocalDate.now().getYear();

    public Dashboard() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        double[] monthlyData = managerActions.returnMonthlyRevenue(anchorYear);
        chartPanel = new RevenueChartPanel(monthLabels, monthlyData);

        // Initialize model (same as before)
        model = new DefaultTableModel(ratingCols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        // Build content into an inner panel so the scroll pane can scroll the whole thing
        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // revenue at top
        content.add(revenueSection(), BorderLayout.NORTH);

        // middle area wrapped in a fixed-size panel
        JPanel middleWrapper = new JPanel(new BorderLayout());
        middleWrapper.setBackground(Color.WHITE);
        middleWrapper.setPreferredSize(new Dimension(300, 180)); // FIXED WIDTH & HEIGHT for middle section
        middleWrapper.add(middleSection(), BorderLayout.CENTER);
        content.add(middleWrapper, BorderLayout.CENTER);

        // bottom (optional extra info) - keep as before (you had bottomSection())
        content.add(bottomSection(), BorderLayout.SOUTH);

        // Put the content panel inside a scroll pane so whole dashboard becomes scrollable
        JScrollPane sc = new JScrollPane(content,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sc.getViewport().setBackground(Color.WHITE);
        sc.setBorder(BorderFactory.createEmptyBorder());
        // Slight performance/useful hints:
        sc.getVerticalScrollBar().setUnitIncrement(16); // smoother scrolling

        // Add scroll pane to this Dashboard panel
        this.setLayout(new BorderLayout());
        this.add(sc, BorderLayout.CENTER);

        // final population
        refreshTable();
    }

    private JPanel revenueSection() {
        // last 10 years (oldest first)
        String[] yearLabels = new String[10];
        for (int i = 0; i < 10; i++) {
            yearLabels[i] = String.valueOf(anchorYear - (9 - i));
        }

        // Revenue Chart Panel with Filter Options
        JPanel revenuePanel = new JPanel(new BorderLayout());
        revenuePanel.setBackground(Color.WHITE);
        // Filter Button
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

        revenuePanel.add(revenueFilterRow, BorderLayout.NORTH); // Add Revenue chart at NORTH
        revenuePanel.add(chartPanel, BorderLayout.CENTER); // Add the Revenue chart at CENTER

        return revenuePanel;
    }

    // Store both Total Appointments and Average Rating for Doctor / Staff
    private JPanel middleSection() {
        // Panel for Storing Total Appointments ( WEST ) + Average Rating ( CENTER ) 
        JPanel middleRow = new JPanel(new BorderLayout(20, 0));
        middleRow.setBackground(Color.WHITE);

        middleRow.add(returnAppointmentCard(), BorderLayout.WEST); // Add AppointmentCard 
        middleRow.add(returnAverageRatingTable(), BorderLayout.CENTER); // Add AverageRating 

        return middleRow;
    }

    private JPanel returnAppointmentCard() {
        // Appointment card JPanel
        JPanel apptCard = new JPanel(new BorderLayout(0, 4));
        apptCard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        apptCard.setBackground(Color.WHITE);

        // Initialise lblCountNum
        JLabel lblCountNum = new JLabel(
                String.valueOf(managerActions.returnTotalAppointment(apptFilter)),
                SwingConstants.CENTER
        );

        // Header: Total Appointment Title ( WEST ) + Filter Options ( EAST )
        JPanel apptHeader = new JPanel(new BorderLayout());
        JLabel lblCountRange = new JLabel("Today", SwingConstants.CENTER); // Initialise lblCountRange to let Filter Button use
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
                            lblCountRange.setText(apptFilter);
                        }
                ),
                BorderLayout.EAST
        );

        // Appointments Count Label
        JLabel lblCountTitle = new JLabel("Appointments Count", SwingConstants.CENTER);
        lblCountTitle.setFont(lblCountTitle.getFont().deriveFont(Font.BOLD, 15f));
        apptHeader.add(lblCountTitle, BorderLayout.CENTER);
        apptCard.add(apptHeader, BorderLayout.NORTH);

        // Total Appointments Number
        lblCountNum.setFont(lblCountNum.getFont().deriveFont(Font.BOLD, 25f));
        apptCard.add(lblCountNum, BorderLayout.CENTER);

        // Range Label
        lblCountRange.setFont(lblCountRange.getFont().deriveFont(Font.PLAIN, 15f));
        apptCard.add(lblCountRange, BorderLayout.SOUTH);
        return apptCard;
    }

    private JPanel returnAverageRatingTable() {
        // Build table model & table
        JTable tbl = new JTable(model);
        tbl.setShowGrid(false);
        tbl.setTableHeader(null);
        tbl.setIntercellSpacing(new Dimension(0, 0));
        tbl.setRowHeight(24);
        tbl.setFillsViewportHeight(true);
        tbl.setBackground(Color.WHITE);

        // Strip built-in header
        JScrollPane tblScroll = new JScrollPane(tbl,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        tblScroll.setColumnHeaderView(null);
        tblScroll.setBorder(BorderFactory.createEmptyBorder());

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tbl.setDefaultRenderer(Object.class, centerRenderer);

        // Custom Header
        JPanel customHeader = new JPanel(new GridLayout(1, ratingCols.length));
        customHeader.setBackground(Color.WHITE);
        for (String h : ratingCols) {
            JLabel lbl = new JLabel(h, SwingConstants.CENTER);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
            customHeader.add(lbl);
        }

        // Feedback Card Container
        JPanel fbCard = new JPanel(new BorderLayout(0, 4));
        fbCard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        fbCard.setBackground(Color.WHITE);

        // Header Section with custom header ( CENTER ) and filter button ( EAST )
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

        // Add Table
        fbCard.add(tblScroll, BorderLayout.CENTER);
        return fbCard;
    }
    
    private JPanel bottomSection() {
        // Panel for Storing Total Appointments ( WEST ) + Average Rating ( CENTER ) 
        JPanel bottomRow = new JPanel(new BorderLayout(20, 0));
        bottomRow.setBackground(Color.WHITE);
        
        bottomRow.add(returnPendingPaymentsTable(), BorderLayout.CENTER); // Add AverageRating 
        bottomRow.add(returnCustomerAvgAgeCard(), BorderLayout.EAST); // Add AppointmentCard 

        return bottomRow;
    }
    
    private JPanel returnPendingPaymentsTable() {
        // Build table model & table
        JTable tbl = new JTable(model);
        tbl.setShowGrid(false);
        tbl.setTableHeader(null);
        tbl.setIntercellSpacing(new Dimension(0, 0));
        tbl.setRowHeight(24);
        tbl.setFillsViewportHeight(true);
        tbl.setBackground(Color.WHITE);

        // Strip built-in header
        JScrollPane tblScroll = new JScrollPane(tbl,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        tblScroll.setColumnHeaderView(null);
        tblScroll.setBorder(BorderFactory.createEmptyBorder());

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tbl.setDefaultRenderer(Object.class, centerRenderer);

        // Custom Header
        JPanel customHeader = new JPanel(new GridLayout(1, paymentCols.length));
        customHeader.setBackground(Color.WHITE);
        for (String h : paymentCols) {
            JLabel lbl = new JLabel(h, SwingConstants.CENTER);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
            customHeader.add(lbl);
        }

        // Feedback Card Container
        JPanel fbCard = new JPanel(new BorderLayout(0, 4));
        fbCard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        fbCard.setBackground(Color.WHITE);

        // Add Customized Table Header
        fbCard.add(customHeader, BorderLayout.NORTH);

        // Add Table
        fbCard.add(tblScroll, BorderLayout.CENTER);
        return fbCard;
    }
    
    private JPanel returnCustomerAvgAgeCard(){
        // Customer Average Age card JPanel
        JPanel avgCard = new JPanel(new BorderLayout(0, 4));
        avgCard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        avgCard.setBackground(Color.WHITE);

        // Initialise lblCountNum
        JLabel lblCountNum = new JLabel(
                String.valueOf(managerActions.returnCustomerAverageAge()),
                SwingConstants.CENTER
        );

        // Header: Total Appointment Title ( WEST ) + Filter Options ( EAST )
        JPanel avgHeader = new JPanel(new BorderLayout());

        // Appointments Count Label
        JLabel lblAvgAgeTitle = new JLabel("Customer Average Age", SwingConstants.CENTER);
        lblAvgAgeTitle.setFont(lblAvgAgeTitle.getFont().deriveFont(Font.BOLD, 15f));
        avgHeader.add(lblAvgAgeTitle, BorderLayout.CENTER);
        avgCard.add(avgHeader, BorderLayout.NORTH);

        // Total Appointments Number
        lblCountNum.setFont(lblCountNum.getFont().deriveFont(Font.BOLD, 25f));
        avgCard.add(lblCountNum, BorderLayout.CENTER);

        return avgCard;
    }

    // Filter Button for Revenue, Total Appointment, Average Rating Section
    private JButton createFilterButton(String title, String[] options, Consumer<String> onSelect) {
        JButton btn = new JButton();
        btn.setBackground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("Filter " + title);
        // Load & Scale Icon
        ImageIcon ic = new ImageIcon(
                new ImageIcon(getClass().getResource("/image/filter.png"))
                        .getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)
        );
        btn.setIcon(ic);

        btn.addActionListener(e -> {
            // Temporarily override UI defaults
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

            // Restore Defaults
            UIManager.put("OptionPane.background", null);
            UIManager.put("Panel.background", null);
            UIManager.put("Button.background", null);
            UIManager.put("ComboBox.background", null);
            UIManager.put("ComboBox.foreground", null);
        });

        return btn;
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
                max = 1;// avoid zero-division
            }
            max = ((max + 9) / 10) * 10; // round up

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

    private void refreshTable() {
        model.setRowCount(0);// Remove old records
        List<String[]> rows = managerActions
                .returnAverageRatingList(avgFilter);// Retrive new records
        for (String[] r : rows) {
            model.addRow(r);
        }
    }
}