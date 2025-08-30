package Doctor;

import Class.FileActions;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

	// data and file path
	private String doctorId;
	private static final String WORKTIME_FILE = "doctor_worktime.txt"; // **<-- Changed path**
	private static final int FILE_LENGTH = 3; // **<-- Added file length constant**

	// file actions class
	private FileActions workTimeFile; // **<-- Added instance of FileActions**

	// time formatter
	private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("HHmm");
	private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	
	// constructor for the DoctorWorkTime
	public DoctorWorkTime(String[] staffDetails) {
		this.doctorId = staffDetails[0];
		this.workTimeFile = new FileActions(WORKTIME_FILE); 
		
		// main content panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// title and info
		titleLabel = new JLabel("Your Working Time", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		currentWorkingTimeLabel = new JLabel("Loading...", SwingConstants.CENTER);
		currentWorkingTimeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		currentWorkingTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// edit button
		editButton = new JButton("Edit Working Time");
		editButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		// input panel for editing
		inputPanel = new JPanel();
		inputPanel.setLayout(new GridBagLayout());
		inputPanel.setBorder(BorderFactory.createTitledBorder("Edit Times"));
		inputPanel.setVisible(false); // hide it by default

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;

		startTimeLabel = new JLabel("Start Time (HHMM):");
		gbc.gridx = 0;
		gbc.gridy = 0;
		inputPanel.add(startTimeLabel, gbc);

		startTimeField = new JTextField(10);
		gbc.gridx = 1;
		gbc.gridy = 0;
		inputPanel.add(startTimeField, gbc);

		endTimeLabel = new JLabel("End Time (HHMM):");
		gbc.gridx = 0;
		gbc.gridy = 1;
		inputPanel.add(endTimeLabel, gbc);

		endTimeField = new JTextField(10);
		gbc.gridx = 1;
		gbc.gridy = 1;
		inputPanel.add(endTimeField, gbc);

		saveButton = new JButton("Save");
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		inputPanel.add(saveButton, gbc);

		// add components to the main panel
		mainPanel.add(titleLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPanel.add(currentWorkingTimeLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPanel.add(editButton);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPanel.add(inputPanel);

		add(mainPanel, BorderLayout.CENTER);

		// event listeners
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				inputPanel.setVisible(true);
				editButton.setEnabled(false);
				// prefill fields with current data if available
				String currentTime = getCurrentTime();
				if (!"Not Set".equals(currentTime)) {
					String[] times = currentTime.split(" - ");
					startTimeField.setText(times[0].replace(":", ""));
					endTimeField.setText(times[1].replace(":", ""));
				} else {
					startTimeField.setText("");
					endTimeField.setText("");
				}
			}
		});

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveWorkingTime();
			}
		});

		// load on startup
		updateWorkingTimeDisplay();
	}


	// retrieve the current working time from the text file
	private void updateWorkingTimeDisplay() {
		String currentTime = getCurrentTime();
		if (currentTime.equals("Not Set")) {
			currentWorkingTimeLabel.setText("You have not set your working time yet.");
		} else {
			currentWorkingTimeLabel.setText("Your current working time is: " + currentTime);
		}
	}

	// read the "doctor_worktime.txt" file and returns the working time for the current doctor
	// @return: a string representing the working time
	private String getCurrentTime() {
		List<String[]> allData = workTimeFile.returnAllDataFromFile(FILE_LENGTH);
		for (String[] parts : allData) {
			if (parts.length >= 3 && parts[0].trim().equals(doctorId)) {
				String startTime = parts[1].trim();
				String endTime = parts[2].trim();
				if ("null".equalsIgnoreCase(startTime) || "null".equalsIgnoreCase(endTime)) {
					return "Not Set";
				}
				return startTime + " - " + endTime;
			}
		}
		return "Not Set";
	}


	// save the new working time to the "doctor_worktime.txt" file
	private void saveWorkingTime() {
		String newStartTimeInput = startTimeField.getText().trim();
		String newEndTimeInput = endTimeField.getText().trim();

		// validate and parse the time using java.time.LocalTime
		LocalTime startTime;
		LocalTime endTime;
		try {
			startTime = LocalTime.parse(newStartTimeInput, INPUT_FORMATTER);
			endTime = LocalTime.parse(newEndTimeInput, INPUT_FORMATTER);
		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(this,
				"Invalid time format. Please enter a 4-digit number (e.g., 0930).",
				"Invalid Input", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// check if end time is after start time
		if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
			JOptionPane.showMessageDialog(this,
				"End time must be after start time.",
				"Invalid Time Range", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// format the time for saving in the file
		String newStartTime = startTime.format(OUTPUT_FORMATTER);
		String newEndTime = endTime.format(OUTPUT_FORMATTER);

		String[] newData = {doctorId, newStartTime, newEndTime};

		// read all lines to find the doctor record
		boolean found = false;
		List<String[]> allData = workTimeFile.returnAllDataFromFile(FILE_LENGTH);
		for (String[] parts : allData) {
			if (parts.length >= FILE_LENGTH && parts[0].trim().equals(doctorId)) {
				workTimeFile.editRowFromFile(FILE_LENGTH, parts, newData);
				found = true;
				break;
			}
		}

		if (!found) {
			workTimeFile.addRowToFile(newData);
		}

		JOptionPane.showMessageDialog(this, "Working time updated successfully!");

		// update display and hide input panel
		updateWorkingTimeDisplay();
		inputPanel.setVisible(false);
		editButton.setEnabled(true);
	}
}
