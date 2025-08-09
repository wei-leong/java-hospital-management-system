package login;

import Class.ImageScaler;
import Class.Person;
import Manager.NavManager;
import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class LoginForm extends JFrame {

    // Fields
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passField = new JPasswordField(20);
    private final JButton btnLogin = new JButton("Login");// Login Button    
    private final JButton btnToggle = new JButton(); // Eye Icon Button

    // Constants
    private static final float titleSize = 25f;
    private static final float labelSize = 12f;
    private static final float inputSize = 18f;
    private static final Color backColor = Color.WHITE;
    private static final Insets insets = new Insets(8, 8, 8, 8);
    private ImageScaler imgScale = new ImageScaler();

    // Image Icon for Hide and View Password
    ImageIcon imgView = imgScale.returnScaledImageIcon("/image/view_password.png", 24, 24);
    ImageIcon imgHide = imgScale.returnScaledImageIcon("/image/hide_password.png", 24, 24);

    public LoginForm() {

        // Window Title
        super("Login");

        // Windows Settings
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);// Disable Maximize                 

        // Windows Title Icon 
        Image windowIcon = imgScale.returnScaledImage("/image/APU_Med_Cen_Assignment.png", 128, 128);
        setIconImage(windowIcon);

        // Panel for APU Medical Centre Image Logo ( Left )
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        ImageIcon logoImg = imgScale.returnScaledImageIcon("/image/APU_Med_Cen_Assignment.png", 225, 225);
        JLabel lblLogo = new JLabel(logoImg);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        root.add(lblLogo, BorderLayout.WEST);

        // Panel for Title and TextBox Input ( Right ) 
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = insets;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel lblTitle = addLabel("APU Medical Centre", titleSize, SwingConstants.CENTER);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER); // Align the Title to Center
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        form.add(lblTitle, gbc);

        // Email Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(addLabel("Email", labelSize, SwingConstants.LEFT), gbc);

        // Email Text Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        emailInputSettings();
        form.add(emailField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 3;
        form.add(addLabel("Password", labelSize, SwingConstants.LEFT), gbc);

        // Password Text Field
        gbc.gridy = 4;
        JPanel passPane = new JPanel(new BorderLayout());
        passInputSettings();
        passPane.add(passField, BorderLayout.CENTER);

        // Toggle View / Hide Button for Password
        btnToggleSettings();

        passPane.add(btnToggle, BorderLayout.EAST);
        form.add(passPane, gbc);

        // Toggle password visibility Function
        btnToggle.addActionListener(e -> {
            togglePasswordVisibility();
        });

        // Login button
        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 100, 10, 100));
        gbc.gridy = 30;
        gbc.gridx = 0;
        gbc.gridwidth = 20;
        btnLoginSettings();

        btnLogin.addActionListener(e -> {
            String userEmail = emailField.getText();
            String userPassword = new String(passField.getPassword());

            onLogin(userEmail, userPassword);
        });

        bottom.add(btnLogin, BorderLayout.CENTER);
        form.add(bottom, gbc);

        root.add(form, BorderLayout.CENTER);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void leftLogoPanel(){
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);

        ImageIcon logoImg = imgScale.returnScaledImageIcon("/image/APU_Med_Cen_Assignment.png", 225, 225);
        JLabel lblLogo = new JLabel(logoImg);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        leftPanel.add(lblLogo, BorderLayout.WEST);
    }
    
    private void bottomBar(){
        
    }

    private void onLogin(String userEmail, String userPassword) {
        Person newUser = new Person(userEmail, userPassword);
        String[] staffDetails = newUser.loginValidate();
        // Check if Account is Logged In
        if (staffDetails == null) {
            ErrorDialog("Login Failed: Incorrect Email or Password.");
            return;
        }

        // Check id Account is Active
        if (!"Active".equalsIgnoreCase(staffDetails[8])) {
            ErrorDialog("Login Failed: Inactive Staff are not Allowed to Login.");
            return;
        }

        String role = staffDetails[1];
        switch (role) {
            case "Manager":
                SwingUtilities.invokeLater(() -> {
                    new NavManager(staffDetails).setVisible(true);
                });
                dispose();  // close login window
                break;

            case "Staff":
                break;

            case "Doctor":
                break;

            case "Customer":
                break;

            default:
                ErrorDialog("Login failed: your role (“" + role + "”) is not recognized.");
                break;
        }
    }

    private void ErrorDialog(String msg) {
        JOptionPane.showMessageDialog(
                this,
                msg,
                "Login Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private JLabel addLabel(String text, float textSize, int alignment) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, textSize));
        lbl.setHorizontalAlignment(alignment);
        return lbl;
    }

    private void emailInputSettings() {
        emailField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remove the Default Border
        emailField.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, // top, left, bottom, right
                Color.BLACK // line color
        )); // Add a 1px bottom border (gray underline)
        emailField.setFont(emailField.getFont().deriveFont(inputSize));
    }

    private void passInputSettings() {
        passField.setEchoChar('*');
        passField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remove the Default Border
        passField.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, // top, left, bottom, right
                Color.BLACK // line color
        )); // Add a 1px bottom border (gray underline)
        passField.setFont(passField.getFont().deriveFont(inputSize));
    }

    private void togglePasswordVisibility() {
        if (passField.getEchoChar() == (char) 0) {
            passField.setEchoChar('*');
            btnToggle.setIcon(imgView);
        } else {
            passField.setEchoChar((char) 0);
            btnToggle.setIcon(imgHide);
        }
    }

    private void btnToggleSettings() {
        btnToggle.setIcon(imgView);
        btnToggle.setFocusable(false);
        btnToggle.setOpaque(true);
        btnToggle.setContentAreaFilled(true);
        btnToggle.setBackground(Color.WHITE);
        btnToggle.setPreferredSize(new Dimension(30, 20));
        btnToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnToggle.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }

    private void btnLoginSettings() {
        btnLogin.setPreferredSize(new Dimension(150, 50));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBackground(Color.WHITE);
    }
}
