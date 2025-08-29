package Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// Import the FileActions class
import Class.FileActions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  * A Java AWT/Swing application for a doctor to manage medicine records. The
 *  * system allows adding, editing, and deleting medicine data stored in a text
 *  * file. The GUI features a JTable to display the data and text fields for  *
 * input.  
 */
public class ManageMedicine extends JPanel {

	private static final String FILE_NAME = "medicine.txt";
	private DefaultTableModel tableModel;
	private JTable medicineTable;
	private JTextField idField, nameField, priceField;
	private JButton addButton, editButton, deleteButton, clearButton;
	private final String ID_PREFIX = "Q";
	private final int DATA_LENGTH = 3; // Number of columns

	// Create an instance of the FileActions class
	private final FileActions _fileActions = new FileActions(FILE_NAME);

	public ManageMedicine() {
		initComponents();
		loadDataFromFile();
	}

	/**
	 * Initializes all GUI components and sets up the layout.
	 */
	private void initComponents() {
		setLayout(new BorderLayout(10, 10));

		// ------------------ Top Table Panel ------------------
		String[] columnNames = {"Medicine ID", "Name", "Price (RM)"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		medicineTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(medicineTable);
		add(scrollPane, BorderLayout.CENTER);
		medicineTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && medicineTable.getSelectedRow() != -1) {
				displaySelectedData();
				addButton.setEnabled(false);
			}
		});

		// ------------------ Bottom Control Panel ------------------
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		addButton = new JButton("Add");
		editButton = new JButton("Edit");
		deleteButton = new JButton("Delete");
		clearButton = new JButton("Clear");
		buttonPanel.add(addButton);
		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(clearButton);
		controlPanel.add(buttonPanel);

		JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
		idField = new JTextField(15);
		idField.setEditable(false);
		nameField = new JTextField(15);
		priceField = new JTextField(15);

		inputPanel.add(new JLabel("Medicine ID:"));
		inputPanel.add(idField);
		inputPanel.add(new JLabel("Medicine Name:"));
		inputPanel.add(nameField);
		inputPanel.add(new JLabel("Medicine Price:"));
		inputPanel.add(priceField);

		controlPanel.add(inputPanel);

		add(controlPanel, BorderLayout.SOUTH);

		// ------------------ Button Actions ------------------
		addButton.addActionListener(e -> addMedicine());
		editButton.addActionListener(e -> editMedicine());
		deleteButton.addActionListener(e -> deleteMedicine());
		clearButton.addActionListener(e -> clearFields());
	}

	/**
	 * Loads medicine data from the text file into the JTable.
	 */
	private void loadDataFromFile() {
		// Use the returnAllDataFromFile method from FileActions
		List<String[]> data = _fileActions.returnAllDataFromFile(DATA_LENGTH);
		tableModel.setRowCount(0); // Clear existing data
		if (data.isEmpty()) {
			JOptionPane.showMessageDialog(this, "The medicine data file is empty or not found.", "No Data Found", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		boolean dataFound = false;
		for (String[] parts : data) {
			if (parts.length == DATA_LENGTH) {
				Vector<String> row = new Vector<>(Arrays.asList(parts));
				tableModel.addRow(row);
				dataFound = true;
			} else {
				System.err.println("Skipping malformed line with incorrect column count.");
			}
		}

		if (!dataFound) {
			JOptionPane.showMessageDialog(this, "The medicine data file is empty.", "No Data Found", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Saves the current data from the JTable back to the text file. NOTE: This
	 * method is no longer needed since addRowToFile and editRowFromFile handle
	 * file I/O directly. The logic is now within the addMedicine, editMedicine,
	 * and deleteMedicine methods.
	 */
	private void saveDataToFile() {
		// This method is now obsolete with the new approach.
		// Its logic has been replaced by the methods in FileActions.
	}

	/**
	 * Populates the text fields with the data from the selected table row.
	 */
	private void displaySelectedData() {
		int selectedRow = medicineTable.getSelectedRow();
		if (selectedRow != -1) {
			idField.setText((String) tableModel.getValueAt(selectedRow, 0));
			nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
			priceField.setText((String) tableModel.getValueAt(selectedRow, 2));
		}
	}

	/**
	 * Adds a new medicine record.
	 */
	private void addMedicine() {
		if (nameField.getText().trim().isEmpty() || priceField.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter both medicine name and price.", "Validation Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String newId = generateNextId();

		// Use the addRowToFile method from FileActions
		String[] newData = {newId, nameField.getText().trim(), priceField.getText().trim()};
		_fileActions.addRowToFile(newData);

		// Re-load data from file to update the table
		loadDataFromFile();
		clearFields();
		JOptionPane.showMessageDialog(this, "Medicine added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Edits the selected medicine record.
	 */
	private void editMedicine() {
		int selectedRow = medicineTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a medicine to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (nameField.getText().trim().isEmpty() || priceField.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter both medicine name and price.", "Validation Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Use the editRowFromFile method from FileActions
		String[] oldData = new String[DATA_LENGTH];
		for (int i = 0; i < DATA_LENGTH; i++) {
			oldData[i] = (String) tableModel.getValueAt(selectedRow, i);
		}

		String[] newData = {
			(String) tableModel.getValueAt(selectedRow, 0),
			nameField.getText().trim(),
			priceField.getText().trim()
		};
		_fileActions.editRowFromFile(DATA_LENGTH, oldData, newData);

		// Re-load data from file to update the table
		loadDataFromFile();
		clearFields();
		JOptionPane.showMessageDialog(this, "Medicine edited successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Deletes the selected medicine record.
	 */
	private void deleteMedicine() {
		int selectedRow = medicineTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a medicine to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Retrieve data for the row to be deleted
		String[] dataToDelete = new String[DATA_LENGTH];
		for (int i = 0; i < DATA_LENGTH; i++) {
			dataToDelete[i] = (String) tableModel.getValueAt(selectedRow, i);
		}

		// Use the delete logic from editRowFromFile by replacing the line with an empty one
		// or by filtering out the data from the list
		List<String[]> allData = _fileActions.returnAllDataFromFile(DATA_LENGTH);
		List<String[]> updatedData = new ArrayList<>();
		for (String[] rowData : allData) {
			if (!Arrays.equals(rowData, dataToDelete)) {
				updatedData.add(rowData);
			}
		}

		// This is a simple but effective way to handle deletion. 
		// If you have a specific method for deletion in FileActions, you should use that instead.
		// For now, let's just re-write the file.
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/txt/" + FILE_NAME))) {
			for (String[] row : updatedData) {
				writer.write(String.join(",", row));
				writer.newLine();
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "An error occurred while deleting the record: " + ex.getMessage(), "File Save Error", JOptionPane.ERROR_MESSAGE);
		}

		// Re-load data from file to update the table
		loadDataFromFile();
		clearFields();
		JOptionPane.showMessageDialog(this, "Medicine deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Clears the input fields and selection.
	 */
	private void clearFields() {
		idField.setText("");
		nameField.setText("");
		priceField.setText("");
		medicineTable.clearSelection();
		addButton.setEnabled(true);
	}

	/**
	 * Generates a new, unique medicine ID based on the highest existing ID in
	 * the file.
	 */
	public String generateNextId() {
		int highestNumber = 0;
		// Use the returnAllDataFromFile method to read data for ID generation
		List<String[]> allData = _fileActions.returnAllDataFromFile(DATA_LENGTH);

		for (String[] data : allData) {
			if (data.length > 0) {
				String idPart = data[0].trim();
				Pattern pattern = Pattern.compile("^" + Pattern.quote(ID_PREFIX) + "(\\d+)$");
				Matcher matcher = pattern.matcher(idPart);
				if (matcher.matches()) {
					try {
						int currentNumber = Integer.parseInt(matcher.group(1));
						if (currentNumber > highestNumber) {
							highestNumber = currentNumber;
						}
					} catch (NumberFormatException e) {
						System.err.println("Skipping malformed ID: " + data[0]);
					}
				}
			}
		}

		return ID_PREFIX + (highestNumber + 1);
	}
}
