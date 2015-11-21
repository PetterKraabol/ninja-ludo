package com.ludo.client.controllers;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

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
import javafx.stage.Stage;

public class LoginController implements Initializable {
	
	// FXML Fields
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;
	
	// FXML Labels
	@FXML
	private Label welcomeLabel;
	@FXML
	private Label errorLabel;
	@FXML
	private Label registerLabel;
	
	// FXML Buttons
	@FXML
	private Button loginBtn;
	@FXML
	private Button registerBtn;
	@FXML
	private Button noImageBtn;
	@FXML
	private Button usImageBtn;
	
	// Create user class
	User user = new User();
	MessageBundle message = new MessageBundle();
	
	@FXML
	public void loginAction(ActionEvent event) throws IOException {
		
		// Check if all Fields are filled inn
		if (usernameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
			errorLabel.setText(message.retriveText("register.error.missingFields"));
		
		// If login is valid
		} else if (user.login(usernameField.getText(), passwordField.getText().trim())) {
			Parent client_page_parent = FXMLLoader.load(getClass().getResource("/com/ludo/client/views/MainView.fxml"));
			Scene client_page_scene = new Scene(client_page_parent);
			Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			app_stage.setScene(client_page_scene);
			app_stage.setTitle(message.retriveText("main.topText"));
			app_stage.show();
			
		// If Login was wrong 
		} else { errorLabel.setText("Wrong username or password!"); }
	}
	
	@FXML
	public void registerAction(ActionEvent event) throws IOException {
		Parent client_page_parent = FXMLLoader.load(getClass().getResource("/com/ludo/client/views/RegisterView.fxml"));
		Scene client_page_scene = new Scene(client_page_parent);
		Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		app_stage.setScene(client_page_scene);
		app_stage.setTitle(message.retriveText("register.topText"));
		app_stage.show();
	}
	
	@FXML
	public void noChangeAction(ActionEvent event) throws IOException {
		message.Message("no");
		
		Parent client_page_parent = FXMLLoader.load(getClass().getResource("/com/ludo/client/views/LoginView.fxml"));
		Scene client_page_scene = new Scene(client_page_parent);
		Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		app_stage.setScene(client_page_scene);
		app_stage.setTitle(message.retriveText("login.topText"));
		app_stage.show();
	}
	
	@FXML
	public void usChangeAction(ActionEvent event) throws IOException {
		message.Message("us");
		
		Parent client_page_parent = FXMLLoader.load(getClass().getResource("/com/ludo/client/views/LoginView.fxml"));
		Scene client_page_scene = new Scene(client_page_parent);
		Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		app_stage.setScene(client_page_scene);
		app_stage.setTitle(message.retriveText("login.topText"));
		app_stage.show();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Change the language of objects in LoginView.fxml
        welcomeLabel.setText(message.retriveText("login.welcomeMessage"));	// Welcome message
        usernameField.setPromptText(message.retriveText("login.username"));	// Username Field
        passwordField.setPromptText(message.retriveText("login.password"));	// Password Field
        loginBtn.setText(message.retriveText("login.btn"));					// Login Button
        registerLabel.setText(message.retriveText("login.registerText"));	// Register Label
        registerBtn.setText(message.retriveText("login.registerBtn"));		// Register Button 
	}

	

}