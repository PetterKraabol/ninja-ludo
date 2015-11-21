/**
 * 
 */
package com.ludo.client.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.ludo.client.User;
import com.ludo.i18n.MessageBundle;

import javafx.event.ActionEvent;
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
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordVerifyField;

    // FXML Labels
    @FXML
    private Label tittleLabel;
    @FXML
    private Label usernameCheckLabel;
    @FXML
    private Label errorLabel;
    
    // FXML Buttons
    @FXML
    private Button checkBtn;
    @FXML
    private Button creatBtn;
    @FXML
    private Button backBtn;
    
    // Create user class
    User user = new User();
    MessageBundle message = new MessageBundle();
    
    /* This is performed then the user have pressed the register button in order to try and register as a user,
       if that is not the case the user will get different error messages to try and help him out.
    */
    @FXML
    public void registerAction(ActionEvent event) throws IOException {
        
        // Check if all Fields are filled inn
        if (firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty()     || usernameField.getText().trim().isEmpty() ||
            passwordField.getText().trim().isEmpty()  || passwordVerifyField.getText().trim().isEmpty()) 
        {   
            errorLabel.setText(message.retriveText("login.error.missingFields"));
        
        // If email is not valid
        } else if (!emailField.getText().contains("@") || !emailField.getText().contains(".")) {
            errorLabel.setText(message.retriveText("register.error.email"));
            
        // If passwords are not the same
        } else if (!passwordField.getText().trim().equals(passwordVerifyField.getText().trim()) ) {
            errorLabel.setText(message.retriveText("register.error.password"));
            passwordField.clear();
            passwordVerifyField.clear();
            
        } else {
            
            // Send the data to the server
            
            // Goes back to the login screen/view
            Parent client_page_parent = FXMLLoader.load(getClass().getResource("/com/ludo/client/views/LoginView.fxml"));
            Scene client_page_scene = new Scene(client_page_parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(client_page_scene);
            app_stage.setTitle(message.retriveText("login.topText"));
            app_stage.show();
            
            JOptionPane.showMessageDialog(null, message.retriveText("login.successful.register"));
        }
    }
    
    // This is performed when the user wants to know if the username is already taken
    @FXML
    public void checkAction(ActionEvent event) throws IOException {
        
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
    
    // This is performed when the user want's to go back to the login screen/view
    @FXML
    public void backAction(ActionEvent event) throws IOException {
        Parent client_page_parent = FXMLLoader.load(getClass().getResource("/com/ludo/client/views/LoginView.fxml"));
        Scene client_page_scene = new Scene(client_page_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(client_page_scene);
        app_stage.setTitle(message.retriveText("login.topText"));
        app_stage.show();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Change the language of objects in RegisterView.fxml
        tittleLabel.setText(message.retriveText("register.tittle"));                            // Window TopText
        firstNameField.setPromptText(message.retriveText("register.firstname"));                // FirstName Field
        lastNameField.setPromptText(message.retriveText("register.lastname"));                  // LastName Field
        emailField.setPromptText(message.retriveText("register.email"));                        // Email Field
        usernameField.setPromptText(message.retriveText("register.username"));                  // Username Field
        usernameCheckLabel.setText(message.retriveText("register.usernameLabel"));              
        checkBtn.setText(message.retriveText("register.usernameCheckBtn"));
        passwordField.setPromptText(message.retriveText("register.password"));
        passwordVerifyField.setPromptText(message.retriveText("register.passwordVerify"));
        creatBtn.setText(message.retriveText("register.btn"));
        backBtn.setText(message.retriveText("register.backBtn"));
    }

}
