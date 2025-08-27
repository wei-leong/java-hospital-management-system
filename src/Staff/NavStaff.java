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
    
    private final AppointmentsManagement Appointment = new AppointmentsManagement();
    private final String AppointmentStr = "Create Appointment";
    
    private final FinanceReport report = new FinanceReport();
    private final String reportStr = "Finance Report";
    
    private final Icon _iconDashboard;
    private final Icon _iconStaffManagement;
    private final Icon _iconFeedback;
    private final Icon _iconAppointment;

    public NavStaff(String[] staffDetails){
        super("APU Medical Centre", staffDetails);
        
        // Define Page Icon
        this._iconDashboard = imgScale.returnScaledImageIcon("/image/dashboard.png", 25, 25);
        this._iconStaffManagement = imgScale.returnScaledImageIcon("/image/staff_management.png", 25, 25);
        this._iconAppointment = imgScale.returnScaledImageIcon("/image/appointment.png", 25, 25);
        this._iconFeedback = imgScale.returnScaledImageIcon("/image/view_feedback.png", 25, 25);
        
        // Initialize pages that needs staffDetails
        this.customerManagement = new CustomerManagement(staffDetails);
        
        // Add Page
        addPage(dashboardStr, dashboard, _iconDashboard);
        addPage(customerManagementStr, customerManagement, _iconStaffManagement);
        addPage(AppointmentStr, Appointment, _iconAppointment);
        addPage(reportStr, report, _iconFeedback);
      
        titleChanger(dashboardStr);
        cards.show(content,dashboardStr);
        
        setVisible(true);
    }
}


//private JPanel buildSidebar() {
//        // Styling Options
//        int iconSize = 25;
//        
//        JPanel bar = new JPanel();
//        bar.setPreferredSize(new Dimension(200, getHeight()));
//        bar.setBackground(Color.BLACK);
//        bar.setLayout(new BoxLayout(bar, BoxLayout.Y_AXIS));
//
//        Icon iconDashboard = loadIcon("/image/dashboard.png", iconSize);
//        Icon iconCustomerManagement = loadIcon("/image/staff_management.png", iconSize);
//        Icon iconAppointments = loadIcon("/image/Appoinments.png", iconSize);
//        Icon iconFinanceReport = loadIcon("/image/FinanceReport.png", iconSize);
//
//        // Menu buttons
//        bar.add(makeSidebarButton("Dashboard", iconDashboard, e -> {
//            cards.show(content, "Dashboard");
//            titleChanger("Dashboard");
//        }));
//        bar.add(makeSidebarButton("Customer Management", iconCustomerManagement, e -> {
//            cards.show(content, "Customer Management");
//            titleChanger("Customer Management");
//        }));
//        bar.add(makeSidebarButton("Appointments", iconAppointments, e -> {
//            cards.show(content, "Appointments");
//            titleChanger("Appointments Management");
//        }));
//        bar.add(makeSidebarButton("Finance Report", iconFinanceReport, e -> {
//            cards.show(content, "Finance Report");
//            titleChanger("Receipts Generate and Payment Collection");
//        }));
//
//        JPanel bottom = new JPanel(new BorderLayout(10, 10));
//        JButton btnLogout = new JButton("Logout");
//
//        btnLogout.addActionListener(e -> {
//            SwingUtilities.getWindowAncestor(bar).dispose();
//            // 2. Open the login form:
//            login.LoginForm login = new login.LoginForm();
//            login.setVisible(true);
//        });
//
//        bottom.setBackground(Color.BLACK);
//        bottom.add(btnLogout, BorderLayout.SOUTH);
//        bottom.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
//        bar.add(bottom, BorderLayout.SOUTH);
//        btnLogout.setBackground(Color.WHITE);
//        btnLogout.setForeground(Color.BLACK);
//        btnLogout.setPreferredSize(new Dimension(150, 35));
//        btnLogout.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//        btnLogout.setOpaque(true);
//        btnLogout.setContentAreaFilled(true);
//        btnLogout.setFocusPainted(false);
//
//        return bar;
//    }