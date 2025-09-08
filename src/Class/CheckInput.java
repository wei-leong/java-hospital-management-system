/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.util.List;

/**
 *
 * @author Wlhoe
 */
public class CheckInput {
    
    public boolean checkLength(String input, int length){
        return input != null && input.length() == length;
    }
    
    public boolean checkComma(String input){
        return input != null && input.contains(",");
    }
    
    public boolean checkUnique(String input, String filePath, int length, int checkIndex){
        FileActions file = new FileActions(filePath);
        List<String[]> allData = file.returnAllDataFromFile(length);
        for(String[] row: allData){
            if(row.length == length && row[checkIndex].equals(input)){
                return false;
            }
        }
        return true;
    }
}
