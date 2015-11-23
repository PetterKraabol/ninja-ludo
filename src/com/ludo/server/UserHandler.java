/**
 * 
 */
package com.ludo.server;

/**
 * @author Petter
 *
 */
public class UserHandler {
    DatabaseHandler database = new DatabaseHandler();
    
    
    public void authenticateUser(String username, String password) {
        
        System.out.println("Trying to authenticate " + username + ":" + password);
        System.out.println(database.query("SELECT COUNT(*) FROM users WHERE user =  " + username + " AND password = " + password));
        
    }
    
    public void newUser() {
        
    }
    
    public boolean userExists() {
        return true;
    }

}
