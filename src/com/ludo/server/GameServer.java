package com.ludo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import com.ludo.config.Config;

public class GameServer extends Thread {
    ServerSocket listener;
    Config config = new Config();
    private static HashSet<Game> games = new HashSet<Game>();
    
    public GameServer() {
        System.out.println("Game server running on port " + config.getConfig("gamePort"));
    }
    
    public void run() {
        
        // Server socket
        try {
            this.listener = new ServerSocket(Integer.parseInt(this.config.getConfig("gamePort")));
        } catch (IOException e) {
            e.printStackTrace();
        }
       
        // Listen for new connections
        try{
            while(true) {
                Game game = new Game();
                
                // Players
                Game.Player[] players = new Game.Player[4];
                
                players[0] = game.new Player(listener.accept(), "red");
                players[1] = game.new Player(listener.accept(), "blue");
                players[2] = game.new Player(listener.accept(), "yellow");
                players[3] = game.new Player(listener.accept(), "green");
                
                game.addPlayers(players);
                
                game.broadcast("STARTGAME");
                
                game.start();
                
                games.add(game);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                listener.close();
            } catch (IOException e) {
                System.out.println("Error closing game server socket");
                e.printStackTrace();
            }
        }
    }
    
    class Game extends Thread  {
        Player[] players = new Player[4];
        
        public Game() {
            System.out.println("New Game");
        }
        
        public void addPlayers(Player[] players) {
            this.players = players;
        }
        
        /**
         * Broadcast a message to all players in a game session
         * @param message Broadcast message
         * @throws IOException Connection exceptions
         */
        private void broadcast(String message) throws IOException {
            
            // For every player in session; send the message
            for(Player player : players) {
                player.getOut().println(message);
            }
        }
        
        /**
         * Thread starts
         */
        public void run() {
            for(Player player : players) {
                player.start();
            }
        }
        
        class Piece {
            int position;
            
            public Piece() {
                this.position = 0;
            }
            
            /**
             * Move piece a specified amount of steps from its current location
             * @param steps
             * @return boolean if the piece can move or not
             */
            public boolean move(int steps) {
                int targetPos = this.position += steps;
                
                // If the user is trying to move the piece past the end.
                if(targetPos > Integer.parseInt(config.getConfig("mapLength"))) {
                    return false;
                }
                
                // If piece is in home, a 6 is required to move out
                else if(isHome() && steps != 6) {
                    return false;
                }
                
                // Valid move
                else {
                    setPosition(targetPos);
                    return true;
                }
            }
            
            private void setPosition(int position) {
                this.position = position;
            }
            
            public int getPosition() {
                return this.position;
            }
            
            public boolean isHome() {
                return this.position == 0;
            }
            
            public boolean isDone() {
                return this.position == Integer.parseInt(config.getConfig("mapLength"));
            }
        }
        
        class Player extends Thread {
            Piece[] pieces = new Piece[4];
            String color;
            Socket socket;
            PrintWriter out;
            BufferedReader in;
            
            public Player(Socket socket, String color) {
                
                // Color
                this.color = color;
                
                // Socket
                this.socket = socket;
                
                // Output
                try {
                    this.out = new PrintWriter(this.socket.getOutputStream(), true);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                // Input
                try {
                    this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
            
            /**
             * Return player socket
             * @return socket
             */
            public Socket getSocket() {
                return this.socket;
            }
            
            /**
             * Return player output writer
             * @return
             */
            public PrintWriter getOut() {
                return this.out;
            }
            
            /**
             * Thread running
             */
            public void run() {
                
            }
        }
    }
}