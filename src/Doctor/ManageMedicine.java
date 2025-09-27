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
import Class.IDGenerator;
import Class.Doctor;
import Class.CheckInput;

public class ManageMedicine extends JPanel {

	private static final String FILE_NAME = "medicine.txt";
	private DefaultTableModel tableModel;
	private JTable medicineTable;
	private JTextField idField, nameField, priceField;
	private JButton addButton, editButton, deleteButton, clearButton;
	private final int DATA_LENGTH = 3;

	private Doctor doctor;
	private CheckInput checkInput;

	// constructor
	public ManageMedicine(Doctor doctor) {
		this.doctor = doctor;
		this.checkInput = new CheckInput();
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

		// panel for the bottom form
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(4, 2, 10, 10));
		formPanel.setBorder(BorderFactory.createTitledBorder("Medicine Details"));

		// id field
		idField = new JTextField(15);
		idField.setEditable(false); // make id not editable
		idField.setBackground(Color.LIGHT_GRAY);
		formPanel.add(new JLabel("Medicine ID:"));
		formPanel.add(idField);

		// name field
		nameField = new JTextField(15);
		formPanel.add(new JLabel("Name:"));
		formPanel.add(nameField);

		// price field
		priceField = new JTextField(15);
		formPanel.add(new JLabel("Price (RM):"));
		formPanel.add(priceField);

		// button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		addButton = new JButton("Add");
		editButton = new JButton("Update");
		deleteButton = new JButton("Delete");
		clearButton = new JButton("Clear");

		buttonPanel.add(addButton);
		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(clearButton);

		// action listener
		addButton.addActionListener(e -> addMedicine());
		editButton.addActionListener(e -> editMedicine());
		deleteButton.addActionListener(e -> deleteMedicine());
		clearButton.addActionListener(e -> clearFields());

		// combine form and button panel
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.add(formPanel, BorderLayout.CENTER);
		southPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(southPanel, BorderLayout.SOUTH);

		// table selection listener to populate the form
		medicineTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = medicineTable.getSelectedRow();
				if (selectedRow != -1) {
					idField.setText((String) tableModel.getValueAt(selectedRow, 0));
					nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
					priceField.setText((String) tableModel.getValueAt(selectedRow, 2));
				}
			}
		});
	}

	// load the data from the text file into the table
	private void loadDataFromFile() {
		tableModel.setRowCount(0);
		List<String[]> allData = doctor.returnMedicineList();
		for (String[] row : allData) {
			tableModel.addRow(row);
		}
	}

	private void addMedicine() {
		int confirmationResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to add?", "Confirmation", JOptionPane.YES_NO_OPTION);
			if (confirmationResult == JOptionPane.NO_OPTION) {
				return;
			}
		
		String name = nameField.getText().trim();
		String price = priceField.getText().trim();
		
		// check comma (not allowing comma in input field)
		if (checkInput.checkComma(name) || checkInput.checkComma(price)) {
			JOptionPane.showMessageDialog(this, "Input cannot contain comma(,)", "Error", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		if (name.isEmpty() || price.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Name and Price cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!price.matches("\\d+(\\.\\d{1,2})?")) {
			JOptionPane.showMessageDialog(this, "Price must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// let the Doctor class handle the logic
		doctor.addMedicine(null, name, price);
		JOptionPane.showMessageDialog(this, "Medicine added successfully!");
		loadDataFromFile();
		clearFields();
	}

	private void editMedicine() {
		int confirmationResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to edit?", "Confirmation", JOptionPane.YES_NO_OPTION);
			if (confirmationResult == JOptionPane.NO_OPTION) {
				return;
			}
		
		String oldId = idField.getText().trim();
		String newName = nameField.getText().trim();
		String newPrice = priceField.getText().trim();
	
		// check comma (not allowing comma in input field)
		if (checkInput.checkComma(newName) || checkInput.checkComma(newPrice)) {
			JOptionPane.showMessageDialog(this, "Input cannot contain comma(,)", "Error", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		if (oldId.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please select a medicine to edit.", "Input Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (newName.isEmpty() || newPrice.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Name and Price cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!newPrice.matches("\\d+(\\.\\d{1,2})?")) {
			JOptionPane.showMessageDialog(this, "Price must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String newId = oldId;
		doctor.editMedicine(oldId, newId, newName, newPrice);

		JOptionPane.showMessageDialog(this, "Medicine edited successfully!");
		loadDataFromFile();
		clearFields();
	}

	private void deleteMedicine() {
		int confirmationResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete?", "Confirmation", JOptionPane.YES_NO_OPTION);
			if (confirmationResult == JOptionPane.NO_OPTION) {
				return;
			}
			
		int selectedRow = medicineTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a medicine to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String idToDelete = (String) tableModel.getValueAt(selectedRow, 0);

		doctor.deleteMedicine(idToDelete);
		JOptionPane.showMessageDialog(this, "Medicine deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
		loadDataFromFile();
		clearFields();
	}

	// clear the input field and selection
	private void clearFields() {
		idField.setText("");
		nameField.setText("");
		priceField.setText("");
		medicineTable.clearSelection();
	}
}
