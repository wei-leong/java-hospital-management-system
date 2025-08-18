/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.awt.CardLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
    
    // Button Attributes
    private final JButton btnToggle = new JButton();
    private final JButton btnLogout= new JButton("Logout");
    private final JButton btnEdit = new JButton("Edit Profile");
    private final JButton btnProfile = new JButton();
    
}
