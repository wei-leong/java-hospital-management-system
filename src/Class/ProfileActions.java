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
                            String.valueOf(newAge) // 8th field placeholder if needed
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
    
    public List<String[]> ShowProfile(String filterRole){
        Path staffData = Paths.get("src","txt", "profile.txt");
        List<String[]> results = new ArrayList<>();
        try{
            List<String> lines = Files.readAllLines(staffData);
            for(String line : lines){
                String[] parts = line.trim().split(",",8);
                if (parts.length == 8) {
                    // if "All" or matches the role
                    if (filterRole.equalsIgnoreCase("All")
                     || parts[1].equals(filterRole)) {
                        results.add(parts);
                    }
                }
            }
        } catch(IOException e){
            System.err.println("Error reading profile.txt: " + e.getMessage());
        }
        return results;
    }
}
