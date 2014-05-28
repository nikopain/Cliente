/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author niko
 */
public class Client {

    
    public static class Clientes extends Thread{
        private Socket cliente=null;
        private PrintWriter os=null;
        private String nombre;
        private String dirIp;
        private String puerto;
        public Clientes(Socket cl) {
            cliente=cl;
        }
        public void run (){
            
        }
    }
}
