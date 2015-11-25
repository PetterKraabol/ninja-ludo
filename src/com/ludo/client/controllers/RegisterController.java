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
    MessageBundle message = new MessageBundle();
    
    /* This is performed then the user have pressed the register button in order to try and register as a user,
       if that is not the case the user will get different error messages to try and help him out.
    */
    
    // This is performed when the user wants to know if the username is already taken
    /*@FXML
    public void checkAction(ActionEvent event) throws IOException {
        
        
    }*/
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Change the language of objects in RegisterView.fxml
        tittleLabel.setText(message.retriveText("register.tittle"));                            // Window TopText
        firstNameField.setPromptText(message.retriveText("register.firstname"));                // FirstName Field
        lastNameField.setPromptText(message.retriveText("register.lastname"));                  // LastName Field
        emailField.setPromptText(message.retriveText("register.email"));                        // Email Field
        usernameField.setPromptText(message.retriveText("register.username"));                  // Username Field
        usernameCheckLabel.setText(message.retriveText("register.usernameLabel"));         		// UsernameCheck Label     
        checkBtn.setText(message.retriveText("register.usernameCheckBtn"));						// Check Btn
        passwordField.setPromptText(message.retriveText("register.password"));					// Password Field
        passwordVerifyField.setPromptText(message.retriveText("register.passwordVerify"));		// PasswordVerify Field
        creatBtn.setText(message.retriveText("register.btn"));									// Creat Btn
        backBtn.setText(message.retriveText("register.backBtn"));								// Back Btn
    }
    
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
                    usernameCheckLabel.setText(message.retriveText("register.username.missingfield"));
                }
                
                // When the username is taken
                else if (user.checkUsername(usernameField.getText().trim())) {
                    usernameCheckLabel.setTextFill(Color.web("#ff0000"));
                    usernameCheckLabel.setText(message.retriveText("register.usernameCheckTrue"));
                
                } else {
                    usernameCheckLabel.setTextFill(Color.web("#00b120"));
                    usernameCheckLabel.setText(message.retriveText("register.usernameCheckFalse"));
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
                	errorLabel.setText(message.retriveText("login.error.missingFields"));
                
                // If email is not valid
                } else if (!emailField.getText().contains("@") || !emailField.getText().contains(".")) {
                	usernameCheckLabel.setText("");
                	errorLabel.setText(message.retriveText("register.error.email"));
                    
                // If passwords are not the same
                } else if (!passwordField.getText().trim().equals(passwordVerifyField.getText().trim()) ) {
                	usernameCheckLabel.setText("");
                	errorLabel.setText(message.retriveText("register.error.password"));
                    passwordField.clear();
                    passwordVerifyField.clear();
                
                // If username is taken
                } else if (user.checkUsername(usernameField.getText().trim())) {
                	usernameCheckLabel.setText("");
                	errorLabel.setText(message.retriveText("register.usernameCheckTrue"));
                   
                // If registration was successful 
                } else { 
                	loginManager.showLoginScreen(); // Go to loginScreen and give a congratulation message with your username
                	JOptionPane.showMessageDialog(null, message.retriveText("login.successful.register.part1") + " " + 
                	usernameField.getText().trim() + " " + message.retriveText("login.successful.register.part2"));
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

}
