/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

/**
 *
 * @author SEAH SW
 */
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableStyle {
    
    //use to set the table style
    public static void applyStyle(JTable table) {
    
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 2));

        DefaultTableCellRenderer cellRend = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, false, row, column);
                lbl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); 
                return lbl;
            }
        };
        table.setDefaultRenderer(Object.class, cellRend);

        if (table.getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setPreferredWidth(40); 
            table.getColumnModel().getColumn(table.getColumnCount()-1).setPreferredWidth(40); 
        }
    }
}
