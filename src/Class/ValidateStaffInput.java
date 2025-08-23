/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import javax.swing.JOptionPane;

/**
 *
 * @author Wlhoe
 */
public class ValidateStaffInput extends ProfileActions{
    private String _name;
    private String _email;
    private String _phone;
    
    public ValidateStaffInput(String name,String email,String phone){
        this._name = name;
        this._email = email;
        this._phone = phone;
    }
    
    public String returnErrorMsg(){
        // Validate Name
        if(_name.isEmpty()){
            return "Please enter a name";
        }
        
        // Validate Email
        if(_email.isEmpty()){
            return "Please enter a email address";
        }
        if(!isEmailEndsWith(_email,"@mail.apu.com")){
            return "Email must end with @mail.apu.com.";
        }
        if(!isEmailUnique(_email)){
            return "This email is already in use.";
        }
        
        // Validate Phone
        if(_phone.isEmpty()){
            return "Please enter a phone number";
        }
        if(!checkPhone(_phone)){
            return "Phone Number must be exactly 10 digits";
        }
        if(!isPhoneUnique(_phone)){
            return "Phone Number is already in use";
        }
        
        return null;
    }
    
    // Add ErrorDialog here with 
    
//    private void ErrorDialog( this, logi here) {
//        JOptionPane.showMessageDialog(
//                this,
//                returnErrorMsg,
//                "Validation Error",
//                JOptionPane.ERROR_MESSAGE
//        );
//    }
}
