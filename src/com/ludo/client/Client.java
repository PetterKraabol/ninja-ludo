package com.ludo.client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Client extends Application {
    
    /**
     * Start JavaFX
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
    	Image icon = new Image (getClass().getResourceAsStream(("/com/ludo/resources/icon.png")));
        Scene scene = new Scene(new StackPane());
        
        LoginManager loginManager = new LoginManager(scene, stage);
        
        stage.setScene(scene);
        stage.getIcons().add(icon);
        stage.show();
        
        /**
         * When the user closes the application, make sure it sends LOGOUT to server
         * so that the server can remove the user from the users list.
         */
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                loginManager.closeWindow();
            }
        }); 
        
    }
    
}
