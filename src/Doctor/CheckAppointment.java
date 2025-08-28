package Doctor;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.io.*;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckAppointment extends JPanel {

	// GUI Components
	private JTable appointmentTable;
	private DefaultTableModel tableModel;
	private JLabel appointIdLabel, doctorIdLabel, customerIdLabel, startTimeLabel,
		statusLabel, paymentIdLabel, commentIdLabel;
	private JButton updateButton;
	private JTextField paymentField;
	private JTextField commentField;

	// A list to hold all appointment data
	private List<String[]> allAppointments;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private String doctorId; // New variable to hold the logged-in doctor's ID

	// Maps to store data from other text files
	private Map<String, String> paymentMap;
	private Map<String, String> commentMap;
	private Map<String, String[]> rawAppointments; // To store raw appointment data for writing back to file

	// Constructor, sets up GUI and load data.
	public CheckAppointment(String[] ownProfile) {
		this.doctorId = ownProfile[0]; // store the doctor's ID
		setLayout(new BorderLayout(10, 10));
		setBackground(Color.WHITE);

		// create and add the table panel first to ensure tableModel is initialized
		JPanel tablePanel = createTablePanel();
		add(tablePanel, BorderLayout.CENTER);

		// create and add the filter panel to the top (north)
		JPanel filterPanel = createFilterPanel();
		add(filterPanel, BorderLayout.NORTH);

		// Create a south panel to hold both the details and update panels
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

		// create and add the detail panel
		JPanel detailPanel = createDetailPanel();
		southPanel.add(detailPanel);

		// create and add the update panel
		JPanel updatePanel = createUpdatePanel();
		southPanel.add(updatePanel);

		add(southPanel, BorderLayout.SOUTH);

		// load all data and populate the table
		loadAllData();
	}

	/**
	 * Creates and configures the filter panel at the top of the frame. This
	 * version uses check boxes for filtering.
	 *
	 * @return The JPanel containing the filter components.
	 */
	private JPanel createFilterPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

		// Create the checkboxes
		JCheckBox todayCheckBox = new JCheckBox("Today");
		JCheckBox tomorrowCheckBox = new JCheckBox("Tomorrow");
		JCheckBox thisWeekCheckBox = new JCheckBox("This Week");
		JCheckBox thisMonthCheckBox = new JCheckBox("This Month");
		JCheckBox showAllCheckBox = new JCheckBox("Show All");
		JButton clearButton = new JButton("Clear Filter");

		// Group the checkboxes so only one can be selected at a time
		ButtonGroup filterGroup = new ButtonGroup();
		filterGroup.add(todayCheckBox);
		filterGroup.add(tomorrowCheckBox);
		filterGroup.add(thisWeekCheckBox);
		filterGroup.add(thisMonthCheckBox);
		filterGroup.add(showAllCheckBox);

		// Add ItemListeners to each checkbox to trigger filtering
		todayCheckBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				filterAppointmentsByDate("Today");
			}
		});
		tomorrowCheckBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				filterAppointmentsByDate("Tomorrow");
			}
		});
		thisWeekCheckBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				filterAppointmentsByDate("This Week");
			}
		});
		thisMonthCheckBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				filterAppointmentsByDate("This Month");
			}
		});
		showAllCheckBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				loadAllAppointments();
			}
		});

		// Add ActionListener to the clear button to reset the table
		clearButton.addActionListener(e -> {
			filterGroup.clearSelection();
			loadOngoingAppointments(); // Default view after clearing
		});

		panel.add(new JLabel("Filter by Date:"));
		panel.add(todayCheckBox);
		panel.add(tomorrowCheckBox);
		panel.add(thisWeekCheckBox);
		panel.add(thisMonthCheckBox);
		panel.add(showAllCheckBox);
		panel.add(clearButton);

		return panel;
	}

	/**
	 * Creates and configures the table panel.
	 *
	 * @return The JPanel containing the scrollable table.
	 */
	private JPanel createTablePanel() {
		// Define table column headers
		String[] columnNames = {"Appoint ID", "Doctor ID", "Customer ID", "Start Time", "Status", "Amount", "Message"};
		// Correctly assign to the class-level field 'tableModel'
		tableModel = new DefaultTableModel(columnNames, 0) {
			// Make all cells non-editable
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Correctly assign to the class-level field 'appointmentTable'
		appointmentTable = new JTable(tableModel);
		// Add a listener to the table for row selection events
		appointmentTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				updateDetailLabels();
			}
		});

		// Add the table to a scroll pane for better usability
		JScrollPane scrollPane = new JScrollPane(appointmentTable);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);
		return panel;
	}

	// Creates and configures the detail panel at the bottom of the frame to display selected row data.
	// @return the JPanel containing the detail labels.
	private JPanel createDetailPanel() {
		// Use single column GridLayout to stack the labels vertically
		JPanel panel = new JPanel(new GridLayout(1, 0, 5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Appointment Details"));

		// Initialize and add all the labels for displaying data
		appointIdLabel = new JLabel("Appoint ID: ");
		doctorIdLabel = new JLabel("Doctor ID: ");
		customerIdLabel = new JLabel("Customer ID: ");
		startTimeLabel = new JLabel("Start Time: ");
		statusLabel = new JLabel("Status: ");
		paymentIdLabel = new JLabel("Payment ID: ");
		commentIdLabel = new JLabel("Comment ID: ");

		panel.add(appointIdLabel);
		panel.add(doctorIdLabel);
		panel.add(customerIdLabel);
		panel.add(startTimeLabel);
		panel.add(statusLabel);
		panel.add(paymentIdLabel);
		panel.add(commentIdLabel);

		return panel;
	}

	/**
	 * Creates and configures the panel for updating appointment details.
	 *
	 * @return The JPanel containing the update components.
	 */
	private JPanel createUpdatePanel() {
		JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Update Appointment"));

		// Initialize text fields and button
		paymentField = new JTextField();
		commentField = new JTextField();
		updateButton = new JButton("Update");
		updateButton.setEnabled(false);

		// Add components to the panel
		panel.add(new JLabel("Payment Amount:"));
		panel.add(paymentField);
		panel.add(new JLabel("Comment:"));
		panel.add(commentField);
		panel.add(new JLabel("")); // Empty label for spacing
		panel.add(updateButton);

		// Add action listener to the update button
		updateButton.addActionListener(e -> updateAppointment());

		return panel;
	}

	// Loads all required data from text files.
	private void loadAllData() {
		loadPaymentData();
		loadCommentData();
		loadAppointmentData();
	}

	// reads payment data from the text file and stores it in a map.
	private void loadPaymentData() {
		paymentMap = new HashMap<>();
		String filePath = "src/txt/payment.txt";
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",");
				if (data.length >= 2) {
					// Map payment ID to amount
					paymentMap.put(data[0].trim(), data[1].trim());
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error reading file: " + filePath, "File Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	// reads comments data from the text file and stores it in a map.
	private void loadCommentData() {
		commentMap = new HashMap<>();
		String filePath = "src/txt/comments.txt";
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",", 3);
				if (data.length >= 2) {
					// Map comment ID to message
					commentMap.put(data[0].trim(), data[1].trim());
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error reading file: " + filePath, "File Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	// read appointment data from a text file, replaces IDs with data from maps, and populates the table model.
	private void loadAppointmentData() {
		String filePath = "src/txt/appointment.txt";
		allAppointments = new ArrayList<>();
		rawAppointments = new HashMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				// split each line by the comma delimiter
				String[] data = line.split(",");
				rawAppointments.put(data[0].trim(), data);

				// check if the appointment same as the logged-in doctor
				if (data.length == 7 && data[1].trim().equals(this.doctorId.trim())) {
					// Replace Payment ID and Comment ID with actual data
					String paymentAmount = paymentMap.getOrDefault(data[5].trim(), "null").equalsIgnoreCase("null") ? "N/A" : paymentMap.get(data[5].trim());
					String commentMessage = commentMap.getOrDefault(data[6].trim(), "null").equalsIgnoreCase("null") ? "N/A" : commentMap.get(data[6].trim());

					String[] rowData = {
						data[0].trim(),
						data[1].trim(),
						data[2].trim(),
						data[3].trim(),
						data[4].trim(),
						paymentAmount,
						commentMessage
					};
					allAppointments.add(rowData);
				}
			}
			// populate the table with only ongoing appointments by default
			loadOngoingAppointments();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error reading file: " + filePath + "\nPlease make sure the file exists and is formatted correctly.", "File Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	// populates the table with all the appointments from the loaded data.
	private void loadAllAppointments() {
		tableModel.setRowCount(0); // Clear the table
		for (String[] rowData : allAppointments) {
			tableModel.addRow(rowData);
		}
	}

	// Populates the table with only appointments that have "ongoing" status.
	private void loadOngoingAppointments() {
		tableModel.setRowCount(0); // Clear the table
		for (String[] rowData : allAppointments) {
			if (rowData[4].equalsIgnoreCase("ongoing")) {
				tableModel.addRow(rowData);
			}
		}
	}

	// Filters the table content based on the selected date filter.
	// @param filter, the type of filter to apply ("Today", "Tomorrow", etc.).
	private void filterAppointmentsByDate(String filter) {
		tableModel.setRowCount(0); // Clear the table
		LocalDate now = LocalDate.now();

		for (String[] rowData : allAppointments) {
			try {
				LocalDate appointmentDate = LocalDate.parse(rowData[3], formatter);
				boolean shouldAdd = false;

				// Condition to only show appointments that are "ongoing" AND match the date filter.
				if (rowData[4].equalsIgnoreCase("ongoing")) {
					switch (filter) {
						case "Today":
							if (appointmentDate.isEqual(now)) {
								shouldAdd = true;
							}
							break;
						case "Tomorrow":
							if (appointmentDate.isEqual(now.plusDays(1))) {
								shouldAdd = true;
							}
							break;
						case "This Week":
							LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
							LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
							if (!appointmentDate.isBefore(startOfWeek) && !appointmentDate.isAfter(endOfWeek)) {
								shouldAdd = true;
							}
							break;
						case "This Month":
							if (appointmentDate.getYear() == now.getYear() && appointmentDate.getMonth() == now.getMonth()) {
								shouldAdd = true;
							}
							break;
					}
				}

				if (shouldAdd) {
					tableModel.addRow(rowData);
				}
			} catch (Exception e) {
				// error handling
				System.err.println("Error parsing date for row: " + rowData[3]);
			}
		}
	}

	// updates the detail labels and enables the update button with data from the selected table row
	private void updateDetailLabels() {
		int selectedRow = appointmentTable.getSelectedRow();
		if (selectedRow != -1) {
			// Convert view row index to model row index
			int modelRow = appointmentTable.convertRowIndexToModel(selectedRow);

			// Get the data from the selected row
			String appointId = (String) tableModel.getValueAt(modelRow, 0);
			String doctorId = (String) tableModel.getValueAt(modelRow, 1);
			String customerId = (String) tableModel.getValueAt(modelRow, 2);
			String startTime = (String) tableModel.getValueAt(modelRow, 3);
			String status = (String) tableModel.getValueAt(modelRow, 4);
			String paymentAmount = (String) tableModel.getValueAt(modelRow, 5);
			String commentMessage = (String) tableModel.getValueAt(modelRow, 6);

			// Update the labels with the new data
			appointIdLabel.setText("Appoint ID: " + appointId);
			doctorIdLabel.setText("Doctor ID: " + doctorId);
			customerIdLabel.setText("Customer ID: " + customerId);
			startTimeLabel.setText("Start Time: " + startTime);
			statusLabel.setText("Status: " + status);
			paymentIdLabel.setText("Amount: " + (paymentAmount.equalsIgnoreCase("null") ? "N/A" : paymentAmount));
			commentIdLabel.setText("Message: " + (commentMessage.equalsIgnoreCase("null") ? "N/A" : commentMessage));

			// Enable the update button and set the text fields
			updateButton.setEnabled(true);
			paymentField.setText(paymentAmount.equalsIgnoreCase("null") ? "" : paymentAmount);
			commentField.setText(commentMessage.equalsIgnoreCase("null") ? "" : commentMessage);

			// Disable the update button if the appointment is already completed
			if (status.equalsIgnoreCase("complete")) {
				updateButton.setEnabled(false);
				paymentField.setEnabled(false);
				commentField.setEnabled(false);
			} else {
				paymentField.setEnabled(true);
				commentField.setEnabled(true);
			}

		} else {
			// If no row is selected, clear the labels and disable the button
			appointIdLabel.setText("Appoint ID: ");
			doctorIdLabel.setText("Doctor ID: " + doctorId);
			customerIdLabel.setText("Customer ID: ");
			startTimeLabel.setText("Start Time: ");
			statusLabel.setText("Status: ");
			paymentIdLabel.setText("Amount: ");
			commentIdLabel.setText("Message: ");

			updateButton.setEnabled(false);
			paymentField.setText("");
			commentField.setText("");
			paymentField.setEnabled(false);
			commentField.setEnabled(false);
		}
	}

	// Handles the update process when the "Update" button is clicked.
	private void updateAppointment() {
		int selectedRow = appointmentTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select an appointment to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int modelRow = appointmentTable.convertRowIndexToModel(selectedRow);
		String appointmentId = (String) tableModel.getValueAt(modelRow, 0);

		String newCommentMessage = commentField.getText().trim();
		String newCommentId = "N/A";

		// Only create a new comment entry if the comment is not empty
		if (!newCommentMessage.isEmpty()) {
			GenerateID idGenerator = new GenerateID("G");
			newCommentId = idGenerator.generateId("src/txt/comments.txt");

			// Append the new comment data to comments.txt
			appendToFile("src/txt/comments.txt", newCommentId + "," + newCommentMessage + "," + customerIdLabel.getText().replace("Customer ID: ", ""));
			commentMap.put(newCommentId, newCommentMessage);
		}

		// Update the raw appointment data
		String[] rawAppointment = rawAppointments.get(appointmentId.trim());
		if (rawAppointment != null) {
			rawAppointment[4] = "complete"; // Update status

			// Update the payment amount and the comment ID
			String paymentId = rawAppointment[5].trim();
			String newPaymentAmount = paymentField.getText().trim();

			if (!paymentId.equalsIgnoreCase("N/A")) {
				// Update payment map and file
				paymentMap.put(paymentId, newPaymentAmount);
				updatePaymentsFile(paymentId, newPaymentAmount, LocalDate.now().format(dateFormatter), "completed");
			}

			rawAppointment[6] = newCommentId; // Update comment ID

			JOptionPane.showMessageDialog(this, "Appointment updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
		}

		// Rewrite the entire appointment.txt file with the updated data
		updateAppointmentsFile();

		// Reload all data to refresh the view
		loadAllData();
	}

	// Appends a new line of data to a text file.
	private void appendToFile(String filePath, String data) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
			writer.println(data);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error writing to file: " + filePath, "File Write Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	// Rewrites the entire appointment.txt file with the updated data.
	private void updateAppointmentsFile() {
		String filePath = "src/txt/appointment.txt";
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
			for (String key : rawAppointments.keySet()) {
				String[] appointment = rawAppointments.get(key);
				writer.println(String.join(",", appointment));
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error updating appointments file: " + filePath, "File Write Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	// Rewrites the entire payments.txt file with the updated data.
	private void updatePaymentsFile(String paymentId, String amount, String issuedDate, String status) {
		String filePath = "src/txt/payment.txt";
		List<String> updatedLines = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",");
				// Check if the current line's payment ID matches the one being updated
				if (data[0].trim().equals(paymentId.trim())) {
					// Reconstruct the line with the new amount, issued date, and status
					updatedLines.add(paymentId + "," + amount + "," + issuedDate + "," + status);
				} else {
					// Keep all other lines as they are
					updatedLines.add(line);
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error reading payments file: " + filePath, "File Read Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}

		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
			for (String updatedLine : updatedLines) {
				writer.println(updatedLine);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error writing to payments file: " + filePath, "File Write Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}

// New class to generate sequential IDs based on existing file content.
class GenerateID {

	private String prefix;

	public GenerateID(String prefix) {
		this.prefix = prefix;
	}

	public String generateId(String filePath) {
		int highestNumber = 0;
		Pattern pattern = Pattern.compile("^" + Pattern.quote(prefix) + "(\\d+)$");

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",", 2);
				if (data.length > 0) {
					Matcher matcher = pattern.matcher(data[0].trim());
					if (matcher.matches()) {
						int currentNumber = Integer.parseInt(matcher.group(1));
						if (currentNumber > highestNumber) {
							highestNumber = currentNumber;
						}
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file to generate ID: " + e.getMessage());
			// Optionally throw a custom exception or return a default ID
		}

		return prefix + (highestNumber + 1);
	}
}
