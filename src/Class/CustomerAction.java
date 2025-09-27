package Class;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JOptionPane;

public class CustomerAction {
    
        //Function about save New Customer Profile Information into txt file
        public void saveProfileInformation(String staffId, String role, String name, String password,
                                       String gender, String email, String phone, Integer age, String status, Component parent) {
        Path profilePath = Paths.get("src", "txt", "profile.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(profilePath.toFile(),true))) {
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
        Path profilePath = Paths.get("src", "txt", "profile.txt");
    File file = profilePath.toFile();
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
                    
                        }
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return "C" + (maxCustomerId + 1);
    }
    
    public boolean checkEmailDuplicate(String email) {
        Path profilePath = Paths.get("src", "txt", "profile.txt");
        File file = profilePath.toFile();

        if (!file.exists()) {
            return false; // 如果文件不存在，表示没有重复
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String existingEmail = parts[5].trim().toLowerCase();
                    if (existingEmail.equals(email.toLowerCase())) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public boolean checkPhoneDuplicate(String phone) {
        Path profilePath = Paths.get("src", "txt", "profile.txt");
        File file = profilePath.toFile();

        if (!file.exists()) {
            return false; 
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String existingPhone = parts[6].trim();
                    if (existingPhone.equals(phone)) {
                        return true; 
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    public boolean EmailFormat(String email) {
    if (email == null || email.trim().isEmpty()) {
        return false;
    }
    return email.toLowerCase().endsWith("@mail.apu.com");
}
}
