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
import com.ludo.client.controllers.LoginController;
import com.ludo.client.controllers.MainController;
import com.ludo.config.Config;
import com.ludo.i18n.MessageBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

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
     * ----------------------------- Game Server Handler -----------------------------
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
            this.gameThread = new GameHandler(controller, this.gameIn, this.gameOut, this.username);
            this.gameThread.start();
            
        } catch(IOException e) {
            // Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error showing game screen: " + e);
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
         * Fields positions
         */
        private Coordinates[] fields;
        
        /**
         * Indicate positions
         */
        private Coordinates[] indicators;
        
        /**
         * Indicator ellipse
         */
        private Ellipse indicator;
        
        /**
         * Player color
         */
        private String color;
        
        /**
         * Remember who's turn it is by color.
         */
        private String turn;
        
        /**
         * Last dice roll
         */
        private int dice;
        
        /**
         * GameController for accessing JavaFX elements
         */
        private GameController controller;
        
        /**
         * Game Chat Manager to handle private game chats
         */
        private GameChatHandler gameChat;
        
        /**
         * Input from server
         */
        private BufferedReader in;
        
        /**
         * Output to server
         */
        private PrintWriter out;
        
        /**
         * Used to indicate if thread is running or not.
         */
        private Boolean running = true;
        
        /**
         * Username
         */
        private String username;
        
        /**
         * Set GameController and get the input buffer reader to get output from server
         * @param controller To interact with the GameController
         * @param in To read output from server
         * @param out Send messages to server
         * @param username This player's username
         */
        public GameHandler(GameController controller, BufferedReader in, PrintWriter out, String username) {
            this.controller = controller;
            this.username = username;
            this.in = in;
            this.out = out;
            
            // JavaFX elements
            this.pieces     = controller.getPieces();
            this.fields     = controller.getCoordinates();
            this.indicators = controller.getIndicators();
            this.indicator  = controller.getIndicator();
            
            // Game Chat
            this.gameChat = new GameChatHandler(controller, in);
            this.gameChat.start();
        }
        
        /**
         * "Kill" thread
         */
        public void kill() {
            System.out.println("Killing thread");
            this.running = false;
        }
        
        /**
         * Calculate position and move the piece
         * @param pieceId
         * @param color
         * @param steps
         */
        private void movePiece(int pieceId, String color, int fieldsMoved) {
            
            /**
             * Offset in list of pieces.
             * 1-4   : red
             * 5-8   : blue
             * 9-12  : yellow
             * 13-16 : green
             */
            int pieceIdOffset = 0;
            
            /**
             * Piece offset from global "start" position
             * of map which is set to red's first field.
             */
            int positionOffset = 0;
            
            /**
             * Position as of where the piece will jump onto the
             * colored field to goal.
             */
            int positionHop = 0;
            
            /**
             * Where to hop to (first field of colored road to goal)
             */
            int positionHopTo = 0;
            
            // Set the above values based on which color is moving.
            if(color.equals("red")) {
                pieceIdOffset = 0;
                positionOffset = 0;
                positionHop = 1;
                positionHopTo = 53;
            }
            
            if(color.equals("blue")) {
                pieceIdOffset = 4;
                positionOffset = 13;
                positionHop = 14;
                positionHopTo = 59;
            }
            
            if(color.equals("yellow")) {
                pieceIdOffset = 8;
                positionOffset = 26;
                positionHop = 27;
                positionHopTo = 65;
            }
            
            if(color.equals("green")) {
                pieceIdOffset = 12;
                positionOffset = 39;
                positionHop = 40;
                positionHopTo = 71;
            }
            
            // Default values that are shared between every color
            
            /**
             * Which field id to wrap around and continue at 1.
             */
            int positionWrap = 52;
            
            /**
             * If the user has wrapped (made 1 lap)
             */
            boolean hasWrapped = false;
            
            
            /**
             * Calculate position
             */
            int position = fieldsMoved + positionOffset;
            
            // Check if piece have wrapped
            if(position > positionWrap) {
                hasWrapped = true;
                position = position % positionWrap;
            }
            
            // Check if piece has hopped
            if(position > positionHop && hasWrapped) {
                position = position % positionHop + positionHopTo;
            }
            
            // Movie piece's X and Y coordinated to the calculated position.
            this.pieces.get(pieceId + pieceIdOffset).setLayoutX(fields[position].getXCoordinates());
            this.pieces.get(pieceId + pieceIdOffset).setLayoutY(fields[position].getYCoordinates());
            
        }
        
        /**
         * Move indicator, which indicated who has this turn.
         * @param color
         */
        public void moveIndicator(String color) {
            
            int colorNumber = 1;
            if(color.equals("red"))    colorNumber = 1;
            if(color.equals("blue"))   colorNumber = 2;
            if(color.equals("yellow")) colorNumber = 3;
            if(color.equals("green"))  colorNumber = 4;
            
            // Move indicator
            this.indicator.setLayoutX(indicators[colorNumber].getXCoordinates());
            this.indicator.setLayoutY(indicators[colorNumber].getYCoordinates());
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
                
                // If not message, continue
                if(line == null) {
                    continue;
                }
                
                // Server is requesting username
                if(line.startsWith("USERNAMEREQUEST")) {
                    out.println("USERNAME " + this.username);
                }
                
                // New user in queue
                if(line.startsWith("NEWUSERINQUEUE")) {
                    // New user in queue
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
            System.out.println("Game loop started");
            String[] args;
            while(true) {
                
                // Get turn
                while(true) {
                    try {
                        line = in.readLine();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    
                    // If no message from server
                    if(line == null) {
                        continue;
                    }
                    
                    // Turn message received
                    if(line.startsWith("TURN")) {
                        args = line.split(" ");
                        
                        this.turn = args[1];
                        this.dice = Integer.parseInt(args[2]);
                        
                        // It's your turn
                        if(this.turn.equals(this.color)) {
                            System.out.println("My turn");
                            controller.itsYourTurn(this.dice);
                        }else {
                            System.out.println(this.turn + " turn");
                        }
                        
                        // Move indicator
                        this.moveIndicator(this.turn);
                        
                        break;
                        
                    }
                }
                
                // Get move from whoever's turn it is
                while(true) {
                    try {
                        line = in.readLine();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    
                    // If no message from server
                    if(line == null) {
                        continue;
                    }
                    
                    // Move request received: MOVE <pieceId (1-4)> <position>
                    if(line.startsWith("MOVE")) {
                        args = line.split(" ");
                        
                        System.out.println(line);
                        
                        this.movePiece(Integer.parseInt(args[1]), this.turn, Integer.parseInt(args[2]));
                        
                        break;
                    }
                    
                    // If move was denied
                    if(line.startsWith("MOVEDENIED")) {
                    	controller.moveDenied();
                    }
                }
                
                // Check if there is a winner
                while(true) {
                    try {
                        line = in.readLine();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    
                    // If no message from server
                    if(line == null) {
                        continue;
                    }
                    
                    // No winners
                    if(line.startsWith("NOWIN")) {
                        break;
                    }
                    
                    // There is a winner
                    if(line.startsWith("WIN")) {
                        args = line.split(" ");
                        
                        // Show end screen with winner color
                        this.controller.endGame(args[1]);
                    }
                }
                
                
            }
            
        }
        
        /**
         * GameChatHandler handles private game chats through the game
         * server. These messages are broadcasted to everyone in the game session.
         * @author Petter
         *
         */
        public class GameChatHandler extends Thread {
            
            /**
             * GameController for JavaFX objects and running controller functions
             */
            private GameController controller;
            
            /**
             * Game chat
             */
            private TextArea gameChat;
            
            /**
             * Buffered reader to read incoming server messages
             */
            private BufferedReader in;
            
            /**
             * Set controller and buffered reader for incoming server messages
             * @param controller GameVier controller for accessing JavaFX objects
             * @param in input from server
             */
            public GameChatHandler(GameController controller, BufferedReader in) {
                this.controller = controller;
                this.in = in;
                
                // Java FX objects
                this.gameChat = controller.getGameChat();
            }
            
            /**
             * Run thread to handle incoming server messages
             */
            public void run() {
                String line = null;
                String[] args;
                
                // Listen for new messages
                while(true) {
                    
                    try {
                        line = in.readLine();
                    } catch (IOException e) {
                        break;
                    }
                    
                    // If incoming message is empty
                    if(line == null) {
                        continue;
                    }
                    
                    // If incoming message, append it to to the chat
                    if(line.startsWith("MESSAGE")) {
                        args = line.split(" ");
                        
                        // Add text to chat
                        this.gameChat.appendText(args[1].substring(0, 1).toUpperCase() + args[1].substring(1) + " Ninja: " + line.substring("MESSAGE ".length() + args[1].length() + 1) + "\n");
                        
                    }
                }
            }
        }
        
    }
   
}