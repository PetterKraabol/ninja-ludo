/**
 * 
 */
package com.ludo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Petter
 *
 */
public class Chat {
    
    /**
     * User class for chat
     * @author Petter
     *
     */
    class User extends Thread {
        Socket socket;
        String username;
        BufferedReader input;
        PrintWriter output;
        
        public User(Socket socket, String username) {
            this.socket = socket;
            this.username = username;
            
            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                
                output = new PrintWriter(socket.getOutputStream(), true);
                
                output.println("WELCOME " + this.username);
            } catch (IOException e) {
                System.out.println(this.username + " disconnected.");
            }
        }
        
        /**
         * Run chat and listen for commands
         */
        public void run() {
            try {
                
                // Listen for commands
                while (true) {
                    String command = input.readLine();
                    
                    if(command.startsWith("TEST")) {
                        System.out.println("Client sent a TEST command");
                    } else if(command.startsWith("QUIT")) {
                        return;
                    }
                }
                
            } catch (IOException e) {
                System.out.println(this.username + " disconnected");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing socket for " + this.username + ": " + e);
                }
            }
        }
    }
}
