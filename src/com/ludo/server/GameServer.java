package com.ludo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;


/**
 * Created by Rudde on 25.11.2015.
 */
public class GameServer {

    private static final int port = 4343;

    private Game[] games = new Game[10];

    public GameServer() throws Exception {

        System.out.println("Gameserver started, listening on port " + port);

        ServerSocket listener = new ServerSocket(port);

        try {
            while (true) {
                new Player(listener.accept()).start();
            }
        } finally {
            listener.close();
        }

    }

    private Integer getGame() {

    }

    private static class Game extends Thread {

        private boolean availabe;
        private boolean inGame;

        private String[] players = new String[4];

        /**
         * Peice locations stored in the following way.
         *
         * {{{X,Y},{X,Y},{X,Y},{X,Y]},{{X,Y},{X,Y},{X,Y},{X,Y]},{{X,Y},{X,Y},{X,Y},{X,Y]},{{X,Y},{X,Y},{X,Y},{X,Y]}}
         *
         * Where the first is Green, Red, Blue, and yellow.
         */
        private String placement;

        public boolean isAvailabe() {
            return availabe;
        }

        public void setPlacements(String newLocations) {
            this.placement = newLocations;
        }

        public String getPlacements() {
            return placement;
        }



    }

    private static class Player extends Thread {
        private Socket socket;

        private String username;
        private Integer gameId;
        private Integer players;
        private BufferedReader in;
        private PrintWriter out;
        private String action;

        /**
         * Peice locations stored in the following way.
         *
         * {{{X,Y},{X,Y},{X,Y},{X,Y]},{{X,Y},{X,Y},{X,Y},{X,Y]},{{X,Y},{X,Y},{X,Y},{X,Y]},{{X,Y},{X,Y},{X,Y},{X,Y]}}
         *
         * Where the first is Green, Red, Blue, and yellow.
         */
        private String placements;

        public Player(Socket connection) {
            System.out.println("New player");
            this.socket = connection;
        }

        public static Integer newGame() {
            return 452;
        }

        public void run() {
            try {

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    action = in.readLine();
                    if (action.startsWith("JOIN")) {
                        out.println("GAMEID");
                        gameId = in.read();
                    } else if (action.startsWith("NEW")) {
                        out.println("You want a new game");
                        out.println(newGame());
                    }
                }

            } catch (IOException e) {

            } finally {

            }
        }



    }

}
