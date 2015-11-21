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
    
    public static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        
        System.out.println("Server is running");
        
        ServerSocket listener = new ServerSocket(port);
        try {
            while(true) {
                new ServerHandler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }

    }

}
