package Class;

import java.util.Date;

public class Appointment {
    private String doctorId;
    private Date date;
    private String time;

    // Getter 和 Setter
    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //combine time and date
    public java.time.LocalDateTime getCombinedDateTime() {
        java.time.LocalDate selectedLocalDate = date.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();

        String[] timeParts = time.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        return selectedLocalDate.atTime(hour, minute);
    }

}
