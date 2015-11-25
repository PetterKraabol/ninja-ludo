package com.ludo.client.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.ludo.client.LoginManager;
import com.ludo.i18n.MessageBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainController implements Initializable {

	// FXML Labels
	@FXML private Label globalChatLabel;
	@FXML private Label friendChatLabel;
	
	// FXML Tab
	@FXML private Tab friendTab;
	@FXML private Tab chatTab;
	
	// FXML TextArea
	@FXML private TextArea globalChatTextArea;
	@FXML private TextArea friendTextArea;
	@FXML private TextArea chatTextArea;
	
	// FXML TextField
	@FXML private TextField globalChatTextField;
	@FXML private TextField friendTextField;
	
	// FXML Buttons
	@FXML private Button logoutBtn;
	@FXML private Button newGameBtn;
	@FXML private Button writeBtn;
	@FXML private Button addFriendBtn;
	
	// Internationalization
	MessageBundle message = new MessageBundle();
 	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	/**
     * Initialized by the Login Manager (LoginManager.java)
     * This function sets up necessary event handlers for the view.
     * @param loginManager
     * @param sessionID
     */
    public void initSessionID(LoginManager loginManager, String sessionID) {
        globalChatLabel.setText(sessionID);
        
        /**
         * New Game Button
         */
        newGameBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    Image icon = new Image (getClass().getResourceAsStream(("/com/ludo/resources/icon.png")));
                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("/com/ludo/client/views/GameView.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle(message.retriveText("login.topText"));
                    stage.getIcons().add(icon);
                    stage.show();
                } catch (IOException e) {
                    System.out.println("Error creating new game window: " + e);
                }
            }
            
        });
        
        /**
         * Logout button
         */
        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                loginManager.logout();
            }
        });
    }

}
