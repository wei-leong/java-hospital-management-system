package Manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

public class StaffManagement extends JPanel {

    public StaffManagement() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // Checkbox ( All, Staff, Doctor, Inactive
        List<JCheckBox> boxes = new ArrayList<>();
        JPanel tagBar = new JPanel(new BorderLayout(8,0));
        tagBar.setBackground(Color.WHITE);
        tagBar.setBorder(BorderFactory.createEmptyBorder());
        
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filters.setBackground(Color.WHITE);
        filters.setBorder(BorderFactory.createEmptyBorder());

        String[] tags = {
            "All", "Staff", "Doctor", "Inactive"
        };

        for (String t : tags) {
            JCheckBox checkBox = new JCheckBox(t);
            checkBox.setBackground(Color.WHITE);
            checkBox.setForeground(Color.BLACK);
            checkBox.setOpaque(true);
            checkBox.setFocusPainted(false);
            filters.add(checkBox);
            boxes.add(checkBox);
        }

        for (JCheckBox chb : boxes) {
            chb.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println(chb.getText());
                    for (JCheckBox otherRole : boxes) {
                        if (otherRole != chb) {
                            otherRole.setSelected(false);
                        }
                    }
                }
            });
        }
        tagBar.add(filters, BorderLayout.WEST);
       
        // Add Staff Button
        JButton btnAdd = new JButton("+ Add Staff");
        btnAdd.setBackground(Color.WHITE);
        btnAdd.setForeground(Color.BLACK);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        tagBar.add(btnAdd, BorderLayout.EAST);
        
        add(tagBar, BorderLayout.NORTH);

        // 2) Column headers
        String[] cols = {"Staff ID", "Staff Name", "Phone Number", "Email"};
        JPanel headerBar = new JPanel(new GridLayout(1, cols.length, 8, 0));
        headerBar.setBackground(Color.WHITE);
        headerBar.setBorder(BorderFactory.createEmptyBorder());  // no panel border

        for (String c : cols) {
            JLabel lbl = new JLabel(c, SwingConstants.LEFT);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
            lbl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // minimal padding
            headerBar.add(lbl);
        }

        JPanel northWrapper = new JPanel();
        northWrapper.setLayout(new BoxLayout(northWrapper, BoxLayout.Y_AXIS));
        northWrapper.setBackground(Color.WHITE);
        northWrapper.add(tagBar);
        northWrapper.add(headerBar);

        add(northWrapper, BorderLayout.NORTH);

        // 3) Data table area
        Object[][] sampleData = {
            {"C1", "Alice Tan", "012-3456789", "alice@apumed.edu"},
            {"M2", "Bob Lee", "013-9876543", "bob@apumed.edu"}
        };
        DefaultTableModel model = new DefaultTableModel(sampleData, cols) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setTableHeader(null);
        table.setFont(table.getFont().deriveFont(20f));
        table.setShowGrid(false);

        table.setIntercellSpacing(new Dimension(0, 10));

        DefaultTableCellRenderer cellRend = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, false, row, column);
                // Outer border: 1px black. Inner border: 5px padding.
                Border outer = BorderFactory.createLineBorder(Color.BLACK, 1);
                Border inner = BorderFactory.createEmptyBorder(5, 5, 5, 5);
                lbl.setBorder(BorderFactory.createCompoundBorder(outer, inner));
                return lbl;
            }
        };
        table.setDefaultRenderer(Object.class, cellRend);

        table.setRowHeight(table.getRowHeight() + 10);

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
    }
}
