package Staff;

import Class.CheckInput;
import Class.CustomerAction;
import Staff.AppointmentsManagement;
import Staff.FinanceReport;
import Staff.CustomerManagement;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class AddCustomer extends JDialog {
    private JTextField CustomerName, CustomerPhone, CustomerEmail, CustomerPasswords, CustomerGender, CustomerAge;
    private JRadioButton CheckGender1;
    private JRadioButton CheckGender2;
    private JRadioButton CheckGender3;
    private JComboBox<Integer> comboBox;
    private boolean saved = false;
    private CustomerManagement refresh;
    Color defaultColor = Color.WHITE;  
    Color hoverColor = Color.LIGHT_GRAY;
    private JPanel Role;
    private final CheckInput check = new CheckInput();
    private final CustomerAction action = new CustomerAction();

        public AddCustomer(CustomerManagement refresh) {
        this.refresh = refresh;
        setTitle("Create New Customer");
        setSize(800, 600);
        setModal(true);  
        setLayout(null); 
        getContentPane().setBackground(Color.WHITE);
       
        
        //Panel for role
        JPanel Role = new JPanel();
        Role.setBounds(490, 35, 280, 130);
        Role.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        //Role
        JLabel role = new JLabel("<html>*Phone Number must be 10 digits<br>"
                       + "*Phone Number must be Integer<br>"
                       + "*User Name can't be Empty<br>"
                       + "*User Name can't start with Number<br>"
                       + "*Email must be lower case</html>");
        role.setFont(role.getFont().deriveFont(15f));
        role.setForeground(Color.GRAY);
        role.setLayout(new FlowLayout(FlowLayout.CENTER,30,10));
        
        Role.add(role);
        add(Role);

        //Name Label and TextField 
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(nameLabel.getFont().deriveFont(20f));
        nameLabel.setBounds(50, 25,100, 50);
        add(nameLabel);

        CustomerName = new JTextField();
        CustomerName.setBounds(175, 35, 270, 40);
        AbstractDocument nameDoc = (AbstractDocument) CustomerName.getDocument();
        
        //Check the name cant start with number
        nameDoc.setDocumentFilter(new DocumentFilter() {
            @Override  //Use for checking write
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                if (text != null) {
                    String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                    String newText = currentText.substring(0, offset) + text + currentText.substring(offset);

                
                    if (!newText.isEmpty() && Character.isDigit(newText.charAt(0))) {
                        return; 
                    }
                }
                super.insertString(fb, offset, text, attr);
            }
            
            //use for checking copy and paste, that mean user also cant paste the work inside the textfield,if the work is not suitable the password role
            @Override  
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null) {
                    String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                    String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

              
                    if (!newText.isEmpty() && Character.isDigit(newText.charAt(0))) {
                        return;
                    }
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });
        add(CustomerName);
        
        //Age
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(nameLabel.getFont().deriveFont(20f));
        ageLabel.setBounds(50, 275,100, 50);
        add(ageLabel);
        
        //ComboBox for choose age
        Integer[] ages = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
        comboBox = new JComboBox<>(ages);
        comboBox.setBounds(175, 285,75, 40); 
        comboBox.setBackground(Color.WHITE);
        add(comboBox);

        //Phone Label and TextField 
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(nameLabel.getFont().deriveFont(20f));
        phoneLabel.setBounds(50, 75, 100, 50);
        add(phoneLabel);
        
        //Key in Phonenumber
        CustomerPhone = new JTextField();
        CustomerPhone.setBounds(175, 85, 270, 40);
        add(CustomerPhone);
        
        //Lock it let user just can writing number and must be 10 digits
        CustomerPhone.addKeyListener(new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
           
            if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                e.consume(); 
            }

         
            if (CustomerPhone.getText().length() >= 10) {
                e.consume();
                }
            }
        });
        
        //Email Label and TextField 
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(nameLabel.getFont().deriveFont(20f));
        emailLabel.setBounds(50, 125, 100, 50);
        add(emailLabel);
        
        //Key in email
        CustomerEmail = new JTextField();
        CustomerEmail.setBounds(175, 135, 270, 40);
        
        //Use for detact the email role, must be lower case 
        AbstractDocument emailDoc = (AbstractDocument) CustomerEmail.getDocument();

        emailDoc.setDocumentFilter(new DocumentFilter() {
            @Override  //check the email writting must be lower case
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                if (text != null) {
                    text = text.toLowerCase(); 
                }
                super.insertString(fb, offset, text, attr);
            }

            @Override  //can't copy paste a upper case character to it
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null) {
                    text = text.toLowerCase(); 
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });

        add(CustomerEmail);
        
        //Passwords Label and TextField()
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(passwordLabel.getFont().deriveFont(20f));
        passwordLabel.setBounds(50,175,100, 50);
        add(passwordLabel);
        
        //Key in Password
        CustomerPasswords = new JTextField();
        CustomerPasswords.setBounds(175, 185, 270, 40);
        add(CustomerPasswords);
        
        //Gender 
        JLabel GenderLabel = new JLabel("Gender:");
        GenderLabel.setFont(GenderLabel.getFont().deriveFont(20f));
        GenderLabel.setBounds(50,225,100, 50);
        add(GenderLabel);
        
        //Choose gender Male/Female
        CheckGender1 = new JRadioButton("Male");
        CheckGender1.setBounds(175, 240, 80, 30); 
        CheckGender1.setBackground(Color.WHITE);
        add(CheckGender1);  

        CheckGender2 = new JRadioButton("Female");
        CheckGender2.setBounds(255, 240, 100, 30);
        CheckGender2.setBackground(Color.WHITE);
        add(CheckGender2);
        
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(CheckGender1);
        genderGroup.add(CheckGender2);
                 
        if(CheckGender1.isSelected()){
            System.out.println("Male");
        }
        else if(CheckGender2.isSelected()){
            System.out.println("Female");
        }

        //Create Button
        JButton createBtn = new JButton("Create");
        createBtn.setBounds(260, 430, 120, 60);
        createBtn.setFont(createBtn.getFont().deriveFont(20f));
        createBtn.addActionListener(e -> {
            try{
                String phone = CustomerPhone.getText().trim();
                if (check.checkComma(phone)){
                    JOptionPane.showMessageDialog(this, "Phone Number cannot have (,).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (phone.length() != 10) {
                    JOptionPane.showMessageDialog(this, "Phone number must be 10 digits.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String name = CustomerName.getText().trim();
                if (check.checkComma(name)){
                    JOptionPane.showMessageDialog(this, "Name cannot have (,).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (name.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Name can't be Empty","Invalid Input",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String email = CustomerEmail.getText().trim();
                if (check.checkComma(email)){
                    JOptionPane.showMessageDialog(this, "Email cannot have (,).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (email.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Email can't be Empty","Invalid Input",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String password = CustomerPasswords.getText().trim();
                if (check.checkComma(password)){
                    JOptionPane.showMessageDialog(this, "Password cannot have (,).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (password.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Password can't be Empty","Invalid Input",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (comboBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this,"Age can't be Empty","Invalid Input",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String gender = "";
                if (CheckGender1.isSelected()) {
                    gender = "Male";
                } else if (CheckGender2.isSelected()) {
                    gender = "Female";
                }
                saved = true;
                String staffId = action.generateStaffID();
                action.saveProfileInformation(staffId, "Customer", name, password, gender, email, phone,
                        (Integer) comboBox.getSelectedItem(), "Active", this);
                refresh.refreshTable();
                dispose(); 
            }catch(Exception ex){
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(createBtn);
        
        
        //Cancel Button 
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(420, 430, 120, 60);
        cancelBtn.setFont(cancelBtn.getFont().deriveFont(20f));
        cancelBtn.addActionListener(e -> dispose());
        add(cancelBtn);
        
        
        //JField settings
        for (JTextField text : new JTextField[]{CustomerName,CustomerEmail,CustomerPhone,CustomerPasswords}){
            text.setFont(new Font("Arial", Font.PLAIN,20));
        }
        
        //Customize Button
        for (JButton btn : new JButton[]{createBtn, cancelBtn}) {
            btn.setBackground(Color.WHITE);    
            btn.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
            btn.setOpaque(true);                
            btn.setFocusPainted(false); 
            applyHoverEffect(btn, defaultColor, hoverColor);
        }
    }
        public boolean isSaved() {
            return saved;
        }
        
        //Settings Button Hover Effect
        public static void applyHoverEffect(JButton btn, Color defaultColor, Color hoverColor) {
        btn.setBackground(defaultColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(defaultColor);
            }
        });
}          
}