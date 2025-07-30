package login;

import Class.Person;
import Manager.NavManager;
import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class LoginForm extends JFrame {

    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passField = new JPasswordField(20);
    private final JButton btnLogin = new JButton("Login");// Login Button    
    private final JButton btnToggle = new JButton(); // Eye Icon Button

    public LoginForm() {
        // Window Title
        super("Login");

        // Styling Options
        float titleSize = 25;
        float labelSize = 12;
        float inputSize = 18;

        // Windows Settings
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);// Disable Maximize                 

        // Windows Title Icon 
        ImageIcon frameIcon = new ImageIcon(
                getClass().getResource("/image/APU_Med_Cen_Assignment.png")
        );
        Image scaledIcon = frameIcon.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
        setIconImage(scaledIcon);

        // Panel for APU Medical Centre Image Logo ( Left )
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        // Add Image to root Panel
        ImageIcon rawIcon = new ImageIcon(
                getClass().getResource("/image/APU_Med_Cen_Assignment.png")
        );

        // Scale APU Medical Centre Image to 200x200
        Image logoImg = rawIcon.getImage().getScaledInstance(225, 225, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(logoImg));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        root.add(lblLogo, BorderLayout.WEST);

        // Panel for Title and TextBox Input ( Right ) 
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel lblTitle = new JLabel("APU Medical Centre");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, titleSize));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER); // Align the Title to Center
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        form.add(lblTitle, gbc);

        // Email Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 100;
        gbc.ipady = 10;
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(lblEmail.getFont().deriveFont(Font.BOLD, labelSize));
        form.add(lblEmail, gbc);

        // Email Text Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        emailField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remove the Default Border
        emailField.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, // top, left, bottom, right
                Color.GRAY // line color
        )); // Add a 1px bottom border (gray underline)
        emailField.setFont(emailField.getFont().deriveFont(inputSize));
        form.add(emailField, gbc);

        // Image Icon for Hide and View Password
        ImageIcon iconView = new ImageIcon(getClass().getResource("/image/view_password.png"));
        Image scaleView = iconView.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon scaledViewIcon = new ImageIcon(scaleView);

        ImageIcon iconHide = new ImageIcon(getClass().getResource("/image/hide_password.png"));
        Image scaleHide = iconHide.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon scaledHideIcon = new ImageIcon(scaleHide);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(lblPassword.getFont().deriveFont(Font.BOLD, labelSize));
        form.add(lblPassword, gbc);

        // Password Text Field
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 100;
        gbc.ipady = 10;
        JPanel passPane = new JPanel(new BorderLayout());
        passField.setEchoChar('*');
        passField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remove the Default Border
        passField.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, // top, left, bottom, right
                Color.GRAY // line color
        )); // Add a 1px bottom border (gray underline)
        passField.setFont(passField.getFont().deriveFont(inputSize));
        passPane.add(passField, BorderLayout.CENTER);

        // Toggle View / Hide Button for Password
        btnToggle.setIcon(scaledViewIcon);
        btnToggle.setFocusable(false);
        btnToggle.setOpaque(true);
        btnToggle.setContentAreaFilled(true);
        btnToggle.setBackground(Color.WHITE);
        btnToggle.setPreferredSize(new Dimension(30, 20));
        btnToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnToggle.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        passPane.add(btnToggle, BorderLayout.EAST);
        form.add(passPane, gbc);

        // Toggle password visibility Function
        btnToggle.addActionListener(e -> {
            if (passField.getEchoChar() == (char) 0) {
                passField.setEchoChar('*');
                btnToggle.setIcon(scaledViewIcon);
            } else {
                passField.setEchoChar((char) 0);
                btnToggle.setIcon(scaledHideIcon);
            }
        });

        // Login button
        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 100, 10, 100));
        gbc.gridy = 30;
        gbc.gridx = 0;
        gbc.gridwidth = 20;
        btnLogin.setPreferredSize(new Dimension(100, 25));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBackground(Color.WHITE);

        btnLogin.addActionListener(e -> {
            String userEmail = emailField.getText();
            String userPassword = new String(passField.getPassword());

            Person newUser = new Person(userEmail, userPassword);
            String[] staffDetails = newUser.login();
            // Check if Account is Logged In
            if (staffDetails == null) {
                ErrorDialog("Login failed: incorrect email or password.");
                return;
            }

            // Check id Account is Active
            if (!"Active".equalsIgnoreCase(staffDetails[8])) {
                ErrorDialog("Login failed: inactive staff are not allowed to login.");
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
        });

        bottom.add(btnLogin, BorderLayout.CENTER);
        form.add(bottom, gbc);

        root.add(form, BorderLayout.CENTER);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void ErrorDialog(String msg) {
        JOptionPane.showMessageDialog(
                this,
                msg,
                "Login Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
