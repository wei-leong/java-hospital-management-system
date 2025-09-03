package Staff;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FinanceReport extends JPanel{

    public FinanceReport(){
        setLayout(new BorderLayout());

        // Panel to put the button
        JPanel contentPanel = new JPanel(new GridBagLayout()); 
        contentPanel.setBackground(Color.WHITE);

        // View History and Generate Receipts Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setBackground(Color.WHITE);
        
        //put the image and text inside the button
        ImageIcon viewIcon = new ImageIcon(getClass().getResource("/image/history.png"));
        ImageIcon generateIcon = new ImageIcon(getClass().getResource("/image/receipts.png"));
        
        Image generateImg = generateIcon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
        generateIcon = new ImageIcon(generateImg);
        
        JButton btnView = new JButton("View Appointment Receipts", viewIcon);
        JButton btnGenerate = new JButton("Generate Receipts", generateIcon);
        

        // customize picture and word in the button
        for (JButton btn : new JButton[]{btnView, btnGenerate}) {
            btn.setPreferredSize(new Dimension(300, 250)); 
            btn.setFont(btn.getFont().deriveFont(Font.BOLD, 16f));
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);

            btn.setHorizontalTextPosition(SwingConstants.CENTER); 
            btn.setVerticalTextPosition(SwingConstants.BOTTOM);   
        }
        
        //button view function (while user click)
        btnView.addActionListener(e->{
            ReceiptsHistoryTable receipttable = new ReceiptsHistoryTable();
                JDialog dialog = new JDialog((Frame) null, "Appointments payment and receipt History", true);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.add(receipttable);
                dialog.pack();
                dialog.setLocationRelativeTo(null); 
                dialog.setVisible(true);
        });
        
        //btn generate function (while user click)
        btnGenerate.addActionListener(e-> {
            GenerateReceipts generatereceipts = new GenerateReceipts();
            generatereceipts.setLocationRelativeTo(this);
            generatereceipts.setVisible(true);
        });
        
        buttonPanel.add(btnView);
        buttonPanel.add(btnGenerate);

        contentPanel.add(buttonPanel, new GridBagConstraints());
        add(contentPanel, BorderLayout.CENTER);
    }
}