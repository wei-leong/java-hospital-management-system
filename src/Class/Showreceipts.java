package Class;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Showreceipts extends JDialog {

    private JLabel lblReceiptId;
    private JLabel lblTotalPayment; //TotalPayment
    private JTextArea txtItems;      // medician name

    public Showreceipts(String receiptId) {
        setTitle("Receipt");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Top side of the panel show receipts ID and payment ID
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS)); 
        topPanel.setBackground(Color.WHITE);

        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        idPanel.add(new JLabel("Receipt ID: "));
        lblReceiptId = new JLabel(receiptId);
        idPanel.add(lblReceiptId);
        topPanel.add(idPanel);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.add(new JLabel("Total Payment: "));
        lblTotalPayment = new JLabel("Loading...");
        totalPanel.add(lblTotalPayment);
        topPanel.add(totalPanel);

        // JTextfield to show the medician name
        txtItems = new JTextArea();
        txtItems.setEditable(false);
        txtItems.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(txtItems);

        // Layout 
        setLayout(new BorderLayout(10, 10));
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadReceiptItems(receiptId);
        loadTotalPayment(receiptId);  
    }
    
    //This function use to load the medician 
    private void loadReceiptItems(String receiptId) {
        Path medicinePath = Paths.get("src", "txt", "medicine.txt");
        Path receiptPath = Paths.get("src", "txt", "receipt.txt");
        List<String> items = new ArrayList<>();

        // read medician.txt and put it into hashmap
        Map<String, String[]> medicineMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(medicinePath.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String medicineId = parts[0].trim();
                    String medicineName = parts[1].trim();
                    String price = parts[2].trim();
                    medicineMap.put(medicineId, new String[]{medicineName, price});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error reading medicine.txt: " + e.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }

        // read receipt.txt to get the medician id, and check the map, what id is what item
        try (BufferedReader br = new BufferedReader(new FileReader(receiptPath.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[1].trim().equals(receiptId)) {
                    String receiptItemId = parts[0].trim();
                    String medicineId = parts[2].trim();
                    String qty = parts[3].trim();

                    // get medician name and price
                    String[] medicineInfo = medicineMap.get(medicineId);
                    if (medicineInfo != null) {
                        String medicineName = medicineInfo[0];
                        String price = medicineInfo[1];
                        items.add("[" + receiptItemId + "] " + medicineName + " (RM " + price + ") x " + qty);
                    } else {
                        items.add("[" + receiptItemId + "] " + medicineId + " x " + qty + " (Not found in medicine.txt)");
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error reading receipt_item.txt: " + e.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }

        
        if (items.isEmpty()) {
            txtItems.setText("No items found for this receipt.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String item : items) {
                sb.append(item).append("\n");
            }
            txtItems.setText(sb.toString());
        }
    }
    
    private void loadTotalPayment(String receiptId) {
        Path receiptPath = Paths.get("src", "txt", "receipt.txt");
        Path paymentPath = Paths.get("src", "txt", "payment.txt");
        String appointmentId = null;
        String paymentId = null;
        String amount = "N/A";

        //read receipt.txt to get appoinment and payment id
        try (BufferedReader br = new BufferedReader(new FileReader(receiptPath.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].trim().equals(receiptId)) {
                    appointmentId = parts[1].trim();
                    paymentId = parts[2].trim();
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error reading receipt.txt: " + e.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }

        // use payment id to check the amount in payment.txt
        if (paymentId != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(paymentPath.toFile()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2 && parts[0].trim().equals(paymentId)) {
                        amount = parts[1].trim();
                        break;
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error reading payment.txt: " + e.getMessage(),
                        "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        lblTotalPayment.setText("RM " + amount);
    }
}