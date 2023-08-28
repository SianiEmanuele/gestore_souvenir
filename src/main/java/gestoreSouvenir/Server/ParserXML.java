package gestoreSouvenir.Server;

import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParserXML {
    private DocumentBuilderFactory dbf;
    private File file;

    //Costruttore che richiede il nome del documento di cui vogliamo fare il parsing
    public ParserXML(String fileName) {
        this.dbf = DocumentBuilderFactory.newInstance();
        this.file = new File(fileName);
    }

    //Metodo che ritorna la root del documento XML sottoforma di Element
    public Element getDOMParsedDocumentRoot() {
        DocumentBuilder db;
        Document document;
        Element root=null;
        try{
            db = this.dbf.newDocumentBuilder();
            document = db.parse(file);
            root = document.getDocumentElement();

        } catch (ParserConfigurationException | SAXException | IOException ex){ //gestione eccezioni parser
            ex.printStackTrace();
            Logger.getLogger(ParserXML.class.getName()).log(Level.SEVERE, null, ex);

        }
        return root;
    }
}
