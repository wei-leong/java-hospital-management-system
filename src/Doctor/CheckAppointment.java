package Doctor;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.event.ActionListener;
import Class.FileActions;
import Class.Doctor;
import Class.CheckInput;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CheckAppointment extends JPanel {

	// GUI component
	private JTable appointmentTable;
	private DefaultTableModel tableModel;
	private JLabel appointIdLabel, doctorIdLabel, staffIdLabel, customerIdLabel, startTimeLabel,
		statusLabel, paymentIdLabel, commentIdLabel;
	private JButton updateButton;
	private JTextField paymentField;
	private JTextField commentField;

	private List<String[]> allAppointments;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private Doctor doctor;
	private CheckInput checkInput;

	public CheckAppointment(String[] ownProfile, Doctor doctor) {
		this.doctor = doctor;
		this.checkInput = new CheckInput();
		setLayout(new BorderLayout(10, 10));
		setBackground(Color.WHITE);

		JPanel tablePanel = createTablePanel();
		add(tablePanel, BorderLayout.CENTER);

		JPanel filterPanel = createFilterPanel();
		add(filterPanel, BorderLayout.NORTH);

		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

		JPanel detailPanel = createDetailPanel();
		southPanel.add(detailPanel);

		JPanel updatePanel = createUpdatePanel();
		southPanel.add(updatePanel);

		add(southPanel, BorderLayout.SOUTH);

		loadAllData();
	}

	private void loadAllData() {
		allAppointments = doctor.returnAppointmentsList("All");
		updateTable(allAppointments);
	}

	private void updateTable(List<String[]> data) {
		tableModel.setRowCount(0);

		Map<String, String> paymentMap = doctor.getPaymentDetails();
		Map<String, String> commentMap = doctor.getCommentDetails();

		for (String[] row : data) {
			String paymentId = row[6];
			String commentId = row[7];
			String paymentAmount = paymentMap.getOrDefault(paymentId, "N/A");
			String commentMessage = commentMap.getOrDefault(commentId, "N/A");

			// create a new row with the updated data
			Object[] newRow = new Object[]{
				row[0],
				row[1],
				row[2],
				row[3],
				row[4],
				row[5],
				paymentAmount, // use amount instead of the ID
				commentMessage // use message instead of the ID
			};
			tableModel.addRow(newRow);
		}
	}

//	private void updateAppointment() {
//		int selectedRow = appointmentTable.getSelectedRow();
//		if (selectedRow == -1) {
//			JOptionPane.showMessageDialog(this, "Please select an ganniniajibaileh appointment to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
//			return;
//		}
//
//		String appointmentId = (String) tableModel.getValueAt(selectedRow, 0);
//		String payment = paymentField.getText().trim();
//		String comment = commentField.getText().trim();
//
//		doctor.updateAppointment(appointmentId, payment, comment);
//		JOptionPane.showMessageDialog(this, "Appointment updated successfully!");
//		loadAllData();
//	}

	private JPanel createTablePanel() {
		JPanel tablePanel = new JPanel(new BorderLayout());
		String[] columnNames = {"Appointment ID", "Doctor ID",  "Staff ID","Customer ID", "Start Time", "Status", "Payment Amount", "Comment Message"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		appointmentTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(appointmentTable);
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		// add listener for row selection
		appointmentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int selectedRow = appointmentTable.getSelectedRow();
					if (selectedRow != -1) {
						appointIdLabel.setText("Appointment ID: " + tableModel.getValueAt(selectedRow, 0));
						doctorIdLabel.setText("Doctor ID: " + tableModel.getValueAt(selectedRow, 1));
						staffIdLabel.setText("Staff ID:" + tableModel.getValueAt(selectedRow, 2));
						customerIdLabel.setText("Customer ID: " + tableModel.getValueAt(selectedRow, 3));
						startTimeLabel.setText("Start Time: " + tableModel.getValueAt(selectedRow, 4));
						statusLabel.setText("Status: " + tableModel.getValueAt(selectedRow, 5));
						paymentIdLabel.setText("Payment Amount: " + tableModel.getValueAt(selectedRow, 6));
						commentIdLabel.setText("Comment Message: " + tableModel.getValueAt(selectedRow, 7));
					} else {
						// clear the labels if no row is selected
						appointIdLabel.setText("Appointment ID:");
						doctorIdLabel.setText("Doctor ID:");
						staffIdLabel.setText("Staff ID:");
						customerIdLabel.setText("Customer ID:");
						startTimeLabel.setText("Start Time:");
						statusLabel.setText("Status:");
						paymentIdLabel.setText("Payment Amount:");
						commentIdLabel.setText("Comment Message:");
					}
				}
			}
		});

		return tablePanel;
	}

	private JPanel createFilterPanel() {
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		String[] ranges = {"All", "Today", "This week", "This month", "This year"};
		JComboBox<String> rangeComboBox = new JComboBox<>(ranges);
		rangeComboBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String range = (String) e.getItem();
				List<String[]> filteredAppointments = doctor.returnAppointmentsList(range);
				updateTable(filteredAppointments);
			}
		});
		filterPanel.add(new JLabel("Filter by:"));
		filterPanel.add(rangeComboBox);
		return filterPanel;
	}

	private JPanel createDetailPanel() {
		JPanel detailPanel = new JPanel(new GridLayout(0, 2, 10, 5));
		detailPanel.setBorder(BorderFactory.createTitledBorder("Appointment Details"));

		appointIdLabel = new JLabel("Appointment ID:");
		doctorIdLabel = new JLabel("Doctor ID:");
		staffIdLabel = new JLabel("Staff ID:");
		customerIdLabel = new JLabel("Customer ID:");
		startTimeLabel = new JLabel("Start Time:");
		statusLabel = new JLabel("Status:");
		paymentIdLabel = new JLabel("Payment Amount:");
		commentIdLabel = new JLabel("Comment Message:");

		detailPanel.add(appointIdLabel);
		detailPanel.add(doctorIdLabel);
		detailPanel.add(staffIdLabel);
		detailPanel.add(customerIdLabel);
		detailPanel.add(startTimeLabel);
		detailPanel.add(statusLabel);
		detailPanel.add(paymentIdLabel);
		detailPanel.add(commentIdLabel);

		return detailPanel;
	}

	private JPanel createUpdatePanel() {
		JPanel updatePanel = new JPanel(new GridLayout(0, 2, 10, 5));
		updatePanel.setBorder(BorderFactory.createTitledBorder("Update Appointment"));

		updatePanel.add(new JLabel("Payment Amount (RM):"));
		paymentField = new JTextField();
		updatePanel.add(paymentField);

		updatePanel.add(new JLabel("Comment:"));
		commentField = new JTextField();
		updatePanel.add(commentField);

		updateButton = new JButton("Update");
		updatePanel.add(new JLabel()); // for spacer
		updatePanel.add(updateButton);

		updateButton.addActionListener(e -> {
			int confirmationResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to update?", "Confirmation", JOptionPane.YES_NO_OPTION);
			if (confirmationResult == JOptionPane.NO_OPTION) {
				return;
			}
			int selectedRow = appointmentTable.getSelectedRow();
			if (selectedRow != -1) {
				String appointmentStatus = tableModel.getValueAt(selectedRow, 5).toString();

				// to prevent updating appointments that are completed
				if (appointmentStatus.equalsIgnoreCase("complete")) {
					JOptionPane.showMessageDialog(this,
						"This appointment is already complete and cannot be updated.",
						"Update Not Allowed",
						JOptionPane.WARNING_MESSAGE);
					return; // stop
				}

				String appointmentId = tableModel.getValueAt(selectedRow, 0).toString();
				String paymentAmount = paymentField.getText();
				String comment = commentField.getText().trim();
				
				// check comma (not allowing comma in input field)
				if (checkInput.checkComma(paymentAmount) || checkInput.checkComma(comment)) {
					JOptionPane.showMessageDialog(this, "Input cannot contain comma(,)", "Error", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				// update to txt file
				try {
					 double amount = Double.parseDouble(paymentAmount);
					doctor.updateAppointment(appointmentId, paymentAmount, comment);

					// refresh the table and clear fields after update successful 
					loadAllData();
					paymentField.setText("");
					commentField.setText("");

					JOptionPane.showMessageDialog(this, "Appointment updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(this,
						"Payment amount must be a valid number.",
						"Input Error",
						JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this,
					"Please select an appointment to update.",
					"Error",
					JOptionPane.WARNING_MESSAGE);
			}
		});

		return updatePanel;
	}
}
