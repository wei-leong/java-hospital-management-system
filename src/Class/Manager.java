/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import Class.FeedbackActions;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wlhoe
 */
public class Manager extends Person{
    private final ProfileActions profileHelper = new ProfileActions();
    private final FeedbackActions feedbackHelper = new FeedbackActions();
    private final String[] _ownProfile;   
    
    public Manager(String email,String password){
        super(email,password);
        this._ownProfile = null;  
    }
    
    public Manager(){this._ownProfile = null;  };
    
    public Manager(String[] ownProfile){
        this._ownProfile = ownProfile;
    }
    
    public void addStaff(String name, String email, int age, String role, String phone, String idPrefix, String gender) {
        profileHelper.AddNewProfile(name, email, age, role, phone, idPrefix, gender);
    }
    
    public List<String[]> returnStaffData(String filterRole){
        return profileHelper.ShowProfile(filterRole,_ownProfile);
    }
    
    public void editStaff(String[] oldData, String[] newData){
        profileHelper.EditProfile(oldData, newData);
    }
    
    public void InactiveStaff(String[] staffDetails){
        profileHelper.InactiveProfile(staffDetails);
    }
    
    public int FeedbackSummary(String staffRole){
        return feedbackHelper.returnAverageRating(staffRole);
    }
}
