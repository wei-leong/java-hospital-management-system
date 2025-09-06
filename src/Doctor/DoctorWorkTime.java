package Doctor;

import Class.FileActions;
import Class.Doctor;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Arrays;

public class DoctorWorkTime extends JPanel {

	// GUI component
	private JLabel titleLabel;
	private JLabel currentWorkingTimeLabel;
	private JLabel startTimeLabel;
	private JLabel endTimeLabel;
	private JTextField startTimeField;
	private JTextField endTimeField;
	private JButton editButton;
	private JButton saveButton;
	private JPanel inputPanel;

	private String doctorId;

	private Doctor doctor;

	private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("HHmm");
	private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	
	public DoctorWorkTime(String[] staffDetails, Doctor doctor) {
		this.doctorId = staffDetails[0];
		this.doctor = doctor;
		
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		titleLabel = new JLabel("Manage Your Working Time");
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		add(titleLabel, BorderLayout.NORTH);

		JPanel mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.CENTER;
		
		currentWorkingTimeLabel = new JLabel();
		currentWorkingTimeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		mainPanel.add(currentWorkingTimeLabel, gbc);

		updateCurrentTimeLabel();

		startTimeLabel = new JLabel("Start Time (HHmm):");
		startTimeField = new JTextField(10);
		endTimeLabel = new JLabel("End Time (HHmm):");
		endTimeField = new JTextField(10);
		
		inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
		inputPanel.setBackground(Color.WHITE);
		inputPanel.add(startTimeLabel);
		inputPanel.add(startTimeField);
		inputPanel.add(endTimeLabel);
		inputPanel.add(endTimeField);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		mainPanel.add(inputPanel, gbc);

		saveButton = new JButton("Save");
		saveButton.addActionListener(e -> saveWorkTime());

		gbc.gridy = 2;
		mainPanel.add(saveButton, gbc);

		add(mainPanel, BorderLayout.CENTER);
	}
	
	private void updateCurrentTimeLabel() {
		String[] workTime = doctor.getCurrentWorkTime();
		if (workTime != null) {
			currentWorkingTimeLabel.setText("Current Working Time: " + workTime[1] + " - " + workTime[2]);
		} else {
			currentWorkingTimeLabel.setText("Current Working Time: Not Set");
		}
	}

	private void saveWorkTime() {
		String newStartTimeInput = startTimeField.getText().trim();
		String newEndTimeInput = endTimeField.getText().trim();

		if (newStartTimeInput.isEmpty() || newEndTimeInput.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill in both start and end times.", "Missing Input", JOptionPane.WARNING_MESSAGE);
			return;
		}

		LocalTime startTime;
		LocalTime endTime;
		try {
			startTime = LocalTime.parse(newStartTimeInput, INPUT_FORMATTER);
			endTime = LocalTime.parse(newEndTimeInput, INPUT_FORMATTER);
		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(this,
				"Invalid time format. Please enter a 4-digit number (exp: 0930).",
				"Invalid Input", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
			JOptionPane.showMessageDialog(this,
				"End time must be after start time.",
				"Invalid Time Range", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String newStartTime = startTime.format(OUTPUT_FORMATTER);
		String newEndTime = endTime.format(OUTPUT_FORMATTER);

		doctor.updateWorkTime(newStartTime, newEndTime);
		
		JOptionPane.showMessageDialog(this, "Work time updated successfully!");
		updateCurrentTimeLabel();
	}
}