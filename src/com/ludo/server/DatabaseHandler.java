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
import java.util.List;

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
     * Database Driver
     */
    private final static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    
    /**
     * Database Name
     */
    private final static String databaseName = "ludo";
    
    /**
     * Database Server URL
     */
    private final static String derbyURL = "jdbc:derby:" + databaseName + ";create=true";
    
    /**
     * DatabaseHandler constructor to construct the necessary
     * database tables.
     */
    public DatabaseHandler() {
        
        System.out.println("Database Handler");
        
        if(createConnection())
            System.out.println("Database connection is OK");
        
    }
    
    /**
     * Create a connection to the database
     * @return boolean
     */
    private boolean createConnection() {
        
        // Try connecting
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(derbyURL);
            
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e);
            return false;
        }
        
        return true;
    }
    
    /**
     * Reset all tables by clearing their data.
     */
    public void resetTables() {
        
        // Truncate users table
        execute("TRUNCATE TABLE users");
    }
    
    /**
     * Create necessary Database Tables
     */
    public void createTables() {
        
        // Create users table query
        String query = "CREATE TABLE users ("
                + "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                + " username VARCHAR(50) NOT NULL UNIQUE,"
                + " password VARCHAR(50),"
                + " UNIQUE(username)"
                + ")";
        
        // Execute the query
        execute(query);
        
        // Test user
        execute("INSERT INTO users (username, password) VALUES ('admin', 'password')");
    }
    
    /**
     * Drop all tables
     */
    public void dropTables() {
        
        // Drop user table
        execute("DROP TABLE users");
    }
    
    /**
     * Execute a list of sql queries
     * @param query
     */
    private ResultSet execute(String query) {
        
        // Check if a database connection can be created
        if(!createConnection()) {
            return null;
        }
            
        // Try executing the queries
        try {
            
            Statement statement = connection.createStatement();
            
            // For each queries and query...
            ResultSet result = statement.executeQuery(query);
            
            // Close database connection
            connection.close();
            
            // Return result
            return result;
            
        } catch (SQLException sqle) {
            
            System.out.println("ERROR, CODE: " + sqle.getErrorCode());
            
            // It's OK if table already exists
            /*if(sqle.getErrorCode() == 30000) {
                System.out.println("Skipped query because table(s) already exists: " + query);
                return null;
            }*/
            
            // Print error
            System.out.println(sqle);
        }
        
        return null;
        
    }
    
    /**
     * Execute raw queries from outside this class.
     * It cannot return any data at this moment.
     * 
     * @param query Raw query String to be executed
     */
    public ResultSet query(String query) {
        
        return execute(query);
        
    }
}
