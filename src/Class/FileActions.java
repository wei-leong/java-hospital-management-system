/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Wlhoe
 */
public class FileActions {
    private final String _filePath;
    
    public FileActions(String filePath) {
        this._filePath = filePath;
    }

    // Return all data from given file path
    public List<String[]> returnAllDataFromFile(int fileLength) {
        Path fileData = Paths.get("src", "txt", _filePath);
        List<String[]> results = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(fileData);
            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) continue;
                String[] parts = line.trim().split(",", fileLength);
                results.add(parts);
            }
            return results;
        } catch (IOException e) {
            System.err.println("Error reading " + _filePath + " : " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public void editRowFromFile(int fileLength, String[] oldData, String[] newData) {
        Path fileData = Paths.get("src", "txt", _filePath);
        try {
            List<String> lines = Files.readAllLines(fileData, StandardCharsets.UTF_8);
            List<String> updatedLines = new ArrayList<>(lines.size());

            String newLine = String.join(",", newData);
            for (String line : lines) {
                String[] parts = line.trim().split(",", fileLength);
                if (parts.length == fileLength && Arrays.equals(parts, oldData)) {
                    updatedLines.add(newLine);
                } else {
                    updatedLines.add(line);
                }
            }

            Files.write(fileData,
                    updatedLines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            System.err.println("Error editing " + _filePath + " : " + e.getMessage());
        }
    }
    
    public void addRowToFile(String[] newData){
        Path fileData = Paths.get("src", "txt", _filePath);
        try {
            List<String> linesToAdd = List.of(
                    "\n" + String.join(",",newData)
            );
            Files.write(
                    fileData,
                    linesToAdd,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {
            System.err.println("Error adding data to " + _filePath + " : " + e.getMessage());
        }
    }
}
