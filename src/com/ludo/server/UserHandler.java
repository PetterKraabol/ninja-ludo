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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return false;
    }
    
    public void newUser() {
        
    }
    
    public boolean userExists() {
        return true;
    }

}
