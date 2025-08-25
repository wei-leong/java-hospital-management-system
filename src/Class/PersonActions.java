/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

/**
 *
 * @author Wlhoe
 */
public interface PersonActions {
    /**
     * Check whether the given credentials are valid.
     * @return the staff details if login succeeds
     */
    String[] loginValidate();
    
    void editProfile(String[] oldData, String[] newData);
}
