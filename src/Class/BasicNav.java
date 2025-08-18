/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Wlhoe
 */
public class BasicNav extends JFrame{
    // Own Staff Details - Remove own profile from StaffManagement page
    private final String[] _staffDetails;
    private final ImageScaler imgScale = new ImageScaler();
    
    private final JPanel sidebar;
    private final CardLayout cards = new CardLayout();
    private final JPanel content= JPanel(cards);
    private String currentPage = "";
    private final JLabel lblTitle = new JLabel();
    
    // Image Setup
    private final ImageIcon toggleIcon = imgScale.returnScaledImageIcon("/image/nav-menu.png", 24, 24);
    Image windowIcon = imgScale.returnScaledImage("/image/APU_Med_Cen_Assignment.png", 128, 128);
    Icon iconProfile = imgScale.returnScaledImageIcon("/image/profile-user.png", 32, 32);
    Icon iconProfileLarge = imgScale.returnScaledImageIcon("/image/profile-user.png", 50, 50);
    
    // Button Attributes
    private final JButton btnToggle = new JButton(toggleIcon);
    private final JButton btnLogout= new JButton("Logout");
    private final JButton btnEdit = new JButton("Edit Profile");
    private final JButton btnProfile = new JButton();
    
    public BasicNav(String windowTitle, String[] staffDetails){
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
        
        setVisible(true); // Ensure the NavManager page is visible
    }
    
    protected JToolBar buildToolBar(){
        // Title Bar 
        JToolBar titleBar = new JToolBar();
        titleBar.setFloatable(false);
        
        // Hamburger Button Toggle
        titleBar.add(btnToggle);
        titleBar.addSeparator();
        btnToggleSettings();

        // Title - APU Medical Centre
        titleBar.add(lblTitle);
        titleBar.add(Box.createHorizontalGlue());

        // Button Profile to Open Staff Details
        titleBar.add(btnProfile);
        btnProfileSettings();

        return titleBar;
    }
}
