package apumedicalcentre;

import javax.swing.*;
import login.loginForm;

public class ApuMedicalCentre extends JFrame {

    public static void main(String[] args) {
        // always start Swing apps on the Event‑Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            loginForm window = new loginForm();
            window.setVisible(true);
        });
    }
}

