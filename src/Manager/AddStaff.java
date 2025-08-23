package Manager;

import Class.ImageScaler;
import Class.Manager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class AddStaff extends JFrame {

    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JSpinner ageField = new JSpinner(new SpinnerNumberModel(18, 16, 100, 1));
    private final JComboBox roleField = new JComboBox<>(new String[]{"Manager", "Staff", "Doctor"});
    private final JTextField phoneField = new JTextField(20);
    private final JButton btnAdd = new JButton("+ Add Staff");
    private final JButton btnBack;
    private final JRadioButton rbM = new JRadioButton("Male",true);
    private final JRadioButton rbF = new JRadioButton("Female",false);
    private final Manager managerActions = new Manager();

    private final ImageScaler imgScale = new ImageScaler();
    ImageIcon returnIcon = imgScale.returnScaledImageIcon("/image/back.png", 24, 24);

    public AddStaff() {
        super("Add Staff");

        // Window settings
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setMinimumSize(new Dimension(500, 500)); // Fixed Width and Height
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));

        btnBack = new JButton(returnIcon);
        add(topBar(), BorderLayout.NORTH); // Return to Staff Management Icon and Add Staff Title
        add(middleSection(), BorderLayout.CENTER); // Staff Details Input Form ( Name, Age, Email, Gender )
        add(bottomBar(), BorderLayout.SOUTH); // Add Staff Button

        pack();
        setSize(600, 500);
        setLocationRelativeTo(null);
    }

    private void ErrorDialog(String msg) {
        JOptionPane.showMessageDialog(
                this,
                msg,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void btnAddSettings() {
        // Bottom Bar with Add Staff Button
        btnAdd.setFont(btnAdd.getFont().deriveFont(Font.BOLD, 14f));
        btnAdd.setPreferredSize(new Dimension(300, 40));
        btnAdd.setBackground(Color.WHITE);
        btnAdd.setForeground(Color.BLACK);
        btnAdd.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> {

            String gender = rbM.isSelected() ? "Male" : "Female";
            int age = (int) ageField.getValue();

            String staffId;
            String staffRole = roleField.getSelectedItem().toString();
            if ("Manager".equals(staffRole)) {
                staffId = "M";
            } else if ("Staff".equals(staffRole)) {
                staffId = "S";
            } else if ("Doctor".equals(staffRole)) {
                staffId = "D";
            } else {
                staffId = "N";
            }

            String staffName = nameField.getText().trim();
            String staffEmail = emailField.getText().trim();
            String staffPhone = phoneField.getText().trim();

            managerActions.addStaff(staffName, staffEmail, age, staffRole, staffPhone, staffId, gender);

            this.dispose();
        });
    }

    private void phoneInputListener() {
        phoneField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String phone = phoneField.getText().trim();
                if (!phone.isEmpty()) {
                    if (!managerActions.isPhoneValid(phone)) {
                        ErrorDialog("Phone Number must be exactly 10 digits");
                        SwingUtilities.invokeLater(() -> phoneField.requestFocusInWindow());
                    } else if (!managerActions.isPhoneUnique(phone)) {
                        ErrorDialog("Phone Number is already in use");
                        SwingUtilities.invokeLater(() -> phoneField.requestFocusInWindow());
                    }
                }
            }
        });
    }

    private void emailInputListener() {
        emailField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String email = emailField.getText().trim();
                if (!email.isEmpty()) {
                    if (!managerActions.isEmailUnique(email)) {
                        ErrorDialog("This email is already in use");
                        SwingUtilities.invokeLater(() -> emailField.requestFocusInWindow());
                    } else if (!managerActions.isEmailEnds(email, "@mail.apu.com")) {
                        ErrorDialog("Email must ends with @mail.apu.com");
                        SwingUtilities.invokeLater(() -> emailField.requestFocusInWindow());
                    }
                }
            }
        });
    }

    private void btnBackSettings() {
        btnBack.setBorder(null);
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> this.dispose()); // Dispose Add Staff page and return to StaffManagement
    }

    private JPanel topBar() {
        // Top Bar Panel for Return Button
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        topBar.setBackground(Color.WHITE);

        // Return Button Icon
        btnBackSettings();
        topBar.add(btnBack);

        // Top Bar Label
        JLabel lblTitle = new JLabel("Add Staff");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 18f));
        topBar.add(lblTitle);

        return topBar;
    }

    private JPanel bottomBar() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottom.setBackground(Color.WHITE);
        bottom.add(btnAdd);
        btnAddSettings();
        return bottom;
    }

    private JPanel middleSection() {
        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Name Title
        gbc.gridx = 0;
        gbc.gridy = row++;
        form.add(new JLabel("Name"), gbc);
        gbc.gridy = row++;
        form.add(nameField, gbc);

        // Age + Role Grid Layout
        gbc.gridy = row++;
        JPanel ageRole = new JPanel(new GridLayout(1, 2, 10, 0));
        ageRole.setBackground(Color.WHITE);

        // Age
        JPanel agePanel = new JPanel(new BorderLayout());
        agePanel.setBackground(Color.WHITE);
        agePanel.add(new JLabel("Age"), BorderLayout.NORTH);
        agePanel.add(ageField, BorderLayout.CENTER);
        ageRole.add(agePanel);

        // Role
        JPanel rolePanel = new JPanel(new BorderLayout());
        rolePanel.setBackground(Color.WHITE);
        rolePanel.add(new JLabel("Role"), BorderLayout.NORTH);
        rolePanel.add(roleField, BorderLayout.CENTER);
        ageRole.add(rolePanel);

        form.add(ageRole, gbc);

        // Email
        gbc.gridy = row++;
        form.add(new JLabel("Email Address"), gbc);
        gbc.gridy = row++;
        form.add(emailField, gbc);
        emailInputListener();

        // Phone
        gbc.gridy = row++;
        form.add(new JLabel("Phone Number"), gbc);
        gbc.gridy = row++;
        form.add(phoneField, gbc);
        phoneInputListener();

        // Gender
        gbc.gridy = row++;
        form.add(new JLabel("Gender"), gbc);
        gbc.gridy = row++;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        genderPanel.setBackground(Color.WHITE);
        rbM.setBackground(Color.WHITE);
        rbF.setBackground(Color.WHITE);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbM);
        bg.add(rbF);
        genderPanel.add(rbM);
        genderPanel.add(rbF);
        form.add(genderPanel, gbc);

        return form;
    }
}
