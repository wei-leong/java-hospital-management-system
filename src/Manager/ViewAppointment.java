/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Manager;

import Class.Manager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.ArrayList;

public class ViewAppointment extends JPanel {

    private String _selectedRange = "Today";
    private final DefaultTableModel model;
    private List<String[]> appointments = List.of();
    private final Manager managerActions = new Manager();
    private final String[] cols = {"Appointment ID", "Doctor ID", "Customer ID", "Start", "Status"};

    public ViewAppointment() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        
        add(northSection(), BorderLayout.NORTH);// Appointment Filter Options + Custom Table Heading
        add(appointmentTable(), BorderLayout.CENTER);// Appointments Data ( Table Data ) 
        
    }
    
    private JPanel northSection(){
        refreshTable(); // populate model

        JPanel headerBar = new JPanel(new GridLayout(1, cols.length, 8, 0));
        headerBar.setBackground(Color.WHITE);
        for (String c : cols) {
            JLabel lbl = new JLabel(c, SwingConstants.LEFT);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
            lbl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            headerBar.add(lbl);
        }

        // 1) Build your tag-checkbox bar
        String[] ranges = {"Today", "This Week", "This Month", "This Year"};
        JPanel tagBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        tagBar.setBackground(Color.WHITE);
        List<JCheckBox> boxes = new ArrayList<>();
        for (String r : ranges) {
            JCheckBox cb = new JCheckBox(r);
            cb.setBackground(Color.WHITE);
            tagBar.add(cb);
            boxes.add(cb);
            cb.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _selectedRange = cb.getText();
                    boxes.forEach(other -> {
                        if (other != cb) {
                            other.setSelected(false);
                        }
                    });
                    refreshTable();
                }
            });
        }
        // Default Selection
        boxes.get(0).setSelected(true);

        // Combine headerBar + tagBar into one northWrapper
        JPanel northWrapper = new JPanel();
        northWrapper.setLayout(new BoxLayout(northWrapper, BoxLayout.Y_AXIS));
        northWrapper.setBackground(Color.WHITE);
        northWrapper.add(tagBar);
        northWrapper.add(headerBar);
        
        return northWrapper;
    }
    
    private JTable appointmentTable(){
        // Create JTable, hide built-in header
        JTable table = new JTable(model);
        table.setTableHeader(null);           // ← remove default header
        table.setFillsViewportHeight(true);
        table.setRowHeight(table.getRowHeight() + 8);

        return table;
    }

    private void refreshTable() {
        model.setRowCount(0);
        // Assume managerActions.returnAppointments(range) returns List<String[]> rows
        appointments = managerActions.returnAppointmentsList(_selectedRange);
        for (String[] row : appointments) {
            model.addRow(row);
        }
    }
}
