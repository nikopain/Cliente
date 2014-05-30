/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author niko
 */
public class Client {
    public static int port = 4000;
    public static void main(String[] args) throws IOException { 
            String sentence;   
            String modifiedSentence;   
            DataOutputStream outToServer=null;
            BufferedReader inFromServer=null;
            BufferedReader inFromUser =null;
            Socket clientSocket = new Socket("localhost", port);   
            while (true){
                inFromUser = new BufferedReader( new InputStreamReader(System.in)); 
                outToServer = new DataOutputStream(clientSocket.getOutputStream());   
                inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));   
                sentence = inFromUser.readLine(); 
                outToServer.writeBytes(sentence + '\n');   
                modifiedSentence = inFromServer.readLine();   
                System.out.println("FROM SERVER: " + modifiedSentence);  
            }
            
    }
    
}
