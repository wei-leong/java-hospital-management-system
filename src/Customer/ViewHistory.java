package Customer;

import Class.FileActions;
import Class.TextAreaRenderer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ViewHistory extends JPanel {

    private final FileActions commentFile;
    private final FileActions feedbackFile;
    private final FileActions appointmentFile;
    private final FileActions paymentFile;
    private final String customerId;
    private final JTabbedPane tabs;

    public ViewHistory(String customerId) {
        this.customerId = customerId;
        this.commentFile = new FileActions("comments.txt");
        this.feedbackFile = new FileActions("feedback.txt");
        this.appointmentFile = new FileActions("appointment.txt");
        this.paymentFile = new FileActions("payment.txt");

        setLayout(new BorderLayout());

        tabs = new JTabbedPane();
        tabs.add("Doctor Comments", buildDoctorCommentsPanel());
        tabs.add("Feedback Given", buildCustomerFeedbackPanel());
        tabs.add("Appointments History", buildAppointmentsHistoryPanel());
        tabs.add("Payment History", buildPaymentsHistoryPanel());

        // 🔄 Auto-refresh whenever a tab is selected
        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int index = tabs.getSelectedIndex();
                switch (index) {
                    case 0 -> tabs.setComponentAt(0, buildDoctorCommentsPanel());
                    case 1 -> tabs.setComponentAt(1, buildCustomerFeedbackPanel());
                    case 2 -> tabs.setComponentAt(2, buildAppointmentsHistoryPanel());
                    case 3 -> tabs.setComponentAt(3, buildPaymentsHistoryPanel());
                }
            }
        });

        add(tabs, BorderLayout.CENTER);
    }

    private JScrollPane buildDoctorCommentsPanel() {
        List<String[]> rows = commentFile.returnAllDataFromFile(3).stream()
                .filter(r -> r.length >= 3 && r[2].equals(customerId))
                .collect(Collectors.toList());

        if (rows.isEmpty()) {
            return wrapNoData("No doctor comments found.");
        }

        String[] cols = {"Comment ID", "Message"};
        String[][] data = new String[rows.size()][cols.length];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i)[0];
            data[i][1] = rows.get(i)[1];
        }

        JTable table = new JTable(new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        });

        table.getColumnModel().getColumn(1).setCellRenderer(new TextAreaRenderer());
        styleTable(table);
        return new JScrollPane(table);
    }

    private JScrollPane buildCustomerFeedbackPanel() {
        List<String[]> rows = feedbackFile.returnAllDataFromFile(6).stream()
                .filter(r -> r.length >= 6 && r[1].equals(customerId))
                .collect(Collectors.toList());

        if (rows.isEmpty()) {
            return wrapNoData("No feedback history found.");
        }

        String[] cols = {"Feedback ID", "Rating", "Message", "Staff/Doctor ID", "Date"};
        String[][] data = new String[rows.size()][cols.length];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i)[0];
            data[i][1] = rows.get(i)[2];
            data[i][2] = rows.get(i)[3];
            data[i][3] = rows.get(i)[4];
            data[i][4] = rows.get(i)[5];
        }

        JTable table = new JTable(new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        });

        table.getColumnModel().getColumn(2).setCellRenderer(new TextAreaRenderer());
        styleTable(table);
        return new JScrollPane(table);
    }

    private JScrollPane buildAppointmentsHistoryPanel() {
        List<String[]> rows = appointmentFile.returnAllDataFromFile(8).stream()
                .filter(r -> r.length >= 6 && r[3].equals(customerId) && r[5].equalsIgnoreCase("Complete"))
                .collect(Collectors.toList());

        if (rows.isEmpty()) {
            return wrapNoData("No appointment history found.");
        }

        String[] cols = {"Appointment ID", "Doctor ID", "Start Time", "Status", "Payment ID"};
        String[][] data = new String[rows.size()][cols.length];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i)[0];
            data[i][1] = rows.get(i)[1];
            data[i][2] = rows.get(i)[4];
            data[i][3] = rows.get(i)[5];
            data[i][4] = rows.get(i)[6];
        }

        JTable table = new JTable(new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        });

        styleTable(table);
        return new JScrollPane(table);
    }

    private JScrollPane buildPaymentsHistoryPanel() {
        List<String[]> appointments = appointmentFile.returnAllDataFromFile(8).stream()
                .filter(r -> r.length >= 6 && r[3].equals(customerId) && r[5].equalsIgnoreCase("Complete"))
                .collect(Collectors.toList());

        List<String[]> payments = paymentFile.returnAllDataFromFile(4);
        List<String[]> rows = payments.stream()
                .filter(p -> appointments.stream().anyMatch(a -> a[6].equals(p[0])))
                .collect(Collectors.toList());

        if (rows.isEmpty()) {
            return wrapNoData("No payment history found.");
        }

        String[] cols = {"Payment ID", "Amount", "Issued Date", "Payment Status"};
        String[][] data = new String[rows.size()][cols.length];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i)[0];
            data[i][1] = rows.get(i)[1];
            data[i][2] = rows.get(i)[2];
            data[i][3] = rows.get(i)[3];
        }

        JTable table = new JTable(new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        });

        styleTable(table);
        return new JScrollPane(table);
    }

    private JScrollPane wrapNoData(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, BorderLayout.CENTER);
        return new JScrollPane(panel);
    }

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                }
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 1 && i != 2) { 
                table.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }
        }
    }
}
