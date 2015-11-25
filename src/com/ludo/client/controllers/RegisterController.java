/**
 * 
 */
package com.ludo.client.controllers;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RegisterController implements Initializable {
    
    // FXML Fields
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField passwordVerifyField;

    // FXML Labels
    @FXML private Label tittleLabel;
    @FXML private Label usernameCheckLabel;
    @FXML private Label errorLabel;
    
    // FXML Buttons
    @FXML private Button checkBtn;
    @FXML private Button creatBtn;
    @FXML private Button backBtn;
    
    // Create user class
    User user = new User();
    MessageBundle messageBundle = new MessageBundle();
    
    /**
     * Initialized by the Login Manager (LoginManager.java)
     * This function sets up necessary event handlers for the view.
     * @param loginManager
     */
    public void initManager(LoginManager loginManager) {
        
        /**
         * Check username availability button
         */
        checkBtn.setOnAction(new EventHandler<ActionEvent>() {
           
            @Override
            public void handle(ActionEvent event) {

            	// If username field is empty
                if (usernameField.getText().trim().isEmpty()) {
                    usernameCheckLabel.setTextFill(Color.web("#ff0000"));
                    usernameCheckLabel.setText(messageBundle.retriveText("register.username.missingfield"));
                }
                
                // When the username is taken
                else if (user.checkUsername(usernameField.getText().trim())) {
                    usernameCheckLabel.setTextFill(Color.web("#ff0000"));
                    usernameCheckLabel.setText(messageBundle.retriveText("register.usernameCheckTrue"));
                
                } else {
                    usernameCheckLabel.setTextFill(Color.web("#00b120"));
                    usernameCheckLabel.setText(messageBundle.retriveText("register.usernameCheckFalse"));
                }
            	
            }
        });
        
        /**
         * Register user button
         */
        creatBtn.setOnAction(new EventHandler<ActionEvent>() {
           
            @Override
            public void handle(ActionEvent event) {
                
            	// Check if all Fields are filled inn
                if (firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty() ||
                    emailField.getText().trim().isEmpty()     || usernameField.getText().trim().isEmpty() ||
                    passwordField.getText().trim().isEmpty()  || passwordVerifyField.getText().trim().isEmpty()) 
                {   
                	usernameCheckLabel.setText("");
                	errorLabel.setText(messageBundle.retriveText("login.error.missingFields"));
                
                // If email is not valid
                } else if (!emailField.getText().contains("@") || !emailField.getText().contains(".")) {
                	usernameCheckLabel.setText("");
                	errorLabel.setText(messageBundle.retriveText("register.error.email"));
                    
                // If passwords are not the same
                } else if (!passwordField.getText().trim().equals(passwordVerifyField.getText().trim()) ) {
                	usernameCheckLabel.setText("");
                	errorLabel.setText(messageBundle.retriveText("register.error.password"));
                    passwordField.clear();
                    passwordVerifyField.clear();
                
                // If username is taken
                } else if (user.checkUsername(usernameField.getText().trim())) {
                	usernameCheckLabel.setText("");
                	errorLabel.setText(messageBundle.retriveText("register.usernameCheckTrue"));
                   
                // If registration was successful 
                } else { 
                	loginManager.showLoginScreen(); // Go to loginScreen and give a congratulation message with your username
                	JOptionPane.showMessageDialog(null, messageBundle.retriveText("login.successful.register.part1") + " " + 
                	usernameField.getText().trim() + " " + messageBundle.retriveText("login.successful.register.part2"));
                }
            	
            }
        });
        
        /**
         * Go back to login screen
         */
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
           
            @Override
            public void handle(ActionEvent event) {
                loginManager.showLoginScreen();
            }
        });
        
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Change the language of objects in RegisterView.fxml
        tittleLabel.setText(messageBundle.retriveText("register.tittle"));                            // Window TopText
        firstNameField.setPromptText(messageBundle.retriveText("register.firstname"));                // FirstName Field
        lastNameField.setPromptText(messageBundle.retriveText("register.lastname"));                  // LastName Field
        emailField.setPromptText(messageBundle.retriveText("register.email"));                        // Email Field
        usernameField.setPromptText(messageBundle.retriveText("register.username"));                  // Username Field
        usernameCheckLabel.setText(messageBundle.retriveText("register.usernameLabel"));         		// UsernameCheck Label     
        checkBtn.setText(messageBundle.retriveText("register.usernameCheckBtn"));						// Check Btn
        passwordField.setPromptText(messageBundle.retriveText("register.password"));					// Password Field
        passwordVerifyField.setPromptText(messageBundle.retriveText("register.passwordVerify"));		// PasswordVerify Field
        creatBtn.setText(messageBundle.retriveText("register.btn"));									// Creat Btn
        backBtn.setText(messageBundle.retriveText("register.backBtn"));								// Back Btn
    }

}
