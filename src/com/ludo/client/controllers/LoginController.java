package com.ludo.client.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.ludo.client.LoginManager;
import com.ludo.client.User;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	
	// FXML Fields
	@FXML private TextField usernameField;
	@FXML private PasswordField passwordField;
	
	// FXML Labels
	@FXML private Label welcomeLabel;
	@FXML private Label errorLabel;
	@FXML private Label registerLabel;
	
	// FXML Buttons
	@FXML private Button loginBtn;
	@FXML private Button registerBtn;
	@FXML private Button noImageBtn;
	@FXML private Button usImageBtn;
	private String serverAdress;
	
	// Create user class
	MessageBundle message = new MessageBundle();
	
	private BufferedReader in;
	private PrintWriter out;
	
	
	private Socket socket;

	/**
     * Initialized by the Login Manager (LoginManager.java)
     * This function sets up necessary event handlers for the view.
     * @param loginManager
     */
    public void initManager(final LoginManager loginManager) {
        
        /**
         * Login Button
         */
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
           
            @Override
            public void handle(ActionEvent event) {
                
            	String line;
            	
            	// Check if all Fields are filled inn
        		if (usernameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
        			errorLabel.setText(message.retriveText("register.error.missingFields"));
        			
        		// If login is valid
        		} else {
        			/*try {
        				out.println(usernameField.getText());
        				System.out.println(usernameField.getText());
        			
        			
        				while(true) {
        					//System.out.println("test");
        				
								line = in.readLine();
							
								if(line.startsWith("LOGINDENIED")) {
									errorLabel.setText(message.retriveText("login.error.missingFields"));
									break;
								}
	        					
								if(line.startsWith("LOGINACCEPTED")) {
	            			
									String sessionID = authorize();
									if (sessionID != null) {
										loginManager.authenticated(sessionID);
									}
								}
        				}		
					} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}*/
        			
        			String sessionID = authorize();
					if (sessionID != null) {
						loginManager.authenticated(sessionID);
					}
        			
        		}
            }
        });
        
        
        /**
         * Register button
         */
        registerBtn.setOnAction(new EventHandler<ActionEvent>() {
           
            @Override
            public void handle(ActionEvent event) {
                loginManager.showRegistrationView();
            }
        });
        
        /**
         * Change language to Norwegian
         */
        noImageBtn.setOnAction(new EventHandler<ActionEvent>() {
           
            @Override
            public void handle(ActionEvent event) {
            	message.Message("no");
            	loginManager.showLoginScreen();
            }
        });
        
        /**
         * Change language to English
         */
        usImageBtn.setOnAction(new EventHandler<ActionEvent>() {
           
            @Override
            public void handle(ActionEvent event) {
            	message.Message("us");
            	loginManager.showLoginScreen();
            }
        });
        
    }
    
    /**
     * Authorize user
     * @return
     */
    private String authorize() {
        return usernameField.getText() + " " + passwordField.getText();
    }
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		/*
	    // Change the language of objects in LoginView.fxml
		serverAdress = JOptionPane.showInputDialog(null, "Server IP Address: ", "Connect", JOptionPane.QUESTION_MESSAGE);
		*/
	    
		welcomeLabel.setText(message.retriveText("login.welcomeMessage"));	// Welcome message
        usernameField.setPromptText(message.retriveText("login.username"));	// Username Field
        passwordField.setPromptText(message.retriveText("login.password"));	// Password Field
        loginBtn.setText(message.retriveText("login.btn"));					// Login Button
        registerLabel.setText(message.retriveText("login.registerText"));	// Register Label
        registerBtn.setText(message.retriveText("login.registerBtn"));		// Register Button 
	}

}