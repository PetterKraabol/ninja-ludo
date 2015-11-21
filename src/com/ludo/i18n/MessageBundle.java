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

public class MessageBundle {

    // Localization
    private String language = "";
    private String country = "";
    private File file;
    private String fileName = "src/com/ludo/config/language.properties";
    
    
    public void Message(String language) {
        writeToFile(language);
    }
    
    public Boolean existsFile() {
        return (new File(fileName).isFile());
    }
    
    public String retriveText(String tmp) {
        
        try {
            language = readFile();
            
            
            if (language.equals("no")) {
                country = "NO";
            } else { country = "US"; }
            
            Locale currentLocale;
            ResourceBundle text;
            currentLocale = new Locale(language, country);
            text = ResourceBundle.getBundle("com.ludo.i18n.MessagesBundle", currentLocale);
            return text.getString(tmp);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return "Error";
    }
    
    public void creatFile() {
        File file = new File(fileName);
        
         boolean blnCreated = false;
         try
         {
           blnCreated = file.createNewFile();
         }
         catch(IOException ioe) {
             System.out.println("Error while creating a new empty file :" + ioe);
         }
        
    }
    
    public void writeToFile(String language) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        
        try {
            bw.write(language);
            bw.close();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public String readFile() throws IOException {
        String languageCode = "";
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            
            languageCode = br.readLine();
            br.close();
            return languageCode;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return languageCode;
    }
}