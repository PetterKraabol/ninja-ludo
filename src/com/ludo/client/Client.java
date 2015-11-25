package com.ludo.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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
        
        Scene scene = new Scene(new StackPane());
        
        LoginManager loginManager = new LoginManager(scene);
        loginManager.showLoginScreen();
        
        stage.setScene(scene);
        stage.show();
        
    }
    
}
