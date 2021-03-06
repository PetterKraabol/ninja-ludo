package com.ludo.client.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.ludo.client.ClientManager;
import com.ludo.config.Config;
import com.ludo.i18n.MessageBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {
    
    // Config
    Config config = new Config();
	
	// FXML Fields
	@FXML private TextField usernameField;
	@FXML private PasswordField passwordField;
	
	// FXML Labels
	@FXML private Label welcomeLabel;
	@FXML private Label errorLabel;
	@FXML private Label registerLabel;
	@FXML private Label serverLabel;
	
	// FXML Buttons
	@FXML private Button loginBtn;
	@FXML private Button registerBtn;
	@FXML private Button noImageBtn;
	@FXML private Button usImageBtn;
	@FXML private Button serverAddressBtn;
	
	// Create user class
	MessageBundle messageBundle = new MessageBundle();
    
	/**
     * Initialized by the Login Manager (LoginManager.java)
     * This function sets up necessary event handlers for the view.
     * @param clientManager
     */
    public void initManager(final ClientManager clientManager) {
        
        /**
         * Login Button
         */
    	loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
            	
            	String username = usernameField.getText().trim();
            	String password = passwordField.getText().trim();
            	
            	// Abort if missing fields
        		if (username.isEmpty() || password.isEmpty()) {
        			errorLabel.setText(messageBundle.retriveText("login.error.missingFields"));
        			return;
        		}
        		
        		// Check if username and password are correct
				int response = clientManager.authenticate(username, password);
				
				// Success
        		if(response == 0) {
				    try {
                        clientManager.showMainScreen();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
				}
        		// Login denied
        		else if(response == 1) {
				    errorLabel.setText(messageBundle.retriveText("login.error.loginDenied"));
				}
        		// Already logged in
        		else if(response == 2) {
        		    errorLabel.setText(messageBundle.retriveText("login.error.alreadyLoggedIn"));
				}
            }
        });
        
        /**
         * Register button
         */
        registerBtn.setOnAction(new EventHandler<ActionEvent>() {
           
            @Override
            public void handle(ActionEvent event) {
                
                // Input
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                
                // Abort if missing fields
                if (username.isEmpty() || password.isEmpty()) {
                    System.out.println("Empty");
                    errorLabel.setText(messageBundle.retriveText("login.error.missingFields"));
                    return;
                }
                
                System.out.println("Not empty");
                
                // Check if username and password are correct
                int response = clientManager.register(username, password);
                
                // Success
                if(response == 0) {
                    // Display registration success message
                    JOptionPane.showMessageDialog(null, messageBundle.retriveText("registration.complete") + ": " + username);
                    
                    // Authenticate and switch to main view
                    if(clientManager.authenticate(username, password) == 0) {
                        try {
                            clientManager.showMainScreen();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, messageBundle.retriveText("registration.error.auth"));
                    }
                }
                
                // Username is taken
                else if(response == 1) {
                    errorLabel.setText(messageBundle.retriveText("registration.error.usernameTaken"));
                } else {
                    
                }
            }
        });
        
        /**
         * Change server address
         */
        serverAddressBtn.setOnAction(new EventHandler<ActionEvent>() {
           
            @Override
            public void handle(ActionEvent event) {
                
                // Show change IP dialogue
                clientManager.changeServerIP();
                
                // Display IP address in label
                serverLabel.setText(messageBundle.retriveText("login.ipaddress") + ": " + config.getConfig("ipaddress"));
                
            }
        });
        
        /**
         * Change language to Norwegian
         */
        noImageBtn.setOnAction(new EventHandler<ActionEvent>() {
           
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Change to Norwegian");
                config.setConfig("language", "no");
                config.setConfig("country", "NO");
                clientManager.showLoginScreen();
            }
        });
        
        /**
         * Change language to English
         */
        usImageBtn.setOnAction(new EventHandler<ActionEvent>() {
           
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Change to English");
                config.setConfig("language", "en");
                config.setConfig("country", "US");
                clientManager.showLoginScreen();
            }
        });
        
    }
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		welcomeLabel.setText(messageBundle.retriveText("login.welcomeMessage"));	// Welcome message
        usernameField.setPromptText(messageBundle.retriveText("login.username"));	// Username Field
        passwordField.setPromptText(messageBundle.retriveText("login.password"));	// Password Field
        loginBtn.setText(messageBundle.retriveText("login.btn"));					// Login Button
        registerLabel.setText(messageBundle.retriveText("login.registerText"));	// Register Label
        registerBtn.setText(messageBundle.retriveText("login.registerBtn"));		// Register Button 
        
        // IP Address label
        if(config.getConfig("ipaddress") != null && !config.getConfig("ipaddress").equals("null")) {
            serverLabel.setText(messageBundle.retriveText("login.ipaddress") + ": " + config.getConfig("ipaddress"));
        } else {
            serverLabel.setText(messageBundle.retriveText("login.no.ipaddress"));
        }
        
        serverAddressBtn.setText(messageBundle.retriveText("login.changeip.btn"));
        
	}

}