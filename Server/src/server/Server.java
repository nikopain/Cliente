package server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author niko
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    private static int port= 4020;
    public static int i =0;
    public static void main(String[] args) throws IOException {
        try{
            ServerSocket skts = new ServerSocket(port);
            while (true){
                Socket cl= skts.accept();
                Clientes Tcl= new Clientes(cl);
                Tcl.start();
            }
        }
        catch(IOException e) {
            System.out.println(e);
        }
    }

    private static class Clientes extends Thread{
        private Socket cliente=null;
        private PrintWriter os=null;
        private String nombre;
        private String dirIp;
        private String puerto;
        private String user;
        private String ip;
        private String mensaje;
        private String path;
        public Clientes(Socket cl) {
            cliente=cl;
        }
        public void run(){
            try{
                BufferedReader in=new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                os = new PrintWriter(new OutputStreamWriter(cliente.getOutputStream(),"8859_1"),true);
                String ruta= "";//Se guarda la cadena de la peticion para luego procesarla y obtener la url
                String next;
                String pag;
                ruta=in.readLine();
                StringTokenizer st = new StringTokenizer(ruta);
                next= st.nextToken();
                pag= st.nextToken();
                System.out.println("pag ="+pag);
                System.out.println("next="+next);
                if ((st.countTokens() >= 1) && next.equals("GET")) 
                {
                    retornaFichero(pag) ;
                }
                else if((st.countTokens()>=1)&&next.equals("POST")&& (pag.equals("/menu.html")||pag.equals("/pag1.html")||pag.equals("/pag2.html"))){
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
                    //next= st.nextToken();
                    if(pag.equals("/pag1.html")||pag.equals("/pag2.html")){
                        retornaFichero(pag);
                    }
                    else if(pag.equals("/menu.html")){
                        if(nombre!=null&&dirIp!=null&&puerto!=null)
                            imprimirFichero(pag,nombre,dirIp,puerto);
                        else
                            retornaFichero(pag);
                    }
                }
                else if((st.countTokens()>=1)&&next.equals("POST")&&pag.equals("/chat.html")){
                    if (i==0){
                        retornaFichero(pag);
                        i=1;
                    }
                    else if(i==1){
                        retornaFichero(pag);
                        String cline=null;
                        try{
                            while(true){
                                cline=in.readLine();
                                System.out.println(cline);
                                if((cline.indexOf("Content-Disposition:")) != -1){
                                    if(cline.indexOf("user")!= -1){
                                        cline= in.readLine();
                                        cline= in.readLine();
                                        user=cline;
                                    }
                                    else if(cline.indexOf("ip")!= -1){
                                        cline= in.readLine();
                                        cline= in.readLine();
                                        ip=cline;
                                    }
                                    else if(cline.indexOf("msje")!= -1){
                                        cline= in.readLine();
                                        cline= in.readLine();
                                        mensaje=cline;
                                    }/*
                                    else if(cline.indexOf("ip2")!=-1){
                                        cline= in.readLine();//PARA SALTAR INFO INNECESARIA
                                        cline= in.readLine();//PARA SALTAR INFO INNECESARIA
                                        ip=cline;
                                    }
                                    else if(cline.indexOf("arch")!=-1){
                                        cline= in.readLine();//PARA SALTAR INFO INNECESARIA
                                        cline= in.readLine();//PARA SALTAR INFO INNECESARIA
                                        path=cline;
                                    }*/
                                }
                                
                                
                            }
                        }catch(Exception e){

                        }
                        try{
                            Socket scliente = new Socket("localhost",5000);
                            DataOutputStream salida = new DataOutputStream(scliente.getOutputStream());
                            if(user!=null&&ip!=null&&mensaje!=null)
                                salida.writeBytes("enviar" + " " + user + " " + ip + " " + mensaje + '\n');
                           /* else if(ip!=null&&path!=null)
                                salida.writeBytes("archivo "+ip + " "+path+'\n');*/
                            scliente.close();
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                else if((st.countTokens()>=1)&&next.equals("POST")&&pag.equals("/verchat.html")){
                    
                    try{
                        String msje;
                        Socket scliente = new Socket("localhost",5000);
                        InetAddress address = InetAddress.getLocalHost();
                        String myIp = address.getHostAddress();
                        DataOutputStream salida = new DataOutputStream(scliente.getOutputStream());
                        BufferedReader entrada= new BufferedReader(new InputStreamReader(scliente.getInputStream()));
                        salida.writeBytes("recibido " + myIp+'\n');
                        msje=entrada.readLine();
                        scliente.close();
                        String contenido="";/*
                        String contenido2="";
                        String contenido3="";*/
                        String comienzo="<html>\n" +
                        "    <head>\n" +
                        "        <title>Mensajes Avioncito de Papel</title>\n" +
                        "        <meta charset=\"UTF-8\">\n" +
                        "    </head>\n" +
                        "    <body bgcolor=\"#A9F5F2\">         \n" +
                        "        <center> \n" +
                        "            <center><h1>Centro de Mensajes</h1></center>\n" +
                        "                <form method=\"POST\" enctype=\"multipart/form-data\" action=\"menu.html\">\n" +
                        "                    <table>\n" +
                        "                        <tr>\n" +
                        "                            <td><CENTER> Mensajes enviados a: " +myIp+" </CENTER><br><br><br></td>\n" +
                        "                            </table>"+
                        "                            <table><tr>  "+
                        "                            <td><CENTER><strong>De</strong></CENTER></td> " +
                        "                            <td><strong><CENTER>\tMensaje\t</CENTER></strong></td> </CENTER><br><br><br>\n" +

                        "                        </tr>\n"  ;
                        StringTokenizer tk=new StringTokenizer(msje);
                        while(tk.hasMoreTokens()){
                            /*contenido=" <tr> <td align=\"left\"   BGCOLOR=\"#00FF80\">"+tk.nextToken()+": </td>\n" ;
                            contenido2="<td WIDTH=130 BGCOLOR=\"#A9F5A9\"><CENTER>"+tk.nextToken()+" </CENTER></td> </tr>\n" ;
                            contenido3 += contenido + contenido2;*/
                            contenido+=" <tr> <td align=\"left\"   BGCOLOR=\"#00FF80\">"+tk.nextToken()+": </td>\n"+
                                    "<td WIDTH=130 BGCOLOR=\"#A9F5A9\"><CENTER>"+tk.nextToken()+" </CENTER></td> </tr>\n" ;
                        }
                        String fin=
                        "                    </table>\n" +
                        "                    <br><br><br><br>\n" +
                        "                    <input type=\"submit\" value=\"Ir a Menú\">\n" +
                        "                </form>\n" +
                        "            </tr>                    \n" +
                        "        </center>\n" +
                        "    </body>\n" +
                        "</html>\n" ;
                        os.println(comienzo+contenido+fin);    
                        os.close();
                    }catch(Exception e){
                        System.out.println(e);
                    }
                }
                else 
                {
                    os.println("400 Petición Incorrecta") ;
                }
                //ruta=in.readLine();
                
            }catch(Exception e){
                System.out.println( e);
            }
            
        }
        void retornaFichero(String sfichero)
	{
            if (sfichero.startsWith("/"))
            {
                    sfichero = sfichero.substring(1) ;
            }
            // si acaba en /, le retornamos el index.htm de ese directorio
            // si la cadena esta vacia, no retorna el index.htm principal
            if (sfichero.endsWith("/") || sfichero.equals(""))
            {
                    sfichero = sfichero + "menu.html" ;
            }
            try
            {
                // Ahora leemos el fichero y lo retornamos
                if(sfichero.equals("pag2.html")){
                    String contactos="";
                    File cont= new File("contactos.txt");
                    if(cont.exists()){
                        StringTokenizer s;
                        BufferedReader fLocal= new BufferedReader(new FileReader(cont));
                        String lin="";
                                              String comienzo="<html>\n" 
                        + " <head>\n" 
                            +"  <title>Mostar Datos</title>\n" 
                            +"  <meta charset=\"UTF-8\">\n" 
                            +"  <meta name=\"viewport\" content=\"width=device-width\">\n" 
                        +"  </head>\n" 
                        
                        + "    <body bgcolor=\"#A9F5F2\">        "
                            +"<center><h1>Avion de Papel</h1><h2>Mostrar Datos</h2></center>"     
                            +"<form method=\"POST\" action=\"insertar.html\">\n" 
                                +"<center> <table>"
                                + "<table>"
                                + "<tr>"
                                + "<td><center>Nombre</center></td><td><center>Mensaje</center></td>"
                                + "<tr>"
                                    +"<td>"
                                    + "<center><select name=\"contactos\" multiple=\"multiple\">";
                                            while((lin=fLocal.readLine())!=null){
                                                s= new StringTokenizer(lin);
                                                contactos= contactos+"<option>"+ s.nextToken()+"</option>\n";
                                                }
                                            String fin= "</select>\n </center>"
                                    + "</td>"
                                    + "<td>"
                                    + "     <textarea rows=\"4\" cols=\"50\"></textarea>    \n" 
                                    + "</td>"
                                + "</tr>"
                                + "<tr>"
                                    + "<td><input type=\"submit\" value=\"Agregar Contacto\">\n</td>" 
                                + "</tr>"
                            +   "</form>\n" 
                        +   "</body>\n" 
                    +   "</html>";
                        os.println(comienzo+contactos+fin);
                        fLocal.close();
                        os.close();
                    }
                    else{
                        os.println("HTTP/1.0 400 ok");
                        os.close();
                    }
                }
                else{
                    File mifichero = new File(sfichero) ;
                    if (mifichero.exists()) 
                    {
                        BufferedReader ficheroLocal = new BufferedReader(new FileReader(mifichero));
                        String linea = "";
                        do			
                        {
                            linea = ficheroLocal.readLine();
                           // if(linea.)
                            if (linea != null )
                            {
                                // sleep(500);
                                os.println(linea);
                            }
                        }
                        while (linea != null);

                        ficheroLocal.close();
                        os.close();

                    }  // fin de si el fiechero existe 
                    else
                    {	
                        os.println("HTTP/1.0 400 ok");
                        os.close();
                    }
                }

            }
            catch(Exception e){
            }

        }

        private void imprimirFichero(String nextToken,String nombre1,String dirIp1,String puerto1) {
            Writer writer = null;

            try {
                
                writer = new BufferedWriter(new OutputStreamWriter(
                      new FileOutputStream("contactos.txt",true), "utf-8"));
                        writer.write("\r\n");
                        writer.write(nombre1+ " ");
                        writer.write(dirIp1+ " ");
                        writer.write(puerto1);
            } catch (IOException ex) {
              // report
            } finally {
               try {writer.close();} catch (Exception ex) {}
            }
            retornaFichero(nextToken);
            //lee archivo si no existe
            
        }
    }
    
}