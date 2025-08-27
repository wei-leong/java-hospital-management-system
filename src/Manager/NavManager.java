/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Wlhoe
 */
package Manager;

import Class.BasicNav;
import Class.ImageScaler;
import javax.swing.Icon;


public class NavManager extends BasicNav {
    
    private final ImageScaler imgScale = new ImageScaler();
    
    // Pages (keep as fields so subclass can interact with them)
    private final Dashboard dashboard = new Dashboard();
    private final String dashboardStr = "Dashboard";

    private final StaffManagement staffManagement;
    private final String staffManagementStr = "Staff Management";

    private final Feedback feedback = new Feedback();
    private final String feedbackStr = "View Feedback";

    private final ViewAppointment viewAppointment = new ViewAppointment();
    private final String viewAppointmentStr = "View Appointment";
    
    private final Icon _iconDashboard;
    private final Icon _iconStaffManagement;
    private final Icon _iconFeedback;
    private final Icon _iconAppointment;

    public NavManager(String[] staffDetails){
        super("APU Medical Centre", staffDetails);
        
        // Define Page Icon
        this._iconDashboard = imgScale.returnScaledImageIcon("/image/dashboard.png", 25, 25);
        this._iconStaffManagement = imgScale.returnScaledImageIcon("/image/staff_management.png", 25, 25);
        this._iconFeedback = imgScale.returnScaledImageIcon("/image/view_feedback.png", 25, 25);
        this._iconAppointment = imgScale.returnScaledImageIcon("/image/appointment.png", 25, 25);
        
        // Initialize pages that needs staffDetails
        this.staffManagement = new StaffManagement(staffDetails);
        
        // Add Page
        addPage(dashboardStr, dashboard, _iconDashboard);
        addPage(staffManagementStr, staffManagement, _iconStaffManagement);
        addPage(feedbackStr, feedback, _iconFeedback);
        addPage(viewAppointmentStr, viewAppointment, _iconAppointment);
        
        titleChanger(dashboardStr);
        cards.show(content,dashboardStr);
        
        setVisible(true);
    }
}
