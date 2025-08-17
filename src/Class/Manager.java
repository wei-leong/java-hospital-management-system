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
public class Manager extends Person{
    private final ProfileActions profileHelper = new ProfileActions();
    
    public Manager(String email,String password){
        super(email,password);
    }
    
    public Manager(){};
    
    public void addStaff(String name, String email, int age, String role, String phone, String idPrefix, String gender) {
        profileHelper.AddNewProfile(name, email, age, role, phone, idPrefix, gender);
    }
    
    public List<String[]> returnStaffData(String filterRole){
        return profileHelper.ShowProfile(filterRole);
    }
    
    public void editStaff(String[] oldData, String[] newData){
        profileHelper.EditProfile(oldData, newData);
    }
    
    public void InactiveStaff(String[] staffDetails){
        profileHelper.InactiveProfile(staffDetails);
    }

    public List<String[]> returnAppointmentsList(String _selectedRange) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
