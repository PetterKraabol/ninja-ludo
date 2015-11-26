/**
 * 
 */
package com.ludo.server;

/**
 * @author Petter
 *
 */
public class Server extends Thread {

    /**
     * TODO: Description
     * 
     * @param args Run arguments
     * @throws Exception Exceptions
     */
    public static void main(String[] args) throws Exception {
        
        System.out.println("Starting servers...");
        
        // Chat Server
        new ChatServer().start();
        
        // Game Server
        new GameServer().start();
      

    }

}
