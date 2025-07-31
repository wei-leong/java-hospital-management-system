/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Wlhoe
 */
package Manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class NavManager extends JFrame {
    // Staff Details
    private final String[] _staffDetails;

    private final JPanel sidebar;
    private final CardLayout cards = new CardLayout();
    private final JPanel content;
    private String currentPage = "Dashboard";
    private final JLabel lblTitle;

    public NavManager(String[] staffDetails) {
        this._staffDetails = staffDetails;
        
        // Window Title
        super("APU Medical Centre");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Windows Title Icon 
        ImageIcon frameLogo = new ImageIcon(
                getClass().getResource("/image/APU_Med_Cen_Assignment.png")
        );
        Image scaledIcon = frameLogo.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
        setIconImage(scaledIcon);

        // Title Bar 
        JToolBar titleBar = new JToolBar();
        titleBar.setFloatable(false);

        // Image Icon for Hamburger Menu
        ImageIcon rawToggle = new ImageIcon(
                getClass().getResource("/image/nav-menu.png")
        );
        Image togImg = rawToggle.getImage()
                .getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon toggleIcon = new ImageIcon(togImg);

        // Hamburger Button Toggle
        JButton btnToggle = new JButton(toggleIcon);
        btnToggle.setFocusable(false);
        titleBar.add(btnToggle);
        titleBar.addSeparator();
        btnToggle.setBorderPainted(false);
        btnToggle.setContentAreaFilled(false);
        btnToggle.setFocusPainted(false);
        btnToggle.setOpaque(false);
        btnToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Title - APU Medical Centre
        lblTitle = new JLabel(currentPage);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 16f));
        titleBar.add(lblTitle);

        titleBar.add(Box.createHorizontalGlue());

        // Profile Picture
        JButton btnProfile = new JButton(new ImageIcon(new ImageIcon(
                getClass().getResource("/image/profile-user.png")
        ).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        btnProfile.setBorder(null);
        btnProfile.setContentAreaFilled(false);
        titleBar.add(btnProfile);

        add(titleBar, BorderLayout.NORTH);

        // Sidebar
        sidebar = buildSidebar();
        add(sidebar, BorderLayout.WEST);

        // Toggle Sidebar Visibility
        btnToggle.addActionListener(e
                -> sidebar.setVisible(!sidebar.isVisible())
        );

        // Profile dialog
        btnProfile.addActionListener(e -> showProfileDialog());

        // Content area with CardLayout 
        content = new JPanel(cards);
        content.add(new Dashboard(), "Dashboard");
        content.add(new StaffManagement(staffDetails), "Staff Management");
        content.add(new Feedback(), "Feedback");
        add(content, BorderLayout.CENTER);

        setVisible(true);
    }
    
    private void titleChanger(String newTitle){
        currentPage = newTitle;
        lblTitle.setText(newTitle);
    }

    private JPanel buildSidebar() {
        // Styling Options
        int iconSize = 25;
        
        JPanel bar = new JPanel();
        bar.setPreferredSize(new Dimension(200, getHeight()));
        bar.setBackground(Color.BLACK);
        bar.setLayout(new BoxLayout(bar, BoxLayout.Y_AXIS));

        Icon iconDashboard = loadIcon("/image/dashboard.png", iconSize);
        Icon iconStaffManagement = loadIcon("/image/staff_management.png", iconSize);
        Icon iconFeedback = loadIcon("/image/view_feedback.png", iconSize);

        // Menu buttons
        bar.add(makeSidebarButton("Dashboard", iconDashboard, e -> {
            cards.show(content, "Dashboard");
            titleChanger("Dasbboard");
        }));
        bar.add(makeSidebarButton("Staff Management", iconStaffManagement, e -> {
            cards.show(content, "Staff Management");
            titleChanger("Staff Management");
        }));
        bar.add(makeSidebarButton("View Feedback", iconFeedback, e -> {
            cards.show(content, "Feedback");
            titleChanger("View Feedback");
        }));

        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        JButton btnLogout = new JButton("Logout");

        btnLogout.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(bar).dispose();
            // 2. Open the login form:
            login.LoginForm login = new login.LoginForm();
            login.setVisible(true);
        });

        bottom.setBackground(Color.BLACK);
        bottom.add(btnLogout, BorderLayout.SOUTH);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        bar.add(bottom, BorderLayout.SOUTH);
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setPreferredSize(new Dimension(150, 35));
        btnLogout.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnLogout.setOpaque(true);
        btnLogout.setContentAreaFilled(true);
        btnLogout.setFocusPainted(false);

        return bar;
    }

    private Icon loadIcon(String path, int size) {
        ImageIcon raw = new ImageIcon(getClass().getResource(path));
        Image scaled = raw.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private JButton makeSidebarButton(String text, Icon icon, ActionListener act) {
        JButton b = new JButton(text, icon);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setForeground(Color.WHITE);
        b.setBackground(Color.DARK_GRAY);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);        // puts icon+text at left edge
        b.setHorizontalTextPosition(SwingConstants.RIGHT);   // text sits to the RIGHT of the icon
        b.setIconTextGap(8);
        b.addActionListener(act);
        return b;
    }

    private void showProfileDialog() {
        // Create dialog
        JDialog dlg = new JDialog(this, "Profile", true);
        dlg.setLayout(new BorderLayout(10, 10));
        dlg.setResizable(false);

        // Header (Profile Picture + ID/Role)
        ImageIcon raw = new ImageIcon(
                getClass().getResource("/image/profile-user.png")
        );
        Image pic = raw.getImage()
                .getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel picLabel = new JLabel(new ImageIcon(pic));
        picLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel idRole = new JPanel(new GridLayout(0, 1, 5, 5));
        idRole.add(new JLabel(_staffDetails[0]));
        idRole.add(new JLabel(_staffDetails[1]));

        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.add(picLabel, BorderLayout.WEST);
        header.add(idRole, BorderLayout.CENTER);
        dlg.add(header, BorderLayout.NORTH);

        // Details (in the CENTER so it expands naturally)
        JPanel details = new JPanel(new GridLayout(0, 1, 5, 5));
        details.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        details.add(new JLabel(_staffDetails[2]));
        details.add(new JLabel(_staffDetails[5]));
        details.add(new JLabel(_staffDetails[6]));
        details.add(new JLabel(_staffDetails[7]));
        dlg.add(details, BorderLayout.CENTER);

        // Edit Profile Button 
        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        JButton btnEdit = new JButton("Edit Profile");

        btnEdit.addActionListener(e -> {
            dlg.dispose();
            new login.LoginForm().setVisible(true);
        });

        bottom.add(btnEdit, BorderLayout.CENTER);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        dlg.add(bottom, BorderLayout.SOUTH);
        btnEdit.setBackground(Color.white);
        btnEdit.setForeground(Color.BLACK);
        btnEdit.setPreferredSize(new Dimension(150, 35));
        btnEdit.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnEdit.setOpaque(true);
        btnEdit.setContentAreaFilled(true);
        btnEdit.setFocusPainted(false);

        // Finalize size & position
        dlg.setSize(300, 250);  // fixed size you prefer
        Point loc = this.getLocationOnScreen();
        dlg.setLocation(loc.x + this.getWidth() - dlg.getWidth() - 10, loc.y + 10);
        dlg.setVisible(true);
    }
}
