/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Staff;

import Staff.AppointmentsManagement;
import Staff.FinanceReport;
import Staff.CustomerManagement;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
/**
 *
 * @author SEAH SW
 */
public class GenerateReceipts extends JDialog{
    private JTextField AppointmentID;
    private JComboBox<String> Appointmentid;
    Color defaultColor = Color.WHITE;  
    Color hoverColor = Color.LIGHT_GRAY;

        public GenerateReceipts() {
        setTitle("Generate Receipts");
        setSize(400, 200);
        setModal(true);  
        setLayout(null); 
        getContentPane().setBackground(Color.WHITE);

        //AppointmentID and comboBox 
        JLabel appointment = new JLabel("Appointment ID:");
        appointment.setFont(appointment.getFont().deriveFont(20f));
        appointment.setBounds(30, 15,160, 50);
        add(appointment);
        
        //Appointment comboBox (Show the appointment id that is doesn't have receipts)
        String[] appointmentid = {};
        Appointmentid = new JComboBox<>(appointmentid);
        Appointmentid.setBounds(200, 20,70, 40); 
        Appointmentid.setBackground(Color.WHITE);
        loadAppointments();
        add(Appointmentid);

        //Create Button
        JButton createBtn = new JButton("Generate");
        createBtn.setBounds(80, 100, 100, 40);
        createBtn.setFont(createBtn.getFont().deriveFont(20f));
        createBtn.addActionListener(e -> {
                savereceipts();
                dispose(); 
        });
        add(createBtn);
          
        //Cancel Button 
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(200, 100, 100, 40);
        cancelBtn.setFont(cancelBtn.getFont().deriveFont(20f));
        cancelBtn.addActionListener(e -> dispose());
        add(cancelBtn);
        
        
        //Customize Button
        for (JButton btn : new JButton[]{createBtn, cancelBtn}) {
            btn.setBackground(Color.WHITE);    
            btn.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
            btn.setOpaque(true);                
            btn.setFocusPainted(false); 
            applyHoverEffect(btn, defaultColor, hoverColor);
        }
    }
        
        //Settings Button Hover Effect
        public static void applyHoverEffect(JButton btn, Color defaultColor, Color hoverColor) {
        btn.setBackground(defaultColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(defaultColor);
            }
        });
}
        
    //Function about save receipts into txt file
    private void savereceipts() {
        String receiptid = generateReceiptsID();
        String appointmentid = (String) Appointmentid.getSelectedItem();
        String paymentid = null;

        // read appointment.txt to find the payment id
        try (BufferedReader br = new BufferedReader(
                new FileReader("D:\\USER BACKUP\\Documents\\NetBeansProjects\\JavaAssignment\\apu-medical-centre\\src\\txt\\appointment.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String currentAppointmentId = parts[0].trim(); 
                    if (currentAppointmentId.equals(appointmentid)) {
                        paymentid = parts[5].trim(); 
                        break;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage());
            return;
        }

        // if doesn't find the payment id, send error
        if (paymentid == null) {
            JOptionPane.showMessageDialog(this, "Payment ID not found" + appointmentid);
            return;
        }

        // save in the receipt.txt
        try (BufferedWriter writer = new BufferedWriter(
            new FileWriter("D:\\USER BACKUP\\Documents\\NetBeansProjects\\JavaAssignment\\apu-medical-centre\\src\\txt\\receipt.txt", true))) {
            writer.write(receiptid + "," + appointmentid + "," + paymentid);
            writer.newLine();
            writer.flush();
            JOptionPane.showMessageDialog(this, "Receipt: " + receiptid + " generated successfully!");
            
            loadAppointments();
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving file:" + ex.getMessage());
        }
    }
    
    //Generate a specific id for each receipts
    private String generateReceiptsID() {
    File file = new File("D:\\USER BACKUP\\Documents\\NetBeansProjects\\JavaAssignment\\apu-medical-centre\\src\\txt\\receipt.txt");
    int maxreceiptId = 0;

    if (file.exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].startsWith("R")) {
                    try {
                        int num = Integer.parseInt(parts[0].substring(1));
                        if (num > maxreceiptId) {
                            maxreceiptId = num;
                        }
                    } catch (NumberFormatException ignored) {
                        
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error:" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    return "R" + (maxreceiptId + 1);
}
    private void loadAppointments() {
     Set<String> completedAppointments = new HashSet<>();
     Set<String> appointmentsWithReceipt = new HashSet<>();
     List<String> validAppointments = new ArrayList<>();

     // read appointment.txt find the appoint status is complete (use to generate receipts)
     try (BufferedReader br = new BufferedReader(new FileReader("D:\\USER BACKUP\\Documents\\NetBeansProjects\\JavaAssignment\\apu-medical-centre\\src\\txt\\appointment.txt"))) {
         String line;
         while ((line = br.readLine()) != null) {
             String[] parts = line.split(",");
             if (parts.length >= 5) {
                 String appointmentId = parts[0].trim();
                 String status = parts[4].trim();     
                 if (status.equalsIgnoreCase("complete")) {
                     completedAppointments.add(appointmentId);
                 }
             }
         }
     } catch (IOException e) {
         e.printStackTrace();
     }

     // read receipt.txt find the appointment that already have receipts
     try (BufferedReader br = new BufferedReader(new FileReader("D:\\USER BACKUP\\Documents\\NetBeansProjects\\JavaAssignment\\apu-medical-centre\\src\\txt\\receipt.txt"))) {
         String line;
         while ((line = br.readLine()) != null) {
             String[] parts = line.split(",");
             if (parts.length >= 2) {
                 String appointmentId = parts[1].trim();
                 appointmentsWithReceipt.add(appointmentId);
             }
         }
     } catch (IOException e) {
         e.printStackTrace();
     }

     // fill out all the appointment that have receipts
     for (String apptId : completedAppointments) {
         if (!appointmentsWithReceipt.contains(apptId)) {
             validAppointments.add(apptId);
         }
     }

     // fill in the comboBox
        Appointmentid.removeAllItems();
        if (validAppointments.isEmpty()) {
            Appointmentid.addItem("No available customer");
        } else {
            for (String c : validAppointments) {
                Appointmentid.addItem(c);
            }
        }
     }
 }


