package Manager;

import Class.Manager;
import Class.ValidateStaffInput;
import Profile.EditOwnProfile;
import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class EditStaff extends JFrame {

    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField passField = new JTextField(20);
    private final JSpinner ageField = new JSpinner(new SpinnerNumberModel(18, 16, 100, 1));
    private final JTextField phoneField = new JTextField(20);
    private final JRadioButton rbM = new JRadioButton("Male");
    private final JRadioButton rbF = new JRadioButton("Female");
    private ValidateStaffInput validateInput;

    private final String[] _currentData;
    private final String currentId;
    private final String currentRole;
    private final String currentName;
    private final String currentPass;
    private final String currentGender;
    private final String currentEmail;
    private final String currentPhone;
    private final int currentAge;

    // Add attributes here
    public EditStaff(String[] currentData) {
        super("Edit Staff");
        this._currentData = currentData;
        this.currentId = _currentData[0];
        this.currentRole = _currentData[1];
        this.currentName = _currentData[2];
        this.currentPass = _currentData[3];
        this.currentGender = _currentData[4];
        this.currentEmail = _currentData[5];
        this.currentPhone = _currentData[6];
        this.currentAge = Integer.parseInt(_currentData[7]);

        this.validateInput = new ValidateStaffInput(currentName, currentEmail, currentPhone,currentPass);

        // Window settings
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setMinimumSize(new Dimension(700, 600)); // Fixed Width and Height
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));

        add(topBar(), BorderLayout.NORTH); // Return Button + Edit Staff Title
        add(form(), BorderLayout.CENTER); // Staff Data Form 
        add(bottomBar(), BorderLayout.SOUTH); // Edit Staff button

        pack();
        setSize(600, 500);
        setLocationRelativeTo(null);
    }

    private JPanel topBar() {
        // Top Bar Panel for Return Button
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        topBar.setBackground(Color.WHITE);

        // Return Button
        ImageIcon backIcon = new ImageIcon(
                getClass().getResource("/image/back.png")
        );
        Image scaleIcon = backIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon scaledReturnIcon = new ImageIcon(scaleIcon);

        JButton btnBack = new JButton(scaledReturnIcon);
        btnBack.setBorder(null);
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            int choice = javax.swing.JOptionPane.showConfirmDialog(
                    this, // Parent Component
                    "Going back will discard any unsaved changes.\nDo you want to continue?", // 
                    "Discard changes?", // title
                    javax.swing.JOptionPane.YES_NO_OPTION, // options
                    javax.swing.JOptionPane.WARNING_MESSAGE // icon
            );

            if (choice == javax.swing.JOptionPane.YES_OPTION) {
                // user confirmed => cancel editing (do not save) and close
                this.dispose();
            }
        });
        topBar.add(btnBack);

        // Top Bar Label
        JLabel lblTitle = new JLabel("Edit Staff");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 18f));
        topBar.add(lblTitle);
        return topBar;
    }

    private JPanel form() {
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
        nameField.setText(currentName);
        form.add(nameField, gbc);

        // Age + Role Grid Layout
        gbc.gridy = row++;
        JPanel ageRole = new JPanel(new GridLayout(1, 2, 10, 0));
        ageRole.setBackground(Color.WHITE);

        // Age
        gbc.gridy = row++;
        form.add(new JLabel("Age"), gbc);
        gbc.gridy = row++;
        ageField.setValue(currentAge);
        form.add(ageField, gbc);

        // Email
        gbc.gridy = row++;
        form.add(new JLabel("Email Address"), gbc);
        gbc.gridy = row++;
        emailField.setText(currentEmail);
        form.add(emailField, gbc);

        // Phone
        gbc.gridy = row++;
        form.add(new JLabel("Phone Number"), gbc);
        gbc.gridy = row++;
        phoneField.setText(currentPhone);
        form.add(phoneField, gbc);

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

        if ("Male".equalsIgnoreCase(currentGender)) {
            rbM.setSelected(true);
        } else if ("Female".equalsIgnoreCase(currentGender)) {
            rbF.setSelected(true);
        }

        // Password
        gbc.gridy = row++;
        form.add(new JLabel("Password"), gbc);
        gbc.gridy = row++;
        passField.setText(currentPass);
        form.add(passField, gbc);
        return form;
    }

    private JPanel bottomBar() {
        // Bottom Bar with Add Staff Button
        JButton btnAdd = new JButton("Edit Staff");
        btnAdd.setFont(btnAdd.getFont().deriveFont(Font.BOLD, 14f));
        btnAdd.setPreferredSize(new Dimension(300, 40));
        btnAdd.setBackground(Color.WHITE);
        btnAdd.setForeground(Color.BLACK);
        btnAdd.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottom.setBackground(Color.WHITE);
        bottom.add(btnAdd);
        add(bottom, BorderLayout.SOUTH);
        btnAdd.addActionListener(e -> {
            String staffGender = rbM.isSelected() ? "Male" : "Female";
            String staffAge = String.valueOf(ageField.getValue());

            Manager managerActions = new Manager();

            String staffName = nameField.getText().trim();
            String staffEmail = emailField.getText().trim();
            String staffPhone = phoneField.getText().trim();
            String staffPass = passField.getText().trim();

            // Set Name, Email, Phone to new value for Validate Staff Input
            validateInput.setName(staffName);
            validateInput.setEmail(staffEmail);
            validateInput.setPhone(staffPhone);
            validateInput.setPassword(staffPass);

            if (validateInput.returnErrorMsg(currentEmail, currentPhone) != null) {
                ErrorDialog(validateInput.returnErrorMsg(currentEmail, currentPhone));
            } else {
                String[] newData = new String[]{
                    currentId,
                    currentRole,
                    staffName,
                    staffPass,
                    staffGender,
                    staffEmail,
                    staffPhone,
                    staffAge,
                    "Active",};
                managerActions.editStaff(_currentData, newData);

                this.dispose();
            }
        });

        return bottom;
    }

    private void ErrorDialog(String msg) {
        JOptionPane.showMessageDialog(
                this,
                msg,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
