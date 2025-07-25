/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Wlhoe
 */
package Manager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class staff_management extends JPanel {

    public staff_management() {
                setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel revenue = new JPanel();
        revenue.setBorder(new TitledBorder("Revenue"));
        revenue.add(new JLabel("<< Revenue Chart Here >>"));
        add(revenue, BorderLayout.NORTH);

        JPanel bottom = new JPanel(new GridLayout(1,2,10,0));
        JPanel appoint = new JPanel(new BorderLayout());
        appoint.setBorder(new TitledBorder("Appointment Count"));
        appoint.add(new JLabel("100 This Month", SwingConstants.CENTER), BorderLayout.CENTER);
        bottom.add(appoint);

        JPanel feed = new JPanel(new BorderLayout());
        feed.setBorder(new TitledBorder("Feedback for Doctor/Staff"));
        feed.add(new JLabel("<< Feedback Table Here >>", SwingConstants.CENTER), BorderLayout.CENTER);
        bottom.add(feed);

        add(bottom, BorderLayout.CENTER);
    }
}
