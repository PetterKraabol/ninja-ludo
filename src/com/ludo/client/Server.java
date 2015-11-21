/**
 * 
 */
package com.ludo.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * @author Petter
 *
 */
public class Server {
    
    /**
     * Client socket
     */
    
    /**
     * Server hostname / IP Address
     */
    private static String hostname;
    
    /**
     * Server port
     */
    private static int port;
    
    /**
     * Server Input / Output
     */
    
    /**
     * Server Constructor
     * 
     * Pass a hostname and port for the remote server.
     * The constructor does establish a connection.
     * 
     * @param h hostame
     * @param p port number
     */
    public Server(String h, int p){
        hostname = h;
        port     = p;
    }
    
    /**
     * Attempt to connect to server
     */
    public void connect() throws Exception {
        Socket socket = new Socket(hostname, port);
        PrintStream out = new PrintStream(socket.getOutputStream());
        out.println("Hello to server from client");
        
        InputStreamReader in = new InputStreamReader(socket.getInputStream());
        BufferedReader buffer = new BufferedReader(in);
        
        String message = buffer.readLine();
        System.out.println(message);
    }
    
}
