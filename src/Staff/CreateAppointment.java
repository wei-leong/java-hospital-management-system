package Staff;

import Class.Appointment;
import Class.DocterAction;
import static Staff.AddCustomer.applyHoverEffect;
import Staff.AppointmentsManagement;
import Staff.FinanceReport;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import com.toedter.calendar.JDateChooser;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.HashSet;

public class CreateAppointment extends JDialog {
    private JComboBox<String> CustomerName;
    private JComboBox<String> DocterName;
    private JComboBox<String> Time;
    private JDateChooser dateChooser;
    private boolean saved = false;
    private AppointmentsManagement refresh;
    Color defaultColor = Color.WHITE;  
    Color hoverColor = Color.LIGHT_GRAY;
    Date currentDate = new Date();
    private String[] staffDetails;
    
    
        public CreateAppointment(AppointmentsManagement refresh, String[] staffDetails) {  
        this.refresh = refresh;
        this.staffDetails = staffDetails;
        setTitle("Create Appointment");
        setSize(500, 350);
        setModal(true);  
        setLayout(null); 
        getContentPane().setBackground(Color.WHITE);
      
        //Customer Name
        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setFont(nameLabel.getFont().deriveFont(20f));
        nameLabel.setBounds(50, 25,200, 50);
        add(nameLabel);
        
        //CustomerName comboBox
        String[] customername = {};
        CustomerName = new JComboBox<>(customername);
        CustomerName.setBounds(230, 35,200, 40); 
        CustomerName.setBackground(Color.WHITE);
        loadCustomers();
        add(CustomerName);

        //Docter Name 
        JLabel phoneLabel = new JLabel("Docter Name:");
        phoneLabel.setFont(nameLabel.getFont().deriveFont(20f));
        phoneLabel.setBounds(50, 75, 200, 50);
        add(phoneLabel);
        
        //Docter ComboBox
        DocterName = new JComboBox<>();
        DocterName.setBounds(230, 85, 200, 40);
        DocterName.setBackground(Color.WHITE);
        add(DocterName);
        

        //Appointment Start Time
        JLabel DnTLabel = new JLabel("Date:");
        DnTLabel.setFont(nameLabel.getFont().deriveFont(20f));
        DnTLabel.setBounds(50, 125,100, 50);
        add(DnTLabel);
        
        // JDateChooser
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd"); 
        dateChooser.setBounds(105, 135, 90, 40);
        add(dateChooser);             
        
        // Time comboBox let user choose the time
        String[] time = {"10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00"};
        Time = new JComboBox<>(time);
        Time.setBounds(210, 135,75, 40); 
        Time.setBackground(Color.WHITE);
        add(Time);  
        
        // This is the call out function from other class, use to check what is the availble docter in that time can receive the appointment
        ActionListener refreshDoctorsListener = e -> refreshDoctorCombo();
        dateChooser.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                refreshDoctorCombo();
            }
        });
        Time.addActionListener(refreshDoctorsListener);
        
       
        //Create Button
        JButton createBtn = new JButton("Create");
        createBtn.setBounds(110, 210, 120, 60);
        createBtn.setFont(createBtn.getFont().deriveFont(20f));
        createBtn.addActionListener(e ->{
            
                String selectedDoctorId = (String) DocterName.getSelectedItem();
                Date selectedDate = dateChooser.getDate();
                String selectedTime = (String) Time.getSelectedItem();
                
                try {
                    Appointment appointment = new Appointment();
                    appointment.setDoctorId((String) DocterName.getSelectedItem());
                    appointment.setDate(dateChooser.getDate());
                    appointment.setTime((String) Time.getSelectedItem());
                    
                    //verify invalid data like doctor id can't be empty
                    if (!validateAppointment(appointment)) {
                    return; 
                 }
                    
                    //Before saving,check the docter id, is it on the same date and time have a appointment or not         
                    if (DocterAction.hasOngoingAppointment(selectedDoctorId, selectedDate, selectedTime)) {
                        JOptionPane.showMessageDialog(this,
                                "This doctor already has an appointment at This time. Please choose another doctor or Date&Time.",
                                "Appointment Conflict",
                                JOptionPane.WARNING_MESSAGE);
                        return; 
                    }
  
                    saveappointment();
                    refresh.refreshAppointmentTable();
                    loadCustomers();
                    dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            });
        add(createBtn);
      
        
        
        //Cancel Button 
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(250, 210, 120, 60);
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
        refreshDoctorCombo();
    }
        public boolean isSaved() {
            return saved;
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
        
    //Function about save New Appointment into txt file
    private void saveappointment() {
    Path appointmentPath = Paths.get("src", "txt", "appointment.txt");
    Path paymentPath = Paths.get("src", "txt", "payment.txt");
    String Appointmentid = generateAppointmentID();
    String customerid = (String) CustomerName.getSelectedItem();
    String docterid = (String) DocterName.getSelectedItem();
    String staffid = staffDetails[0];
    String Astatus = "ongoing";
    String Pstatus = "pending";
    String paymentid = generaPaymentID();
    String commentid = null;
    Integer amount = 0;
    
    //check the date of the user choose
    java.util.Date selectedDate = dateChooser.getDate();
    String dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
    String time = (String) Time.getSelectedItem();
    
    //while saving convern the date and time into one column to save into txt file
    String datetime = dateStr + " " + time;
    
    try (BufferedWriter writer = new BufferedWriter(
        new FileWriter(appointmentPath.toFile(),true))) {
        writer.write(Appointmentid + "," + docterid + "," + staffid + "," + customerid + "," + datetime + "," + Astatus + "," + paymentid + "," + commentid);
        writer.newLine();
        writer.flush();
        JOptionPane.showMessageDialog(this, "Appointment "+ Appointmentid +" Create successfully!");
    } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
    }
    
    try (BufferedWriter writer = new BufferedWriter(
        new FileWriter(paymentPath.toFile(),true))) {
        writer.write(paymentid + "," + amount + "," + dateStr + "," + Pstatus);
        writer.newLine();
        writer.flush();
    } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
    }
}
        
        //The function use to check empty and combine date&time for create appointment
        private boolean validateAppointment(Appointment appointment) {
        // Check doctor ID
        if (appointment.getDoctorId() == null || appointment.getDoctorId().equals("No doctor available")) {
            JOptionPane.showMessageDialog(this,
                "Doctor ID can't be empty",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check date
        if (appointment.getDate() == null) {
            JOptionPane.showMessageDialog(this,
                "Date can't be empty",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // check time
        if (appointment.getTime() == null) {
            JOptionPane.showMessageDialog(this,
                "Time can't be empty",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check the choose time is not early than current time and get current time
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.LocalDateTime selectedDateTime = appointment.getCombinedDateTime();

        if (selectedDateTime.isBefore(now)) {
            JOptionPane.showMessageDialog(this,
                "Invalid Time, can't choose earlier than current time",
                "Invalid Date & Time",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true; 
    }
    
    //Use to generate a unique id for each appointment
    private String generateAppointmentID() {
        Path appointmentPath = Paths.get("src", "txt", "appointment.txt");
    File file = appointmentPath.toFile();
    int maxAppointmentId = 0;

    if (file.exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].startsWith("A")) {
                    try {
                        int num = Integer.parseInt(parts[0].substring(1));
                        if (num > maxAppointmentId) {
                            maxAppointmentId = num;
                        }
                    } catch (NumberFormatException ignored) {
                        
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error:" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    return "A" + (maxAppointmentId + 1);
}
        //Use to fill in the docter is suitable the time to receive the appointment
        private void refreshDoctorCombo() {
        Date selectedDate = dateChooser.getDate();
        String selectedTime = (String) Time.getSelectedItem();

        if (selectedDate == null || selectedTime == null) {
            return;
        }

        // clear the docter list
        DocterName.removeAllItems();

        // Use the DocterFitterManagement class to do the fitter docter function
        List<String> availableDoctors = DocterAction.getAvailableDoctors(selectedDate, selectedTime);
        
        //if no docter availble in that time show "No Available Docter"
        if (availableDoctors.isEmpty()) {
            DocterName.addItem("No doctor available");
        } else {
            for (String doctorId : availableDoctors) {
                DocterName.addItem(doctorId);
            }
        }
    }
        
        //Use to load customer name that is suitable (No appointment, because one customer just have on appointment)
        private void loadCustomers() {
        Path profilePath = Paths.get("src", "txt", "profile.txt");
        Path appointmentPath = Paths.get("src", "txt", "appointment.txt");
        Set<String> customersWithOngoing = new HashSet<>();
        List<String> validCustomers = new ArrayList<>();

        // read appointment.txt for checking who of the customer have an appointment status is "ongoing" (that is not suitable in the comboBox)
        try (BufferedReader br = new BufferedReader(new FileReader(appointmentPath.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    String customerId = parts[3].trim();  
                    String status = parts[5].trim();    
                    if (status.equalsIgnoreCase("ongoing")) {
                        customersWithOngoing.add(customerId);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read profile.txt to find all the customer, staff id is start from C
        try (BufferedReader br = new BufferedReader(new FileReader(profilePath.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String id = parts[0].trim();
                    String role = parts[1].trim();

                    // just add the customer that is no apoointment 
                    if (role.equalsIgnoreCase("Customer") && !customersWithOngoing.contains(id)) {
                        validCustomers.add(id);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // fill the customer name into the copmboBox
        CustomerName.removeAllItems();
        if (validCustomers.isEmpty()) {
            CustomerName.addItem("No available customer");
        } else {
            for (String c : validCustomers) {
                CustomerName.addItem(c);
            }
        }
    }
        
    //Generate Payment ID
        private String generaPaymentID() {
        Path paymentPath = Paths.get("src", "txt", "payment.txt");
        File file = paymentPath.toFile();
        int maxPaymentId = 0;

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0 && parts[0].startsWith("P")) {
                        try {
                            int num = Integer.parseInt(parts[0].substring(1));
                            if (num > maxPaymentId) {
                                maxPaymentId = num;
                            }
                        } catch (NumberFormatException ignored) {

                        }
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error:" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return "P" + (maxPaymentId + 1);
        }
}
