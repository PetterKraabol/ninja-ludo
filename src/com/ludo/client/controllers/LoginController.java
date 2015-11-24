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
	
	private String serverAdress;
	
	// Create user class
	MessageBundle message = new MessageBundle();
	
	private BufferedReader in;
	private PrintWriter out;
	
	
	private Socket socket;
	
	
	
	@FXML
	public void loginAction(ActionEvent event) throws IOException {
		
		String line;
		
		
		// Check if all Fields are filled inn
		if (usernameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
			errorLabel.setText(message.retriveText("register.error.missingFields"));
		
			
			
		// If login is valid
		} else {
			out.println(usernameField.getText());
			System.out.println(usernameField.getText());
			
			
			
			while(true) {
				//System.out.println("test");
				line = in.readLine();
				//System.out.println("test2");
				
				
				//System.out.print("Line" + line);
				
				if(line.startsWith("LOGINDENIED")) {
					errorLabel.setText(message.retriveText("login.error.missingFields"));
					break;
				}
					
				if(line.startsWith("LOGINACCEPTED")) { 
	        		
					System.out.println("LOGINACCEPTED");
					socket.close();
	        		Parent client_page_parent = FXMLLoader.load(getClass().getResource("/com/ludo/client/views/MainView.fxml"));
	    			Scene client_page_scene = new Scene(client_page_parent);
	    			Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    			app_stage.setScene(client_page_scene);
	    			app_stage.setTitle(message.retriveText("main.topText"));
	    			app_stage.show();
	    			System.out.println("break");
	    			break;
	        	} 
	        	
			}
		}
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
		serverAdress = JOptionPane.showInputDialog(null, "Server IP Address: ", "Connect", JOptionPane.QUESTION_MESSAGE);
		
		
	    try {
			socket = new Socket(serverAdress, 4040);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
	    
		
		welcomeLabel.setText(message.retriveText("login.welcomeMessage"));	// Welcome message
        usernameField.setPromptText(message.retriveText("login.username"));	// Username Field
        passwordField.setPromptText(message.retriveText("login.password"));	// Password Field
        loginBtn.setText(message.retriveText("login.btn"));					// Login Button
        registerLabel.setText(message.retriveText("login.registerText"));	// Register Label
        registerBtn.setText(message.retriveText("login.registerBtn"));		// Register Button 
	}

	

}