package login;

import javax.swing.*;
import java.awt.*;

public class loginForm extends JFrame {
    private final JTextField emailField  = new JTextField(30);
    private final JPasswordField passField = new JPasswordField(30);    
    private final JButton btnLogin  = new JButton("Login");// Login Button    
    private final JButton btnToggle = new JButton(); // Eye Icon Button

    public loginForm() {
        super("Login");        // window title
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);                    // no maximize
        setType(Window.Type.UTILITY);           // utility style (no minimize)

        // Panel for APU Medical Centre Image Logo ( Left )
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        // Add Image to root Panel
        ImageIcon rawIcon = new ImageIcon(
            getClass().getResource("/image/APU_Med_Cen_Assignment.png")
        );
        // Scale to 200x200
        Image logoImg = rawIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(logoImg));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        root.add(lblLogo, BorderLayout.WEST);

        // Panel for Email and Password TextBox ( Right ) 
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel lblTitle = new JLabel("APU Medical Centre");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 25f));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        form.add(lblTitle, gbc);

        // Email Label+TextField
        gbc.gridx=0;
        gbc.gridy=1; gbc.gridwidth=1;
        form.add(new JLabel("Email"), gbc);
        gbc.gridx=0;
        gbc.gridy=2;
        form.add(emailField, gbc);
        
        ImageIcon iconView = new ImageIcon(getClass().getResource("/image/view_password.png"));
        Image scaleView = iconView.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon scaledViewIcon = new ImageIcon(scaleView);
        
        ImageIcon iconHide = new ImageIcon(getClass().getResource("/image/hide_password.png"));
        Image scaleHide = iconHide.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon scaledHideIcon = new ImageIcon(scaleHide);

        // Password Label+TextField+View Password
        gbc.gridy=3; gbc.gridx=0;
        form.add(new JLabel("Password"), gbc);
        gbc.gridy=4;
        JPanel passPane = new JPanel(new BorderLayout());
        passField.setEchoChar('*');
        passPane.add(passField, BorderLayout.CENTER);
        btnToggle.setIcon(scaledViewIcon);
        btnToggle.setFocusable(false);
        btnToggle.setBorder(null);
        btnToggle.setContentAreaFilled(false);
        passPane.add(btnToggle, BorderLayout.EAST);
        form.add(passPane, gbc);

        // Toggle password visibility
        btnToggle.addActionListener(e -> {
            if(passField.getEchoChar()== (char)0){
                passField.setEchoChar('*');
                btnToggle.setIcon(scaledViewIcon);
            }else{
                passField.setEchoChar((char)0);
                btnToggle.setIcon(scaledHideIcon);
            }
        });

        // Login button
        gbc.gridy=30; gbc.gridx=0; gbc.gridwidth=20;
        btnLogin.setPreferredSize(new Dimension(150,40));
        form.add(btnLogin, gbc);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBackground(Color.WHITE);

        root.add(form, BorderLayout.CENTER);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
    }
}

