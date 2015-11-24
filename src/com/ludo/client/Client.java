/**
 * 
 */
package com.ludo.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.ludo.i18n.MessageBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author Petter
 *
 */
public class Client extends Application {
    
	String ludo = "";
	
    /**
     * Default hostname
     */
    private static String hostname = "localhost";
    
    /**
     * Default port
     */
    private static int port = 4040;
    
    /**
     * Settings map
     */
    private static Map<String, String> config = new HashMap<String, String>();
    
    public Client() throws Exception {
        System.out.println("Client");
        //Server server = new Server(hostname, port);
        //server.connect();
        //ChatClient chatClient = new ChatClient();
    }
    
    /**
     * TODO: Description
     * @param args Run arguments
     * @throws Exception Exceptions 
     */
    public static void main(String[] args) throws Exception {
        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        
        Image icon = new Image (getClass().getResourceAsStream(("/com/ludo/resources/icon.png")));
        MessageBundle message = new MessageBundle();
        
        if (message.existsFile()) {
            message.writeToFile(message.readFile());
            
        } else { 
            message.creatFile();
            message.writeToFile("us");
        }
            
    
        
        Parent root = FXMLLoader.load(getClass().getResource("/com/ludo/client/views/LoginView.fxml"));
        
        
        root = FXMLLoader.load(getClass().getResource("/com/ludo/client/views/MainView.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(message.retriveText("login.topText"));
        stage.getIcons().add(icon);
        stage.show();
        
        //--------- Ask for another scene -------------
        String answer = JOptionPane.showInputDialog(null,
                "Choose a screen name: ",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
        
        // If user wants login, show login, else show mainview
        if(answer.equals("login")) {
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("/com/ludo/client/views/LoginView.fxml")));
            stage.setScene(newScene);
        } else {
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("/com/ludo/client/views/MainView.fxml")));
            stage.setScene(newScene);
        }
        
    }

}
