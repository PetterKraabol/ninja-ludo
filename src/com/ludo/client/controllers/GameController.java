package com.ludo.client.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.ludo.client.Coordinates;
import com.ludo.i18n.MessageBundle;
import com.ludo.client.ClientManager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    
    /**
     * Message Bundle
     */
    private MessageBundle messageBundle = new MessageBundle();
    
    /**
     * Turn
     */
    private boolean myTurn = false; 
    
    /**
     * Last dice roll
     */
    private int dice;

    // FXML Fields
    @FXML private TextArea gameChatTextArea;
    @FXML private TextField commentTextField;
    
    // FXML Labels
    @FXML private Label chatLabel;
    
    // FXML Buttons
    @FXML private Button writeBtn;
    @FXML private Button gameBtn;
    
    
    // Red Pieces
    @FXML private Circle redPiece1;
    @FXML private Circle redPiece2;
    @FXML private Circle redPiece3;
    @FXML private Circle redPiece4;
    
    // Blue Pieces
    @FXML private Circle bluePiece1;
    @FXML private Circle bluePiece2;
    @FXML private Circle bluePiece3;
    @FXML private Circle bluePiece4;
    
    // Green Pieces
    @FXML private Circle greenPiece1;
    @FXML private Circle greenPiece2;
    @FXML private Circle greenPiece3;
    @FXML private Circle greenPiece4;
    
    // Yellow Pieces
    @FXML private Circle yellowPiece1;
    @FXML private Circle yellowPiece2;
    @FXML private Circle yellowPiece3;
    @FXML private Circle yellowPiece4;
    
    // FXML Ellipse
    @FXML private Ellipse turneIndicatorEllipse;
    
    
    
    private Coordinates[] fields;
    private Coordinates[] indicator;
    
    public void selectPiece() {
        
        indicator = new Coordinates[4];
        indicator[1] = new Coordinates(225, 225);   // Red indicator
        indicator[2] = new Coordinates(405, 225);   // Blue indicator
        indicator[3] = new Coordinates(405, 405);   // Yellow indicator
        indicator[4] = new Coordinates(225, 405);   // Green indicator
        
        // Start is the first red field and the goes the same was as when you play the game
        fields = new Coordinates[77];
        fields[1]  = new Coordinates(63, 272);  // Red field
        fields[2]  = new Coordinates(105, 272);
        fields[3]  = new Coordinates(146, 272);
        fields[4]  = new Coordinates(188, 272);
        fields[5]  = new Coordinates(230, 272);
        fields[6]  = new Coordinates(272, 230);
        fields[7]  = new Coordinates(272, 189);
        fields[8]  = new Coordinates(272, 147);
        fields[9]  = new Coordinates(272, 105);
        fields[10] = new Coordinates(272, 63);
        fields[11] = new Coordinates(272, 22);
        fields[12] = new Coordinates(314, 22);
        fields[13] = new Coordinates(356, 22);
        fields[14] = new Coordinates(356, 63);  // Blue field
        fields[15] = new Coordinates(356, 105);
        fields[16] = new Coordinates(356, 147);
        fields[17] = new Coordinates(356, 189);
        fields[18] = new Coordinates(356, 230);
        fields[19] = new Coordinates(398, 272);
        fields[20] = new Coordinates(440, 272);
        fields[21] = new Coordinates(482, 272);
        fields[22] = new Coordinates(524, 272);
        fields[23] = new Coordinates(566, 272);
        fields[24] = new Coordinates(608, 272);
        fields[25] = new Coordinates(608, 315);
        fields[26] = new Coordinates(608, 357);
        fields[27] = new Coordinates(566, 357); // Yellow field
        fields[28] = new Coordinates(524, 357);
        fields[29] = new Coordinates(482, 357);
        fields[30] = new Coordinates(440, 357);
        fields[31] = new Coordinates(398, 357);
        fields[32] = new Coordinates(356, 400);
        fields[33] = new Coordinates(356, 441);
        fields[34] = new Coordinates(356, 483);
        fields[35] = new Coordinates(356, 525);
        fields[36] = new Coordinates(356, 567);
        fields[37] = new Coordinates(356, 608);
        fields[38] = new Coordinates(314, 608);
        fields[39] = new Coordinates(272, 608);
        fields[40] = new Coordinates(272, 567); // Green field
        fields[41] = new Coordinates(272, 525);
        fields[42] = new Coordinates(272, 483);
        fields[43] = new Coordinates(272, 441);
        fields[44] = new Coordinates(272, 440);
        fields[45] = new Coordinates(230, 357);
        fields[46] = new Coordinates(188, 357);
        fields[47] = new Coordinates(146, 357);
        fields[48] = new Coordinates(105, 357);
        fields[49] = new Coordinates(63, 357);
        fields[50] = new Coordinates(21, 357);
        fields[51] = new Coordinates(21, 315);
        fields[52] = new Coordinates(21, 272);
        
        // Color field after normal road - Red
        fields[53] = new Coordinates(63, 315);  // Color field for red second field
        fields[54] = new Coordinates(105, 315);
        fields[55] = new Coordinates(146, 315);
        fields[56] = new Coordinates(188, 315);
        fields[57] = new Coordinates(230, 315);
        fields[58] = new Coordinates(270, 332); // Arrow red - 7 på Y
        
        // Color field after normal road - Blue
        fields[59] = new Coordinates(314, 63);  // Color field for blue second field
        fields[60] = new Coordinates(314, 105);
        fields[61] = new Coordinates(314, 147);
        fields[62] = new Coordinates(314, 189);
        fields[63] = new Coordinates(314, 230);
        fields[64] = new Coordinates(297, 271); // Arrow blue + 7 på X
        
        // Color field after normal road - Yellow
        fields[65] = new Coordinates(566, 315); // Color field for yellow second field
        fields[66] = new Coordinates(524, 315);
        fields[67] = new Coordinates(482, 315);
        fields[68] = new Coordinates(440, 315);
        fields[69] = new Coordinates(398, 315);
        fields[70] = new Coordinates(558, 298); // Arrow yellow + 7 på Y
        
        // Color field after normal road - Yellow
        fields[71] = new Coordinates(314, 567); // Color field for yellow second field
        fields[72] = new Coordinates(314, 525);
        fields[73] = new Coordinates(314, 483);
        fields[74] = new Coordinates(314, 441);
        fields[75] = new Coordinates(314, 400);
        fields[76] = new Coordinates(331, 359); // Arrow yellow - 7 på X
        
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
    }
    
    /**
     * initManager is run by a GameManager object to share information
     * @param gameManager
     * @param out
     */
    public void initManager(ClientManager clientManager, PrintWriter out) {
        
        writeBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                // If chat input is not empty
                if(!commentTextField.getText().trim().isEmpty()) {
                    
                    // Send message to server
                    out.println("MESSAGE " + commentTextField.getText().trim());
                    
                    // Clear chat input field
                    commentTextField.setText("");
                }
            }
        });
        
        // Click gameBtn to roll the dice
        gameBtn.setOnAction(new EventHandler<ActionEvent>() {
           
            @Override
            public void handle(ActionEvent event) {
                
                if(myTurn) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(dice);
                    JOptionPane.showMessageDialog(null, messageBundle.retriveText("game.moveDice") + " " + sb.toString() + " " + messageBundle.retriveText("game.moveDice.steps"));
                    myTurn = false;
                 
                }
                
            }
        });
        
        //---- Pieces ---- //
        
        // Red
        redPiece1.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
            out.println("MOVE 1 red");
            }
        });
        redPiece2.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
             out.println("MOVE 2 red");
            }
        });
        redPiece3.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
             out.println("MOVE 3 red");
            }
        });
        redPiece4.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
             out.println("MOVE 4 red");
            }
        });

        // Blue
        bluePiece1.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
              out.println("MOVE 1 blue");
            }
        });
        bluePiece2.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
              out.println("MOVE 2 blue");
            }
        });
        bluePiece3.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
              out.println("MOVE 3 blue");
            }
        });
        bluePiece4.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
              out.println("MOVE 4 blue");
            }
        });

        // Yellow
        yellowPiece1.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                out.println("MOVE 1 yellow");
            }
        });
        yellowPiece2.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                out.println("MOVE 2 yellow");
            }
        });
        yellowPiece3.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                out.println("MOVE 3 yellow");
            }
        });
        yellowPiece4.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                out.println("MOVE 4 yellow");
            }
        });

        // Green
        greenPiece1.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
               out.println("MOVE 1 green");
            }
        });
        greenPiece2.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
               out.println("MOVE 2 green");
            }
        });
        greenPiece3.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
               out.println("MOVE 3 green");
            }
        });
        greenPiece4.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
               out.println("MOVE 4 green");
            }
        });
    }
    
    /**
     * Get coordinates
     * @return coordinate fields
     */
    public Coordinates[] getCoordinates() {
        
        Coordinates[] fieldArray = new Coordinates[76+1];
        fieldArray[1]  = new Coordinates(63, 272);  // Red field
        fieldArray[2]  = new Coordinates(105, 272);
        fieldArray[3]  = new Coordinates(146, 272);
        fieldArray[4]  = new Coordinates(188, 272);
        fieldArray[5]  = new Coordinates(230, 272);
        fieldArray[6]  = new Coordinates(272, 230);
        fieldArray[7]  = new Coordinates(272, 189);
        fieldArray[8]  = new Coordinates(272, 147);
        fieldArray[9]  = new Coordinates(272, 105);
        fieldArray[10] = new Coordinates(272, 63);
        fieldArray[11] = new Coordinates(272, 22);
        fieldArray[12] = new Coordinates(314, 22);
        fieldArray[13] = new Coordinates(356, 22);
        fieldArray[14] = new Coordinates(356, 63);  // Blue field
        fieldArray[15] = new Coordinates(356, 105);
        fieldArray[16] = new Coordinates(356, 147);
        fieldArray[17] = new Coordinates(356, 189);
        fieldArray[18] = new Coordinates(356, 230);
        fieldArray[19] = new Coordinates(398, 272);
        fieldArray[20] = new Coordinates(440, 272);
        fieldArray[21] = new Coordinates(482, 272);
        fieldArray[22] = new Coordinates(524, 272);
        fieldArray[23] = new Coordinates(566, 272);
        fieldArray[24] = new Coordinates(608, 272);
        fieldArray[25] = new Coordinates(608, 315);
        fieldArray[26] = new Coordinates(608, 357);
        fieldArray[27] = new Coordinates(566, 357); // Yellow field
        fieldArray[28] = new Coordinates(524, 357);
        fieldArray[29] = new Coordinates(482, 357);
        fieldArray[30] = new Coordinates(440, 357);
        fieldArray[31] = new Coordinates(398, 357);
        fieldArray[32] = new Coordinates(356, 400);
        fieldArray[33] = new Coordinates(356, 441);
        fieldArray[34] = new Coordinates(356, 483);
        fieldArray[35] = new Coordinates(356, 525);
        fieldArray[36] = new Coordinates(356, 567);
        fieldArray[37] = new Coordinates(356, 608);
        fieldArray[38] = new Coordinates(314, 608);
        fieldArray[39] = new Coordinates(272, 608);
        fieldArray[40] = new Coordinates(272, 567); // Green field
        fieldArray[41] = new Coordinates(272, 525);
        fieldArray[42] = new Coordinates(272, 483);
        fieldArray[43] = new Coordinates(272, 441);
        fieldArray[44] = new Coordinates(272, 440);
        fieldArray[45] = new Coordinates(230, 357);
        fieldArray[46] = new Coordinates(188, 357);
        fieldArray[47] = new Coordinates(146, 357);
        fieldArray[48] = new Coordinates(105, 357);
        fieldArray[49] = new Coordinates(63, 357);
        fieldArray[50] = new Coordinates(21, 357);
        fieldArray[51] = new Coordinates(21, 315);
        fieldArray[52] = new Coordinates(21, 272);
        
        // Color field after normal road - Red
        fieldArray[53] = new Coordinates(63, 315);  // Color field for red second field
        fieldArray[54] = new Coordinates(105, 315);
        fieldArray[55] = new Coordinates(146, 315);
        fieldArray[56] = new Coordinates(188, 315);
        fieldArray[57] = new Coordinates(230, 315);
        fieldArray[58] = new Coordinates(270, 332); // Arrow red - 7 på Y
        
        // Color field after normal road - Blue
        fieldArray[59] = new Coordinates(314, 63);  // Color field for blue second field
        fieldArray[60] = new Coordinates(314, 105);
        fieldArray[61] = new Coordinates(314, 147);
        fieldArray[62] = new Coordinates(314, 189);
        fieldArray[63] = new Coordinates(314, 230);
        fieldArray[64] = new Coordinates(297, 271); // Arrow blue + 7 på X
        
        // Color field after normal road - Yellow
        fieldArray[65] = new Coordinates(566, 315); // Color field for yellow second field
        fieldArray[66] = new Coordinates(524, 315);
        fieldArray[67] = new Coordinates(482, 315);
        fieldArray[68] = new Coordinates(440, 315);
        fieldArray[69] = new Coordinates(398, 315);
        fieldArray[70] = new Coordinates(558, 298); // Arrow yellow + 7 på Y
        
        // Color field after normal road - Yellow
        fieldArray[71] = new Coordinates(314, 567); // Color field for yellow second field
        fieldArray[72] = new Coordinates(314, 525);
        fieldArray[73] = new Coordinates(314, 483);
        fieldArray[74] = new Coordinates(314, 441);
        fieldArray[75] = new Coordinates(314, 400);
        fieldArray[76] = new Coordinates(331, 359); // Arrow yellow - 7 på X
        
        return fieldArray;
    }
    
    /**
     * Get indicators
     * @return
     */
    public Coordinates[] getIndicators() {
        Coordinates[] indicatorArray = new Coordinates[4+1];
        indicatorArray[1] = new Coordinates(225, 225);   // Red
        indicatorArray[2] = new Coordinates(405, 225);   // Blue
        indicatorArray[3] = new Coordinates(405, 405);   // Yellow
        indicatorArray[4] = new Coordinates(225, 405);   // Green
        
        return indicatorArray;
    }
    
    /**
     * Get the list of all piece objects
     * @return list of pieces
     */
    public List<Circle> getPieces() {
        
        // List of pieces
        List<Circle> pieces = new ArrayList<Circle>();
        
        // Red Pieces
        pieces.add(redPiece1);
        pieces.add(redPiece2);
        pieces.add(redPiece3);
        pieces.add(redPiece4);

        // Blue Pieces
        pieces.add(bluePiece1);
        pieces.add(bluePiece2);
        pieces.add(bluePiece3);
        pieces.add(bluePiece4);

        // Yellow Pieces
        pieces.add(yellowPiece1);
        pieces.add(yellowPiece2);
        pieces.add(yellowPiece3);
        pieces.add(yellowPiece4);

        // Green Pieces
        pieces.add(greenPiece1);
        pieces.add(greenPiece2);
        pieces.add(greenPiece3);
        pieces.add(greenPiece4);
        
        return pieces;
        
    }
    
    /**
     * Get indicator ellipse object
     * @return
     */
    public Ellipse getIndicator() {
        return turneIndicatorEllipse;
    }
    
    /**
     * Display a message that the user is in a game queue
     */
    public void waitingInQueue() {
        JOptionPane.showMessageDialog(null, messageBundle.retriveText("game.alert.queue.message"), messageBundle.retriveText("game.alert.queue.title"), JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Display a message to user that the game has started and 
     * what color they are.
     * @param color
     */
    public void gameHasStarted(String color) {
        JOptionPane.showMessageDialog(null, messageBundle.retriveText("game.alert.hasStarted.message") + ": " + color.substring(0, 1).toUpperCase() + color.substring(1) + " Ninja", messageBundle.retriveText("game.alert.hasStarted.title"), JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * A player has won, end the game
     * @param string
     */
    public void endGame(String winner) {
        JOptionPane.showMessageDialog(null, messageBundle.retriveText("game.end.message") + ": " + winner);
        
    }
    
    /**
     * It's your turn
     */
    public void itsYourTurn(int dice) {
        JOptionPane.showMessageDialog(null, messageBundle.retriveText("game.btn.roll"));
        this.dice = dice;
        this.myTurn = true;
    }
    
    /**
     * Move is denied, show message
     */
	public void moveDenied() {
        JOptionPane.showMessageDialog(null, messageBundle.retriveText("game.alert.moveDenied"));

		
	}
	
	/**
	 * Get game chat TextArea
	 * @return Game chat
	 */
    public TextArea getGameChat() {
        return gameChatTextArea;
    }

}
