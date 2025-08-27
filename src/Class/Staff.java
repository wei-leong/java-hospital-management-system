/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.util.List;

/**
 *
 * @author SEAH SW
 */
public class Staff extends Person{
    private final ProfileActions profileHelper = new ProfileActions();
    private final String[] _ownProfile;
    
    public Staff(String email, String password) {
    super(email, password);
    this._ownProfile = null;
    }

    public Staff() {
        this._ownProfile = null;
    }
    
    public Staff(String[] ownProfile) {
        this._ownProfile = ownProfile;
    }
    
    public void addCustomer(String name, String email, int age, String role, String phone, String idPrefix, String gender) {
        profileHelper.AddNewProfile(name, email, age, role, phone, idPrefix, gender);
    }
    
        public List<String[]> returnCustomerData(String filterRole) {
        return profileHelper.ShowCustomerProfile(filterRole);
    }
    
    
    public void editCustomer(String[] oldData, String[] newData){
        profileHelper.EditProfile(oldData, newData);
    }
    
    public void InactiveCustomer(String[] oldData,String[] staffDetails) {
        profileHelper.InactiveProfile(oldData,staffDetails);
    }
}
