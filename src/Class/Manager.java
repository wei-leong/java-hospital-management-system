/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import Class.FeedbackActions;
import java.io.IOException;
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
public class Manager extends Person{

    private final ProfileActions profileHelper = new ProfileActions();
    private final FeedbackActions feedbackHelper = new FeedbackActions();
    private final RevenueActions revenueHelper = new RevenueActions();
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

    public void InactiveStaff(String[] oldData,String[] staffDetails) {
        profileHelper.InactiveProfile(oldData,staffDetails);
    }

    public int FeedbackSummary(String staffRole) {
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
        return profileHelper.checkPhone(phone);
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

    public List<String[]> returnAverageRatingList(String staffRole) {
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

    public double[] returnMonthlyRevenue(int year) {
        return revenueHelper.returnMonthlyRevenue(year);
    }

    public double[] returnYearsRevenue(int anchorYear) {
        int span = 10;
        int startYear = anchorYear - (span - 1);  // e.g. 2025 - 9 = 2016
        double[] totals = new double[span];
        Path paymentData = Paths.get("src", "txt", "payment.txt");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            for (String line : Files.readAllLines(paymentData)) {
                String[] parts = line.trim().split(",", 4);
                double amount = Double.parseDouble(parts[1]);
                LocalDate date = LocalDate.parse(parts[2], df);
                int y = date.getYear();
                int idx = y - startYear;      // 2016→0, 2017→1, …, 2025→9
                if (0 <= idx && idx < span) {
                    totals[idx] += amount;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading payment.txt: " + e.getMessage());
        }
        return totals;
    }
}
