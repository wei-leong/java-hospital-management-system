package Manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StaffManagement extends JPanel {
    public StaffManagement() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setBackground(Color.WHITE);

        // 1) Tag bar
        JPanel tagBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        tagBar.setBackground(Color.WHITE);
        String[] tags = {
            "All", "Manager", "Reservation Coordinator", "Customer", "Chef", "Inactive"
        };
        for (String t : tags) {
            JCheckBox cb = new JCheckBox(t);
            cb.setBackground(Color.WHITE);
            tagBar.add(cb);
        }
        add(tagBar, BorderLayout.NORTH);

        // 2) Column headers
        JPanel headerBar = new JPanel(new GridLayout(1, 1));
        headerBar.setBackground(Color.WHITE);
        headerBar.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        String[] cols = { "Staff ID", "Staff Name", "Phone Number", "Email" };
        for (String c : cols) {
            JLabel lbl = new JLabel(c, SwingConstants.LEFT);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
            lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.WHITE));
            headerBar.add(lbl);
        }
        add(headerBar, BorderLayout.CENTER);

        // 3) Data table area
        String[] columnNames = cols;
        Object[][] sampleData = {
            { "C1", "Alice Tan", "012-3456789", "alice@apumed.edu" },
            { "M2", "Bob Lee",   "013-9876543", "bob@apumed.edu" }
            // ... your real data here
        };
        DefaultTableModel model = new DefaultTableModel(sampleData, columnNames) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.SOUTH);
    }
}
