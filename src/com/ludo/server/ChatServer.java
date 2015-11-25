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
        private String password;
        private String request;
        private String[] args;
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
            try {
                
                // Input reader (from client)
                this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                
                // Output printer (to client)
                this.out = new PrintWriter(this.socket.getOutputStream(), true);
                
                /**
                 * First, send a login request command REQUESTLOGIN
                 * continuously to client until login data has been received.
                 */
                while(true) {
                    out.println("LOGINREQUEST");
                    this.request = in.readLine();
                    
                    // If no login data is received, keep reading inputs
                    if (request == null) {
                        return;
                    }
                    
                    System.out.println(request);
                    
                    // Split incoming message
                    this.args = this.request.split(" ");
                    
                    // If client is attempting to log in with LOGIN <username> <password>
                    if(this.request.startsWith("LOGIN") && args.length == 3) {
                        
                        System.out.println("Login reuqest:" + request);
                        
                        this.username = args[1].toLowerCase();
                        this.password = args[2];
                        
                        // Attempt to authenticate user
                        synchronized(users) {
                            if(userHandler.authenticateUser(username, password)) {
                                if(!users.contains(username)) {
                                    users.add(username);
                                    break;
                                } else {
                                    out.println("ALREADYLOGGEDIN");
                                }
                            } else {
                                out.println("LOGINDENIED");
                            }
                        }
                        
                    }
                }
                
                // Report back to client that login authentication succeeded.
                this.out.println("LOGINACCEPTED");
                writers.add(out);
                
                /**
                 * Handle incoming chat messages from client and
                 * broadcast them to every connected client.
                 */
                while(true) {
                    this.request = in.readLine();
                    
                    // Skip if no request
                    if(request == null) {
                        continue;
                    }
                    
                    // Split request into arguments
                    this.args = request.split(" ");
                    
                    // If this is a MESSAGE request from client, broadcast to everyone in chat
                    if(this.request.startsWith("MESSAGE") && this.args.length >= 2) {
                        
                        for(PrintWriter writer : writers) {
                            writer.println("MESSAGE " + this.username + " " + this.request.substring("MESSAGE ".length()));
                        }
                    }
                    
                    // Manual logout from client
                    if(this.request.startsWith("LOGOUT")) {
                        
                        System.out.println(this.username + " logged out.");
                        
                        // Remove username from users list
                        if(this.username != null) {
                            users.remove(this.username);
                        }
                        
                        // Remove client from writers list
                        if(this.out != null) {
                            writers.remove(this.out);
                        }
                        
                        // Close socket with client
                        try {
                            this.socket.close();
                        } catch (IOException e) {
                            System.out.println("Error closing socket for " + username + ": " + e);
                        }
                    }
                    
                }
                
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                
                /**
                 * On disconnecting, do some cleanup
                 */
                
                // Remove username from users list
                if(username != null) {
                    users.remove(this.username);
                }
                
                // Remove client from writers list
                if(out != null) {
                    writers.remove(this.out);
                }
                
                // Close socket with client
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing socket for " + username + ": " + e);
                }
            }
        }
    }
    
}
