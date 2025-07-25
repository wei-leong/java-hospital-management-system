package apumedicalcentre;

import Manager.NavManager;
import javax.swing.*;
//import login.loginForm;

public class ApuMedicalCentre extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NavManager().setVisible(true);
        });
    }
}

