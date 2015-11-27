/**
 * 
 */
package com.ludo.server;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import com.ludo.config.Config;

/**
 * @author Petter
 *
 */
public class ChatServer extends Thread {
    
    /**
     * Configurations
     */
    private static Config config = new Config();
    
    /**
     * Chat server port
     */
    private static int port;
    
    /**
     * List of connected user names
     */
    private static HashSet<String> users = new HashSet<String>();
    
    /**
     * List of writers for broadcasting messages to every user
     */
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    
    /**
     * Server Socket
     */
    ServerSocket listener;
    
    /**
     * The chat server constructor listens for new connections on
     * a specified port and creates new Handler objects
     * to handle server communications.
     * @throws Exception
     */
    public ChatServer() throws Exception {
        
        // Set port
        port = Integer.parseInt(config.getConfig("chatPort"));
        
        System.out.println("Chat server running on port " + port);
        
    }
    
    public void run() {
        
        // Open server socket for specified port.
        try {
            this.listener = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // On new connections, start a new thread to handle communications.
        try {
            while(true) {
                new Handler(this.listener.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                this.listener.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            this.socket = socket;
        }
        
        /**
         * Running the chat communication for input and outputs.
         * It will first handle login and register communication with
         * the client, then proceed to handle chat messages and logout requests.
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
                    
                    // If client is attempting to register a new user REGISTER <username> <password>
                    if(this.request.startsWith("REGISTER")) {
                        
                        /**
                         * Arguments
                         * 
                         * args[0] = REGISTER
                         * args[1] = username
                         * args[2] = password
                         */
                        
                        // Register username if not already taken.
                        if(!userHandler.usernameTaken(args[1])) {
                            userHandler.newUser(args[1], args[2]);
                            out.println("REGISTERACCEPTED");
                        } else {
                            out.println("ALREADYEXISTS");
                        }
                    }
                    
                    // If client is attempting to log in with LOGIN <username> <password>
                    if(this.request.startsWith("LOGIN") && args.length == 3) {
                        
                        this.username = args[1].toLowerCase();
                        this.password = args[2];
                        
                        // Attempt to authenticate user. Make sure the users list is used synchronously
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
                        
                        // Write chat to file
                        FileWriter fw = new FileWriter("chatlog.dat", true);
                        fw.write(this.username + ": " + this.request.substring("MESSAGE ".length()) + "\n");
                        fw.close();
                    }
                    
                    // Manual logout from client
                    if(this.request.startsWith("LOGOUT")) {
                        
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
                 * Cleanup when the client is disconnected.
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
