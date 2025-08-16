/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wlhoe
 */
public class AverageRatingActions extends FileActions {
    
    private static final int idx_id = 0;
    private static final int idx_ = 1;
    private static final int idx_date = 2;
    private static final int idx_status = 3;
    private static final int txt_len = 4;

    public AverageRatingActions() {
        super("feedback.txt");
    }

    public List<String[]> returnAverageRatingList(String staffRole) {
        
        List<String[]> allData = returnAllDataFromFile(txt_len);
        
        Path staffData = Paths.get("src", "txt", "profile.txt");
        List<String[]> results = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(staffData);
            for (String line : lines) {
                String[] parts = line.trim().split(",", 9);
                if (parts.length == 9 && parts[1].equalsIgnoreCase(staffRole) && parts[8].equals("Active")) {
                    String avgStr = String.format("%.2f", returnAverageRating(parts[0]));
                    results.add(new String[]{
                        parts[0], // appointment ID
                        parts[2], // doctorId
                        avgStr,});
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading appointment.txt: " + e.getMessage());
        }
        return results;
    }

    public double returnAverageRating(String staffId) {
        Path feedbackData = Paths.get("src", "txt", "feedback.txt");
        double totalRating = 0;
        int count = 0;
        try {
            List<String> lines = Files.readAllLines(feedbackData);
            for (String line : lines) {
                String[] parts = line.trim().split(",", 6);
                if (parts.length == 6 && parts[4].equalsIgnoreCase(staffId)) {
                    totalRating += Double.parseDouble(parts[2]);
                    count += 1;
                }
            }
            if (count == 0) {
                return 0.0;
            }
            return totalRating / count;
        } catch (Exception e) {
            System.err.println("Error reading appointment.txt: " + e.getMessage());
            return 0.0;
        }
    }
}
