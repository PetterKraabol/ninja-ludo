package com.ludo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JOptionPane;

import com.ludo.client.controllers.GameController;
import com.ludo.client.controllers.GameQueueController;
import com.ludo.client.controllers.LoginController;
import com.ludo.client.controllers.MainController;
import com.ludo.config.Config;
import com.ludo.i18n.MessageBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * LoginManager handles login, registration and chat messages.
 * @author Petter
 *
 */
public class ClientManager {
    
    /**
     * MessageBundle for I18N
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
     * Socket connection to chat server
     */
    private Socket chatSocket;
    private Socket gameSocket;
    
    /**
     * Input from server
     */
    private BufferedReader chatIn;
    private BufferedReader gameIn;
    
    /**
     * Output to server
     */
    private PrintWriter chatOut;
    private PrintWriter gameOut;
    
    /**
     * Application configuration
     */
    private Config config = new Config();
    
    /**
     * Username
     */
    private String username;
    
    /**
     * Chat thread to handle incoming chat messages
     */
    Thread chatThread;
    Thread gameThread;
    
    /**
     * Constructor to hold scene and create a socket connection to the server.
     * @param scene
     */
    public ClientManager(Scene scene, Stage stage) {
        
        // Scene
        this.scene = scene;
        this.stage = stage;
        
        // Display login screen first
        showLoginScreen();
        
        // Set socket to connect to server.
        this.chatSocket = connectToChatServer(true);
        
    }
    
