package Staff;

import Class.Staff;
import Class.TableStyle;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

public class AppointmentsManagement extends JPanel{
    
    private String _selectedRole = "All";
    private List<String[]> CustomerData = List.of();
    private DefaultTableModel model;
    private JTable table;
    Staff CustomerDetails = new Staff();
    private JButton AddCustomerbtn;
            
    public AppointmentsManagement() {
    setLayout(new BorderLayout());
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filters.setBackground(Color.WHITE);
        filters.setBorder(BorderFactory.createEmptyBorder());
        
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
        
        btnAdd.addActionListener(e -> {
            CreateAppointment dialog = new CreateAppointment(AppointmentsManagement.this);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });
}
        Button1.add(btnAdd);
        
        // 2) Column headers
        String[] cols = {"Appointments ID", "Docter Name", "Customer Name", "Time","Status","Edit"};
        JPanel headerBar = new JPanel(new GridLayout(1, cols.length, 12, 0));
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
        topBar.add(Button1, BorderLayout.EAST);

        northWrapper.add(topBar);
        northWrapper.add(headerBar);
        northWrapper.add(headerBar);
        add(northWrapper, BorderLayout.NORTH);
        
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
