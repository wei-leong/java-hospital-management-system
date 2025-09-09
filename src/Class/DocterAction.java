package Class;

import java.io.*;
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
    List<String> availableDoctors = new ArrayList<>();

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    String chosenDate = dateFormat.format(selectedDate);  
    String chosenTime = selectedTime;                      

    try (BufferedReader br = new BufferedReader(new FileReader("D:\\USER BACKUP\\Documents\\NetBeansProjects\\JavaAssignment\\apu-medical-centre\\src\\txt\\doctor_worktime.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                String doctorId = parts[0].trim();
                String startTime = parts[1].trim();
                String endTime = parts[2].trim();

                // Check the choose time, who is free and in the work time
                if (chosenTime.compareTo(startTime) >= 0 && chosenTime.compareTo(endTime) <= 0) {
                    
                    // Check appointment.txt, make sure the docter doesn't have another appointment in the same time
                    boolean isBooked = false;
                    try (BufferedReader br2 = new BufferedReader(new FileReader("D:\\USER BACKUP\\Documents\\NetBeansProjects\\JavaAssignment\\apu-medical-centre\\src\\txt\\appointment.txt"))) {
                        String line2;
                        while ((line2 = br2.readLine()) != null) {
                            String[] apptParts = line2.split(",");
                            if (apptParts.length >= 4) {
                                String apptDoctor = apptParts[1].trim();
                                String apptDateTime = apptParts[3].trim(); // in txt file, that is the date&time convern data

                                // the data break to two part time & date
                                if (apptDoctor.equals(doctorId)) {
                                    String[] dtSplit = apptDateTime.split(" ");
                                    if (dtSplit.length == 2) {
                                        String apptDate = dtSplit[0];
                                        String apptTime = dtSplit[1];

                                        if (apptDate.equals(chosenDate) && apptTime.equals(chosenTime)) {
                                            isBooked = true;
                                            break;
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
        Set<String> activeDoctors = new HashSet<>();
        File file = new File("D:\\USER BACKUP\\Documents\\NetBeansProjects\\JavaAssignment\\apu-medical-centre\\src\\txt\\profile.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[2].equalsIgnoreCase("doctor") && parts[3].equalsIgnoreCase("active")) {
                    activeDoctors.add(parts[0]); 
                }
            }
        }
        return activeDoctors;
    }

    // read docter_worktime.txt to load the avalable time docter
    private static Set<String> loadDoctorWorktime(String date, String time) throws IOException {
    Set<String> worktimeDoctors = new HashSet<>();
    File file = new File("D:\\USER BACKUP\\Documents\\NetBeansProjects\\JavaAssignment\\apu-medical-centre\\src\\txt\\doctor_worktime.txt"); // 注意名字 docter vs doctor

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

    // read appointment.txt to find who the docter already have appointment base on the choose time
    private static Set<String> loadBusyDoctors(String dateTime) throws IOException {
        Set<String> busyDoctors = new HashSet<>();
        File file = new File("D:\\USER BACKUP\\Documents\\NetBeansProjects\\JavaAssignment\\apu-medical-centre\\src\\txt\\appointment.txt");

        if (!file.exists()) return busyDoctors;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String doctorId = parts[1];
                    String appointmentDateTime = parts[3];
                    if (appointmentDateTime.equals(dateTime)) {
                        busyDoctors.add(doctorId);
                    }
                }
            }
        }
        return busyDoctors;
    }

    // The function use to detact the choose time is on the docter work time start and end (if it out of the range of the docter work time,then no avalable docter)
    private static boolean isTimeWithinRange(String target, String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime targetTime = LocalTime.parse(target, formatter);
        LocalTime startTime = LocalTime.parse(start, formatter);
        LocalTime endTime = LocalTime.parse(end, formatter);

    return !targetTime.isBefore(startTime) && !targetTime.isAfter(endTime);
    }
}