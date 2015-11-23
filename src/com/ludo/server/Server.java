/**
 * 
 */
package com.ludo.server;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author Petter
 *
 */
public class Server extends Thread {
    
    /**
     * Listening port
     */
    private static int port = 4040;
    
    /**
     * Client connections
     */
    public static ArrayList<Socket> connections = new ArrayList<Socket>();
    
    /**
     * List of current users
     */
    public static HashSet<String> users = new HashSet<String>();
    
    /**
     * List of writers for input and output
     * from and to connected users.
     */
    public static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    /**
     * TODO: Description
     * 
     * @param args Run arguments
     * @throws Exception Exceptions
     */
    public static void main(String[] args) throws Exception {
        
        System.out.println("Server is running");
        
        // Database
        DatabaseHandler database = new DatabaseHandler();
        System.out.println("Reset");
        database.resetTables();
        
        System.out.println("Drop");
        database.dropTables();
        
        System.out.println("Create");
        database.createTables();
        
        // Listeners
        ServerSocket listener = new ServerSocket(port);
        try {
            while(true) {
                new ChatServer(listener.accept()).start();
            }
        } finally {
            listener.close();
        }

    }

}
