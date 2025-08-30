package Staff;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Dashboard extends JPanel{
    
    public Dashboard() {
        setBackground(Color.WHITE);
        JLabel dashboard = new JLabel("<html>Healthy is the life key<br>Good rest for better work state</html>");
        dashboard.setFont(new Font("Time New Roman", Font.BOLD, 30));
        add(dashboard);
    }   
    
}
