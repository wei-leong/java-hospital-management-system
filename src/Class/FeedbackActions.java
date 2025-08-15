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
public class FeedbackActions extends ProfileActions {

//    ProfileActions profileActions = new ProfileActions();
//    int feedbackLen = 6;
    private static final int idx_id = 0;
    private static final int idx_cusId = 1;
    private static final int idx_rating = 2;
    private static final int idx_feedback = 3;
    private static final int idx_staffId = 4;
    private static final int idx_date = 5;
    private static final int txt_len = 6;

    public int returnAverageRating(String staffId) {
        List<String[]> allData = returnAllDataFromFile(txt_len);
        int totalRating = 0;
        int counts = 0;
        for (String[] row : allData) {
            if (row.length == txt_len && row[4].startsWith(staffId)) {
                totalRating += Integer.parseInt(row[idx_rating]);
                counts++;
            }
        }
        return counts == 0 ? 0 : totalRating / counts;
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
