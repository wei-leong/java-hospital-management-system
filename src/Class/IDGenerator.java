package Class;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IDGenerator {

	private String prefix;
	private String filePath;

	public IDGenerator(String prefix, String filePath) {
		this.prefix = prefix;
		this.filePath = filePath;
	}

	public String generateNextId() {
		int highestNumber = 0;
		Pattern pattern = Pattern.compile("^" + Pattern.quote(prefix) + "(\\d+)$");
		
		Path filePathObject = Paths.get("src", "txt", filePath);

		try (BufferedReader reader = new BufferedReader(new FileReader(filePathObject.toFile()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(",", -1);
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
		}

		return prefix + (highestNumber + 1);
	}
}
