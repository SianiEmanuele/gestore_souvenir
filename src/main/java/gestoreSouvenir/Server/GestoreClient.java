/*
Protocollo comunicazione client server:
# == Fine stringhe da inviare
* == Voglio chiudere la comunicazione
 */
package gestoreSouvenir.Server;

import com.fasterxml.jackson.core.JsonProcessingException;
import gestoreSouvenir.Data.*;
import gestoreSouvenir.Users.Utente;

import java.io.*;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GestoreClient implements Runnable {
    private static final String CLOSE = "*"; //carattere che indica al client di voler chiudere la connessione
    private static final String ASK = "#"; //caratattere che indica al client di volere una risposta
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    Utente user;
    GestoreDB gestoreDB;
    Collezione collection;
    public GestoreClient(Socket socket, GestoreDB gestoreDB) {
        this.socket = socket;
        this.user = null; //di default l'utente viene messo a null
        this.gestoreDB = gestoreDB;
        try {
            //collego in e out con il server
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(),true);
        } catch(IOException e){
            System.err.println("Errore di comunicazione con il server");
            System.exit(-1);
        }
    }

    //menu principale, serve per loggare, registrarsi ed uscire
    public void WelcomeMenu() throws Exception {
        int rispostaClient;
        boolean esci = false; //flag per uscire dal menu di base
        boolean logged = false; //flag che indica che l'utente sia loggato
            while (!esci) {
                if (logged) { //se l'utente e' loggato, entra nel suo menu
                    CollectionMenu();
                    logged = false; //se entra qui e' perche' l'utente vuole andare al menu principale
                } else { //se l'utente non e' loggato continua ad entrare nel menu principale fin tanto che logga o decide di uscire
                    this.out.println("Ciao, cosa desideri fare?\n1.Login\n2.Registrazione\n3.Esci");
                    this.out.println(ASK); //il server indica di volere una risposta
                    rispostaClient = Integer.parseInt(in.readLine());
                    switch (rispostaClient) {
                        case 1:
                            logged = LoginMenu();
                            break;
                        case 2:
                            logged = RegisterMenu();
                            break;
                        case 3:
                            this.out.println("Arrivederci!");
                            //logged = false;
                            esci = true;
                            break;
                        default:
                            out.println("Risposta non valida, riprovare");
                            out.println();
                            break;
                    }
                }
            }
        this.out.println(CLOSE); //il server indica di voler chiudere la comunicazione
        in.close();
        out.close();
        socket.close();
    }

    public boolean LoginMenu() throws IOException{
        String username;
        String password;
        this.out.println("Inserisci il nome utente: ");
        this.out.println(ASK);
        username = in.readLine();
        this.out.println("Inserisci la password: ");
        this.out.println(ASK);
        password = in.readLine();
        this.user = gestoreDB.loginUtente(username,password);
        if(this.user != null) { //se l'utente non e' null allora e' loggato
            this.collection = this.user.getCollection(); //se e' loggato prendo la collezione
            out.println();
            return true;
        }
        else {
            out.println("Credenziali errate, riprova");
            out.println();
            out.println();
            return false;
        }
    }

    public boolean RegisterMenu() throws IOException {
        String username,password,nome,cognome,giorno,mese,anno, formattedData;
        Date data;
        boolean logged = false;
        this.out.println("Inserisci il nome utente: ");
        this.out.println(ASK);
        username = in.readLine();
        this.out.println("Inserisci la password: ");
        this.out.println(ASK);
        password = in.readLine();
        this.out.println("Inserisci il tuo nome: ");
        this.out.println(ASK);
        nome = in.readLine();
        this.out.println("Inserisci il tuo cognome: ");
        this.out.println(ASK);
        cognome = in.readLine();
        this.out.println("Inserisci anno di nascita: ");
        this.out.println(ASK);
        anno = in.readLine();
        this.out.println("Inserisci mese di nascita: ");
        this.out.println(ASK);
        mese = in.readLine();
        this.out.println("Inserisci giorno di nascita: ");
        this.out.println(ASK);
        giorno = in.readLine();
        formattedData = anno + "/" + mese + "/" + giorno;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); //se mette mese maggiore di 13 va avanti negli anni
        try {
            data = sdf.parse(formattedData);
        } catch (ParseException e) { //se l'utente inserisce un formato di data non valido, lo gestisco rimandandolo al menu principale
            out.println("Data inserita non valida");
            out.println();
            out.println();
            return false;
        }
        Utente user = new Utente(nome,cognome,data,username,password);
        logged = gestoreDB.registraUtente(user);
        if(logged) {
            this.user = user;
            this.collection = user.getCollection(); //se si registra prendo la collezione
            return true;
        }
        else{
            out.println("Lo username '" + username + "' e' gia' in uso \nUtilizzane un altro");
            out.println();
            out.println();
            return false;
        }
    }

    public void CollectionMenu() throws Exception {
        boolean principale = false; //flag per tornare al menu principale
        int selezione; //variabile per far navigare il client nel menu
        int tipoSouvenir;
        String provenienza,materiale, tipoBicchiere;
        double altezza, larghezza;
        Date data;
        out.println("Ciao " +this.user.getNome() + ", benvenuto nella tua Area Personale!");
        while(!principale) {
            boolean esci = false; //flag per uscire dai sotto menu
            //NB: e' la continuazione del print di loginMenu e di registerMenu, per questo lo spazio iniziale
            out.println("Cosa desideri fare?" +
                    "\n1. Visualizza collezione\n2. Aggiungi un souvenir\n3. Rimuovi souvenir\n4. Torna al menu principale");
            out.println(ASK);
            try { //se l'utente scrive una frase e non, viene gestito
                selezione = Integer.parseInt(in.readLine());
                switch (selezione) {
                    case 1: //Visualizza collezione dell'utente
                        out.println(this.collection);
                        break;
                    case 2: //aggiungi un souvenir
                        data = new Date(System.currentTimeMillis()); //data in cui viene inserito

                        while (!esci) { //cicla finche' non aggiunge il souvenir o l'utente vuole annullare
                            out.println("Inserisci la provenienza: ");
                            out.println(ASK);
                            provenienza = in.readLine();
                            out.println("Insersci il materiale: ");
                            out.println(ASK);
                            materiale = in.readLine();
                            out.println("Scegli il tipo di souvenir:\n1. Cartolina\n2. Calamita\n3. Bicchiere\n4. Annulla");
                            out.println(ASK);
                            tipoSouvenir = Integer.parseInt(in.readLine());
                            //switch case per aggiungere un souvenir
                            switch (tipoSouvenir) {

                                //Inserimento Cartolina
                                case 1:
                                    out.println("Inserisci l'altezza");
                                    out.println(ASK);
                                    altezza = Double.parseDouble(in.readLine());
                                    out.println("Insersci la larghezza:");
                                    out.println(ASK);
                                    larghezza = Double.parseDouble(in.readLine());
                                    Cartolina cartolina = new Cartolina(provenienza, data, materiale, larghezza, altezza);
                                        try {
                                            esci = gestoreDB.addSouvenir(cartolina, this.user); //inserisco il souvenir in tabella collezione e tabella souvenir
                                            if (!esci) {
                                                out.println("Souvenir gia' presente nella collezione");
                                                out.println();
                                                break;
                                            }
                                            out.println("Souvenir inserito con successo");
                                            out.println();
                                        } catch (JsonProcessingException e) { //eccezione ritornata da addSouvenir
                                            out.println("Errore di comunicazione con il database" + e);
                                            out.println();
                                            break;
                                        }
                                    break;

                                //Inserimento Calamita
                                case 2:
                                    out.println("Inserisci l'altezza");
                                    out.println(ASK);
                                    altezza = Double.parseDouble(in.readLine());
                                    out.println("Insersci la larghezza:");
                                    out.println(ASK);
                                    larghezza = Double.parseDouble(in.readLine());
                                    Calamita calamita = new Calamita(provenienza, data, materiale, larghezza, altezza);
                                    try {
                                        esci = gestoreDB.addSouvenir(calamita, this.user); //inserisco il souvenir in tabella collezione, tabella souvenir e collezione utente
                                        if (!esci) {
                                            out.println("Souvenir gia' presente nella collezione");
                                            out.println();
                                            break;
                                        }
                                        out.println("Souvenir inserito con successo");
                                        out.println();
                                    } catch (JsonProcessingException e) { //eccezione ritornata da addSouvenir
                                        out.println("Errore di comunicazione con il database" + e);
                                        out.println();
                                        break;
                                    }
                                    break;

                                //Inserimento Bicchiere
                                case 3:
                                    out.println("Inserisci la tipologia di bicchiere: \nCalice, Shot, Champagne_flute");
                                    out.println(ASK);
                                    tipoBicchiere = in.readLine();
                                    Bicchiere bicchiere = new Bicchiere(provenienza, data, materiale, tipoBicchiere);

                                    while (bicchiere.getTipo().equals(TipoBicchiere.undefined)) { //entra qui se il tipo di bicchiere inserito non e' valido
                                        out.println("Tipo non valido, inserirne uno valido");
                                        out.println(ASK);
                                        tipoBicchiere = in.readLine();
                                        bicchiere.setTipo(tipoBicchiere);
                                    }
                                    try {
                                        esci = gestoreDB.addSouvenir(bicchiere, this.user); //inserisco il souvenir in tabella collezione e tabella souvenir
                                        if (!esci) {
                                            out.println("Souvenir gia' presente nella collezione");
                                            out.println();
                                            break;
                                        }
                                        out.println("Souvenir inserito con successo");
                                        out.println();
                                    } catch (JsonProcessingException e) { //eccezione ritornata da addSouvenir
                                        out.println("Errore di comunicazione con il database" + e);
                                    }
                                    break;

                                //Torno al menu collezione
                                case 4:
                                    esci=true;
                                    break;
                                default:
                                    out.println("La risposta inserita non e' valida, riprovare");
                                    esci=true;
                                    break;
                            }
                        }
                        break;
                    case 3: //rimuovi un souvenir
                        if(this.user.getCollection().getNumItem() == 0){
                            out.println("La tua collezione non contiene elementi\n\n");
                            break;
                        }
                        out.println("Digita l'indice del souvenir da rimuovere");
                        out.println(this.collection.stampaPerEliminare());
                        out.println(ASK);
                        int indice = Integer.parseInt(in.readLine()); //se viene inserita una parola viene gestito nel catch finale
                        while (!((indice <= collection.getNumItem()) && (indice >= 0))) { //verifica che il numero sia valido
                            out.println("Inserire un numero valido");
                            out.println(ASK);
                            indice = Integer.parseInt(in.readLine());
                        }
                        Souvenir removedSouvenir = collection.getListaSouvenir().get(indice - 1); //il -1 perche' lista parte da 0
                        gestoreDB.removeSouvenir(indice -1,this.user);
                        System.out.println(removedSouvenir);
                        break;
                    case 4: //menu principale
                        out.println();
                        principale = true;
                        break;
                    default:
                        out.println("Risposta non valida, riprovare");
                        out.println();
                        break;
                }
            } catch (NumberFormatException num){ //eccezione se non viene inserito un numero
                out.println("Inserire un numero, non dei caratteri");
            }

        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                WelcomeMenu();
            } catch (Exception e) { //qualsiasi tipo di eccezione arrivi dal Welcome menu verra' gestita e rilanciato
                out.println("Risposta non valida, riprovare");
                continue;
            }
            break; //se non ci sono eccezioni deve uscire
        }
    }
}
