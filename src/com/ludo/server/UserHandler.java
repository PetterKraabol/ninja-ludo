/**
 * 
 */
package com.ludo.server;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserHandler takes care of handling users, such as checking them in the
 * database to help the server authenticate, and create new users.
 * @author Petter
 *
 */
public class UserHandler {
    DatabaseHandler database = new DatabaseHandler();
    
    /**
     * Authenticate user by checking if a row with specified username and password exists in database.
     * @param username
     * @param password
     * @return boolean if the user exists or not.
     */
    public boolean authenticateUser(String username, String password) {
        
        // Fetch user with password from database
        ResultSet results = database.select("SELECT id, username, password FROM users WHERE username=\"" + username + "\" AND password=\"" + password + "\";");
        
        try {
            
            // If user is found, authentication is successful
            if(results.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error finding username and password from the database");
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Insert a new user to the database.
     * @param username
     * @param password
     */
    public void newUser(String username, String password) {
        
        // Insert username and password to database
        database.insert("INSERT INTO users (username, password) VALUES (\"" + username + "\", \"" + password + "\");");
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
