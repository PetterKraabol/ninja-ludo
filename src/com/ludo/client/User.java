package com.ludo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class User {

	private BufferedReader in;
	private PrintWriter out;
	
    public User() {
        
    }
    /*
    // Return true if login is correct
    public boolean login(String username, String password, String serverAdress) throws IOException {
        String serverAddress = serverAdress;
        Socket socket = new Socket(serverAddress, 4040);
        in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
      
        String line = in.readLine();
        	
        //out.println("LOGIN " + username + " " + password);
        out.println(username);
        System.out.println("LOGIN " + username + " " + password);
        	
       
        	
     
        
        
    } */
    
    // 
    public boolean checkUsername(String username) {
        
        // Return false if username is not taken
        
        
        // Return true if username is taken
        return false;
    }
}