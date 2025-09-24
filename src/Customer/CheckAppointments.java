package Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import Class.FileActions;

public class CheckAppointments extends JPanel {
    private JTable appointmentTable;
    private final FileActions appointmentFile;
    private final String customerId;

    public CheckAppointments(String customerId) {
        this.customerId = customerId;
        setLayout(new BorderLayout());

        JLabel title = new JLabel("My Appointments", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        appointmentFile = new FileActions("appointment.txt");

        showAppointments();
    }

    private void showAppointments() {
    // must read 8 columns
    List<String[]> appointments = appointmentFile.returnAllDataFromFile(8);

    // Filter only ongoing appointments for this customer
    List<String[]> filtered = appointments.stream()
            .filter(appt -> appt[3].trim().equalsIgnoreCase(customerId.trim())) // index 3 = Customer ID
            .filter(appt -> appt[5].trim().equalsIgnoreCase("ongoing"))         // index 5 = Status
            .collect(Collectors.toList());

    if (filtered.isEmpty()) {
        add(new JLabel("There are no appointments.", SwingConstants.CENTER), BorderLayout.CENTER);
        return;
    }

    // Column headers (Staff ID + Guardian ID hidden)
    String[] columnNames = {"Appointment ID", "Doctor ID", "Start Time", "Payment ID"};

    // Build table data
    String[][] tableData = new String[filtered.size()][4];
    for (int i = 0; i < filtered.size(); i++) {
        String[] appt = filtered.get(i);
        tableData[i][0] = appt[0]; // Appointment ID
        tableData[i][1] = appt[1]; // Doctor ID
        tableData[i][2] = appt[4]; // Start Time
        tableData[i][3] = appt[6]; // Payment ID
    }

    // Non-editable table
    DefaultTableModel model = new DefaultTableModel(tableData, columnNames) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    appointmentTable = new JTable(model);
    appointmentTable.setFillsViewportHeight(true);
    appointmentTable.setRowHeight(25);
    appointmentTable.setFont(new Font("Arial", Font.PLAIN, 13));
    appointmentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

    // Striped rows
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
    for (int i = 0; i < appointmentTable.getColumnCount(); i++) {
        appointmentTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
    }

    JScrollPane scrollPane = new JScrollPane(appointmentTable);
    add(scrollPane, BorderLayout.CENTER);
    }
}
