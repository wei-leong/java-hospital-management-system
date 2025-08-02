package apumedicalcentre;

import javax.swing.*;
import login.LoginForm;
//import login.loginForm;

public class ApuMedicalCentre extends JFrame {

    public static void main(String[] args) { 
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}

