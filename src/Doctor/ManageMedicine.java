package Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Java AWT/Swing application for a doctor to manage medicine records. The
 * system allows adding, editing, and deleting medicine data stored in a text
 * file. The GUI features a JTable to display the data and text fields for
 * input.
 */
public class ManageMedicine extends JPanel {

	// Updated file path as per your request
	private static final String FILE_NAME = "src/txt/medicine.txt";
	private DefaultTableModel tableModel;
	private JTable medicineTable;
	private JTextField idField, nameField, priceField;
	private JButton addButton, editButton, deleteButton, clearButton;
    private final String ID_PREFIX = "Q"; // The prefix for the medicine ID

	public ManageMedicine() {

		// Initialize components
		initComponents();
		loadDataFromFile();
	}

	/**
	 * Initializes all GUI components and sets up the layout.
	 */
	private void initComponents() {
		setLayout(new BorderLayout(10, 10));

		// ------------------ Top Table Panel ------------------
		// Table model with columns for ID, Name, and Price
		String[] columnNames = {"Medicine ID", "Name", "Price (RM)"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// Return false to make all cells in the table non-editable
				return false;
			}
		};
		medicineTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(medicineTable);
		add(scrollPane, BorderLayout.CENTER);
        
		// Add a listener to handle row selection
		medicineTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && medicineTable.getSelectedRow() != -1) {
				displaySelectedData();
                // Disable the "Add" button when a row is selected
                addButton.setEnabled(false);
			}
		});

		// ------------------ Bottom Control Panel ------------------
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));

		// Buttons
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

		// Text input fields
		JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
		idField = new JTextField(15);
		idField.setEditable(false); // ID should not be manually edited, as requested
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
	 * Loads medicine data from the text file into the JTable. Handles file not
	 * found, empty file, and null/empty string data.
	 */
	private void loadDataFromFile() {
		File file = new File(FILE_NAME);
		if (!file.exists()) {
			JOptionPane.showMessageDialog(this, "The file " + FILE_NAME + " does not exist. A new one will be created upon saving data.", "File Not Found", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			tableModel.setRowCount(0); // Clear existing data
			String line;
			boolean dataFound = false;
			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) {
					continue; // Skip empty lines
				}
				String[] parts = line.split(",\\s*"); // Split by comma and optional whitespace

				// Proper error handling for malformed data
				if (parts.length != 3) {
					System.err.println("Skipping malformed line: " + line);
					continue;
				}

				// Handling for "null" string or empty data
				String id = (parts[0].equalsIgnoreCase("null") || parts[0].trim().isEmpty()) ? "N/A" : parts[0];
				String name = (parts[1].equalsIgnoreCase("null") || parts[1].trim().isEmpty()) ? "N/A" : parts[1];
				String price = (parts[2].equalsIgnoreCase("null") || parts[2].trim().isEmpty()) ? "N/A" : parts[2];

				Vector<String> row = new Vector<>();
				row.add(id);
				row.add(name);
				row.add(price);
				tableModel.addRow(row);
				dataFound = true;
			}
			if (!dataFound) {
				JOptionPane.showMessageDialog(this, "The medicine data file is empty.", "No Data Found", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "An error occurred while reading the file: " + ex.getMessage(), "File Read Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Saves the current data from the JTable back to the text file.
	 */
	private void saveDataToFile() {
		// Ensure the directory exists before trying to save
		File directory = new File("src/txt");
		if (!directory.exists()) {
			directory.mkdirs(); // Creates the directory if it doesn't exist
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				String id = (String) tableModel.getValueAt(i, 0);
				String name = (String) tableModel.getValueAt(i, 1);
				String price = (String) tableModel.getValueAt(i, 2);
				writer.write(id + ", " + name + ", " + price);
				writer.newLine();
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "An error occurred while saving the file: " + ex.getMessage(), "File Save Error", JOptionPane.ERROR_MESSAGE);
		}
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
	 * Adds a new medicine record to the table and saves it to the file. This
	 * method now uses the auto-generated ID.
	 */
	private void addMedicine() {
		// Simple validation
		if (nameField.getText().trim().isEmpty() || priceField.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter both medicine name and price.", "Validation Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Use the new method to generate a unique ID
		String newId = generateNextId();

		Vector<String> newRow = new Vector<>();
		newRow.add(newId);
		newRow.add(nameField.getText().trim());
		newRow.add(priceField.getText().trim());

		tableModel.addRow(newRow);
		saveDataToFile();
		clearFields();
	}

	/**
	 * Edits the selected medicine record in the table and saves changes.
	 */
	private void editMedicine() {
		int selectedRow = medicineTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a medicine to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Simple validation
		if (nameField.getText().trim().isEmpty() || priceField.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter both medicine name and price.", "Validation Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Update the table model with the new values
		tableModel.setValueAt(nameField.getText().trim(), selectedRow, 1);
		tableModel.setValueAt(priceField.getText().trim(), selectedRow, 2);

		saveDataToFile();
		clearFields();
	}

	/**
	 * Deletes the selected medicine record from the table and saves changes.
	 */
	private void deleteMedicine() {
		int selectedRow = medicineTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a medicine to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
			return;
		}

		tableModel.removeRow(selectedRow);
		saveDataToFile();
		clearFields();
	}

	/**
	 * Clears the input fields and selection.
	 */
	private void clearFields() {
		idField.setText("");
		nameField.setText("");
		priceField.setText("");
		medicineTable.clearSelection();
        // Re-enable the "Add" button when fields are cleared
        addButton.setEnabled(true);
	}

	/**
	 * Generates a new, unique medicine ID based on the highest existing ID in
	 * the file. This prevents duplicate IDs, even after deletions.
	 *
	 * @return The next available medicine ID as a String.
	 */
	public String generateNextId() {
		int highestNumber = 0;
		File file = new File(FILE_NAME);

		// Check if the file exists and is not empty
		if (file.exists() && file.length() > 0) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] data = line.split(",\\s*", 2);
					if (data.length > 0) {
						String idPart = data[0].trim();
						// Use regex to find the numeric part after the prefix
						Pattern pattern = Pattern.compile("^" + Pattern.quote(ID_PREFIX) + "(\\d+)$");
						Matcher matcher = pattern.matcher(idPart);
						if (matcher.matches()) {
							try {
								int currentNumber = Integer.parseInt(matcher.group(1));
								if (currentNumber > highestNumber) {
									highestNumber = currentNumber;
								}
							} catch (NumberFormatException e) {
								// Ignore lines with non-numeric parts after the prefix
								System.err.println("Skipping malformed ID: " + data[0]);
							}
						}
					}
				}
			} catch (IOException e) {
				System.err.println("Error reading file to generate ID: " + e.getMessage());
			}
		}
		// Return the new ID with the prefix
		return ID_PREFIX + (highestNumber + 1);
	}
}
