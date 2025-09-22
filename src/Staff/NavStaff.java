package Staff;

import Class.BasicNav;
import Class.ImageScaler;
import Staff.AppointmentsManagement;
import Staff.CustomerManagement;
import Staff.FinanceReport;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class NavStaff extends BasicNav{
    
    private final ImageScaler imgScale = new ImageScaler();
    
    // Pages (keep as fields so subclass can interact with them)
    private final Dashboard dashboard = new Dashboard();
    private final String dashboardStr = "Dashboard";

    private final CustomerManagement customerManagement;
    private final String customerManagementStr = "Customer Management";
    
    private final AppointmentsManagement Appointment;
    private final String AppointmentStr = "Appointment Management";
    
    private final FinanceReport report = new FinanceReport();
    private final String reportStr = "Finance Report";
    
    private final Icon _iconDashboard;
    private final Icon _iconStaffManagement;
    private final Icon _iconfinancereport;
    private final Icon _iconAppointment;

    public NavStaff(String[] staffDetails){
        super("APU Medical Centre", staffDetails);

        // Define Page Icon
        this._iconDashboard = imgScale.returnScaledImageIcon("/image/dashboard.png", 25, 25);
        this._iconStaffManagement = imgScale.returnScaledImageIcon("/image/staff_management.png", 25, 25);
        this._iconAppointment = imgScale.returnScaledImageIcon("/image/appointment.png", 25, 25);
        this._iconfinancereport = imgScale.returnScaledImageIcon("/image/FinanceReport.png", 25, 25);
        
        // Initialize pages that needs staffDetails
        this.customerManagement = new CustomerManagement(staffDetails);
        this.Appointment = new AppointmentsManagement(staffDetails);
        
        // Add Page
        addPage(dashboardStr, dashboard, _iconDashboard);
        addPage(customerManagementStr, customerManagement, _iconStaffManagement);
        addPage(AppointmentStr, Appointment, _iconAppointment);
        addPage(reportStr, report, _iconfinancereport);

        setVisible(true);
    }
}
