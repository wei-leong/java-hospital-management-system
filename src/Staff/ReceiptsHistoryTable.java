/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Staff;

import Class.Showreceipts;
import Class.Staff;
import Class.TableStyle;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author SEAH SW
 */
public class ReceiptsHistoryTable extends JPanel{
  
    private List<String[]> AppointmentData = List.of();
    private DefaultTableModel model;
    private JTable table;
    Staff appointmenttable = new Staff();
    private JButton AddCustomerbtn;
            
    public ReceiptsHistoryTable() {
    setLayout(new BorderLayout());
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filters.setBackground(Color.WHITE);
        filters.setBorder(BorderFactory.createEmptyBorder());
       
        
        // 1) Column headers
        String[] cols = {"Receipt ID","Appointments ID","Payment","Receipt"};
        JPanel headerBar = new JPanel(new GridLayout(1, cols.length, 12, 0));
        headerBar.setBackground(Color.WHITE);
        headerBar.setBorder(BorderFactory.createEmptyBorder());  

        for (String c : cols) {
            JLabel lbl = new JLabel(c, SwingConstants.LEFT);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
            lbl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // minimal padding
            headerBar.add(lbl);
        }
        
        //Panel for put in the table for read
        JPanel northWrapper = new JPanel();
        northWrapper.setLayout(new BoxLayout(northWrapper, BoxLayout.Y_AXIS));
        northWrapper.setBackground(Color.WHITE);
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);

        northWrapper.add(topBar);
        northWrapper.add(headerBar);
        add(northWrapper, BorderLayout.NORTH);
        
        //Tabel for showing the receipts data
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

            //Just can cleck the last column to use the showreceipts function
            if (row >= 0 && col == table.getColumnCount() - 1) {
            String receiptId = table.getValueAt(row, 0).toString();
            
            //show the JFrame window to let user see the receipts
            Window parentWindow = SwingUtilities.getWindowAncestor(ReceiptsHistoryTable.this);
            Showreceipts receiptWindow = new Showreceipts(receiptId);
            receiptWindow.setModal(true);   
            receiptWindow.setLocationRelativeTo(parentWindow);
            receiptWindow.setVisible(true);
            }
        }
    });
        
        //put the image icon on the column
        table.getColumnModel().getColumn(model.getColumnCount() - 1).setCellRenderer(new DefaultTableCellRenderer() {
        ImageIcon rawIcon = new ImageIcon(getClass().getResource("/image/receipts.png"));
        Image img = rawIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Icon editIcon = new ImageIcon(img); 
        
        //add the icon function to the table
        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            JLabel lbl = new JLabel(editIcon);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            return lbl;
        }
    });
        
        //add table data border
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

        refreshreceiptsTable();
    }
    
    //Use to refresh the receipts showing table
    public void refreshreceiptsTable() {
        Path paymentPath = Paths.get("src", "txt", "payment.txt");
        Path receiptPath = Paths.get("src", "txt", "receipt.txt");
        if (model == null) return;
        model.setRowCount(0);  // clear old data

        // read payment.txt top get amount
        Map<String, String> paymentMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(paymentPath.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String paymentId = parts[0].trim();    
                    String amount = parts[1].trim();  
                    paymentMap.put(paymentId, amount);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error reading file:" + e.getMessage(),
                "File Error", JOptionPane.ERROR_MESSAGE);
        }

        // read receipt.txt to get receipt id, appointment id, payment id
        try (BufferedReader br = new BufferedReader(new FileReader(receiptPath.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] row = line.split(",");

                if (row.length >= 3) {
                    String receiptId = row[0].trim();
                    String appointmentId = row[1].trim();
                    String paymentId = row[2].trim();

                    // use payment id in the map to check the amout of payment 
                    String totalPayment = paymentMap.getOrDefault(paymentId, "N/A");

                    Object[] newRow = {
                        receiptId,
                        appointmentId,
                        totalPayment,  
                        ""
                    };
                    model.addRow(newRow);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error reading file:" + e.getMessage(),
                "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

