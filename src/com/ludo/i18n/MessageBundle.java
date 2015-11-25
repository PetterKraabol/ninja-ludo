package com.ludo.i18n;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Locale;
import java.util.ResourceBundle;

import com.ludo.config.Config;

public class MessageBundle {
    
    /**
     * Configurations to load settings such
     * as the language and country code for 
     * selected language.
     */
    Config config = new Config();
    
    /**
     * Language code
     */
    private String language;
    
    /**
     * Country code
     */
    private String country;
    
    /**
     * Locale
     */
    Locale currentLocale;
    
    /**
     * Resource Bundle
     */
    ResourceBundle resource;
    
    public MessageBundle() {
        
    }
    
    public String retriveText(String text) {
        
        // Load language from config file
        this.language = config.getConfig("language");
        this.country = config.getConfig("country");
        
        // Set language and country for locale
        this.currentLocale = new Locale(this.language, this.country);
        
        // Get language bundle with selected locale.
        this.resource = ResourceBundle.getBundle("com.ludo.i18n.MessagesBundle", this.currentLocale);
        
        // Return value
        return resource.getString(text);
    }
}