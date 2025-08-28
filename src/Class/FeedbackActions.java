/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wlhoe
 */
public class FeedbackActions extends FileActions {

    public FeedbackActions() {
        super("feedback.txt");
    }

    ProfileActions profileActions = new ProfileActions();
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
        List<String[]> allData = returnAllDataFromFile(txt_len);
        List<String[]> results = new ArrayList<>();

        for (String[] row : allData) {
            // skip non-matching unless we're in “All” mode
            if (!"All".equalsIgnoreCase(staffId) && !row[idx_staffId].startsWith(staffId)) {
                continue;
            }

            // look up customer
            String[] customerData = profileActions.returnCustomerProfile(row[idx_cusId]);
            if (customerData == null) {
                continue;  // missing or malformed
            }

            // look up staff/doctor
            String[] staffData = profileActions.returnStaffProfile(row[idx_staffId]);
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
                row[idx_date],
                row[idx_feedback],
                staffName,
                staffIdUsed,
                row[idx_rating]
            });
        }

        return results;
    }

    // Modify here
    public List<String[]> returnAverageRatingList(String staffRole) {
        List<String[]> staffData = profileActions.ReturnAllStaffData();
        List<String[]> results = new ArrayList<>();

        for (String[] row : staffData) {
            if (row.length == 9 && row[1].equalsIgnoreCase(staffRole) && row[8].equals("Active")) {
                String avgStr = String.format("%.2f", returnAverageStaffRating(row[0]));
                results.add(new String[]{
                    row[0], // staff id
                    row[2], // staff name
                    avgStr,});
            }
        }
        return results;
    }

    public double returnAverageStaffRating(String staffId) {
        List<String[]> allData = returnAllDataFromFile(txt_len);
        double totalRating = 0;
        int count = 0;
        for (String[] row : allData) {
            if (row.length == txt_len && row[4].trim().equalsIgnoreCase(staffId.trim())) {
                totalRating += Double.parseDouble(row[2]);
                count += 1;
            }
        }
        if (count == 0) {
            return 0.0;
        }
        return totalRating / count;
    }
}
