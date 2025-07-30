/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Wlhoe
 */
// Person has the ability to Login ( Abstraction ) 
public class Person implements Login{
    private String _email;
    private String _password;
    
    public Person(String email , String password ){
        this._email = email;
        this._password = password;
    }
    
    public Person(){
        
    }
    
    @Override
    public String[] login(){
        Path staffData = Paths.get("src","txt", "profile.txt");
        try{
            List<String> lines = Files.readAllLines(staffData);
            for(String line : lines){
                String[] parts = line.trim().split(",",9);
                if(parts.length == 9 && parts[5].equals(_email.trim()) && parts[3].equals(_password.trim())){
                    return parts;
                }
            }
        } catch(IOException e){
            System.err.println("Error reading profile.txt: " + e.getMessage());
        }
        return null;
    }
}
