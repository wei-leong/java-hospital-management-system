package Manager;

import javax.swing.*;
import java.awt.*;

public class AddStaff extends JFrame {
    public AddStaff() {
        super("Add Staff");

        // 1) Window settings
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(400, 400));
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(10,10));

        // ─── Top Bar ───────────────────────────────────────────────
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        topBar.setBackground(Color.WHITE);

        // Back button (icon) + listener
        ImageIcon backIcon = new ImageIcon(
            getClass().getResource("/image/back.png")
        );
        Image scaleIcon = backIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon scaledReturnIcon = new ImageIcon(scaleIcon);
        
        JButton btnBack = new JButton(scaledReturnIcon);
        btnBack.setBorder(null);
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> this.dispose());
        topBar.add(btnBack);

        JLabel lblTitle = new JLabel("Add Staff");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 18f));
        topBar.add(lblTitle);

        add(topBar, BorderLayout.NORTH);

        // ─── Main Form ──────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets    = new Insets(8,8,8,8);
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.weightx   = 1.0;

        int row = 0;

        // Name
        gbc.gridx = 0; gbc.gridy = row++;
        form.add(new JLabel("Name"), gbc);
        gbc.gridy = row++;
        form.add(new JTextField(), gbc);

        // Age + Role
        gbc.gridy = row++;
        JPanel ageRole = new JPanel(new GridLayout(1,2,10,0));
        ageRole.setBackground(Color.WHITE);

        // Age
        JPanel agePanel = new JPanel(new BorderLayout());
        agePanel.setBackground(Color.WHITE);
        agePanel.add(new JLabel("Age"), BorderLayout.NORTH);
        agePanel.add(new JSpinner(new SpinnerNumberModel(18,16,100,1)),
                     BorderLayout.CENTER);
        ageRole.add(agePanel);

        // Role
        JPanel rolePanel = new JPanel(new BorderLayout());
        rolePanel.setBackground(Color.WHITE);
        rolePanel.add(new JLabel("Role"), BorderLayout.NORTH);
        rolePanel.add(new JComboBox<>(new String[]{"Manager","Staff","Doctor"}),
                      BorderLayout.CENTER);
        ageRole.add(rolePanel);

        form.add(ageRole, gbc);

        // Email
        gbc.gridy = row++;
        form.add(new JLabel("Email Address"), gbc);
        gbc.gridy = row++;
        form.add(new JTextField(), gbc);

        // Phone
        gbc.gridy = row++;
        form.add(new JLabel("Phone Number"), gbc);
        gbc.gridy = row++;
        form.add(new JTextField(), gbc);

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

        // ─── Bottom Bar ─────────────────────────────────────────────
        JButton btnAdd = new JButton("+ Add Staff");
        btnAdd.setFont(btnAdd.getFont().deriveFont(Font.BOLD, 14f));
        btnAdd.setPreferredSize(new Dimension(120, 36));
        btnAdd.setBackground(Color.WHITE);
        btnAdd.setForeground(Color.BLACK);
        btnAdd.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottom.setBackground(Color.WHITE);
        bottom.add(btnAdd);
        add(bottom, BorderLayout.SOUTH);

        // 2) Pack then enforce default size
        pack();
        setSize(600, 500);
        setLocationRelativeTo(null);
    }
}
