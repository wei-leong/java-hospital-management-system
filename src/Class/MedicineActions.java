package Class;

import Class.FileActions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;

public class MedicineActions {

	private final FileActions medicineFile;
	private final IDGenerator medicineIdGenerator;
	private final String medicineFilePath;
	private static final int DATA_LENGTH = 3;

	public MedicineActions(FileActions medicineFile, IDGenerator medicineIdGenerator, String medicineFilePath) {
		this.medicineFile = medicineFile;
		this.medicineIdGenerator = medicineIdGenerator;
		this.medicineFilePath = medicineFilePath;
	}

	// add medicine, generate ID automatically
	public void addMedicine(String id, String name, String price) {
		String newId = medicineIdGenerator.generateNextId();
		String[] newRecord = {newId, name, price};
		medicineFile.addRowToFile(newRecord);
	}

	// edit medicine details
	public void editMedicine(String oldId, String newId, String newName, String newPrice) {
		List<String[]> allData = medicineFile.returnAllDataFromFile(DATA_LENGTH);
		String[] oldData = null;
		String[] newData = {newId, newName, newPrice};
		for (String[] data : allData) {
			if (data[0].equals(oldId)) {
				oldData = data;
				break;
			}
		}
		if (oldData != null) {
			medicineFile.editRowFromFile(DATA_LENGTH, oldData, newData);
		}
	}

	// deleteee
	public void deleteMedicine(String id) {
		try {
			Path filePath = Paths.get("src", "txt", medicineFilePath);
			List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

			List<String> updatedLines = new ArrayList<>();
			boolean found = false;
			for (String line : lines) {
				String[] parts = line.trim().split(",", DATA_LENGTH);
				if (parts.length == DATA_LENGTH && parts[0].equals(id)) {
					found = true;
				} else {
					updatedLines.add(line);
				}
			}

			if (found) {
				Files.write(
					filePath,
					updatedLines,
					StandardCharsets.UTF_8,
					StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING
				);
			} else {
				System.err.println("Record with ID " + id + " not found to delete.");
			}
		} catch (IOException e) {
			System.err.println("Error deleting data from file: " + e.getMessage());
		}
	}

	// return da weeee
	public List<String[]> returnMedicineList() {
		return medicineFile.returnAllDataFromFile(DATA_LENGTH);
	}
}
