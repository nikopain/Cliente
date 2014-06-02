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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import serv.Serv;
/**
 *
 * @author niko
 */
public class Client {
    
    public static int port = 4000;
    private static PrintWriter os=null;
    public static String nombre;
    public static String dirIp;
    public static String puerto;
    public static void main(String[] args) throws IOException { 
            Socket cliente = new Socket("localhost", port);
            Serv sl= new Serv(cliente);
            try{
                BufferedReader in=new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                //el true es para autoflush, "8859_1" es un charset
                os = new PrintWriter(new OutputStreamWriter(cliente.getOutputStream(),"8859_1"),true);
                String ruta= "";//Se guarda la cadena de la peticion para luego procesarla y obtener la url
                String next;
                ruta=in.readLine();
                System.out.println(ruta);
                StringTokenizer st = new StringTokenizer(ruta);
                next= st.nextToken();
                if ((st.countTokens() >= 2) && next.equals("GET")) 
                {
                    sl.retornaFichero(st.nextToken()) ;
                }
                else if((st.countTokens()>=2)&&next.equals("POST")){
                    String currentLine =null;
                    do{
                        currentLine = in.readLine();            
                        if((currentLine.indexOf("Content-Disposition:")) != -1){
                            if(currentLine.indexOf("nombre")!= -1){//REVISA SI TIENE EL NAME=NOMBRE PUESTO EN EL FORM DEL HTML
                                currentLine= in.readLine();//PARA SALTAR INFO INNECESARIA
                                currentLine= in.readLine();//PARA SALTAR INFO INNECESARIA
                                nombre=currentLine;
                            }
                            else if(currentLine.indexOf("dirip")!= -1){//REVISA SI TIENE EL NAME=DIRIP PUESTO EN EL FORM DEL HTML
                                currentLine= in.readLine();//PARA SALTAR INFO INNECESARIA
                                currentLine= in.readLine();//PARA SALTAR INFO INNECESARIA
                                dirIp=currentLine;
                            }
                            else if(currentLine.indexOf("puerto")!= -1){//REVISA SI TIENE EL NAME=PUERTO PUESTO EN EL FORM DEL HTML
                                currentLine= in.readLine();//PARA SALTAR INFO INNECESARIA
                                currentLine= in.readLine();//PARA SALTAR INFO INNECESARIA
                                puerto=currentLine;
                            }
                        }
                    }while(in.ready());
                    next= st.nextToken();
                    if(next.equals("/pag1.html")||next.equals("/pag2.html")){
                        sl.retornaFichero(next);
                    }
                    else if(next.equals("/menu.html")){
                        sl.imprimirFichero(next,nombre,dirIp,puerto);
                    }
                }
                else 
                {
                    os.println("400 Petición Incorrecta") ;
                }
                ruta=in.readLine();
                
            }catch(Exception e){
                System.out.println( e);
            }
            
    }
}
