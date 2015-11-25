package com.ludo.client;

import java.io.IOException;
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
import javafx.stage.Stage;

public class LoginManager {
    
    /**
     * MessageBundle for I18N
     */
    MessageBundle messageBundle = new MessageBundle();
    
    /**
     * JavaFX Scene
     */
    private Scene scene;
    private Stage stage;
    
    /**
     * Socket connection to chat server
     */
    private Socket socket;
    
    /**
     * Application configuration
     */
    private Config config = new Config();
    
    /**
     * Constructor to hold scene and create a socket connection to the server.
     * @param scene
     */
    public LoginManager(Scene scene, Stage stage) {
        
        // Scene
        this.scene = scene;
        this.stage = stage;
        
        // Set socket to connect to server.
        this.socket = connectToServer();
    }
    
    /**
     * If the user is authenticated, show the main view scene.
     * @param sessionID
     */
    public void authenticated(String sessionID) {
        showMainView(sessionID);
    }
    
    /**
     * If the user logs out, refresh the socket and dispolay the login screen
     */
    public void logout() {
        
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
        Socket socket;
        
        try {
            socket = new Socket(askForIPAddress(), Integer.parseInt(config.getConfig("chatPort")));
            return socket;
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error connecting to server");
            e.printStackTrace();
        }
        
        return null;
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
    
    public void showMainView(String sessionID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ludo/client/views/MainView.fxml"));
            scene.setRoot((Parent) loader.load());
            stage.setTitle(messageBundle.retriveText("main.topText"));
            stage.sizeToScene();
            
            // Main View Controller
            MainController controller = loader.<MainController>getController();
            controller.initSessionID(this, sessionID);
        } catch(IOException e) {
            // Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error showing main view: " + e);
        }
    }

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
}
