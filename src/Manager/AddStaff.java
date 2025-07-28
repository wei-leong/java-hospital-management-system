package Manager;

import Class.AddProfile;
import javax.swing.*;
import java.awt.*;

public class AddStaff extends JFrame {
    
    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JSpinner ageField = new JSpinner(new SpinnerNumberModel(18,16,100,1));
    private final JComboBox roleField = new JComboBox<>(new String[]{"Manager","Staff","Doctor"});
    private final JTextField phoneField = new JTextField(20);
    
    public AddStaff() {
        super("Add Staff");

        // Window settings
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setMinimumSize(new Dimension(500, 500)); // Fixed Width and Height
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(10,10));

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
        btnBack.addActionListener(e -> this.dispose()); // Dispose Add Staff page and return to StaffManagement
        topBar.add(btnBack);
        
        // Top Bar Label
        JLabel lblTitle = new JLabel("Add Staff");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 18f));
        topBar.add(lblTitle);

        add(topBar, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets    = new Insets(8,8,8,8);
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.weightx   = 1.0;

        int row = 0;

        // Name Title
        gbc.gridx = 0; gbc.gridy = row++;
        form.add(new JLabel("Name"), gbc);
        gbc.gridy = row++;
        form.add(nameField, gbc);

        // Age + Role Grid Layout
        gbc.gridy = row++;
        JPanel ageRole = new JPanel(new GridLayout(1,2,10,0));
        ageRole.setBackground(Color.WHITE);

        // Age
        JPanel agePanel = new JPanel(new BorderLayout());
        agePanel.setBackground(Color.WHITE);
        agePanel.add(new JLabel("Age"), BorderLayout.NORTH);
        agePanel.add(ageField,BorderLayout.CENTER);
        ageRole.add(agePanel);

        // Role
        JPanel rolePanel = new JPanel(new BorderLayout());
        rolePanel.setBackground(Color.WHITE);
        rolePanel.add(new JLabel("Role"), BorderLayout.NORTH);
        rolePanel.add(roleField,BorderLayout.CENTER);
        ageRole.add(rolePanel);

        form.add(ageRole, gbc);

        // Email
        gbc.gridy = row++;
        form.add(new JLabel("Email Address"), gbc);
        gbc.gridy = row++;
        form.add(emailField, gbc);

        // Phone
        gbc.gridy = row++;
        form.add(new JLabel("Phone Number"), gbc);
        gbc.gridy = row++;
        form.add(phoneField, gbc);

        // Gender
        gbc.gridy = row++;
        form.add(new JLabel("Gender"), gbc);
        gbc.gridy = row++;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        genderPanel.setBackground(Color.WHITE);
        JRadioButton rbM = new JRadioButton("Male");
        JRadioButton rbF = new JRadioButton("Female");
        rbM.setBackground(Color.WHITE);
        rbF.setBackground(Color.WHITE);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbM); bg.add(rbF);
        genderPanel.add(rbM);
        genderPanel.add(rbF);
        form.add(genderPanel, gbc);

        add(form, BorderLayout.CENTER);

        // Bottom Bar with Add Staff Button
        JButton btnAdd = new JButton("+ Add Staff");
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
            AddProfile newStaff = new AddProfile();
            
            String gender = rbM.isSelected() ? "Male" : "Female";
            int age = (int) ageField.getValue();
            
            String staffId;
            if ( "Manager".equals(roleField.toString())){
                staffId = "M";
            }else if ( "Staff".equals(roleField.toString())){
                staffId = "S";
            }else {
                staffId = "D";
            }
            newStaff.AddNewProfile(nameField.toString(), emailField.toString(), age, roleField.toString(), phoneField.toString(), staffId, gender);
        });

        pack();
        setSize(600, 500);
        setLocationRelativeTo(null);
    }
}
