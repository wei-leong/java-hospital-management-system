/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    
    
}
