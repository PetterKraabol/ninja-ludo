/**
 * 
 */
package com.ludo.server;

import java.net.Socket;

/**
 * @author Petter
 *
 */
public class User {
    private String username;
    private Socket socket;
    
    public User() {
        
    }
    
    /**
     * Get the user's username
     * @return username
     */
    public String username() {
        return username;
    }
    
    /**
     * Get the user's socket
     * @return socket
     */
    public Socket socket() {
        return socket;
    }

}
