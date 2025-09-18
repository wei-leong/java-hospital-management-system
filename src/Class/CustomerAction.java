package Class;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

public class CustomerAction {
        //Function about save New Customer Profile Information into txt file
        public void saveProfileInformation(String staffId, String role, String name, String password,
                                       String gender, String email, String phone, Integer age, String status, Component parent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\USER BACKUP\\Documents\\NetBeansProjects\\apu-medical-centre1\\src\\txt\\profile.txt", true))) {
            writer.write(staffId + "," + role + "," + name + "," + password + "," + gender + "," + email + "," + phone + "," + age + "," + status);
            writer.newLine();
            writer.flush();
            JOptionPane.showMessageDialog(parent, "Customer " + name + " saved successfully!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "Error saving file: " + ex.getMessage());
        }
    }
        
    // Use to auto generate customerid
    public String generateStaffID() {
    File file = new File("D:\\USER BACKUP\\Documents\\NetBeansProjects\\apu-medical-centre1\\src\\txt\\profile.txt");
        int maxCustomerId = 0;

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0 && parts[0].startsWith("C")) {
                        try {
                            int num = Integer.parseInt(parts[0].substring(1));
                            if (num > maxCustomerId) {
                                maxCustomerId = num;
                            }
                        } catch (NumberFormatException ignored) {
                            // 忽略无法解析的行
                        }
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return "C" + (maxCustomerId + 1);
    }
}
