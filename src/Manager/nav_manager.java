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

public class nav_manager extends JFrame {

    private final JPanel sidebar;
    private final CardLayout cards = new CardLayout();
    private final JPanel content;

    public nav_manager() {
        super("APU Medical Centre");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Windows Title Icon 
        ImageIcon frameIcon = new ImageIcon(
                getClass().getResource("/image/APU_Med_Cen_Assignment.png")
        );
        Image scaledIcon = frameIcon.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
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
        JLabel lblTitle = new JLabel("Dashboard");
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

        // ─── Content area with CardLayout ────────────────────────────
        content = new JPanel(cards);
        content.add(new dashboard(), "Dashboard");
        content.add(new staff_management(), "Staff Management");
        content.add(new JLabel("View Feedback", SwingConstants.CENTER), "Feedback");
        add(content, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel buildSidebar() {
        JPanel bar = new JPanel();
        bar.setPreferredSize(new Dimension(200, getHeight()));
        bar.setBackground(Color.BLACK);
        bar.setLayout(new BoxLayout(bar, BoxLayout.Y_AXIS));

        // Logo + title
        JLabel lblLogo = new JLabel("APU Medical Centre", JLabel.LEFT);
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(lblLogo.getFont().deriveFont(Font.BOLD, 18f));

        ImageIcon raw = new ImageIcon(
                getClass().getResource("/image/APU_Med_Cen_Assignment.png")
        );

        Image iconImg = raw.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        lblLogo.setIcon(new ImageIcon(iconImg));

        lblLogo.setHorizontalAlignment(SwingConstants.LEFT);
        lblLogo.setHorizontalTextPosition(SwingConstants.RIGHT);
        lblLogo.setIconTextGap(5); // Gap between Logo and Text

        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblLogo.setMaximumSize(new Dimension(
                Integer.MAX_VALUE,
                lblLogo.getPreferredSize().height
        ));

        lblLogo.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        bar.add(lblLogo);

        // Menu buttons
        bar.add(makeSidebarButton("Dashboard", e -> cards.show(content, "Dashboard")));
        bar.add(makeSidebarButton("Staff Management", e -> cards.show(content, "Staff")));
        bar.add(makeSidebarButton("View Feedback", e -> cards.show(content, "Feedback")));

        bar.add(Box.createVerticalGlue());
        bar.add(makeSidebarButton("Logout", e -> {
            // 1. Close Current Form
            SwingUtilities.getWindowAncestor(bar).dispose();
            // 2. Open the login form:
            login.loginForm login = new login.loginForm();
            login.setVisible(true);
        }));

        return bar;
    }

    private JButton makeSidebarButton(String text, ActionListener act) {
        JButton b = new JButton(text);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setForeground(Color.WHITE);
        b.setBackground(Color.DARK_GRAY);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.addActionListener(act);
        return b;
    }

    private void showProfileDialog() {
        // 1) Create dialog
        JDialog dlg = new JDialog(this, "Profile", true);
        dlg.setLayout(new BorderLayout(10, 10));
        dlg.setResizable(false);

        // 2) Header (picture + ID/Role)
        ImageIcon raw = new ImageIcon(
                getClass().getResource("/image/profile-user.png")
        );
        Image pic = raw.getImage()
                .getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel picLabel = new JLabel(new ImageIcon(pic));
        picLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel idRole = new JPanel(new GridLayout(0, 1, 5, 5));
        idRole.add(new JLabel("Staff ID"));
        idRole.add(new JLabel("Staff Role"));

        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.add(picLabel, BorderLayout.WEST);
        header.add(idRole, BorderLayout.CENTER);
        dlg.add(header, BorderLayout.NORTH);

        // 3) Details (in the CENTER so it expands naturally)
        JPanel details = new JPanel(new GridLayout(0, 1, 5, 5));
        details.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        details.add(new JLabel("Name"));
        details.add(new JLabel("Email"));
        details.add(new JLabel("Phone Number"));
        details.add(new JLabel("Age"));
        dlg.add(details, BorderLayout.CENTER);

        // 4) Button in SOUTH, left‑aligned
        JButton btnEdit = new JButton("Edit Profile");
        btnEdit.addActionListener(e -> {
            dlg.dispose();
            new login.loginForm().setVisible(true);
        });
        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        bottom.add(btnEdit,BorderLayout.CENTER);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        dlg.add(bottom, BorderLayout.SOUTH);
        btnEdit.setBackground(Color.white);
        btnEdit.setForeground(Color.BLACK);
        btnEdit.setPreferredSize(new Dimension(150, 35));
        btnEdit.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnEdit.setOpaque(true);
        btnEdit.setContentAreaFilled(true);
        btnEdit.setFocusPainted(false);

        // 5) Finalize size & position
        dlg.setSize(300, 250);  // fixed size you prefer
        Point loc = this.getLocationOnScreen();
        dlg.setLocation(loc.x + this.getWidth() - dlg.getWidth() - 10, loc.y + 10);
        dlg.setVisible(true);
    }
}