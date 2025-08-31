package Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Class.FileActions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Doctor.IDGenerator;

public class ManageMedicine extends JPanel {

	private static final String FILE_NAME = "medicine.txt";
	private DefaultTableModel tableModel;
	private JTable medicineTable;
	private JTextField idField, nameField, priceField;
	private JButton addButton, editButton, deleteButton, clearButton;
	private final String ID_PREFIX = "Q";
	private final int DATA_LENGTH = 3;

	private final FileActions _fileActions = new FileActions(FILE_NAME);

	
	// constructor for the ManageMedicine
	public ManageMedicine() {
		initComponents();
		loadDataFromFile();
	}


	// initialize all GUI component and set up the layout
	private void initComponents() {
		setLayout(new BorderLayout(10, 10));

		// panel for the top table 
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

		// panel for the bottom control 
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

		// button action listener
		addButton.addActionListener(e -> addMedicine());
		editButton.addActionListener(e -> editMedicine());
		deleteButton.addActionListener(e -> deleteMedicine());
		clearButton.addActionListener(e -> clearFields());
	}


	// load medicine data from the text file into the JTable
	private void loadDataFromFile() {
		List<String[]> data = _fileActions.returnAllDataFromFile(DATA_LENGTH);
		tableModel.setRowCount(0);
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


	// populate the text fields with data from selected table row
	private void displaySelectedData() {
		int selectedRow = medicineTable.getSelectedRow();
		if (selectedRow != -1) {
			idField.setText((String) tableModel.getValueAt(selectedRow, 0));
			nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
			priceField.setText((String) tableModel.getValueAt(selectedRow, 2));
		}
	}


	// add new medicine
	private void addMedicine() {
		if (nameField.getText().trim().isEmpty() || priceField.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter both medicine name and price.", "Validation Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		IDGenerator idGenerator = new IDGenerator(ID_PREFIX, "src/txt/" + FILE_NAME);
		String newId = idGenerator.generateNextId();

		String[] newData = {newId, nameField.getText().trim(), priceField.getText().trim()};
		_fileActions.addRowToFile(newData);

		// to update the table
		loadDataFromFile();
		clearFields();
		JOptionPane.showMessageDialog(this, "Medicine added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	}


	// edit the selected medicine
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

		// to update the table
		loadDataFromFile();
		clearFields();
		JOptionPane.showMessageDialog(this, "Medicine edited successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	}


	// delete selected medicine
	private void deleteMedicine() {
		int selectedRow = medicineTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a medicine to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// retrieve data for the row to be deleted
		String[] dataToDelete = new String[DATA_LENGTH];
		for (int i = 0; i < DATA_LENGTH; i++) {
			dataToDelete[i] = (String) tableModel.getValueAt(selectedRow, i);
		}

		List<String[]> allData = _fileActions.returnAllDataFromFile(DATA_LENGTH);
		List<String[]> updatedData = new ArrayList<>();
		for (String[] rowData : allData) {
			if (!Arrays.equals(rowData, dataToDelete)) {
				updatedData.add(rowData);
			}
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/txt/" + FILE_NAME))) {
			for (String[] row : updatedData) {
				writer.write(String.join(",", row));
				writer.newLine();
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "An error occurred while deleting the record: " + ex.getMessage(), "File Save Error", JOptionPane.ERROR_MESSAGE);
		}

		// to update the table
		loadDataFromFile();
		clearFields();
		JOptionPane.showMessageDialog(this, "Medicine deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	}

	// clear the input field and selection
	private void clearFields() {
		idField.setText("");
		nameField.setText("");
		priceField.setText("");
		medicineTable.clearSelection();
		addButton.setEnabled(true);
	}

}