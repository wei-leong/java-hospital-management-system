package Customer;

import Class.BasicNav;
import Class.ImageScaler;
import javax.swing.Icon;
import javax.swing.JFrame;

public class NavCustomer extends BasicNav {

    private final ImageScaler imgScale = new ImageScaler();

    // Pages
    private final Dashboard dashboard;
    private final String dashboardStr = "Dashboard";

    private final CheckAppointments checkAppointments;
    private final String checkAppointmentsStr = "Check Appointments";

    private final ProvideComments provideComments;
    private final String provideCommentsStr = "Provide Comments";

    private final ViewHistory viewHistory;
    private final String viewHistoryStr = "View History";

    // Icons
    private final Icon _iconDashboard;
    private final Icon _iconAppointments;
    private final Icon _iconComments;
    private final Icon _iconHistory;

    public NavCustomer(String[] customerDetails, JFrame loginForm) {
        super("APU Medical Centre - Customer", customerDetails, loginForm);

        // Pages initialization (no customer ID needed)
        dashboard = new Dashboard();
        checkAppointments = new CheckAppointments(customerDetails[0]); // assuming [0] = customer ID
        provideComments = new ProvideComments(customerDetails[0]);
        viewHistory = new ViewHistory(customerDetails[0]);

        // Define icons
        _iconDashboard    = imgScale.returnScaledImageIcon("/image/dashboard.png", 25, 25);
        _iconAppointments = imgScale.returnScaledImageIcon("/image/appointment.png", 25, 25);
        _iconComments     = imgScale.returnScaledImageIcon("/image/view_feedback.png", 25, 25);
        _iconHistory      = imgScale.returnScaledImageIcon("/image/history.png", 25, 25);

        // Add pages
        addPage(dashboardStr, dashboard, _iconDashboard);
        addPage(checkAppointmentsStr, checkAppointments, _iconAppointments);
        addPage(provideCommentsStr, provideComments, _iconComments);
        addPage(viewHistoryStr, viewHistory, _iconHistory);

        // Default page
        titleChanger(dashboardStr);
        cards.show(content, dashboardStr);

        setVisible(true);
    }
}


