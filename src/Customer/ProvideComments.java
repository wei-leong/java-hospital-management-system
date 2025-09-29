package Customer;

import Class.CheckInput;
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
    private final JComboBox<String> targetDropdown;
    private final JTextField messageField; // CHANGED: JTextArea -> JTextField
    private final JButton submitButton;

    private final FileActions appointmentFile;
    private final FileActions feedbackFile;

    public ProvideComments(String customerId) {
        this.customerId = customerId;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Provide Feedback", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        formPanel.setBackground(Color.WHITE);

        appointmentFile = new FileActions("appointment.txt");
        feedbackFile = new FileActions("feedback.txt");

        appointmentDropdown = new JComboBox<>();
        loadAppointments();

        ratingDropdown = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        targetDropdown = new JComboBox<>(new String[]{"Doctor", "Staff"});

        // CHANGED: Single-line input instead of multi-line
        messageField = new JTextField();

        formPanel.add(new JLabel("Select Appointment:"));
        formPanel.add(appointmentDropdown);

        formPanel.add(new JLabel("Feedback For:"));
        formPanel.add(targetDropdown);

        formPanel.add(new JLabel("Rating (1-5):"));
        formPanel.add(ratingDropdown);

        formPanel.add(new JLabel("Message:"));
        formPanel.add(messageField);

        add(formPanel, BorderLayout.CENTER);

        submitButton = new JButton("Submit Feedback");
        submitButton.addActionListener(e -> submitFeedback());
        add(submitButton, BorderLayout.SOUTH);
    }

    private void loadAppointments() {
        List<String[]> appointments = appointmentFile.returnAllDataFromFile(8);

        List<String[]> customerAppointments = appointments.stream()
                .filter(appt -> appt[3].equals(customerId))
                .collect(Collectors.toList());

        for (String[] appt : customerAppointments) {
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
        String apptId = selectedAppt.split(" ")[0];

        List<String[]> appointments = appointmentFile.returnAllDataFromFile(8);
        String[] selectedApptRow = appointments.stream()
                .filter(appt -> appt[0].equals(apptId))
                .findFirst()
                .orElse(null);

        if (selectedApptRow == null) {
            JOptionPane.showMessageDialog(this, "Appointment data not found.");
            return;
        }

        String target = (String) targetDropdown.getSelectedItem();
        String targetId = target.equals("Doctor") ? selectedApptRow[1] : selectedApptRow[2];

        int rating = (int) ratingDropdown.getSelectedItem();
        String message = messageField.getText().trim();
        String date = LocalDate.now().toString();

        CheckInput checker = new CheckInput();

        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a feedback message.");
            return;
        }

        if (checker.checkComma(message)) {
            JOptionPane.showMessageDialog(this, "Message cannot contain commas (,).");
            return;
        }

        IDGenerator idGen = new IDGenerator("F", "feedback.txt");
        String feedbackId = idGen.generateNextId();

        if (!checker.checkUnique(feedbackId, "feedback.txt", 6, 0)) {
            JOptionPane.showMessageDialog(this, "Generated feedback ID already exists. Please try again.");
            return;
        }

        String[] newFeedback = {feedbackId, customerId, String.valueOf(rating), message, targetId, date};
        feedbackFile.addRowToFile(newFeedback);

        JOptionPane.showMessageDialog(this, "Feedback submitted successfully for " + target + "!");
        messageField.setText("");
    }
}
