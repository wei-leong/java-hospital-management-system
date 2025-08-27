package Staff;

import Class.Staff;
import Class.TableStyle;
import Manager.EditStaff;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomerManagement extends JPanel{
    private final Staff StaffActions;
    private String _selectedRole = "Active";
    private List<String[]> CustomerData = List.of();
    private DefaultTableModel model;
    private JTable table;
    Staff CustomerDetails = new Staff();
    private JButton AddCustomerbtn;
            
    public CustomerManagement(String[] ownProfile) {
    StaffActions = new Staff(ownProfile);
    setLayout(new BorderLayout());
       //Checkbox
        List<JCheckBox> boxes = new ArrayList<>();
        JPanel tagBar = new JPanel(new BorderLayout(8, 0));
        tagBar.setBackground(Color.WHITE);
        tagBar.setBorder(BorderFactory.createEmptyBorder());

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filters.setBackground(Color.WHITE);
        filters.setBorder(BorderFactory.createEmptyBorder());

        String[] tags = {
           "Active","Inactive"
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
   
                    for (JCheckBox otherRole : boxes) {
                        if (otherRole != chb) {
                            otherRole.setSelected(false);
                        }
                    }
                    refreshTable();
                }
            });
        }

        boxes.get(0).setSelected(true);

        tagBar.add(filters, BorderLayout.WEST);
        
        // 2) Add and Edit customer Button
        JPanel Button1 = new JPanel();
        Button1.setLayout(new FlowLayout(FlowLayout.RIGHT,10,5));
        Button1.setForeground(Color.white);
        Button1.setBackground(Color.WHITE);
        Button1.setOpaque(true); 
        
        JButton btnAdd = new JButton("Create");
        
        //Set button Color
        for (JButton btn : new JButton[]{btnAdd}) {
        btn.setBackground(Color.WHITE);                            //
        btn.setContentAreaFilled(false);                           
        btn.setOpaque(true);                                     
        btn.setFocusPainted(false);                               

}
        Button1.add(btnAdd);
        btnAdd.addActionListener(e -> {
        AddCustomer dialog = new AddCustomer(CustomerManagement.this); 
        dialog.setLocationRelativeTo(this);     
        dialog.setVisible(true); // set the page visible
    });
        
        // 2) Column headers
        String[] cols = {"Staff ID", "Staff Role", "Name", "Password","Gender","Email","Phone Number","Age","Status","Edit"};
        JPanel headerBar = new JPanel(new GridLayout(1, cols.length, 10, 0));
        headerBar.setBackground(Color.WHITE);
        headerBar.setBorder(BorderFactory.createEmptyBorder());  

        for (String c : cols) {
            JLabel lbl = new JLabel(c, SwingConstants.LEFT);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
            lbl.setBorder(BorderFactory.createEmptyBorder(2, 2, 4, 2)); // minimal padding
            headerBar.add(lbl);
        }

        JPanel northWrapper = new JPanel();
        northWrapper.setLayout(new BoxLayout(northWrapper, BoxLayout.Y_AXIS));
        northWrapper.setBackground(Color.WHITE);
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.add(tagBar, BorderLayout.WEST);
        topBar.add(Button1, BorderLayout.EAST);

        northWrapper.add(topBar);
        northWrapper.add(headerBar);
        northWrapper.add(headerBar);
        add(northWrapper, BorderLayout.NORTH);
        
        // Table for showing the needed data
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);
        table.setTableHeader(null);
        table.setFont(table.getFont().deriveFont(20f));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 10));
        
        TableStyle.applyStyle(table);
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            int row = table.rowAtPoint(e.getPoint());
            int col = table.columnAtPoint(e.getPoint());

            if (col == model.getColumnCount() - 1 && row >= 0) {
                String[] customer = CustomerData.get(row);

                EditStaff edit = new EditStaff(customer);

                edit.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        refreshTable();
                    }
                });

                SwingUtilities.invokeLater(() -> edit.setVisible(true));
            }
        }
});
        
        //Edit icon Button (use for edit customer information)
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellRenderer(new DefaultTableCellRenderer() {
        ImageIcon rawIcon = new ImageIcon(getClass().getResource("/image/edit_profile.png"));
        Image img = rawIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Icon editIcon = new ImageIcon(img); 
        
        
        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            JLabel lbl = new JLabel(editIcon);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            return lbl;
        }
    });

        DefaultTableCellRenderer cellRend = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, false, row, column);
                Border outer = BorderFactory.createLineBorder(Color.BLACK, 1);
                Border inner = BorderFactory.createEmptyBorder(5, 5, 5, 10);
                lbl.setBorder(BorderFactory.createCompoundBorder(outer, inner));
                return lbl;
            }
        };
        table.setDefaultRenderer(Object.class, cellRend);
        table.setRowHeight(table.getRowHeight() + 10);

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        refreshTable();
    }
       
       //While new customer data is get in, the function use to refresh the table. Let user can see the new data in the table at the first time
       public void refreshTable() {
        if (model == null) return;
        model.setRowCount(0);
        CustomerData = CustomerDetails.returnCustomerData(_selectedRole);
        if (CustomerData != null) {
            CustomerData.stream()
            .filter(row -> row[0] != null && row[0].startsWith("C"))
            .forEach(row ->{
            Object[] newRow = new Object[row.length + 1];
                System.arraycopy(row, 0, newRow, 0, row.length);

                newRow[newRow.length - 1] = ""; 

                model.addRow(newRow);
            });
        }
    }   
       
}

