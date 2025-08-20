/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Wlhoe
 */
public class BasicNav extends JFrame {

    // Own Staff Details - Used to remove own profile from StaffManagement page
    protected final String[] _staffDetails;
    
    // UI Elements
    protected final JPanel sidebar;
    protected final CardLayout cards = new CardLayout();
    protected final JPanel content = new JPanel(cards);
    protected final JLabel lblTitle = new JLabel();

    // Image Setup
    private final ImageScaler imgScale = new ImageScaler();
    protected final ImageIcon toggleIcon = imgScale.returnScaledImageIcon("/image/nav-menu.png", 24, 24);
    private final Image windowIcon = imgScale.returnScaledImage("/image/APU_Med_Cen_Assignment.png", 128, 128);
    private final Icon iconProfile = imgScale.returnScaledImageIcon("/image/profile-user.png", 32, 32);
    private final Icon iconProfileLarge = imgScale.returnScaledImageIcon("/image/profile-user.png", 50, 50);

    // Button Attributes
    protected final JButton btnToggle = new JButton(toggleIcon);
    protected final JButton btnLogout = new JButton("Logout");
    protected final JButton btnEdit = new JButton("Edit Profile");
    protected final JButton btnProfile = new JButton();

    protected final Map<String, JButton> sidebarButtons = new LinkedHashMap<>();

    public BasicNav(String windowTitle, String[] staffDetails) {
        super("APU Medical Centre");
        this._staffDetails = staffDetails;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Dispose Navigation Menu
        addWindowListener(new WindowAdapter() { // Reopen Login Form
            @Override
            public void windowClosed(WindowEvent e) {
                SwingUtilities.invokeLater(() -> new login.LoginForm().setVisible(true));
            }
        });

        // Window Size
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setIconImage(windowIcon); // Set window icon

        // Initialize lblTitle 
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 16f));

        add(buildToolbar(), BorderLayout.NORTH);

        sidebar = buildSidebar();
        add(sidebar, BorderLayout.WEST);

        add(content, BorderLayout.CENTER);

        setVisible(true); // Ensure the NavManager page is visible
    }
    
    // Button for Navigating through other panel ( Dashboard, View Feedback, Staff Management, View Appointment )
    protected JButton makeSidebarButton(String text, Icon icon, ActionListener act) {
        JButton b = new JButton(text, icon);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setForeground(Color.WHITE);
        b.setBackground(Color.DARK_GRAY);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setHorizontalTextPosition(SwingConstants.RIGHT);
        b.setIconTextGap(8);
        b.addActionListener(act);
        return b;
    }
    
    // To store toggle sidebar button, panel title and profile button
    protected JToolBar buildToolbar() {
        // Title Bar 
        JToolBar titleBar = new JToolBar();
        titleBar.setFloatable(false);

        // Hamburger Button Toggle
        titleBar.add(btnToggle);
        titleBar.addSeparator();
        
        // Show Current Panel Title ( Dashboard, Staff Management )
        titleBar.add(lblTitle);
        titleBar.add(Box.createHorizontalGlue());

        // Button Profile to Open Staff Details
        titleBar.add(btnProfile);
        
        // Configure Button Settings
        btnToggleSettings();
        btnProfileSettings();

        return titleBar;
    }

    protected JPanel buildSidebar() {
        Color sidebarBack = Color.BLACK;
        
        // Add sidebar button with the panel from addPage method
        JPanel bar = new JPanel();
        bar.setPreferredSize(new Dimension(200, getHeight()));
        bar.setBackground(sidebarBack);
        bar.setLayout(new BoxLayout(bar, BoxLayout.Y_AXIS));
        bar.add(Box.createVerticalGlue()); // Glue the sidebar button at the top of the sidebar

        // Bottom panel contain btnLogout
        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        bottom.setBackground(sidebarBack);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        bottom.add(btnLogout, BorderLayout.SOUTH);
        btnLogoutSettings(bar);

        bar.add(bottom); // Add bottom area to sidebar
        return bar;
    }
    
    // Add Panel function 
    protected void addPage(String name, Component component, Icon icon) {
        content.add(component, name); 
        
        // Create Navigation Button to Panel
        JButton b = makeSidebarButton(name, icon, e -> {
            cards.show(content, name);
            titleChanger(name);
        });
        
        // Insert button before the last component to prevent button showing at the top
        int insertIndex = Math.max(0, sidebar.getComponentCount() - 1); 
        sidebar.add(b, insertIndex); 
        
        // Lets swing recalculate sidebar and repaints new button
        sidebar.revalidate(); 
        sidebar.repaint();
        sidebarButtons.put(name, b);
    }
    
    // Chance title when accessing new Panel
    protected void titleChanger(String newTitle) {
        lblTitle.setText(newTitle);
    }
    
    // Show Profile Details ( Staff ID, Staff Role, Name, Age ) when btnProfile is pressed
    protected void showProfileDialog() {
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
    
    // Button Style and Function Settings
    protected void btnToggleSettings() {
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

    protected void btnLogoutSettings(JPanel bar) {
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

    protected void btnEditSettings() {
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

    protected void btnProfileSettings() {
        btnProfile.setBorder(null);
        btnProfile.setContentAreaFilled(false);
        btnProfile.setIcon(iconProfile);
        btnProfile.addActionListener(e -> showProfileDialog()); // Show Own Profile Details and Enable user to edit their own profile
    }
}
