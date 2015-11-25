/**
 * 
 */
package com.ludo.server;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Petter
 *
 */
public class UserHandler {
    DatabaseHandler database = new DatabaseHandler();
    
    
    public boolean authenticateUser(String username, String password) {
        
        // Fetch user with password from database
        ResultSet results = database.select("SELECT id, username, password FROM users WHERE username=\"" + username + "\" AND password=\"" + password + "\";");
        
        try {
            
            // If user is found, authentication is successful
            if(results.next()) {
                System.out.println("User Authenticated: " + results.getString("username"));
                return true;
            } else {
                System.out.println("Auth failed: " + username);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error finding username and password from the database");
            e.printStackTrace();
        }
        
        return false;
    }
    
    public void newUser(String username, String password) {
        
        // Insert username and password to database
        database.insert("SELECT id, username, password FROM users WHERE username=\"" + username + "\" AND password=\"" + password + "\";");
    }
    
    /**
     * Check if a username exists in the database
     * @param username
     * @return boolean
     */
    public boolean usernameTaken(String username) {
        
        // Try to find user from database
        ResultSet results = database.select("SELECT id, username FROM users WHERE username=\"" + username + "\";");
        
        try {
            
            // If username is found
            if(results.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error returning if user exists in the database");
            e.printStackTrace();
        }
        
        return false;
    }

}
