package Doctor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A Java Swing application for doctors to manage their working time. This class
 * provides a GUI where a doctor can view, set, and edit their start and end
 * working hours. It simulates a login system by accepting a doctor's ID and
 * name as an array parameter.
 * <p>
 * The application reads and writes working time data to a text file named
 * "doctor_worktime.txt". It handles cases where a doctor's record does not
 * exist or has "null" working hours.
 */
public class DoctorWorkTime extends JPanel {

	// --- GUI Components ---
	private JLabel titleLabel;
	private JLabel currentWorkingTimeLabel;
	private JLabel startTimeLabel;
	private JLabel endTimeLabel;
	private JTextField startTimeField;
	private JTextField endTimeField;
	private JButton editButton;
	private JButton saveButton;
	private JPanel inputPanel;

	// --- Data and File Paths ---
	private String doctorId;
	private String doctorName;
	private static final String WORKTIME_FILE = "src/txt/doctor_worktime.txt";
	private static final String PROFILE_FILE = "src/txt/profile.txt";

	// --- Time Formatters ---
	private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("HHmm");
	private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	/**
	 * Constructor for the DoctorWorkTime.
	 *
	 * @param userData An array of strings containing user information, expected
	 * to be [profileId, role, name, ...].
	 */
	public DoctorWorkTime(String[] userData) {
		this.doctorId = userData[0];
		this.doctorName = userData[2];

		// --- Frame Setup ---
//		setTitle("Manage Working Time - Dr. " + this.doctorName);
//		setSize(500, 300);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setLocationRelativeTo(null); // Center the frame
//		setLayout(new BorderLayout(10, 10)); // Use BorderLayout for a clean layout
		// --- Main Content Panel ---
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Title and Info
		titleLabel = new JLabel("Your Working Time", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		currentWorkingTimeLabel = new JLabel("Loading...", SwingConstants.CENTER);
		currentWorkingTimeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		currentWorkingTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Edit Button
		editButton = new JButton("Edit Working Time");
		editButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		// --- Input Panel for Editing ---
		inputPanel = new JPanel();
		inputPanel.setLayout(new GridBagLayout());
		inputPanel.setBorder(BorderFactory.createTitledBorder("Edit Times"));
		inputPanel.setVisible(false); // Hidden by default

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5); // Padding
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

		// Add components to the main panel
		mainPanel.add(titleLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPanel.add(currentWorkingTimeLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPanel.add(editButton);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPanel.add(inputPanel);

		// Add main panel to the frame
		add(mainPanel, BorderLayout.CENTER);

		// --- Event Listeners ---
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				inputPanel.setVisible(true);
				editButton.setEnabled(false);
				// Pre-fill fields with current data if available
				String currentTime = getCurrentTime();
				if (!"Not Set".equals(currentTime)) {
					String[] times = currentTime.split(" - ");
					startTimeField.setText(times[0].replace(":", ""));
					endTimeField.setText(times[1].replace(":", ""));
				} else {
					startTimeField.setText("");
					endTimeField.setText("");
				}
//				pack(); // Resize frame to fit new components
			}
		});

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveWorkingTime();
			}
		});

		// Initial data load on startup
		updateWorkingTimeDisplay();
	}

	/**
	 * Retrieves the current working time for the doctor from the text file.
	 * Handles cases where the record is not found or is "null".
	 */
	private void updateWorkingTimeDisplay() {
		String currentTime = getCurrentTime();
		if (currentTime.equals("Not Set")) {
			currentWorkingTimeLabel.setText("You have not set your working time yet.");
		} else {
			currentWorkingTimeLabel.setText("Your current working time is: " + currentTime);
		}
	}

	/**
	 * Reads the doctor_worktime.txt file and returns the working time for the
	 * current doctor.
	 *
	 * @return A string representing the working time (e.g., "09:00 - 17:00") or
	 * "Not Set" if not found or invalid.
	 */
	private String getCurrentTime() {
		try (BufferedReader br = new BufferedReader(new FileReader(WORKTIME_FILE))) {
			String line;
			while ((line = br.readLine()) != null) {
				// Split by comma
				String[] parts = line.split(",");
				if (parts.length >= 3 && parts[0].trim().equals(doctorId)) {
					String startTime = parts[1].trim();
					String endTime = parts[2].trim();
					if ("null".equalsIgnoreCase(startTime) || "null".equalsIgnoreCase(endTime)) {
						return "Not Set";
					}
					// The time is already in HH:mm format, so we can just return it.
					return startTime + " - " + endTime;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
		}
		return "Not Set";
	}

	/**
	 * Saves the new working time to the doctor_worktime.txt file. This method
	 * reads all lines, updates the specific doctor's record, and rewrites the
	 * entire file.
	 */
	private void saveWorkingTime() {
		String newStartTimeInput = startTimeField.getText().trim();
		String newEndTimeInput = endTimeField.getText().trim();

		// Validate and parse the time using java.time.LocalTime
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

		// Check if end time is after start time
		if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
			JOptionPane.showMessageDialog(this,
				"End time must be after start time.",
				"Invalid Time Range", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Format the times for saving in the file
		String newStartTime = startTime.format(OUTPUT_FORMATTER);
		String newEndTime = endTime.format(OUTPUT_FORMATTER);

		// Read all lines to find and update the doctor's record
		List<String> lines = new ArrayList<>();
		boolean found = false;
		try (BufferedReader br = new BufferedReader(new FileReader(WORKTIME_FILE))) {
			String line;
			while ((line = br.readLine()) != null) {
				// Split by comma
				String[] parts = line.split(",");
				if (parts.length >= 3 && parts[0].trim().equals(doctorId)) {
					// Update the line with the new working time
					lines.add(doctorId + "," + newStartTime + "," + newEndTime);
					found = true;
				} else {
					lines.add(line);
				}
			}
		} catch (IOException e) {
			// File might not exist yet, which is fine. A new record will be created.
			System.err.println("File not found or read error. A new record will be created.");
		}

		if (!found) {
			// If the doctor's ID wasn't found, add a new line
			lines.add(doctorId + "," + newStartTime + "," + newEndTime);
		}

		// Write all lines back to the file
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(WORKTIME_FILE))) {
			for (String line : lines) {
				bw.write(line);
				bw.newLine();
			}
			JOptionPane.showMessageDialog(this, "Working time updated successfully!");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Failed to save working time: " + e.getMessage(),
				"Save Error", JOptionPane.ERROR_MESSAGE);
			return; // Exit on write failure
		}

		// Update the display and hide input panel
		updateWorkingTimeDisplay();
		inputPanel.setVisible(false);
		editButton.setEnabled(true);
//		pack();
	}
}
