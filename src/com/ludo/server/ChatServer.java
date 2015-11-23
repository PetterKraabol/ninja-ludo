/**
 * 
 */
package com.ludo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

/**
 * @author Petter
 *
 */
public class ChatServer {
    
    /**
     * Chat server port
     */
    private static final int port = 4040;
    
    /**
     * List of connected user names
     */
    private static HashSet<String> users = new HashSet<String>();
    
    /**
     * List of writers for broadcasting messages to every user
     */
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    
    /**
     * The chat server contrusctor listens for new connections on
     * a specified port and creates new Handler objects
     * to handle server communications.
     * @throws Exception
     */
    public ChatServer() throws Exception {
        System.out.println("Chat server listening on port " + port);
        ServerSocket listener = new ServerSocket(port);
        try {
            while(true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }
    
    /**
     * Handles communication between a client and the server.
     * @author Petter
     *
     */
    private static class Handler extends Thread {
        private String username;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private UserHandler userHandler = new UserHandler();
        
        /**
         * Sets the socket for communication between the new
         * client and the server.
         * @param socket
         */
        public Handler(Socket socket) {
            System.out.println("Handling a new connection");
            this.socket = socket;
        }
        
        /**
         * Running the chat communication for input and outputs
         */
        public void run() {
            System.out.println("Run");
            try {
                
                // Input reader (from client)
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                // Output printer (to client)
                out = new PrintWriter(socket.getOutputStream(), true);
                
                /**
                 * First, send a login request command REQUESTLOGIN
                 * continuously to client until login data has been received.
                 */
                while(true) {
                    out.println("LOGINREQUEST");
                    username = in.readLine();
                    
                    // If no login data is received, keep reading inputs
                    if (username == null) {
                        return;
                    }
                    
                    // If login data has been received, attempt to authenticate user.
                    synchronized(users) {
                        if(!users.contains(username)) {
                            System.out.println("New user: " + username);
                            if(userHandler.authenticateUser(username, "password")) {
                                System.out.println("New user: " + username);
                                users.add(username);
                                break;
                            } else {
                                out.println("LOGINDENIED");
                            }
                        }
                    }
                }
                
                // Report back to client that login authentication succeeded.
                out.println("LOGINACCEPTED");
                writers.add(out);
                
                /**
                 * Handle incoming chat messages from client and
                 * broadcast them to every connected client.
                 */
                while(true) {
                    String input = in.readLine();
                    
                    if(input == null) {
                        return;
                    }
                    
                    for(PrintWriter writer : writers) {
                        writer.println("MESSAGE " + username + " " + input);
                    }
                    
                }
                
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                
                if(username != null) {
                    users.remove(username);
                }
                
                if(out != null) {
                    writers.remove(out);
                }
                
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing socket for " + username + ": " + e);
                }
            }
        }
    }
    
}
