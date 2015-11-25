package com.ludo.client;

import java.io.IOException;

import com.ludo.client.controllers.LoginController;
import com.ludo.client.controllers.MainController;
import com.ludo.client.controllers.RegisterController;
import com.ludo.i18n.MessageBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginManager {
    private Scene scene;
    private Stage stage;
    
    MessageBundle message = new MessageBundle();
    
    public LoginManager(Scene scene, Stage stage) {
        this.scene = scene;
        this.stage = stage;
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
            stage.setTitle(message.retriveText("login.topText"));
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
            stage.setTitle(message.retriveText("main.topText"));
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
            stage.setTitle(message.retriveText("register.topText"));
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
