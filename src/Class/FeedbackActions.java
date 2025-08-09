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

    ProfileActions profileActions = new ProfileActions();

    int feedbackLen = 6;

    public int returnAverageRating(String staffId) {
        Path feedbackData = Paths.get("src", "txt", "feedback.txt");
        try {
            List<String> lines = Files.readAllLines(feedbackData);
            int totalRating = 0;
            int counts = 0;
            for (String line : lines) {
                String[] parts = line.trim().split(",", feedbackLen);

                if (parts.length == feedbackLen && parts[4].startsWith(staffId)) {
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

    public List<String[]> returnRatingList(String staffId) {
        Path feedbackData = Paths.get("src", "txt", "feedback.txt");
        List<String[]> results = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(feedbackData);
            for (String line : lines) {
                // feedback.txt fields: F1,C1,5,Great,D1,2025-08-02
                String[] parts = line.trim().split(",", 6);
                if (parts.length != 6) {
                    continue;
                }

                String customerId = parts[1];
                String rating = parts[2];
                String comments = parts[3];
                String staffOrDocId = parts[4];
                String date = parts[5];

                // skip non-matching unless we're in “All” mode
                if (!"All".equalsIgnoreCase(staffId) && !staffOrDocId.startsWith(staffId)) {
                    continue;
                }

                // look up customer
                String[] customerData = profileActions.returnCustomerProfile(customerId);
                if (customerData == null) {
                    continue;  // missing or malformed
                }
                // look up staff/doctor
                String[] staffData = profileActions.returnStaffProfile(staffOrDocId);
                if (staffData == null) {
                    continue;
                }

                String customerName = customerData[0];
                String customerEmail = customerData[1];
                String staffName = staffData[1];
                String staffIdUsed = staffData[0];

                results.add(new String[]{
                    customerName,
                    customerEmail,
                    date,
                    comments,
                    staffName,
                    staffIdUsed,
                    rating
                });
            }
        } catch (IOException e) {
            System.err.println("Error reading feedback.txt: " + e.getMessage());
        }
        return results;
    }
}
