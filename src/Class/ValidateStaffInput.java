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
public class ValidateStaffInput extends ProfileActions {

    private String _name;
    private String _email;
    private String _phone;
    private String _password;
    
    public ValidateStaffInput(){}

    public ValidateStaffInput(String name, String email, String phone, String password) {
        this._name = name;
        this._email = email;
        this._phone = phone;
        this._password = password;
    }

    // Add setters to override new values
    public void setName(String name) {this._name = name;}
    public void setEmail(String email) {this._email = email;}
    public void setPhone(String phone) {this._phone = phone;}
    public void setPassword(String password) {this._password = password;}
    
    // Used in EditStaff and EditOwnProfile
    public String returnMsgEditProfile(String currentEmail, String currentPhone) {
        CheckInput check = new CheckInput();
        
        // Validate Name
        if (_name.isEmpty()) {
            return "Please enter a name";
        }

        // Validate Email
        if (_email.isEmpty()) {
            return "Please enter a email address";
        }
        if (!isEmailEndsWith(_email, "@mail.apu.com")) {
            return "Email must end with @mail.apu.com.";
        }
        if (!_email.equalsIgnoreCase(currentEmail) && !isEmailUnique(_email)) {
            return "This email is already in use.";
        }

        // Validate Phone
        if (_phone.isEmpty()) {
            return "Please enter a phone number";
        }
        if (!checkPhoneLength(_phone)) {
            return "Phone Number must be exactly 10 digits";
        }
        if (!_phone.equalsIgnoreCase(currentPhone) && !isPhoneUnique(_phone)) {
            return "Phone Number is already in use";
        }
        if (!checkPhoneDigit(_phone)){
            return "Phone Number must be in number format";
        }

        // Validate Password
        if (_password.isEmpty()) {
            return "Please enter a password";
        }
        if (!checkPassword(_password)) {
            return "Password must be at least 6 character long";
        }
        if(check.checkComma(_password)){
            return "Password cannot contain letter coma ( , ) ";
        }

        return null;
    }
    
    // Used in Add Staff
    public String returnMsgAddProfile() {
        // Validate Name
        if (_name.isEmpty()) {
            return "Please enter a name";
        }

        // Validate Email
        if (_email.isEmpty()) {
            return "Please enter a email address";
        }
        if (!isEmailEndsWith(_email, "@mail.apu.com")) {
            return "Email must end with @mail.apu.com.";
        }
        if (!isEmailUnique(_email)) {
            return "This email is already in use.";
        }

        // Validate Phone
        if (_phone.isEmpty()) {
            return "Please enter a phone number";
        }
        if (!checkPhoneLength(_phone)) {
            return "Phone Number must be exactly 10 digits";
        }
        if (!isPhoneUnique(_phone)) {
            return "Phone Number is already in use";
        }

        return null;
    }
}
