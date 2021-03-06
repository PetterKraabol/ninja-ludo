package com.ludo.client.controllers;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

import com.ludo.client.ClientManager;
import com.ludo.i18n.MessageBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController implements Initializable {

	// FXML Labels
	@FXML private Label globalChatLabel;
	@FXML private Label friendChatLabel;
	@FXML private Label welcomeMessageLabel;
	
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
	MessageBundle messageBundle = new MessageBundle();

	/**
     * Initialized by the Login Manager (LoginManager.java)
     * This function sets up necessary event handlers for the view.
     * @param clientManager
     * @param in Input from server
     * @param out Output to server
     */
    public void initManager(ClientManager clientManager, PrintWriter out) {
        
        /**
         * New Game Button
         */
        newGameBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                clientManager.showGameScreen();
            }
            
        });
        
        /**
         * Logout button
         */
        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                clientManager.logout();
            }
        });
        
        /**
         * Send message in chat
         */
        writeBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                // If chat input is not empty
                if(!globalChatTextField.getText().trim().isEmpty()) {
                    
                    // Send message to server
                    out.println("MESSAGE " + globalChatTextField.getText().trim());
                    
                    // Clear chat input field
                    globalChatTextField.setText("");
                }
            }
        });
    }
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	globalChatLabel.setText(messageBundle.retriveText("main.globalChat.label"));
    	globalChatTextField.setPromptText(messageBundle.retriveText("main.globalChat.TextField"));
    	friendChatLabel.setText(messageBundle.retriveText("main.friendsChat.label"));
    	friendTab.setText(messageBundle.retriveText("main.friendTab"));
    	chatTab.setText(messageBundle.retriveText("main.chatTab"));
    	friendTextField.setText(messageBundle.retriveText("main.addFriend.TextField"));
    	addFriendBtn.setText(messageBundle.retriveText("main.addFrien.btn"));
    	welcomeMessageLabel.setText(messageBundle.retriveText("main.welcomeMessage"));
    	newGameBtn.setText(messageBundle.retriveText("main.newGame.btn"));
    	logoutBtn.setText(messageBundle.retriveText("main.signOut.btn"));
    	writeBtn.setText(messageBundle.retriveText("main.write.btn"));
	}
    
    /**
     * Return chat window TextArea
     * @return TextArea Chat
     */
    public TextArea getGlobalChat() {
        globalChatTextArea.setWrapText(true);
        return globalChatTextArea;
    }

}
