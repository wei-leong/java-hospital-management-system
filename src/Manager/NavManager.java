/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Wlhoe
 */
package Manager;

import Class.ImageScaler;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NavManager extends JFrame {

    // Own Staff Details - Remove own profile from StaffManagement page
    private final String[] _staffDetails;
    private final ImageScaler imgScale = new ImageScaler();

    private final JPanel sidebar;
    private final CardLayout cards = new CardLayout();
    private final JPanel content;
    private String currentPage = "Dashboard";
    private final JLabel lblTitle;
    private final JButton btnToggle;
    private final JButton btnLogout;
    private final JButton btnEdit;
    private final JButton btnProfile;

    // Dashboard Page
    private final Dashboard dashboard = new Dashboard();
    private final String dashboardStr = "Dashboard";

    // Staff Management Page
    private final StaffManagement staffManagement;
    private final String staffManagementStr = "Staff Management";

    // Feedback Page
    private final Feedback feedback = new Feedback();
    private final String feedbackStr = "View Feedback";

    // Appointment Page
    private final ViewAppointment viewAppointment = new ViewAppointment();
    private final String viewAppointmentStr = "View Appointment";

    // Image
    ImageIcon toggleIcon = imgScale.returnScaledImageIcon("/image/nav-menu.png", 24, 24);
    Image windowIcon = imgScale.returnScaledImage("/image/APU_Med_Cen_Assignment.png", 128, 128);
    Icon iconDashboard = imgScale.returnScaledImageIcon("/image/dashboard.png", 25, 25);
    Icon iconStaffManagement = imgScale.returnScaledImageIcon("/image/staff_management.png", 25, 25);
    Icon iconFeedback = imgScale.returnScaledImageIcon("/image/view_feedback.png", 25, 25);
    Icon iconAppointment = imgScale.returnScaledImageIcon("/image/appointment.png", 25, 25);
    Icon iconProfile = imgScale.returnScaledImageIcon("/image/profile-user.png", 32, 32);
    Icon iconProfileLarge = imgScale.returnScaledImageIcon("/image/profile-user.png", 50, 50);

    public NavManager(String[] staffDetails) {
        // Window Title
        super("APU Medical Centre");
        this._staffDetails = staffDetails;
        this.staffManagement = new StaffManagement(_staffDetails);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Dispose Navigation Menu
        addWindowListener(new WindowAdapter() { // Reopen Login Form
            @Override
            public void windowClosed(WindowEvent e) {
                SwingUtilities.invokeLater(() -> new login.LoginForm().setVisible(true));
            }
        });

        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize Button 
        this.btnToggle = new JButton(toggleIcon);
        this.btnLogout = new JButton("Logout");
        this.btnEdit = new JButton("Edit Profile");
        this.btnProfile = new JButton(iconProfile);

        // Windows Title Icon 
        setIconImage(windowIcon);

        lblTitle = new JLabel(currentPage);
        add(toolBar(), BorderLayout.NORTH); // Toolbar section

        content = new JPanel(cards);
        sidebar = buildSidebar();
        add(sidebar, BorderLayout.WEST); // Sidebar
        add(contentSection(), BorderLayout.CENTER);
        
        setVisible(true); // Ensure the NavManager page is visible
    }

    private JToolBar toolBar() {
        // Title Bar 
        JToolBar titleBar = new JToolBar();
        titleBar.setFloatable(false);

        // Hamburger Button Toggle
        titleBar.add(btnToggle);
        titleBar.addSeparator();
        btnToggleSettings();

        // Title - APU Medical Centre
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 16f));
        titleBar.add(lblTitle);
        titleBar.add(Box.createHorizontalGlue());

        // Button Profile to Open Staff Details
        titleBar.add(btnProfile);
        btnProfileSettings();

        return titleBar;
    }

    private JPanel contentSection() {
        // Content Area with CardLayout 
        content.add(dashboard, dashboardStr);
        content.add(staffManagement, staffManagementStr);
        content.add(feedback, feedbackStr);
        content.add(viewAppointment, viewAppointmentStr);
        
        return content;
    }

    // Changes the title besides the Navigation Menu Icon
    private void titleChanger(String newTitle) {
        currentPage = newTitle;
        lblTitle.setText(newTitle);
    }

    private JPanel buildSidebar() {
        JPanel bar = new JPanel();
        bar.setPreferredSize(new Dimension(200, getHeight()));
        bar.setBackground(Color.BLACK);
        bar.setLayout(new BoxLayout(bar, BoxLayout.Y_AXIS));

        // Menu buttons
        bar.add(makeSidebarButton(dashboardStr, iconDashboard, e -> {
            cards.show(content, dashboardStr);
            titleChanger(dashboardStr);
        }));
        bar.add(makeSidebarButton(staffManagementStr, iconStaffManagement, e -> {
            cards.show(content, staffManagementStr);
            titleChanger(staffManagementStr);
        }));
        bar.add(makeSidebarButton(feedbackStr, iconFeedback, e -> {
            cards.show(content, feedbackStr);
            titleChanger(feedbackStr);
        }));
        bar.add(makeSidebarButton(viewAppointmentStr, iconAppointment, e -> {
            cards.show(content, viewAppointmentStr);
            titleChanger(viewAppointmentStr);
        }));

        JPanel bottom = new JPanel(new BorderLayout(10, 10));

        bottom.setBackground(Color.BLACK);
        bottom.add(btnLogout, BorderLayout.SOUTH);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        bar.add(bottom, BorderLayout.SOUTH);
        btnLogoutSettings(bar);

        return bar;
    }

    private JButton makeSidebarButton(String text, Icon icon, ActionListener act) {
        JButton b = new JButton(text, icon);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setForeground(Color.WHITE); // Set Color Text
        b.setBackground(Color.DARK_GRAY); // Set Button backgroun color
        b.setBorderPainted(false); // Remove border painted sty;e
        b.setFocusPainted(false); // Remove focus painted style
        b.setHorizontalAlignment(SwingConstants.LEFT);        // Set Icon to the Left
        b.setHorizontalTextPosition(SwingConstants.RIGHT);   // Place Text to the Right
        b.setIconTextGap(8); // Set the gap between Icon and Text
        b.addActionListener(act);
        return b;
    }

    private void showProfileDialog() {
        // Create dialog to show Staff Details
        JDialog dlg = new JDialog(this, "Profile", true);
        dlg.setLayout(new BorderLayout(10, 10));
        dlg.setResizable(false);

        // Header (Profile Picture + Staff Id and Staff Role)
        JLabel picLabel = new JLabel(iconProfileLarge);
        picLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        // Create JPanel to group profile picture, staff id and staff role together
        JPanel idRole = new JPanel(new GridLayout(0, 1, 5, 5));
        idRole.add(new JLabel(_staffDetails[0])); // Staff ID
        idRole.add(new JLabel(_staffDetails[1])); // Staff Role

        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.add(picLabel, BorderLayout.WEST);
        header.add(idRole, BorderLayout.CENTER);
        dlg.add(header, BorderLayout.NORTH);

        // Details (in the CENTER so it expands naturally)
        JPanel details = new JPanel(new GridLayout(0, 1, 5, 5));
        details.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        details.add(new JLabel(_staffDetails[2])); // Staff Name
        details.add(new JLabel(_staffDetails[5])); // Staff Email
        details.add(new JLabel(_staffDetails[6])); // Staff Phone Number
        details.add(new JLabel(_staffDetails[7])); // Staff Age
        dlg.add(details, BorderLayout.CENTER);

        // Edit Profile Button 
        JPanel bottom = new JPanel(new BorderLayout(10, 10));

        bottom.add(btnEdit, BorderLayout.CENTER);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        dlg.add(bottom, BorderLayout.SOUTH);
        btnEditSettings();

        // Finalize size & position
        dlg.setSize(300, 250);  // fixed size you prefer
        Point loc = this.getLocationOnScreen();
        dlg.setLocation(loc.x + this.getWidth() - dlg.getWidth() - 10, loc.y + 10);
        dlg.setVisible(true);
    }

    private void btnToggleSettings() {
        btnToggle.setFocusable(false);
        btnToggle.setBorderPainted(false);
        btnToggle.setContentAreaFilled(false);
        btnToggle.setFocusPainted(false);
        btnToggle.setOpaque(false);
        btnToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Toggle Sidebar Visibility
        btnToggle.addActionListener(e
                -> sidebar.setVisible(!sidebar.isVisible())
        );
    }

    private void btnLogoutSettings(JPanel bar) {
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setPreferredSize(new Dimension(150, 35));
        btnLogout.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnLogout.setOpaque(true);
        btnLogout.setContentAreaFilled(true);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> {
            // Dispose NavManager JFrame
            SwingUtilities.getWindowAncestor(bar).dispose();
            // Open Login Form
            login.LoginForm login = new login.LoginForm();
            login.setVisible(true);
        });
    }

    private void btnEditSettings() {
        btnEdit.setBackground(Color.white);
        btnEdit.setForeground(Color.BLACK);
        btnEdit.setPreferredSize(new Dimension(150, 35));
        btnEdit.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnEdit.setOpaque(true);
        btnEdit.setContentAreaFilled(true);
        btnEdit.setFocusPainted(false);
        btnEdit.addActionListener(e -> {
            // Edit Own Profile Logic Here
        });
    }

    private void btnProfileSettings() {
        btnProfile.setBorder(null);
        btnProfile.setContentAreaFilled(false);

        // Show Own Profile Details and Enable user to edit their own profile
        btnProfile.addActionListener(e -> showProfileDialog());
    }
}
