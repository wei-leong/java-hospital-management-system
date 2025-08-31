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
import Class.FileActions;
import Doctor.IDGenerator;

public class CheckAppointment extends JPanel {

	// GUI component
	private JTable appointmentTable;
	private DefaultTableModel tableModel;
	private JLabel appointIdLabel, doctorIdLabel, customerIdLabel, startTimeLabel,
		statusLabel, paymentIdLabel, commentIdLabel;
	private JButton updateButton;
	private JTextField paymentField;
	private JTextField commentField;

	// a list to hold all appointment data
	private List<String[]> allAppointments;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private String doctorId;

	// hashmap to store data from other text files
	private Map<String, String> paymentMap;
	private Map<String, String> commentMap;
	private Map<String, String[]> rawAppointments; // to store raw appointment data for writing back to file

	// class to handle file I/O
	private final FileActions appointmentActions = new FileActions("appointment.txt");
	private final FileActions paymentActions = new FileActions("payment.txt");
	private final FileActions commentActions = new FileActions("comments.txt");

	// constructor to sets up the GUI and load data
	public CheckAppointment(String[] ownProfile) {
		this.doctorId = ownProfile[0]; // store the doctor's ID
		setLayout(new BorderLayout(10, 10));
		setBackground(Color.WHITE);

		// create and add the table panel first to ensure tableModel is initialized =))))
		JPanel tablePanel = createTablePanel();
		add(tablePanel, BorderLayout.CENTER);

		// create and add the filter panel to the top (north)
		JPanel filterPanel = createFilterPanel();
		add(filterPanel, BorderLayout.NORTH);

		// create a south panel to hold the details and update panels
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

		// create and add the detail panel
		JPanel detailPanel = createDetailPanel();
		southPanel.add(detailPanel);

		// create and add the update panel
		JPanel updatePanel = createUpdatePanel();
		southPanel.add(updatePanel);

		add(southPanel, BorderLayout.SOUTH);

		// load all data and populate table
		loadAllData();
	}


	// create and configure the filter panel at the top of the frame
	// @return: the JPanel containing the filter components
	private JPanel createFilterPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

		// create the checkboxes
		JCheckBox todayCheckBox = new JCheckBox("Today");
		JCheckBox tomorrowCheckBox = new JCheckBox("Tomorrow");
		JCheckBox thisWeekCheckBox = new JCheckBox("This Week");
		JCheckBox thisMonthCheckBox = new JCheckBox("This Month");
		JCheckBox showAllCheckBox = new JCheckBox("Show All");
		JButton clearButton = new JButton("Clear Filter");

		// group the checkboxes so only one can be selected at a time
		ButtonGroup filterGroup = new ButtonGroup();
		filterGroup.add(todayCheckBox);
		filterGroup.add(tomorrowCheckBox);
		filterGroup.add(thisWeekCheckBox);
		filterGroup.add(thisMonthCheckBox);
		filterGroup.add(showAllCheckBox);

		// add ItemListeners to each checkbox to trigger filtering
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

