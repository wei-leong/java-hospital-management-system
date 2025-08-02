/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wlhoe
 */
public class FeedbackActions {
    public int returnAverageRating(String staffId){
        Path feedbackData = Paths.get("src", "txt", "feedback.txt");
        try {
            List<String> lines = Files.readAllLines(feedbackData);
            int totalRating = 0;
            int counts = 0;
            for (String line : lines) {
                String[] parts = line.trim().split(",", 5);
                
                if(parts.length == 5 && parts[4].startsWith(staffId)){
                    totalRating += Integer.parseInt(parts[2]);
                    counts++;
                }
                
            }
            
            return totalRating / counts;
        } catch (IOException e) {
            System.err.println("Error reading profile.txt: " + e.getMessage());
            return 0;
        }
    }
}
