package com.ludo.client.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.ludo.client.Coordinates;
import com.ludo.client.ClientManager;

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
    @FXML private TextArea gameChatTextArea;
    @FXML private TextField commentTextField;
    
    // FXML Labels
    @FXML private Label chatLabel;
    @FXML private Label usernameRedLabel;
    @FXML private Label usernameBlueLabel;
    @FXML private Label usernameGreenLabel;
    @FXML private Label usernameYellowLabel;
    
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
    
    
    // color variable
    private String color = "red"; 
    
    private Coordinates[] fields;
    private Coordinates[] indicator;
    
    public void selectPiece(MouseEvent event) throws IOException {
        
        if (color.equals("red")) {
            System.out.println("X: " + ((Node) event.getSource()).getLayoutX());
            System.out.println("Y: " + ((Node) event.getSource()).getLayoutY());
            
            indicator = new Coordinates[4];
            indicator[1] = new Coordinates(225, 225);   // Red indicator
            indicator[2] = new Coordinates(405, 225);   // Blue indicator
            indicator[3] = new Coordinates(405, 405);   // Yellow indicator
            indicator[4] = new Coordinates(225, 405);   // Green indicator
            
            // Start is the first red field and the goes the same was as when you play the game
            fields = new Coordinates[76];
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
        System.out.println("GameView->initManager");
    }

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

}
