/**
 * 
 */
package com.ludo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.ludo.client.controllers.MainController;
import com.ludo.config.Config;
import com.ludo.i18n.MessageBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * @author Petter
 *
 */
public class GameManager {
    /**
     * MessageBundle for i18n
     */
    private MessageBundle messageBundle = new MessageBundle();
    
    /**
     * JavaFX Scene
     */
    private Scene scene;
    
    /**
     * JavaFX Stage
     */
    private Stage stage;
    
    /**
     * Socket connection to server
     */
    private Socket socket;
    
    /**
     * Input from server
     */
    private BufferedReader in;
    
    /**
     * Output to server
     */
    private PrintWriter out;
    
    /**
     * Application configuration
     */
    private Config config = new Config();
    
    /**
     * Username
     */
    private String username;
    
    /**
     * Game thread to handle game requests
     */
    Thread gameThread;
    
    
    /**
     * TODO Description
     * @param stage
     * @param scene
     * @param username
     */
    public GameManager(Stage stage, Scene scene, String username) {
        
        // Scene
        this.scene = scene;
        this.stage = stage;
        
        // Display login screen first
        showGameQueueView();
        
        // Set socket to connect to server.
        this.socket = connectToServer();
    }
    
    /**
     * Connect to remote game server
     * @return Socket socket connection to game server
     */
    private Socket connectToServer() {
        Socket socket = null;
        
        // Connect to server
        try {
            
            // Create new socket connectin with server
            socket = new Socket(config.getConfig("ipaddress"), Integer.parseInt(config.getConfig("gamePort")));
            
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error connecting to game server");
            e.printStackTrace();
        }
        
        // Open I/O stream with server
        try {
            this.in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error establishing I/O with game server");
            e.printStackTrace();
        }
        
        return socket;
    }
    
    /**
     * Show game queue view
     */
    public void showGameQueueView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ludo/client/views/GameQueueView.fxml"));
            scene.setRoot((Parent) loader.load());
            stage.setTitle(messageBundle.retriveText("main.topText"));
            stage.sizeToScene();
            
            // Main View Controller
            MainController controller = loader.<MainController>getController();
            //-----controller.initManager(this, this.out);
            
        } catch(IOException e) {
            // Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error showing main view: " + e);
        }
    }
    
    private static class GameHandler extends Thread {
        private Circle[] pieces;
        private BufferedReader in;
        private Boolean running;
        
        public GameHandler(Circle[] pieces, BufferedReader in) {
            this.pieces = pieces;
            this.in= in;
        }
        
        /**
         * "Kill" thread
         */
        public void kill() {
            System.out.println("Killing thread");
            this.running = false;
        }
        
        public void run() {
            
            // Listen for incoming server messages
            while(this.running) {
                
             // Try reading from server
                try {
                    String request = this.in.readLine();
                    
                    if(request == null) {
                        continue;
                    }
                    
                    String[] args = request.split(" ");
                    
                    // Test message
                    if(request.startsWith("TEST")) {
                        System.out.println("TEST from server");
                    }
                    
                } catch (IOException e) {
                    System.out.println("Lost connection to game server.");
                    break;
                }
                
            }
            
        }
        
    }

}