    /**
     * Attempt to register a user by sending a REGISTER <username> <password> to server
     * @param username
     * @param password
     * @return int Response 0=REGISTERACCEPTED, 1=ALREADYEXISTS
     */
    public int register(String username, String password) {
        
        // Send register request to server with username and password
        this.chatOut.println("REGISTER " + username + " " + password);
        
        // Listen for response from server
        String line = null;
        
        while(true) {
            
            // Read server message
            try {
                line = chatIn.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            // Continue if no input
            if(line == null) {
                continue;
            }
            
            // Registration accepted
            if(line.startsWith("REGISTERACCEPTED")) {
                return 0;
            }
            
            // Username already exists
            if(line.startsWith("ALREADYEXISTS")) {
                return 1;
            }
            
        }
        
    }
    
    /**
     * Authenticate user by sending a login request to the server and wait for a reply
     * @param username
     * @param password
     * @return int Response 0=LOGINACCEPTED, 1=LOGINDENIED, 2=ALREADYLOGGEDIN
     */
    public int authenticate(String username, String password) {
        
        this.username = username;
        
        // Send login request to server with username and password
        this.chatOut.println("LOGIN " + this.username + " " + password);
        
        // Listen for response to see if it was accepted or not
        String line = null;
        
        while(true) {
            
            // Try reading server message
            try {
                line = chatIn.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            // Continue if no input
            if(line == null) {
                continue;
            }
            
            // Login accepted
            if(line.startsWith("LOGINACCEPTED")) {
                return 0;
            }
            
            // Login denied
            if(line.startsWith("LOGINDENIED")) {
                return 1;
            }
            
            // Already logged in
            if(line.startsWith("ALREADYLOGGEDIN")) {
                return 2;
            }
            
            
        }
    }
    
    /**
     * If the user logs out, refresh the socket and display the login screen
     */
    public void logout() {
        
        // Send logout request
        this.chatOut.println("LOGOUT");
        
        // Put thread to sleep and kill it before disconnecting the socket.
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ((ChatHandler) this.chatThread).kill();
        
        // Close current socket
        try {
            this.chatSocket.close();
        } catch (IOException e) {
            System.out.println("Error disconnecting from server");
            e.printStackTrace();
        }
        
        // Clear username variable
        this.username = null;
        
        // Reconnect to server with a new socket
        this.chatSocket = connectToChatServer(true);
        
        // Display login screen to user
        showLoginScreen();
    }
    
    /**
     * Connect to remote chat server
     * @param Boolean useConfig WHether the program should use the config file or not to find IP address
     * @return Socket socket connection to chat server
     */
    private Socket connectToChatServer(Boolean useConfig) {
        Socket socket = null;
        
        // Connect to server
        try {
            
            // Create new socket connectin with server
            socket = new Socket(askForIPAddress(useConfig), Integer.parseInt(config.getConfig("chatPort")));
            
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error connecting to chat server");
            e.printStackTrace();
        }
        
        // Open I/O stream with server
        try {
            this.chatIn  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.chatOut = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error establishing I/O with chat server");
            e.printStackTrace();
        }
        
        return socket;
    }
    
    /**
     * Let user change server IP address
     */
    public void changeServerIP() {
        this.chatSocket = connectToChatServer(false);
    }
    
    /**
     * Ask for Server IP Address
     * @return String IP Address
     */
    private String askForIPAddress(Boolean useConfig) {
        
        if(useConfig && config.getConfig("ipaddress") != null && !config.getConfig("ipaddress").equals("null")) {
            return config.getConfig("ipaddress");
        } else {
            System.out.println("false");
            String address =  JOptionPane.showInputDialog(null,
                                messageBundle.retriveText("connect.question"),
                                messageBundle.retriveText("connect.title"),
                                JOptionPane.QUESTION_MESSAGE);
            config.setConfig("ipaddress", address);
            
            return address;
        }
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
    public void showMainScreen() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ludo/client/views/MainView.fxml"));
            scene.setRoot((Parent) loader.load());
            stage.setTitle(messageBundle.retriveText("main.topText"));
            stage.sizeToScene();
            
            // Main View Controller
            MainController controller = loader.<MainController>getController();
            controller.initManager(this, this.chatOut);
            
            // Start chat thread
            this.chatThread = new ChatHandler(controller, chatIn);
            this.chatThread.start();
            
        } catch(IOException e) {
            System.out.println("Error showing main view: " + e);
        }
    }
    
    /**
     * When user closes client window, send LOGOUT request to server
     */
    public void closeWindow() {
        this.chatOut.println("User closed window");
    }
    
    /**
     * The chat handler takes care of incoming chat messages.
     * @author Petter
     *
     */
    private static class ChatHandler extends Thread {
        
        /**
         * Global Chat text area
         */
        private TextArea globalChat;
        
        /**
         * Request from chat server
         */
        private String request;
        
        /**
         * Request arguments
         */
        private String[] args;
        
        /**
         * Input from chat server
         */
        private BufferedReader in;
        
        /**
         * Decides if thread should be listening on server messages
         */
        private Boolean running;
        
        /**
         * MainController to access JavaFX elements.
         */
        private MainController controller;
        
        /**
         * Set controller and the input reader from chat server
         * @param controller MainController for JavaFX elements
         * @param in Reads messages coming in from chat server
         */
        public ChatHandler(MainController controller, BufferedReader in) {
            this.controller = controller;
            this.in = in;
            this.running = true;
            
            // JavaFX elements from controller
            this.globalChat = controller.getGlobalChat();
        }
        
        /**
         * "Kill" thread to make it stop listening for new server messages
         */
        public void kill() {
            System.out.println("Killing thread");
            this.running = false;
        }
        
        /**
         * Run thread that listens for incoming chat messages
         */
        public void run() {
            
            // Listen for incoming messages while thread is running
            while(this.running) {
                
                // Try reading from server
                try {
                    this.request = this.in.readLine();
                    
                    if(this.request == null) {
                        continue;
                    }
                    
                    if(this.request.startsWith("MESSAGE")) {
                        this.args = this.request.split(" ");
                        
                        // Add text to chat
                        this.globalChat.appendText(args[1] + ": " + this.request.substring("MESSAGE ".length() + this.args[1].length() + 1) + "\n");
                        
                    }
                    
                } catch (IOException e) {
                    System.out.println("Lost connection to chat server.");
                    break;
                }
            }
            
        }
        
    }
    
    /**
     * ------------------ Game Server ------------------
     */
    
    /**
     * Connect to remote game server
     * @return Socket socket connection to game server
     */
    private Socket connectToGameServer() {
        Socket socket = null;
        
        // Connect to server
        try {
            
            // Create new socket connecting with server
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
            this.gameIn  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.gameOut = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error establishing I/O with game server");
            e.printStackTrace();
        }
        
        return socket;
    }
    
    /**
     * Show the game queue window and put the user in a game queue before starting a game session.
     */
    public void showGameQueue() {
        
        // Connect to game server
        this.gameSocket = connectToGameServer();
        
        try {
            
            // Change scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ludo/client/views/GameQueueView.fxml"));
            scene.setRoot((Parent) loader.load());
            stage.setTitle("Game");
            stage.sizeToScene();
            
            // Game queue controller
            GameQueueController controller = loader.<GameQueueController>getController();
            controller.initManager(this, this.gameOut);
            
            // Wait until game is ready
            
            String line = null;
            while(true) {
                
                // Try reading server message
                try {
                    line = gameIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                if(line == null) {
                    continue;
                }
                
                if(line.startsWith("WAITING")) {
                    continue;
                }
                
                if(line.startsWith("NEWUSERINQUEUE")) {
                    System.out.println("NEW USER");
                }
                
                if(line.startsWith("STARTGAME")) {
                    System.out.println("Starting game");
                    showGameScreen();
                    break;
                }
                
            }
            
            showGameScreen();
            
        } catch(IOException e) {
            System.out.println("Error showing game queue view: " + e);
        }
    }
    
    /**
     * Show game view
     */
    public void showGameScreen() {
        
        // Connect to game server
        this.gameSocket = connectToGameServer();
        
        try {
            // Change view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ludo/client/views/GameView.fxml"));
            scene.setRoot((Parent) loader.load());
            stage.setTitle(messageBundle.retriveText("main.topText"));
            stage.sizeToScene();
            
            // Main View Controller
            GameController controller = loader.<GameController>getController();
            controller.initManager(this, this.gameOut);
            
            // Start chat thread
            this.gameThread = new GameHandler(controller, this.gameIn);
            this.gameThread.start();
            
        } catch(IOException e) {
            // Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error showing main view: " + e);
        }
    }
    
    /**
     * Game Handler
     * TODO Description
     * @author Petter
     *
     */
    private static class GameHandler extends Thread {
        
        /**
         * List of pieces
         * 
         * Index
         * 1-4   : red
         * 5-8   : blue
         * 9-12  : yellow
         * 13-16 : green
         */
        private List<Circle> pieces;
        
        /**
         * Player color
         */
        private String color;
        
        /**
         * GameController for accessing JavaFX elements
         */
        private GameController controller;        
        
        /**
         * Input from server
         */
        private BufferedReader in;
        
        /**
         * Used to indicate if thread is running or not.
         */
        private Boolean running = true;
        
        public GameHandler(GameController controller, BufferedReader in) {
            this.controller = controller;
            this.in = in;
            
            // JavaFX elements
            this.pieces = controller.getPieces();
        }
        
        /**
         * "Kill" thread
         */
        public void kill() {
            System.out.println("Killing thread");
            this.running = false;
        }
        
        /**
         * Run thread to read incoming game server messsages
         */
        public void run() {
            
            // Input from server
            String line = null;
            
            /**
             * A game has been created and the player
             * is waiting for other players to connect.
             */
            
            // Tell user there is a game queue
            controller.waitingInQueue();
            
            while(true) {
                
                // Try reading server message
                try {
                    line = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                if(line == null) {
                    continue;
                }
                
                // Waiting for players...
                if(line.startsWith("WAITING")) {
                    continue;
                }
                
                // New user in queue
                if(line.startsWith("NEWUSERINQUEUE")) {
                    System.out.println("NEW USER");
                }
                
                // The game is starting. Broadcast: STARTGAME <player.getColor()>
                if(line.startsWith("STARTGAME")) {
                    System.out.println("Starting game");
                    
                    // Split to get color
                    String[] args = line.split(" ");
                    this.color = args[1];
                    
                    break;
                }
                
            }
            
            // Tell user the game is starting
            controller.gameHasStarted(this.color);
            
            /**
             * All players have connected and the game has started.
             */
            while(true) {
                System.out.println("Game loop started");
                break;
            }
            
            // Tell user there is a game queue
            controller.waitingInQueue();
            
        }
        
    }
}