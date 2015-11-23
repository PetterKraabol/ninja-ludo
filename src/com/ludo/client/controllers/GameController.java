package com.ludo.client.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;

public class GameController implements Initializable{

	// FXML Fields
	@FXML
	private TextArea gameChatTextArea;
	@FXML
	private TextField commentTextField;
	
	// FXML Labels
	@FXML
	private Label chatLabel;
	@FXML
	private Label usernameRedLabel;
	@FXML
	private Label usernameBlueLabel;
	@FXML
	private Label usernameGreenLabel;
	@FXML
	private Label usernameYellowLabel;
	
	// FXML Buttons
	@FXML
	private Button writeBtn;
	@FXML
	private Button gameBtn;
	
	
	// FXML Bricks
	@FXML
	private Circle redBrick1;
	@FXML
	private Circle redBrick2;
	@FXML
	private Circle redBrick3;
	@FXML
	private Circle redBrick4;
	
	// Blue bricks
	@FXML
	private Circle blueBrick1;
	@FXML
	private Circle blueBrick2;
	@FXML
	private Circle blueBrick3;
	@FXML
	private Circle blueBrick4;
	
	// Blue bricks
	@FXML
	private Circle greenBrick1;
	@FXML
	private Circle greenBrick2;
	@FXML
	private Circle greenBrick3;
	@FXML
	private Circle greenBrick4;
	
	// Blue bricks
	@FXML
	private Circle yellowBrick1;
	@FXML
	private Circle yellowBrick2;
	@FXML
	private Circle yellowBrick3;
	@FXML
	private Circle yellowBrick4;
	
	// FXML Ellipse
	@FXML
	private Ellipse turneIndicatorEllipse;
	
	
	// color variable
	private String color = "red"; 
	
	public void selectBrick(MouseEvent event) throws IOException {
		
		if (color.equals("red")) {
			System.out.println("X: " + ((Node) event.getSource()).getLayoutX());
			System.out.println("Y: " + ((Node) event.getSource()).getLayoutY());
			
			redBrick1.setLayoutX(250);
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
