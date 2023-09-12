package gestoreSouvenir.Server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import gestoreSouvenir.Data.*;
import gestoreSouvenir.Users.Utente;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class GestoreDB {
    private static String uri;
    private static String dbName;
    private MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> tabellaUtenti;
    private static MongoCollection<Document> tabellaCalamite;
    private static MongoCollection<Document> tabellaCartoline;
    private static MongoCollection<Document> tabellaBicchieri;
    private static MongoCollection<Document> tabellaCollezioni;
    private static ArrayList<Utente> listaUtenti;
    private static ArrayList<Souvenir> listaSouvenir;

    //togli il throws
    public GestoreDB(String uri, String dbName){
        // Crea una connessione al database MongoDB ed alle rispettive tabelle
        mongoClient = MongoClients.create(uri); //uri in xml
        database = mongoClient.getDatabase(dbName); //dbname in xml
        tabellaUtenti = database.getCollection("Utenti");
        tabellaCalamite = database.getCollection("Calamite");
        tabellaCartoline = database.getCollection("Cartoline");
        tabellaBicchieri = database.getCollection("Bicchieri");
        tabellaCollezioni = database.getCollection("Collezioni");
        listaUtenti = getListaUtenti(); //alloca la lista utenti
        listaSouvenir = new ArrayList<Souvenir>(); //alloca la lista di Souvenir
        listaSouvenir.addAll(getListaCartoline()); //aggiunge tutte le cartoline ai souvenir
        listaSouvenir.addAll(getListaCalamite());
        listaSouvenir.addAll(getListaBicchieri());
    }

    //scarica la lista degli utenti dalla tabella utenti, ritorna la lista degli utenti
    public ArrayList<Utente> getListaUtenti(){
        ArrayList<Utente> listaUtenti = new ArrayList<Utente>();
        Document query = new Document();
        ArrayList<Document> results = tabellaUtenti.find(query).into(new ArrayList<Document>());
        for (Document result: results) {
            String nome = result.getString("nome");
            String username = result.getString("username");
            String password = result.getString("password");
            String cognome = result.getString("cognome");
            Long dataLong =result.getLong("dataDiNascita"); //e' un long salvato nel DB
            String idStringa = result.getString("idCollezione");
            ObjectId idCollezione = new ObjectId(idStringa);
            Date data = new Date(dataLong);
            Utente user = new Utente(idCollezione, nome,cognome,data,username,password);
            listaUtenti.add(user);
        }
        return listaUtenti;
    }
    //metodo che ritorna una lista contente tutte le Cartoline della tabella Cartoline
    public ArrayList<Cartolina> getListaCartoline(){
        ArrayList<Cartolina> listaCartoline = new ArrayList<Cartolina>();
        Document query = new Document();
        ArrayList<Document> results = tabellaCartoline.find(query).into(new ArrayList<Document>());
        for (Document result: results) {
            ObjectId id = new ObjectId(result.getString("_id"));
            String provenienza = result.getString("provenienza");
            String materiale = result.getString("materiale");
            double larghezza = result.getDouble("larghezza");
            double altezza = result.getDouble("altezza");
            long dataLong =result.getLong("data"); //e' un long salvato nel DB
            Date data = new Date(dataLong); //crea la data
            Cartolina cartolina = new Cartolina(id,provenienza,data,materiale,larghezza,altezza); //crea la cartolina
            listaCartoline.add(cartolina); //aggiunge la cartolina alla lista
        }
        return  listaCartoline;
    }

    //metodo che ritorna una lista contente tutte i Bicchieri della tabella Bicchieri
    public ArrayList<Bicchiere> getListaBicchieri(){
        ArrayList<Bicchiere> listaBicchieri = new ArrayList<Bicchiere>();
        Document query = new Document();
        ArrayList<Document> results = tabellaBicchieri.find(query).into(new ArrayList<Document>());
        for (Document result: results) {
            ObjectId id = new ObjectId(result.getString("_id"));
            String provenienza = result.getString("provenienza");
            String materiale = result.getString("materiale");
            String tipo = result.getString("tipo");
            long dataLong =result.getLong("data"); //e' un long salvato nel DB
            Date data = new Date(dataLong); //crea la data
            Bicchiere bicchiere = new Bicchiere(id,provenienza,data,materiale,tipo); //crea il bicchiere
            listaBicchieri.add(bicchiere); //aggiunge il bicchiere alla lista
        }
        return  listaBicchieri;
    }

    //metodo che ritorna una lista contente tutte le Calamite della tabella Calamite
    public ArrayList<Calamita> getListaCalamite(){
        ArrayList<Calamita> listaCalamite = new ArrayList<Calamita>();
        Document query = new Document();
        ArrayList<Document> results = tabellaCalamite.find(query).into(new ArrayList<Document>());
        for (Document result: results) {
            ObjectId id = new ObjectId(result.getString("_id"));
            String provenienza = result.getString("provenienza");
            String materiale = result.getString("materiale");
            double larghezza = result.getDouble("larghezza");
            double altezza = result.getDouble("altezza");
            long dataLong =result.getLong("data"); //e' un long salvato nel DB
            Date data = new Date(dataLong); //creo la data da passare
            Calamita calamita = new Calamita(id,provenienza,data,materiale,larghezza,altezza); //crea la calamita
            listaCalamite.add(calamita); //aggiunge alla lista di calamite
        }
        return  listaCalamite;
    }

    //ritorna l'utente se il login e' andato a buon fine, null in caso contrario.
    public Utente loginUtente(String username, String password){
        for (Utente user:listaUtenti) {
            if((user.getUsername().equals(username)) && (user.getPassword().equals(password)))
                return user;
        }
        return null;
    }

    //permette di registrare un nuovo utente, se lo username e' gia' in uso lancia l'eccezione e torna false. Al contrario torna true e lo inserisce nel db
    public boolean registraUtente(Utente newUser) throws JsonProcessingException {
        for (Utente usr:listaUtenti) {
            if((usr.getUsername().equals(newUser.getUsername())))
                return false;
        }
        listaUtenti.add(newUser);
        ObjectMapper mapper = new ObjectMapper(); //classe per trasformare un oggetto in un JSON

        //serializzatore Custom per salvare i campi nella maniera desiderata
        SimpleModule module = new SimpleModule("CustomUtenteSerializer", new Version(1, 0, 0, null, null, null));
        module.addSerializer(Utente.class, new CustomUtenteSerializer());
        mapper.registerModule(module);
        String jsonString = mapper.writeValueAsString(newUser); //trasforma l'utente in JSON
        tabellaUtenti.insertOne(Document.parse(jsonString)); //inserisce l'utente nel DB
        return true;
    }

    //Dato un idCollection fa le query nel DB per prendere la lista di Souvenir. Ritorna la lista
    public ArrayList<Souvenir> getCollezione(ObjectId idCollezione){
        ArrayList<Souvenir> souvenirCollezione = new ArrayList<Souvenir>();
        List<String> lista; //lista degli id degli oggetti della collezione (Stringhe)
        Document query = new Document("_id",idCollezione.toString());
        ArrayList<Document> results = tabellaCollezioni.find(query).into(new ArrayList<Document>());

        //ciclo per scorrere i risultati della query(collezione cercata)
        for(Document result : results){
            lista = result.getList("objects_id",String.class); //Prendo la lista di elementi e la casto a String, non e' possibile castarla ad ObjectId

            //ciclo per scorrere la lista di id dei Souvenir trovati nella collezione
            for(String elem : lista){
                ObjectId id = new ObjectId(elem); //qui converto le stringhe in objectID

                //ciclo for per scorrere la lista di tutti i souvenir
                for (Souvenir souvenir:listaSouvenir) {
                    if (souvenir.getId().equals(id)) //se trova un souvenir con lo stesso id, lo aggiunge
                        souvenirCollezione.add(souvenir);
                }
            }
        }
        return souvenirCollezione;
    }

    //metodo che carica tutte le collezioni di tutti gli utenti
    public void loadCollections(){
        for(Utente user : listaUtenti){
            ObjectId idColl = user.getIdCollection();
            Collezione collection = new Collezione(idColl,getCollezione(idColl));
            user.setCollection(collection);
        }
    }

    //inserisce il Souvenir nel DB
    public boolean addSouvenir(Souvenir souvenir, Utente user) throws JsonProcessingException {
        Collezione collection = user.getCollection();
        String idCollezione = collection.getId().toString();
        boolean added = collection.addSouvenir(souvenir);
        ObjectMapper mapper = new ObjectMapper(); //oggetto per trasformare una classe in un JSON
        if(added){
            listaSouvenir.add(souvenir); //aggiungo alla lista souvenir del gestoreDB
            //controllo che tipo di souvenir e' ed inserisco nella tabella apposita
            if(souvenir instanceof Cartolina){
                Cartolina cartolina = (Cartolina) souvenir;
                SimpleModule moduleCartolina = new SimpleModule("CustomCartolinaSerializer", new Version(1, 0, 0, null, null, null));
                moduleCartolina.addSerializer(Cartolina.class, new CustomCartolinaSerializer());
                mapper.registerModule(moduleCartolina);
                String jsonString = mapper.writeValueAsString(cartolina); //trasforma la cartolina in JSON
                Document docAdded = Document.parse(jsonString);
                tabellaCartoline.insertOne(docAdded); //inserisce la cartolina nel DB
            }
            else if (souvenir instanceof Calamita){
                Calamita calamita = (Calamita) souvenir;
                SimpleModule moduleCalamita= new SimpleModule("CustomCalamitaSerializer", new Version(1, 0, 0, null, null, null));
                moduleCalamita.addSerializer(Calamita.class, new CustomCalamitaSerializer());
                mapper.registerModule(moduleCalamita);
                String jsonString = mapper.writeValueAsString(calamita); //trasforma la calamita in JSON
                tabellaCalamite.insertOne(Document.parse(jsonString)); //inserisce la calamita nel DB
            }
            else if(souvenir instanceof Bicchiere){
                Bicchiere bicchiere = (Bicchiere) souvenir;
                System.out.println("aggiungo bicchiere");
                SimpleModule moduleBicchiere= new SimpleModule("CustomBicchiereSerializer", new Version(1, 0, 0, null, null, null));
                moduleBicchiere.addSerializer(Bicchiere.class, new CustomBicchiereSerializer());
                mapper.registerModule(moduleBicchiere);
                String jsonString = mapper.writeValueAsString(bicchiere); //trasforma il bicchiere in JSON
                tabellaBicchieri.insertOne(Document.parse(jsonString)); //inserisce il bicchiere nel DB
            }
            else{
                return false;
            }

            //qui inserisco il souvenir anche nella tabella della collezione
            Document query = new Document("_id",idCollezione);

            Bson updates = Updates.combine(
                    Updates.addToSet("objects_id", String.valueOf(souvenir.getId())));
            UpdateOptions options = new UpdateOptions().upsert(true);
            tabellaCollezioni.updateOne(query,updates,options); //NB: se la collezione non esiste, la crea
            return true;
        }
        else{
            return false;
        }
    }

    public boolean removeSouvenir(int index, Utente user) throws JsonProcessingException {
        Collezione collection = user.getCollection();
        String idCollezione = collection.getId().toString();
        Souvenir souvenir = collection.removeSouvenir(index);
        ObjectMapper mapper = new ObjectMapper(); //oggetto per trasformare una classe in un JSON
        if(souvenir != null){
            listaSouvenir.remove(souvenir); //rimuovo dalla lista souvenir del gestoreDB
            //controllo che tipo di souvenir e rimuovo dalla tabella apposita
            if(souvenir instanceof Cartolina){
                Cartolina cartolina = (Cartolina) souvenir;
                SimpleModule moduleCartolina = new SimpleModule("CustomCartolinaSerializer", new Version(1, 0, 0, null, null, null));
                moduleCartolina.addSerializer(Cartolina.class, new CustomCartolinaSerializer());
                mapper.registerModule(moduleCartolina);
                String jsonString = mapper.writeValueAsString(cartolina); //trasforma la cartolina in JSON
                tabellaCartoline.deleteOne(Document.parse(jsonString)); //rimuove la cartolina dal DB
            }
            else if (souvenir instanceof Calamita){
                SimpleModule moduleCalamita= new SimpleModule("CustomCalamitaSerializer", new Version(1, 0, 0, null, null, null));
                moduleCalamita.addSerializer(Calamita.class, new CustomCalamitaSerializer());
                mapper.registerModule(moduleCalamita);
                Calamita calamita = (Calamita) souvenir;
                String jsonString = mapper.writeValueAsString(calamita); //trasforma la calamita in JSON
                tabellaCalamite.deleteOne(Document.parse(jsonString)); //rimuove la calamita dal DB
            }
            else if(souvenir instanceof Bicchiere){
                SimpleModule moduleBicchiere= new SimpleModule("CustomBicchiereSerializer", new Version(1, 0, 0, null, null, null));
                moduleBicchiere.addSerializer(Bicchiere.class, new CustomBicchiereSerializer());
                mapper.registerModule(moduleBicchiere);
                Bicchiere bicchiere = (Bicchiere) souvenir;
                String jsonString = mapper.writeValueAsString(bicchiere); //trasforma il bicchiere in JSON
                tabellaBicchieri.deleteOne(Document.parse(jsonString)); //rimuove il bicchiere dal DB
            }
            else{
                return false;
            }
            //qui rimuove il souvenir anche dalla tabella della collezione
            Document query = new Document("_id",idCollezione);
            Bson updates = Updates.combine(
                    Updates.pull("objects_id", String.valueOf(souvenir.getId())));
            UpdateOptions options = new UpdateOptions().upsert(true);
            tabellaCollezioni.updateOne(query,updates,options);

            return true;
        }
        else{
            return false;
        }
    }
}
