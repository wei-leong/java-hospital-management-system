package Class;

import Class.FileActions;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class WorkTimeActions {

	private final FileActions workTimeFile;
	private static final int FILE_LENGTH = 3;

	public WorkTimeActions(FileActions workTimeFile) {
		this.workTimeFile = workTimeFile;
	}

	public void updateWorkTime(String doctorId, String newStartTime, String newEndTime) {
		List<String[]> allData = workTimeFile.returnAllDataFromFile(FILE_LENGTH);
		String[] newData = {doctorId, newStartTime, newEndTime};
		boolean found = false;

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
	}

	public String[] getCurrentWorkTime(String doctorId) {
		List<String[]> allData = workTimeFile.returnAllDataFromFile(FILE_LENGTH);
		for (String[] parts : allData) {
			if (parts.length >= FILE_LENGTH && parts[0].trim().equals(doctorId)) {
				return parts;
			}
		}
		return null;
	}
}
