package com.ludo.client.controllers;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

import com.ludo.client.ClientManager;
import com.ludo.i18n.MessageBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class GameQueueController implements Initializable{
    /**
     * Message bundle for i18n
     */
    MessageBundle messageBundle = new MessageBundle();
    
    @FXML private Label QueueLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        QueueLabel.setText(messageBundle.retriveText("game.queue.message"));
    }
    
    /**
     * initManager is used by GameManager to share information
     * @param gameManager
     * @param out
     */
    public void initManager(ClientManager clientManager, PrintWriter out) {
        
        System.out.println("GameQueueView->initManager");
    }
    
}
