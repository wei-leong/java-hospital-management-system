package Staff;

import Class.Staff;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomerManagement extends JPanel{
    
    private String _selectedRole = "All";
    private List<String[]> CustomerData = List.of();
    private DefaultTableModel model;
    private JTable table;
    Staff CustomerDetails = new Staff();
    private JButton AddCustomerbtn;
            
    public CustomerManagement() {
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
            "All", "Customer","Inactive"
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
        JButton btnEdit = new JButton("Edit");
        
        //Set button Color
        for (JButton btn : new JButton[]{btnAdd, btnEdit}) {
        btn.setBackground(Color.WHITE);                            //
        btn.setContentAreaFilled(false);                           
        btn.setOpaque(true);                                     
        btn.setFocusPainted(false);                               

}
        Button1.add(btnAdd);
        Button1.add(btnEdit);
        btnAdd.addActionListener(e -> {
        AddCustomer dialog = new AddCustomer(); // 创建 AddCustomer 对话框
        dialog.setLocationRelativeTo(this);     // 设置弹窗居中
        dialog.setVisible(true);                // 显示对话框

        // 如果 AddCustomer 有提供 getCustomerData() 之类的方法，你可以在这里添加到 table：
        if (dialog.isSaved()) {
            String[] newCustomer = dialog.getCustomerData(); // 示例方法，自己定义
            model.addRow(newCustomer); // 加入表格
        }
    });
        // 2) Column headers
        String[] cols = {"Staff ID", "Staff Name", "Phone Number", "Email"};
        JPanel headerBar = new JPanel(new GridLayout(1, cols.length, 8, 0));
        headerBar.setBackground(Color.WHITE);
        headerBar.setBorder(BorderFactory.createEmptyBorder());  

        for (String c : cols) {
            JLabel lbl = new JLabel(c, SwingConstants.LEFT);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
            lbl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // minimal padding
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
        

        // 3) Data table area
        Object[][] sampleData = {
            {"C1", "Alice Tan", "012-3456789", "alice@apumed.edu"},
            {"M2", "Bob Lee", "013-9876543", "bob@apumed.edu"}
        };
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

        DefaultTableCellRenderer cellRend = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, false, row, column);
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

        // 初始加载一次（因为默认 All 被选中）
        refreshTable();
    }
    
       private void refreshTable() {
        if (model == null) return; // 保险
        model.setRowCount(0);
        CustomerData = CustomerDetails.returnCustomerData(_selectedRole);
        if (CustomerData != null) {
            CustomerData.forEach(model::addRow);
        }
    }
}
