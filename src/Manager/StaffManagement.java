package Manager;

import Class.Manager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

public class StaffManagement extends JPanel {

    // When We need to Reactive Staff, we just need to Edit the Inactive Staff
    private String _selectedRole = "All";
    private final DefaultTableModel model;
    private List<String[]> staffData = List.of();
    private final Manager managerActions ;
    
    public StaffManagement(String[] ownProfile) {
        
        managerActions = new Manager(ownProfile);
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // Checkbox ( All, Staff, Doctor, Inactive
        List<JCheckBox> boxes = new ArrayList<>();
        JPanel tagBar = new JPanel(new BorderLayout(8, 0));
        tagBar.setBackground(Color.WHITE);
        tagBar.setBorder(BorderFactory.createEmptyBorder());

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filters.setBackground(Color.WHITE);
        filters.setBorder(BorderFactory.createEmptyBorder());

        String[] tags = {
            "All", "Staff", "Manager", "Doctor", "Inactive"
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
                    _selectedRole = chb.getText();
                    refreshTable();
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

        btnAdd.addActionListener(e -> {
            AddStaff add = new AddStaff();
            add.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent we) {
                    // 2) Once it’s closed, refresh your table
                    refreshTable();
                }
            });
            SwingUtilities.invokeLater(() -> add.setVisible(true));
        });

        tagBar.add(btnAdd, BorderLayout.EAST);

        add(tagBar, BorderLayout.NORTH);

        // COlumn Headers
        String[] cols = {"Staff ID", "Staff Role", "Staff Name", "Password","Gender", "Email","Phone Number","Age"};
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

        // Build Table 
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        
        for (JCheckBox chb : boxes) {
            if ("All".equals(chb.getText())) {
                chb.setSelected(true);    // fires your ItemListener → calls refreshTable()
                break;
            }
        }

        // Create the JTable
        JTable table = new JTable(model);
        table.setTableHeader(null);
        table.setBackground(Color.WHITE);
        table.setFont(table.getFont().deriveFont(20f));
        table.setShowGrid(false);
        table.setRowHeight(40);
        table.setIntercellSpacing(new Dimension(0, 10));
        table.setRowHeight(table.getRowHeight() + 10);

        // Call Renderer for Table Data 
        DefaultTableCellRenderer cellRend = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, false, row, column);
                Border outer = BorderFactory.createLineBorder(Color.BLACK, 1);
                Border inner = BorderFactory.createEmptyBorder(0, 0, 0, 0);
                lbl.setBorder(BorderFactory.createCompoundBorder(outer, inner));
                return lbl;
            }
        };
        table.setDefaultRenderer(Object.class, cellRend);

        // Context Menu ( Right Click ) 
        JPopupMenu popup = new JPopupMenu();
        JMenuItem miEdit = new JMenuItem("Edit");
        JMenuItem miInact = new JMenuItem("Inactive User");
        popup.add(miEdit);
        popup.add(miInact);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // for Windows/Linux
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            private void showMenu(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && row < table.getRowCount()) {
                    table.setRowSelectionInterval(row, row);
                    popup.show(table, e.getX(), e.getY());
                }
            }
        });

        // Edit Profile
        miEdit.addActionListener(evt -> {
            int row = table.getSelectedRow();
            String[] staff = staffData.get(row);

            // 1) Create the EditStaff window
            EditStaff edit = new EditStaff(staff);

            // 2) When it closes, refresh this panel’s table
            edit.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    refreshTable();
                }
            });

            // 3) Show it
            SwingUtilities.invokeLater(() -> edit.setVisible(true));
        });
        
        // Inactive User
        miInact.addActionListener(evt -> {
            int row = table.getSelectedRow();
            String[] staff = staffData.get(row);

            managerActions.InactiveStaff(staff);
            refreshTable();
        });

        // 6) Finally add to your scroll pane & container
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);
    }

    private void refreshTable() {
        model.setRowCount(0);
        staffData = managerActions.returnStaffData(_selectedRole);
        staffData.forEach(model::addRow);
    }
}
