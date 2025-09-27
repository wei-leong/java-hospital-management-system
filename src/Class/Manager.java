/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import Class.ProfileActions;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Wlhoe
 */
public class Manager extends Person {

    // Composition  -> Manager has ProfileAction, FeedbackAction and RevenueActions
    private final ProfileActions profileHelper = new ProfileActions();
    private final FeedbackActions feedbackHelper = new FeedbackActions();
    private final RevenueActions revenueHelper = new RevenueActions();
    private final String[] _ownProfile;

    // Polymorphism ( Constructor Overloading ) 
    public Manager(String email, String password) {
        super(email, password);
        this._ownProfile = null;
    }

    public Manager() {
        this._ownProfile = null;
    }

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

    public void InactiveStaff(String[] oldData, String[] staffDetails) {
        profileHelper.InactiveProfile(oldData, staffDetails);
    }

    public double FeedbackSummary(String staffRole) {
        return feedbackHelper.returnAverageRating(staffRole);
    }

    public List<String[]> returnFeedbackList(String staffId) {
        return feedbackHelper.returnRatingList(staffId);
    }

    public boolean isEmailEnds(String email, String endsWith) {
        return profileHelper.isEmailEndsWith(email, endsWith);
    }

    public boolean isEmailUnique(String email) {
        return profileHelper.isEmailUnique(email);
    }

    public boolean isPhoneUnique(String phone) {
        return profileHelper.isPhoneUnique(phone);
    }

    public boolean isPhoneValid(String phone) {
        return profileHelper.checkPhoneLength(phone);
    }
    
    public boolean isPhoneDigit(String phone){
        return profileHelper.checkPhoneDigit(phone);
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
                String[] parts = line.trim().split(",", 7);
                LocalDateTime appointment = LocalDateTime.parse(parts[3], dateFormat);
                if (parts.length == 7 && !appointment.isBefore(startWindow) && appointment.isBefore(endWindow)) {
                    results.add(new String[]{
                        parts[0], // appointment ID
                        parts[1], // doctorId
                        parts[2], // customerId
                        parts[3], // start
                        parts[4] // status
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading appointment.txt: " + e.getMessage());
        }
        
        Collections.reverse(results);
        return results;
    }

    public int returnTotalAppointment(String range) {
        Path appointmentData = Paths.get("src", "txt", "appointment.txt");
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
            int totalAppointments = 0;
            for (String line : lines) {
                String[] parts = line.trim().split(",", 7);
                LocalDateTime appointment = LocalDateTime.parse(parts[3], dateFormat);
                if (parts.length == 7 && !appointment.isBefore(startWindow) && appointment.isBefore(endWindow) && parts[4].equals("complete")) {
                    totalAppointments += 1;
                }
            }
            return totalAppointments;
        } catch (Exception e) {
            System.err.println("Error reading appointment.txt: " + e.getMessage());
            return 0;
        }
    }

    public List<String[]> returnDoctorRankingList(String range) {
        List<String[]> staffData = profileHelper.ReturnAllStaffData();
        List<String[]> results = new ArrayList<>();

        for (String[] row : staffData) {
            if (row.length == 9 && row[1].equalsIgnoreCase("Doctor") && row[8].equals("Active")) {
                String countStr = Integer.toString(returnDoctorTotalAppointment(range, row[0]));
                results.add(new String[]{
                    row[0], // staff id
                    row[2], // staff name
                    countStr,});
            }
        }

        int n = results.size(); // Return how many columns results have
        for (int i = 0; i < n - 1; i++) {
            int maxIdx = i;
            for (int j = i + 1; j < n; j++) {
                int valJ = Integer.parseInt(results.get(j)[2]);
                int valMax = Integer.parseInt(results.get(maxIdx)[2]);
                if (valJ > valMax) {
                    maxIdx = j;
                }
            }
            // swap results[i] and results[maxIdx] if needed
            if (maxIdx != i) {
                String[] tmp = results.get(i);
                results.set(i, results.get(maxIdx));
                results.set(maxIdx, tmp);
            }
        }
        return results;
    }

    public int returnDoctorTotalAppointment(String range, String doctorId) {
        Path appointmentData = Paths.get("src", "txt", "appointment.txt");
        // DateTime Format
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Get Current Time
        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime startWindow, endWindow;

        switch (range) {
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
            int totalAppointments = 0;
            for (String line : lines) {
                String[] parts = line.trim().split(",", 7);
                LocalDateTime appointment = LocalDateTime.parse(parts[3], dateFormat);
                if (parts.length == 7 && !appointment.isBefore(startWindow) && appointment.isBefore(endWindow) && parts[4].equals("complete") && parts[1].equals(doctorId)) {
                    totalAppointments += 1;
                }
            }
            return totalAppointments;
        } catch (Exception e) {
            System.err.println("Error reading appointment.txt: " + e.getMessage());
            return 0;
        }
    }

    public List<String[]> returnAverageRatingList(String staffRole) {
        return feedbackHelper.returnAverageRatingList(staffRole);
    }

    public double[] returnMonthlyRevenue(int year) {
        return revenueHelper.returnMonthlyRevenue(year);
    }

    public double[] returnYearsRevenue(int anchorYear) {
        return revenueHelper.returnYearsRevenue(anchorYear);
    }

    public int returnCustomerAverageAge() {
        return profileHelper.returnCustomerAverageAge();
    }

}
