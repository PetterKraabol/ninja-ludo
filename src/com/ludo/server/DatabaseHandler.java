/**
 * This application uses a Derby database connection.
 * 
 * The database handler will handle everything database
 * related, such as creating necessary tables, adding,
 * editing and removing data from the database.
 */
package com.ludo.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author Petter
 *
 */
public class DatabaseHandler {
    
    /**
     * Database Connection
     */
    private static Connection connection;
    
    /**
     * Database Server URL
     */
    private final static String url = "jdbc:mysql://localhost:3306/ludo";
    
    /**
     * MySQL User
     */
    private final static String user = "root";
    
    /**
     * MySQL Password
     */
    private static final String password = null;
    
    /**
     * DatabaseHandler constructor to construct the necessary
     * database tables.
     */
    public DatabaseHandler() {
        
        createConnection();
        
    }
    
    /**
     * Create a connection to the database
     * @return boolean
     */
    private boolean createConnection() {
        
        // Try connecting
        try {
            connection = DriverManager.getConnection(url, user, password);
            
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e);
            return false;
        }
        
        return true;
    }
    
    /**
     * Reset all tables by clearing their data.
     */
    public void truncateTable(String table) {
        
        // Truncate users table
        String truncateTable = "TRUNCATE TABLE users";
        
        // Try to execute the query
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(truncateTable);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Create necessary database Tables
     */
    public void createTables() {
        
        ArrayList<String> queries = new ArrayList<String>();
        
        // Create users table query
        queries.add("CREATE TABLE IF NOT EXISTS `users` (`id` int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, `username` varchar(45) NOT NULL UNIQUE, `password` varchar(45) NOT NULL)");
        
        // Try to execute the queries
        try {
            Statement statement = connection.createStatement();
            
            for (String query : queries) {
                statement.executeUpdate(query);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Drop (delete) a table in the database
     * @param table Table name
     */
    public void dropTable(String table) {
        
        // Drop user table
        String dropTable = "DROP TABLE IF EXISTS " + table;
        
        // Try to execute the query
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(dropTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Execute an SQL query and resturn the result set.
     * @param query
     * @return
     */
    public ResultSet select(String query) {
        
        // Try to execute the query
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Execute an insert query to the database.
     * @param query
     */
    public void insert(String query) {
        
        // Try to execute the query
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void update(String query) {
        
        // Try to execute the query
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
