/**
 * 
 */
package com.ludo.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Petter
 *
 */
public class Client {
    
    /**
     * Default hostname
     */
    private static String hostname = "localhost";
    
    /**
     * Default port
     */
    private static int port = 4040;
    
    /**
     * Settings map
     */
    private static Map<String, String> config = new HashMap<String, String>();
    
    public Client() throws Exception {
        System.out.println("Client");
        //Server server = new Server(hostname, port);
        //server.connect();
        ChatClient chatClient = new ChatClient();
    }
    
    /**
     * @param args
     * @throws IOException 
     * @throws UnknownHostException 
     */
    public static void main(String[] args) throws Exception {
        
        Client client = new Client();

    }

}
