package Staff;

import Class.Staff;
import Class.TableStyle;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

public class AppointmentsManagement extends JPanel{
    
    private String _selectedRole = "All";
    private List<String[]> AppointmentData = List.of();
    private DefaultTableModel model;
    private JTable table;
    Staff appointmenttable = new Staff();
    private JButton AddCustomerbtn;
    private final Staff StaffActions;
    private String[] ownProfile;
            
    public AppointmentsManagement(String[] ownProfile) {
    this.ownProfile = ownProfile;  
    this.StaffActions = new Staff(ownProfile);
    setLayout(new BorderLayout());
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filters.setBackground(Color.WHITE);
        filters.setBorder(BorderFactory.createEmptyBorder());
        
        // 1) Create Appointment Button
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
        
        //Call out Create Appointment Function
        btnAdd.addActionListener(e -> {
            CreateAppointment dialog = new CreateAppointment(AppointmentsManagement.this,ownProfile);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });
}
        Button1.add(btnAdd);
        
        // 2) Column headers
        String[] cols = {"Appointments ID", "Docter ID", "Customer ID", "Date & Time","Status","Cancel"};
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
        add(northWrapper, BorderLayout.NORTH);
        
        
        //Table for showing appointment data
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
        
        //Table style use to control the table size
        TableStyle.applyStyle(table);
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            int row = table.rowAtPoint(e.getPoint());    // get the row u click
            int col = table.columnAtPoint(e.getPoint()); // get the column u click

            // Click for the last column (Cause that the function icon is put on the last column)
            if (col == model.getColumnCount() - 1 && row >= 0) {
                int confirm = JOptionPane.showConfirmDialog(
                        AppointmentsManagement.this,
                        "Are you sure you want to delete this appointment?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    // get the appointment id that u want to delete
                    String appointmentId = (String) model.getValueAt(row, 0);

                    // remove from table
                    model.removeRow(row);

                    // remove the data from the txt file
                    removeAppointmentFromFile(appointmentId);
                }
            }
        }
    });
        
        //put the icon image into the last column on the table
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellRenderer(new DefaultTableCellRenderer() {
        ImageIcon rawIcon = new ImageIcon(getClass().getResource("/image/delete.png"));
        Image img = rawIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Icon editIcon = new ImageIcon(img); 
        
        //use to add the icon to the table 
        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            JLabel lbl = new JLabel(editIcon);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            return lbl;
        }
    });
        
        //set the data have an border on the table
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

        refreshAppointmentTable();
    }
    
    
    //Use for refresh the appointment showing table
    public void refreshAppointmentTable() {
        if (model == null) return;
        model.setRowCount(0);  // clear all of the old data

        try (BufferedReader br = new BufferedReader(new FileReader("D:\\USER BACKUP\\Documents\\NetBeansProjects\\apu-medical-centre\\src\\txt\\appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // if the line is empty, keep continue the function
                if (line.trim().isEmpty()) continue;

                // all the data is use "," to save
                String[] row = line.split(",");

                // make sure the lenght of the data is biggest than 4
                if (row.length < 5) {
                System.out.println("Invalid line skipped: " + line);
                continue; 
             }
                
                String appointmentId = row[0].trim();
                String doctorId = row[1].trim();
                String customerId = row[3].trim();
                String dateTime = row[4].trim();
                String status = row[5].trim();
                
                Object[] newRow = { appointmentId, doctorId, customerId, dateTime, status, "" };
                model.addRow(newRow);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error reading : " + e.getMessage(),
                "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //This function use to delete the data i delete form table on the appoint txt file
    private void removeAppointmentFromFile(String appointmentId) {
        String filePath = "D:\\USER BACKUP\\Documents\\NetBeansProjects\\apu-medical-centre\\src\\txt\\appointment.txt";
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(appointmentId + ",")) { 
                    lines.add(line); // keep the data of the line is not delete
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (java.io.FileWriter fw = new java.io.FileWriter(filePath)) {
            for (String l : lines) {
                fw.write(l + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
