package com.ludo.client;

import java.io.IOException;

import com.ludo.client.controllers.LoginController;
import com.ludo.client.controllers.MainController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class LoginManager {
    private Scene scene;
    
    public LoginManager(Scene scene) {
        this.scene = scene;
    }
    
    /**
     * If the user is authenticated, show the main view scene.
     * @param sessionID
     */
    public void authenticated(String sessionID) {
        showMainView(sessionID);
    }
    
    /**
     * If the user logs out, show the login screen;
     */
    public void logout() {
        showLoginScreen();
    }
    
    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ludo/client/views/LoginView.fxml"));
            scene.setRoot((Parent) loader.load());
            
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
            
            // Main View Controller
            MainController controller = loader.<MainController>getController();
            controller.initSessionID(this, sessionID);
        } catch(IOException e) {
            // Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error showing main view: " + e);
        }
    }
}