		// add ActionListener to the clear button for resetting the table
		clearButton.addActionListener(e -> {
			filterGroup.clearSelection();
			loadOngoingAppointments(); // default the view to all ongoing appointment after clearing
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


	// creates and configures the table panel
	// @return: the JPanel containing the scrollable table
	private JPanel createTablePanel() {
		// define table column headers
		String[] columnNames = {"Appoint ID", "Doctor ID", "Customer ID", "Start Time", "Status", "Amount", "Message"};
		// assign to the class-level field "tableModel"
		tableModel = new DefaultTableModel(columnNames, 0) {
			// make cells non-editable
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// assign to the class-level field "appointmentTable"
		appointmentTable = new JTable(tableModel);
		// add a listener to the table to make it selectable
		appointmentTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				updateDetailLabels();
			}
		});

		// add the table to a scroll pane for better usability
		JScrollPane scrollPane = new JScrollPane(appointmentTable);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);
		return panel;
	}

	// creates and configures the detail panel at the bottom of the frame to display selected row data
	// @return: the JPanel containing the detail labels
	private JPanel createDetailPanel() {
		// stack the labels horizontally
		JPanel panel = new JPanel(new GridLayout(1, 0, 5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Appointment Details"));

		// initialize and add all the labels for displaying data
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

	
	// creates and configures the panel for updating appointment details
	// @return: the JPanel containing the update components
	private JPanel createUpdatePanel() {
		JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Update Appointment"));

		// initialize text fields and button
		paymentField = new JTextField();
		commentField = new JTextField();
		updateButton = new JButton("Update");
		updateButton.setEnabled(false);

		// add components to the panel
		panel.add(new JLabel("Payment Amount:"));
		panel.add(paymentField);
		panel.add(new JLabel("Comment:"));
		panel.add(commentField);
		panel.add(new JLabel("")); // empty label use for spacing
		panel.add(updateButton);

		// add action listener to the update button
		updateButton.addActionListener(e -> updateAppointment());

		return panel;
	}

	// loads all data from the text files
	private void loadAllData() {
		loadPaymentData();
		loadCommentData();
		loadAppointmentData();
	}

	// reads payment data from the text file and stores it in a hashmap
	private void loadPaymentData() {
		paymentMap = new HashMap<>();
		// use FileActions to read all data from "payment.txt"
		List<String[]> paymentData = paymentActions.returnAllDataFromFile(4);
		for (String[] data : paymentData) {
			if (data.length >= 2) {
				// map paymentID with amount
				paymentMap.put(data[0].trim(), data[1].trim());
			}
		}
	}

	// read comments data from the text file and stores it in hashmap
	private void loadCommentData() {
		commentMap = new HashMap<>();
		List<String[]> commentData = commentActions.returnAllDataFromFile(3);
		for (String[] data : commentData) {
			if (data.length >= 2) {
				// map commentID to message
				commentMap.put(data[0].trim(), data[1].trim());
			}
		}
	}

	// read appointment data from a text file, replaces IDs with data from maps, and populates the table model
	private void loadAppointmentData() {
		allAppointments = new ArrayList<>();
		rawAppointments = new HashMap<>();
		List<String[]> dataLines = appointmentActions.returnAllDataFromFile(7);

		for (String[] data : dataLines) {
			rawAppointments.put(data[0].trim(), data);

			// check if the appointment is same as the logged-in doctor details
			if (data.length == 7 && data[1].trim().equals(this.doctorId.trim())) {
				// replace paymentID and commentID with actual data (want to show the actual data on the table instead of the IDs)
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
	}

	// populate the table with all the appointments from the loaded data
	private void loadAllAppointments() {
		tableModel.setRowCount(0); // Clear the table
		for (String[] rowData : allAppointments) {
			tableModel.addRow(rowData);
		}
	}

	// populate the table with only appointments that have "ongoing" status
	private void loadOngoingAppointments() {
		tableModel.setRowCount(0); // Clear the table
		for (String[] rowData : allAppointments) {
			if (rowData[4].equalsIgnoreCase("ongoing")) {
				tableModel.addRow(rowData);
			}
		}
	}

	// filter the table content based on the selected date filter
	private void filterAppointmentsByDate(String filter) {
		tableModel.setRowCount(0); // clear the table
		LocalDate now = LocalDate.now();

		for (String[] rowData : allAppointments) {
			try {
				LocalDate appointmentDate = LocalDate.parse(rowData[3], formatter);
				boolean shouldAdd = false;

				// to only show the appointments that are "ongoing" and match the date filter
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
				System.err.println("Error parsing date for row: " + rowData[3]);
			}
		}
	}

	// update the detail labels, and enable the update button with data from the selected table row
	private void updateDetailLabels() {
		int selectedRow = appointmentTable.getSelectedRow();
		if (selectedRow != -1) {
			// convert view row index to model row index
			int modelRow = appointmentTable.convertRowIndexToModel(selectedRow);

			// get the data from the selected row
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

			// enable the update button and set the text field empty
			updateButton.setEnabled(true);
			paymentField.setText(paymentAmount.equalsIgnoreCase("null") ? "" : paymentAmount);
			commentField.setText(commentMessage.equalsIgnoreCase("null") ? "" : commentMessage);

			// disable the update button if the appointment is already "completed"
			if (status.equalsIgnoreCase("complete")) {
				updateButton.setEnabled(false);
				paymentField.setEnabled(false);
				commentField.setEnabled(false);
			} else {
				paymentField.setEnabled(true);
				commentField.setEnabled(true);
			}

		} else {
			// clear the labels and disable the button if no row is selected
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

	// to handle the update process when update button is clicked
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

		// create a new comment entry 
		if (!newCommentMessage.isEmpty()) {
			IDGenerator idGenerator = new IDGenerator("G", "src/txt/comments.txt");
			newCommentId = idGenerator.generateNextId();

			// add new comment row in text file
			commentActions.addRowToFile(new String[]{newCommentId, newCommentMessage, customerIdLabel.getText().replace("Customer ID: ", "")});
			commentMap.put(newCommentId, newCommentMessage);
		}

		// update the raw appointment data
		String[] rawAppointment = rawAppointments.get(appointmentId.trim());
		if (rawAppointment != null) {
			String[] oldAppointmentData = rawAppointment.clone();
			rawAppointment[4] = "complete"; // update status

			// update the payment amount and the commentID
			String paymentId = rawAppointment[5].trim();
			String newPaymentAmount = paymentField.getText().trim();

			if (!paymentId.equalsIgnoreCase("N/A")) {
				List<String[]> allPaymentData = paymentActions.returnAllDataFromFile(4);
				String[] oldPaymentData = null;
				for (String[] data : allPaymentData) {
					if (data[0].trim().equals(paymentId)) {
						oldPaymentData = data;
						break;
					}
				}

				if (oldPaymentData != null) {
					String[] newPaymentData = new String[4];
					newPaymentData[0] = oldPaymentData[0];
					newPaymentData[1] = newPaymentAmount; // new amount
					newPaymentData[2] = oldPaymentData[2];
					newPaymentData[3] = "completed"; // new status

					paymentActions.editRowFromFile(4, oldPaymentData, newPaymentData);

					// update the local map with the new amount for display purposes
					paymentMap.put(paymentId, newPaymentAmount);
				}
			}

			rawAppointment[6] = newCommentId; // update the commentID

			// update the appointment row in text file
			appointmentActions.editRowFromFile(7, oldAppointmentData, rawAppointment);

			JOptionPane.showMessageDialog(this, "Appointment updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
		}

		loadAllData();
	}
}