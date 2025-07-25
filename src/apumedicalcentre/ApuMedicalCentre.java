package apumedicalcentre;

import Manager.nav_manager;
import javax.swing.*;
//import login.loginForm;

public class ApuMedicalCentre extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new nav_manager().setVisible(true);
        });
    }
}

