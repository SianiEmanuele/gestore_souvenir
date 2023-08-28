package gestoreSouvenir.Server;
import org.w3c.dom.Element;

import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main (String[] args) throws Exception{
        ServerSocket serverSocket;
        Socket socket;
        GestoreClient gestoreClient;
        boolean esci = false;
        int port;
        String uri, dbName;
        Thread thread;
        ParserXML parser = new ParserXML("config_server.xml"); //parser per leggere la configurazione di server e DB
        Element root = parser.getDOMParsedDocumentRoot(); //il parser prende la root config
        Element db = (Element) root.getElementsByTagName("db").item(0); //figlio db
        uri = db.getElementsByTagName("uri").item(0).getTextContent(); //uri del server
        dbName = db.getElementsByTagName("dbname").item(0).getTextContent(); //nome del DB

        Element server = (Element) root.getElementsByTagName("server").item(0); //figlio server
        port = Integer.parseInt(server.getElementsByTagName("port").item(0).getTextContent()); //porta del server

        serverSocket = new ServerSocket(port);
        GestoreDB gestoreDB = new GestoreDB(uri,dbName);//un solo gestoreDB per tutti i clienti
        gestoreDB.loadCollections();


        //Il server deve sempre stare in ascolto
        while(!esci){
            System.out.println("Server in ascolto");
            socket = serverSocket.accept();
            System.out.println("E' arrivato un cliente");
            gestoreClient = new GestoreClient(socket,gestoreDB); //quando arriva un cliente lo gestisco con un thread
            thread = new Thread(gestoreClient);
            thread.start();
        }
    }

}
