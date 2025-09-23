package Class;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class DocterAction {
    // Bring date & time to fillter the available docter name
    public static List<String> getAvailableDoctors(Date selectedDate, String selectedTime) {
        Path worktimePath = Paths.get("src", "txt", "doctor_worktime.txt");
        Path appointmentPath = Paths.get("src", "txt", "appointment.txt");
        List<String> availableDoctors = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String chosenDate = dateFormat.format(selectedDate);
        String chosenTime = selectedTime;

        try (BufferedReader br = new BufferedReader(new FileReader(worktimePath.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String doctorId = parts[0].trim();
                    String startTime = parts[1].trim();
                    String endTime = parts[2].trim();

                    // Check if the chosen time is within the doctor's work time
                    if (chosenTime.compareTo(startTime) >= 0 && chosenTime.compareTo(endTime) <= 0) {
                        boolean isBooked = false;
                        // Check appointment.txt to ensure the doctor doesn't have an appointment at the same date and time
                        try (BufferedReader br2 = new BufferedReader(new FileReader(appointmentPath.toFile()))) {
                            String line2;
                            while ((line2 = br2.readLine()) != null) {
                                String[] apptParts = line2.split(",");
                                if (apptParts.length >= 8) {
                                    String apptDoctor = apptParts[1].trim();
                                    String apptDateTime = apptParts[4].trim();
                                    String status = apptParts[5].trim();

                                    // Check if the doctor ID matches and the appointment is ongoing
                                    if (apptDoctor.equals(doctorId) && status.equalsIgnoreCase("ongoing")) {
                                        String[] dtSplit = apptDateTime.split(" ");
                                        if (dtSplit.length == 2) {
                                            String apptDate = dtSplit[0];
                                            String apptTime = dtSplit[1];

                                            // Here's the fix: Check for both matching date and time
                                            if (apptDate.equals(chosenDate) && apptTime.equals(chosenTime)) {
                                                isBooked = true;
                                                break; // Found an ongoing appointment for this doctor at this time, so break the inner loop
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (!isBooked) {
                            availableDoctors.add(doctorId);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return availableDoctors;
}



    // read profile.txt to find the docter
    private static Set<String> loadActiveDoctors() throws IOException {
        Path profilePath = Paths.get("src", "txt", "profile.txt");
        Set<String> activeDoctors = new HashSet<>();
        File file = profilePath.toFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && parts[2].equalsIgnoreCase("doctor") && parts[8].equalsIgnoreCase("active")) {
                    activeDoctors.add(parts[0]); 
                }
            }
        }
        return activeDoctors;
    }

    // read docter_worktime.txt to load the avalable time docter
    private static Set<String> loadDoctorWorktime(String date, String time) throws IOException {
    Path worktimePath = Paths.get("src", "txt", "doctor_worktime.txt");
    Set<String> worktimeDoctors = new HashSet<>();
    File file = worktimePath.toFile(); 

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                String doctorId = parts[0];
                String startTime = parts[1];
                String endTime = parts[2];

                if (isTimeWithinRange(time, startTime, endTime)) {
                    worktimeDoctors.add(doctorId);
                }
            }
        }
    }
    return worktimeDoctors;
}

    // The function use to detact the choose time is on the docter work time start and end (if it out of the range of the docter work time,then no avalable docter)
    private static boolean isTimeWithinRange(String target, String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime targetTime = LocalTime.parse(target, formatter);
        LocalTime startTime = LocalTime.parse(start, formatter);
        LocalTime endTime = LocalTime.parse(end, formatter);

    return !targetTime.isBefore(startTime) && !targetTime.isAfter(endTime);
    }
    
        //This function is use to check the docter have the ongoing status appointment on the selected time and date or not
        public static boolean hasOngoingAppointment(String doctorId, Date selectedDate, String selectedTime) {
        Path appointmentPath = Paths.get("src", "txt", "appointment.txt");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String chosenDate = dateFormat.format(selectedDate);
        String chosenTime = selectedTime;

        try (BufferedReader br = new BufferedReader(new FileReader(appointmentPath.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] apptParts = line.split(",");
                if (apptParts.length >= 8) {
                    String apptDoctor = apptParts[1].trim();
                    String apptDateTime = apptParts[4].trim();
                    String status = apptParts[5].trim();
                    
                    if (apptDoctor.equals(doctorId) && status.equalsIgnoreCase("ongoing")) {
                        String[] dtSplit = apptDateTime.split(" ");
                        if (dtSplit.length == 2) {
                            String apptDate = dtSplit[0].trim();
                            String apptTime = dtSplit[1].trim();

                            if (apptDate.equals(chosenDate) && apptTime.equals(chosenTime)) {
                                return true; 
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; 
    }
}
