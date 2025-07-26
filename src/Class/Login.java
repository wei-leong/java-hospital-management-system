/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

/**
 *
 * @author Wlhoe
 */
public interface Login {
    /**
     * Check whether the given credentials are valid.
     * @param email the user’s name
     * @param password the user’s password
     * @return the staff id if login succeeds
     */
    String login();
}
