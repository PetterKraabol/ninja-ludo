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
public class ChatServer extends Server {
    private String name;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private UserHandler userHandler = new UserHandler();
    
    public ChatServer(Socket socket) {
        System.out.print("Chat Server Handler");
        this.socket = socket;
    }
    
    public void run() {
        
        try {
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            while(true) {
                out.println("SUBMITNAME");
                name = in.readLine();
                if(name == null) {
                    return;
                }
                
                synchronized(users) {
                    if(!users.contains(name)) {
                        System.out.println("New user: " + name);
                        if(userHandler.authenticateUser(name, "password")) {
                            System.out.println("New user: " + name);
                            users.add(name);
                            break;
                        }
                    }
                }
            }
            
            out.println("NAMEACCEPTED");
            writers.add(out);
            
            // Accept chat messages and broadcast to everyone
            while(true) {
                String input = in.readLine();
                if(input == null) {
                    return;
                }
                
                for(PrintWriter writer : writers) {
                    writer.println("MESSAGE " + name + ": " + input);
                }
            }
            
        } catch(IOException e) {
            System.out.println(e);
        } finally {
            
            // Client is leaving
            if(name != null) users.remove(name);
            if(out  != null) writers.remove(out);
            
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Could not close socket: " + e);
            }
            
        }
        
    }
}
