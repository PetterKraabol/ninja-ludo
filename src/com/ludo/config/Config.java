/**
 * 
 */
package com.ludo.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Petter
 *
 */

/**
 * Config loads configuration settings from config.ini into a configuration map.
 * On every config request, the program looks up the current config value in
 * the configuration map and any new changes to the configuration is written to
 * the configuration file, config.ini, and reloaded to the configuration map.
 * @author Petter
 *
 */
public class Config {
    private String filename = "config.ini";
    private Map<String, String> configuration = new HashMap<String, String>();
    
    /**
     * Load Configurations
     */
    public Config() {
        readFromFile(this.filename);
    }
    
    /**
     * Load custom configurations from another file name
     * @param filename
     */
    public Config(String filename) {
        this.filename = filename;
        readFromFile(this.filename);
    }
    
    /**
     * Get a configuration value from configuration map.
     * @param configName
     * @return
     */
    public String getConfig(String configName) {
        return configuration.get(configName);
    }
    
    /**
     * Set a configuration value which will be written to file and reloaded into the configuration map.
     * @param configName Config name
     * @param value Config value
     * @return
     */
    public void setConfig(String key, String value) {
        
        // Update configuration Map
        this.configuration.put(key, value);
        
        // Save configuration Map to file
        saveToFile(this.configuration, this.filename);
        
    }
    
    /**
     * Save a configuration Map to file using this format:
     * configKey=configValue
     * @param configuration
     */
    private void saveToFile(Map<String, String> configuration, String filename) {
        try {
            PrintWriter output = new PrintWriter(filename);
            
            // Start with a comment describing the file
            output.println("# Configuration file for Ludo Application.");
            
            // Write every configuration to file
            for (Map.Entry<String, String> entry : configuration.entrySet()) {
                
                // Write it to file
                output.println(entry.getKey() + "=" + entry.getValue());
            }
            
            // Close file
            output.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            e.printStackTrace();
        }
    }
    
    /**
     * Configuration files should have the structure:
     * # This is a comment
     * configKey=configValue
     * anotherKey=anotherValue
     * @param filename
     */
    private void readFromFile(String filename) {
        try {
            
            // File Reader
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            
            String line = null;
            
            // Read from file, line by line
            while((line = reader.readLine()) != null) {
                
                // Skip any comments
                if(line.startsWith("#")) {
                    continue;
                }
                
                // Split config to get name and value
                String[] config = line.split("=");
                
                // If configuration is not set, skip.
                if(config.length != 2) {
                    continue;
                }
                
                // Load configuration name and value to the configuration map.
                this.configuration.put(config[0].trim(), config[1].trim());
            }
            
            // Close file
            reader.close();
            
        } catch(IOException e) {
            System.out.println("Error reading from " + filename + ": " + e );
        }
    }
}
