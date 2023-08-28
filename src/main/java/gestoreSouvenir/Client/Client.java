/*
Protocollo comunicazione client server:
# == Fine stringhe da inviare
* == Voglio chiudere la comunicazione
 */

package gestoreSouvenir.Client;
import gestoreSouvenir.Server.ParserXML;
import org.w3c.dom.Element;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String CLOSE = "*"; //carattere che indica al client di voler chiudere la connessione
    private static final String ASK = "#"; //carattere che indica al client di volere una risposta

    public static void main (String[] args) throws Exception {
        Socket socket; //socket per collegarsi al server
        PrintWriter out;
        BufferedReader in;
        Scanner keyboard = new Scanner(System.in);
        String rispostaClient, domandaServer;
        String ip;
        int port;
        ParserXML parser = new ParserXML("config_client.xml"); //classe parser per prendere ip e porta da xml
        Element root = parser.getDOMParsedDocumentRoot();
        ip = root.getElementsByTagName("ip").item(0).getTextContent();
        port = Integer.parseInt(root.getElementsByTagName("port").item(0).getTextContent());
        socket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        //il client comunica con il server leggendo sempre cio' che scrnive fin quando la connessione non viene chiusa
        while (true) {
            domandaServer = in.readLine();
            if(domandaServer.equals(CLOSE)) //il server vuole smettere di comunicare
                break;
            else if(domandaServer.equals(ASK)) { //il server vuole una risposta
                rispostaClient = keyboard.nextLine();
                out.println(rispostaClient);
            }
            else //devo stampare la domanda
                System.out.println(domandaServer);
        }
        //chiusura comunicazione
        in.close();
        out.close();
        socket.close();
    }
}
