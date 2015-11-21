/**
 * 
 */
package com.ludo.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author Petter
 *
 */
public class ChatClient {
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Chatter");
    private JTextField textField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8,40);
    
    public ChatClient() {
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
        // Listeners
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
                
            }
        });   
    }
    
    // Ask for username
    private String getName() {
        return JOptionPane.showInputDialog(frame,
                        "Choose a screen name: ",
                        "Screen name selection",
                        JOptionPane.PLAIN_MESSAGE);
    }
    
    private String getServerAddress() {
        return JOptionPane.showInputDialog(frame,
                "Server IP Address: ",
                "Connect",
                JOptionPane.QUESTION_MESSAGE);
    }
    
    // Connect to server
    private void run() throws IOException {
        
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 4040);
        in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        // Process incoming messages
        while(true) {
            String line = in.readLine();
            
            if(line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if(line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if(line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n");
            }
        }
        
    }
    
    // Run Chat Client
    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}
