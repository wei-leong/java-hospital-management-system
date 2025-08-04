/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import Class.FeedbackActions;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wlhoe
 */
public class Manager extends Person {

    private final ProfileActions profileHelper = new ProfileActions();
    private final FeedbackActions feedbackHelper = new FeedbackActions();
    private final String[] _ownProfile;

    public Manager(String email, String password) {
        super(email, password);
        this._ownProfile = null;
    }

    public Manager() {
        this._ownProfile = null;
    }

    ;
    
    public Manager(String[] ownProfile) {
        this._ownProfile = ownProfile;
    }

    public void addStaff(String name, String email, int age, String role, String phone, String idPrefix, String gender) {
        profileHelper.AddNewProfile(name, email, age, role, phone, idPrefix, gender);
    }

    public List<String[]> returnStaffData(String filterRole) {
        return profileHelper.ShowProfile(filterRole, _ownProfile);
    }

    public void editStaff(String[] oldData, String[] newData) {
        profileHelper.EditProfile(oldData, newData);
    }

    public void InactiveStaff(String[] staffDetails) {
        profileHelper.InactiveProfile(staffDetails);
    }

    public int FeedbackSummary(String staffRole) {
        return feedbackHelper.returnAverageRating(staffRole);
    }

    public List<String[]> returnFeedbackList(String staffId) {
        return feedbackHelper.returnRatingList(staffId);
    }

    public List<String[]> returnAppointmentsList(String range) {
        Path appointmentData = Paths.get("src", "txt", "appointment.txt");
        List<String[]> results = new ArrayList<>();

        // DateTime Format
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Get Current Time
        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime startWindow, endWindow;

        switch (range) {
            case "Today":
                startWindow = currentTime.toLocalDate().atStartOfDay();
                endWindow = startWindow.plusDays(1);

                break;
            case "This Week":
                // Week starts on Monday
                LocalDate mon = currentTime.toLocalDate().with(DayOfWeek.MONDAY);
                startWindow = mon.atStartOfDay();
                endWindow = startWindow.plusWeeks(1);
                break;
            case "This Month":
                LocalDate thisMonth = currentTime.toLocalDate().withDayOfMonth(1);
                startWindow = thisMonth.atStartOfDay();
                endWindow = startWindow.plusMonths(1);
                break;
            case "This Year":
                LocalDate thisYear = currentTime.toLocalDate().withDayOfYear(1);
                startWindow = thisYear.atStartOfDay();
                endWindow = startWindow.plusYears(1);
                break;
            default:
                startWindow = currentTime.toLocalDate().atStartOfDay();
                endWindow = startWindow.plusDays(1);
                break;
        }

        try {
            List<String> lines = Files.readAllLines(appointmentData);
            for (String line : lines) {
                String[] parts = line.trim().split(",", 10);
                LocalDateTime appointment = LocalDateTime.parse(parts[3], dateFormat);
                if (parts.length == 10 && !appointment.isBefore(startWindow) && appointment.isBefore(endWindow)) {
                    results.add(new String[]{
                        parts[0], // appointment ID
                        parts[1], // doctorId
                        parts[2], // customerId
                        parts[3], // start
                        parts[4], // end
                        parts[5] // status
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading appointment.txt: " + e.getMessage());
        }
        return results;
    }
}
