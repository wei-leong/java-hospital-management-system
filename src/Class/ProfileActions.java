/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wlhoe
 */
public class ProfileActions extends FileActions {

    private static final int idx_id = 0;
    private static final int idx_role = 1;
    private static final int idx_name = 2;
    private static final int idx_pass = 3;
    private static final int idx_gender = 4;
    private static final int idx_email = 5;
    private static final int idx_phone = 6;
    private static final int idx_age = 7;
    private static final int idx_status = 8;
    private static final int txt_len = 9;
//    private final List<String[]> _allData; Can't use this attribute or it won't refresh table for StaffManagement

    public ProfileActions() {
        super("profile.txt");
    }

    public void AddNewProfile(String newName, String newEmail, int newAge, String newRole, String newPhone, String id, String newGender) {
        List<String[]> allData = returnAllDataFromFile(txt_len);
        int maxId = 0;

        for (String[] row : allData) {
            // Need to change the startwith
            if (row.length == txt_len && row[idx_id].startsWith(id)) {
                try {
                    int num = Integer.parseInt(row[idx_id].substring(1)); // Skip the tag ( S1 skip S )
                    if (num > maxId) {
                        maxId = num; // Replace maxId with higher number
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid id
                }
            }
        }

        String newId = id + (maxId + 1);
        String password = newId + newPhone;

        String[] newStaff = {
            newId,
            newRole,
            newName,
            password,
            newGender,
            newEmail,
            newPhone,
            String.valueOf(newAge),
            "Active"
        };

        addRowToFile(newStaff);
    }

    // I would also need to filter out staff role Customer
    public List<String[]> ShowProfile(String filterRole, String[] ownProfile) {
        List<String[]> allData = returnAllDataFromFile(txt_len);
        List<String[]> filteredData = new ArrayList<>(txt_len);

        for (String[] row : allData) {
            if (row.length == 9 && row[idx_id].equals(ownProfile[0])) {
                continue;
            }

            if (row.length == 9 && row[idx_role].equals("Customer")) {
                continue;
            }

            if (row.length == 9 && row[idx_status].equals("Active")) {
                // if "All" or matches the role
                if (filterRole.equalsIgnoreCase("All")
                        || row[idx_role].equals(filterRole)) {
                    filteredData.add(row);
                }
            } else if (row.length == 9 && filterRole.equalsIgnoreCase("Inactive")) {
                if (row[idx_status].equalsIgnoreCase("Inactive")) {
                    filteredData.add(row);
                }
            }
        }
        return filteredData;
    }

    public void EditProfile(String[] oldData, String[] newData) {
        editRowFromFile(txt_len, oldData, newData);
    }

    public void InactiveProfile(String[] oldData,String[] staffProfile) {
        editRowFromFile(txt_len,oldData,staffProfile);
    }

    public String[] returnStaffProfile(String staffId) {
        List<String[]> allData = returnAllDataFromFile(txt_len);
        for (String[] row : allData) {
            if(row.length == txt_len && row[idx_id].equals(staffId)){
                return new String[]{row[idx_id],row[idx_name]};
            }
        }
        return null; // or return new String[0]; if you prefer empty instead of null
    }

    public String[] returnCustomerProfile(String customerId) {
        List<String[]> allData = returnAllDataFromFile(txt_len);
        for (String[] row : allData) {
            if(row.length == txt_len && row[idx_id].equals(customerId)){
                return new String[]{row[idx_name],row[idx_email]};
            }
        }
        return null;
    }

    public boolean isEmailEndsWith(String email, String emailEnds) {
        return email.endsWith(emailEnds);
    }

    public boolean isEmailUnique(String email) {
        List<String[]> allData = returnAllDataFromFile(txt_len);
        for(String[] row: allData){
            if(row.length == txt_len && row[idx_email].equals(email)){
                return false;
            }
        }
        return true;
    }

    public boolean isPhoneUnique(String phone) {
        List<String[]> allData = returnAllDataFromFile(txt_len);
        for(String[] row: allData){
            if(row.length == txt_len && row[idx_phone].equals(phone)){
                return false;
            }
        }
        return true;
    }

    public boolean checkPhone(String phone) {
        return phone != null && phone.length() == 10;
    }
    
    public boolean checkPhoneDigit(String phone){
        return phone.chars().allMatch(Character::isDigit);
    }
    
    public boolean checkPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    public List<String[]> ReturnAllStaffData(){
        return returnAllDataFromFile(txt_len);
    }
    
    public int returnCustomerAverageAge(){
        List<String[]> allData = returnAllDataFromFile(txt_len);
        int totalAge = 0;
        int total = 0;
        
        
        for(String[] row: allData){
            if(row.length == txt_len){
                totalAge += Integer.parseInt(row[idx_age]);
                total++;
            }
        }
        
        return totalAge / total;
    }
}
