/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

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
public class ProfileActions {

    public void AddNewProfile(String newName, String newEmail, int newAge, String newRole, String newPhone, String id, String newGender) {
        Path file = Paths.get("src", "txt", "profile.txt");
        try {
            List<String> lines = Files.readAllLines(file);
            int maxId = 0;
            for (String line : lines) {
                String[] parts = line.trim().split(",", 8);
                String staffID = parts[0];
                if (parts.length == 8 && staffID.startsWith(id)) {
                    String numPart = staffID.substring(id.length()); // e.g. from M3 → "3"
                    try {
                        int num = Integer.parseInt(numPart); // convert "3" to 3
                        if (num > maxId) {
                            maxId = num; // keep the largest one found
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            String newId = id + (maxId + 1);

            List<String> linesToAdd = List.of(
                    "\n" + String.join(",",
                            newId, // e.g., "M4"
                            newRole,
                            newName,
                            newId + String.valueOf(newAge), // Password based on Phone Number and Name
                            newGender,
                            newEmail,
                            newPhone,
                            String.valueOf(newAge), // 8th field placeholder if needed
                            "Active"
                    )
            );
            Files.write(
                    file,
                    linesToAdd,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println("Error reading profile.txt: " + e.getMessage());
        }
    }

    // I would also need to filter out staff role Customer
    public List<String[]> ShowProfile(String filterRole, String[] ownProfile) {
        Path staffData = Paths.get("src", "txt", "profile.txt");
        List<String[]> results = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(staffData);
            for (String line : lines) {
                String[] parts = line.trim().split(",", 9);

                if (parts.length == 9 && parts[0].equals(ownProfile[0])) {
                    continue;
                }

                if (parts.length == 9 && parts[1].equals("Customer")) {
                    continue;
                }

                if (parts.length == 9 && parts[8].equals("Active")) {
                    // if "All" or matches the role
                    if (filterRole.equalsIgnoreCase("All")
                            || parts[1].equals(filterRole)) {
                        results.add(parts);
                    }
                } else if (parts.length == 9 && filterRole.equalsIgnoreCase("Inactive")) {
                    if (parts[8].equalsIgnoreCase("Inactive")) {
                        results.add(parts);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading profile.txt: " + e.getMessage());
        }
        return results;
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
    
    public boolean isEmailEndsWith(String email,String emailEnds){
        return email.endsWith(emailEnds);
    }
    
    public boolean isEmailUnique(String email){
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
    
    public boolean isPhoneUnique(String phone){
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
    
    public boolean checkPhone(String phone){
        return phone!= null && phone.length() == 10 && phone.chars().allMatch(Character::isDigit);
    }
}
