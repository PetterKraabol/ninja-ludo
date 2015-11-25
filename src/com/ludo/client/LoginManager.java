package com.ludo.client;

import java.awt.DisplayMode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import com.ludo.client.controllers.LoginController;
import com.ludo.client.controllers.MainController;
import com.ludo.client.controllers.RegisterController;
import com.ludo.config.Config;
import com.ludo.i18n.MessageBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class LoginManager {
    
    /**
     * MessageBundle for I18N
     */
    private MessageBundle messageBundle = new MessageBundle();
    
    /**
     * JavaFX
     */
    private Scene scene;
    private Stage stage;
    
    /**
     * Socket connection to chat server
     */
    private Socket socket;
    
    // Input and Output from/to server
    private BufferedReader in;
    private PrintWriter out;
    
    /**
     * Application configuration
     */
    private Config config = new Config();
    
    // Username
    private String username = null;
    
    // Chat thread
    Thread chatThread = null;
    
    /**
     * Constructor to hold scene and create a socket connection to the server.
     * @param scene
     */
    public LoginManager(Scene scene, Stage stage) {
        
        // Scene
        this.scene = scene;
        this.stage = stage;
        
        // Display login screen first
        showLoginScreen();
        
        // Set socket to connect to server.
        this.socket = connectToServer();
        
    }
    
    /**
     * TODO Description
     */
    public boolean authenticate(String username, String password) {
        
        // Send login request to server with username and password
        this.out.println("LOGIN " + username + " " + password);
        
        // Listen for response to see if it was accepted or not
        String line = null;
        
        while(true) {
            
            // Try reading server message
            try {
                line = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            // Login accepted
            if(line.startsWith("LOGINACCEPTED")) {
                return true;
            }
            
            // Login denied
            if(line.startsWith("LOGINDENIED")) {
                return false;
            }
            
        }
    }
    
    /**
     * If the user logs out, refresh the socket and display the login screen
     */
    public void logout() {
        
        // Send logout request
        this.out.println("LOGOUT");
        
        // Put thread to sleep and kill it before disconnecting the socket.
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ((ChatHandler) this.chatThread).kill();
        
        // Close current socket
        try {
            this.socket.close();
        } catch (IOException e) {
            System.out.println("Error disconnecting from server");
            e.printStackTrace();
        }
        
        // Reconnect to server with a new socket
        this.socket = connectToServer();
        
        // Display login screen to user
        showLoginScreen();
    }
    
    /**
     * Connect to remote server
     * @return Socket socket connection to server
     */
    public Socket connectToServer() {
        Socket socket = null;
        
        // Connect to server
        try {
            
            // Create new socket connectin with server
            socket = new Socket(askForIPAddress(), Integer.parseInt(config.getConfig("chatPort")));
            
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error connecting to server");
            e.printStackTrace();
        }
        
        // Open I/O stream with server
        try {
            this.in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error establishing I/O with chat server");
            e.printStackTrace();
        }
        
        return socket;
    }
    
    /**
     * Ask for Server IP Address
     * @return String IP Address
     */
    public String askForIPAddress() {
        return JOptionPane.showInputDialog(null,
                messageBundle.retriveText("connect.question"),
                messageBundle.retriveText("connect.title"),
                JOptionPane.QUESTION_MESSAGE);
    }
    
    /**
     * Switch to login view
     */
    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ludo/client/views/LoginView.fxml"));
            scene.setRoot((Parent) loader.load());
            
            stage.setTitle(messageBundle.retriveText("login.topText"));
            stage.sizeToScene();
            
            // Login View Controller
            LoginController controller = loader.<LoginController>getController();
            controller.initManager(this);
        } catch(IOException e) {
            //Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error showing login screen: " + e);
        }
    }
    
    /**
     * Switch to main view
     */
    public void showMainView() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ludo/client/views/MainView.fxml"));
            scene.setRoot((Parent) loader.load());
            stage.setTitle(messageBundle.retriveText("main.topText"));
            stage.sizeToScene();
            
            // Main View Controller
            MainController controller = loader.<MainController>getController();
            controller.initManager(this, this.out);
            
            // Start chat thread
            this.chatThread = new ChatHandler(controller.getTextArea(), in);
            this.chatThread.start();
            
        } catch(IOException e) {
            // Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error showing main view: " + e);
        }
    }
    
    /**
     * Switch to registration view
     */
    public void showRegistrationView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ludo/client/views/RegisterView.fxml"));
            scene.setRoot((Parent) loader.load());
            stage.setTitle(messageBundle.retriveText("register.topText"));
            stage.sizeToScene();
            
            // Main View Controller
            RegisterController controller = loader.<RegisterController>getController();
            controller.initManager(this);
        } catch(IOException e) {
            // Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error showing registration view: " + e);
        }
    }
    
    /**
     * The chat handler takes care of incoming chat messages.
     * @author Petter
     *
     */
    private static class ChatHandler extends Thread {
        private TextArea chat;
        private String request;
        private String[] args;
        private BufferedReader in;
        private Boolean running;
        
        public ChatHandler(TextArea chat, BufferedReader in) {
            this.chat = chat;
            this.chat.setWrapText(true);
            this.in = in;
            this.running = true;
        }
        
        /**
         * "Kill" process
         */
        public void kill() {
            System.out.println("Killing thread");
            this.running = false;
        }
        
        private Boolean alive() {
            return this.running;
        }
        
        /**
         * Run thread that listens for incoming chat messages
         */
        public void run() {
            
            // Listen for incoming messages while thread is running
            while(alive()) {
                
                // Try reading from server
                try {
                    this.request = this.in.readLine();
                    
                    if(this.request == null) {
                        continue;
                    }
                    
                    if(this.request.startsWith("MESSAGE")) {
                        this.args = this.request.split(" ");
                        
                        // Add text to chat
                        this.chat.appendText(args[1] + ": " + this.request.substring("MESSAGE ".length() + this.args[1].length() + 1) + "\n");
                        
                    }
                    
                } catch (IOException e) {
                    System.out.println("Couldn't listen for incoming server messages for chat");
                    e.printStackTrace();
                }
            }
            
            // Close socket
            
        }
        
    }
}
