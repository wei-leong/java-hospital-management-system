package Customer;

import Class.FileActions;
import Class.IDGenerator;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ProvideComments extends JPanel {

    private final String customerId;
    private final JComboBox<String> appointmentDropdown;
    private final JComboBox<Integer> ratingDropdown;
    private final JComboBox<String> targetDropdown; // NEW: Feedback target
    private final JTextArea messageArea;
    private final JButton submitButton;

    private final FileActions appointmentFile;
    private final FileActions feedbackFile;

    public ProvideComments(String customerId) {
        this.customerId = customerId;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("Provide Feedback", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Panel for form fields
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        formPanel.setBackground(Color.WHITE);

        appointmentFile = new FileActions("appointment.txt");
        feedbackFile = new FileActions("feedback.txt");

        // Dropdown: Appointments for this customer
        appointmentDropdown = new JComboBox<>();
        loadAppointments();

        // Dropdown: Rating 1–5
        ratingDropdown = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});

        // Dropdown: Feedback target (Doctor/Staff)
        targetDropdown = new JComboBox<>(new String[]{"Doctor", "Staff"});

        // Text area: Message
        messageArea = new JTextArea(3, 20);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        // Add form fields
        formPanel.add(new JLabel("Select Appointment:"));
        formPanel.add(appointmentDropdown);

        formPanel.add(new JLabel("Feedback For:"));
        formPanel.add(targetDropdown);

        formPanel.add(new JLabel("Rating (1-5):"));
        formPanel.add(ratingDropdown);

        formPanel.add(new JLabel("Message:"));
        formPanel.add(new JScrollPane(messageArea));

        add(formPanel, BorderLayout.CENTER);

        // Submit button
        submitButton = new JButton("Submit Feedback");
        submitButton.addActionListener(e -> submitFeedback());
        add(submitButton, BorderLayout.SOUTH);
    }

    private void loadAppointments() {
        List<String[]> appointments = appointmentFile.returnAllDataFromFile(8); // appointment now has 8 fields

        // Only show appointments belonging to this customer
        List<String[]> customerAppointments = appointments.stream()
                .filter(appt -> appt[3].equals(customerId)) // index 3 = customer id
                .collect(Collectors.toList());

        for (String[] appt : customerAppointments) {
            // Show "AppointmentID (DoctorID/StaffID - Date)"
            String display = appt[0] + " (Doctor: " + appt[1] + ", Staff: " + appt[2] + " - " + appt[4] + ")";
            appointmentDropdown.addItem(display);
        }
    }

    private void submitFeedback() {
        if (appointmentDropdown.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select an appointment.");
            return;
        }

        String selectedAppt = (String) appointmentDropdown.getSelectedItem();
        String apptId = selectedAppt.split(" ")[0]; // extract appointment ID

        // Retrieve appointment data for the selected ID
        List<String[]> appointments = appointmentFile.returnAllDataFromFile(8);
        String[] selectedApptRow = appointments.stream()
                .filter(appt -> appt[0].equals(apptId))
                .findFirst()
                .orElse(null);

        if (selectedApptRow == null) {
            JOptionPane.showMessageDialog(this, "Appointment data not found.");
            return;
        }

        // Choose Doctor or Staff based on dropdown
        String target = (String) targetDropdown.getSelectedItem();
        String targetId = target.equals("Doctor") ? selectedApptRow[1] : selectedApptRow[2];

        int rating = (int) ratingDropdown.getSelectedItem();
        String message = messageArea.getText().trim();
        String date = LocalDate.now().toString();

        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a feedback message.");
            return;
        }

        // Generate feedback ID
        IDGenerator idGen = new IDGenerator("F", "feedback.txt");
        String feedbackId = idGen.generateNextId();

        // Save: [feedback id], [customer id], [rating], [message], [staff/doctor id], [date]
        String[] newFeedback = {feedbackId, customerId, String.valueOf(rating), message, targetId, date};
        feedbackFile.addRowToFile(newFeedback);

        JOptionPane.showMessageDialog(this, "Feedback submitted successfully for " + target + "!");
        messageArea.setText(""); // clear message after submission
    }
}
