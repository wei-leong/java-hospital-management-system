/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import Class.FileActions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wlhoe
 */

public class ProfileActions extends FileActions {

    private static final int idx_id = 0;
    private static final int idx_role = 1;
    private static final int idx_name = 2;
    private static final int idx_pass = 3;
    private static final int idx_gender = 4;
    private static final int idx_email = 5;
    private static final int idx_phone = 6;
    private static final int idx_age = 7;
    private static final int idx_status = 8;
    private static final int txt_len = 9;

    public ProfileActions() {
        super("profile.txt");
    }

    public void AddNewProfile(String newName, String newEmail, int newAge, String newRole, String newPhone, String id, String newGender) {

        List<String[]> allData = returnAllDataFromFile(txt_len);
        int maxId = 0;
        
        for(String[] row : allData){
            // Need to change the startwith
            if(row.length == txt_len && row[idx_id].startsWith(id)){
                try{
                    int num = Integer.parseInt(row[idx_id].substring(1)); // Skip the tag ( S1 skip S )
                    if(num > maxId){
                        maxId = num; // Replace maxId with higher number
                    }
                }catch (NumberFormatException e){
                    // Ignore invalid id
                }
            }
        }
        
        String newId = id + (maxId + 1);
        String password = newId + newAge; 

            
        String[] newStaff = {
            newId,
            newRole,
            newName,
            password,
            newGender,
            newEmail,
            newPhone,
            String.valueOf(newAge),
            "Active"
        };

        addRowToFile(newStaff);  
    }

    // I would also need to filter out staff role Customer
    public List<String[]> ShowProfile(String filterRole, String[] ownProfile) {
        List<String[]> allData = returnAllDataFromFile(txt_len);
        List<String[]> filteredData = new ArrayList<>(txt_len);
        
        for(String[] row : allData){
            if(row.length == 9 && row[idx_id].equals(ownProfile[0])){
                continue;
            }
            
            if(row.length == 9 && row[idx_role].equals("Customer")){
                continue;
            }
            
            if (row.length == 9 && row[idx_status].equals("Active")) {
                // if "All" or matches the role
                if (filterRole.equalsIgnoreCase("All")
                        || row[idx_role].equals(filterRole)) {
                    filteredData.add(row);
                }
            } else if (row.length == 9 && filterRole.equalsIgnoreCase("Inactive")) {
                if (row[idx_status].equalsIgnoreCase("Inactive")) {
                    filteredData.add(row);
                }
            }
            
        }
        return filteredData;
    }

    public void EditProfile(String[] oldData, String[] newData) {
        Path staffData = Paths.get("src", "txt", "profile.txt");
        String oldId = oldData[0];
        try {
            // Remove Old Staff Data
            List<String> lines = Files.readAllLines(staffData);
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.trim().split(",", 8);
                if (parts.length == 8 && !parts[0].equals(oldId.trim())) {
                    updatedLines.add(line);
                }
            }

            Files.write(staffData, updatedLines);

            String id = newData[0];
            String newRole = newData[1];
            String newName = newData[2];
            String newPass = newData[3];
            String newGender = newData[4];
            String newEmail = newData[5];
            String newPhone = newData[6];
            String newAge = newData[7];

            List<String> linesToAdd = List.of(
                    "\n" + String.join(",",
                            id, // e.g., "M4"
                            newRole,
                            newName,
                            newPass, // Password
                            newGender,
                            newEmail,
                            newPhone,
                            newAge, // 8th field placeholder if needed
                            "Active"
                    )
            );
            Files.write(
                    staffData,
                    linesToAdd,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {
            System.err.println("Error reading profile.txt: " + e.getMessage());
        }
    }

    public void InactiveProfile(String[] staffProfile) {
        Path staffData = Paths.get("src", "txt", "profile.txt");
        String staffId = staffProfile[0];
        try {
            // Remove Old Staff Data
            List<String> lines = Files.readAllLines(staffData);
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.trim().split(",", 8);
                if (parts.length == 8 && !parts[0].equals(staffId.trim())) {
                    updatedLines.add(line);
                }
            }

            Files.write(staffData, updatedLines);

            String staffRole = staffProfile[1];
            String staffName = staffProfile[2];
            String staffPass = staffProfile[3];
            String staffGender = staffProfile[4];
            String staffEmail = staffProfile[5];
            String staffPhone = staffProfile[6];
            String staffAge = staffProfile[7];

            List<String> linesToAdd = List.of(
                    "\n" + String.join(",",
                            staffId, // e.g., "M4"
                            staffRole,
                            staffName,
                            staffPass, // Password
                            staffGender,
                            staffEmail,
                            staffPhone,
                            staffAge, // 8th field placeholder if needed
                            "Inactive"
                    )
            );
            Files.write(
                    staffData,
                    linesToAdd,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {
            System.err.println("Error reading profile.txt: " + e.getMessage());
        }
    }

    public String[] returnStaffProfile(String staffId) {
        Path staffData = Paths.get("src", "txt", "profile.txt");
        try {
            List<String> lines = Files.readAllLines(staffData);
            for (String line : lines) {
                String[] parts = line.trim().split(",", 9);
                if (parts.length == 9 && parts[0].equals(staffId)) {
                    return new String[]{parts[0], parts[2]};
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading profile.txt: " + e.getMessage());
        }
        return null; // or return new String[0]; if you prefer empty instead of null
    }

    public String[] returnCustomerProfile(String customerId) {
        Path staffData = Paths.get("src", "txt", "profile.txt");
        try {
            List<String> lines = Files.readAllLines(staffData);
            for (String line : lines) {
                String[] parts = line.trim().split(",", 9);
                if (parts.length == 9 && parts[0].equals(customerId)) {
                    return new String[]{parts[2], parts[5]};
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading profile.txt: " + e.getMessage());
        }
        return null; // or return new String[0]; if you prefer empty instead of null
    }

    public boolean isEmailEndsWith(String email, String emailEnds) {
        return email.endsWith(emailEnds);
    }

    public boolean isEmailUnique(String email) {
        Path staffData = Paths.get("src", "txt", "profile.txt");
        try {
            List<String> lines = Files.readAllLines(staffData);
            for (String line : lines) {
                String[] parts = line.trim().split(",", 9);
                if (parts.length == 9 && parts[5].equals(email)) {
                    return false;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading profile.txt: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean isPhoneUnique(String phone) {
        Path staffData = Paths.get("src", "txt", "profile.txt");
        try {
            List<String> lines = Files.readAllLines(staffData);
            for (String line : lines) {
                String[] parts = line.trim().split(",", 9);
                if (parts.length == 9 && parts[6].equals(phone)) {
                    return false;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading profile.txt: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean checkPhone(String phone) {
        return phone != null && phone.length() == 10 && phone.chars().allMatch(Character::isDigit);
    }

}