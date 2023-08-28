package gestoreSouvenir.Users;
import gestoreSouvenir.Data.Collezione;
import gestoreSouvenir.Data.Souvenir;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.Date;

public class Utente extends Persona {
    private Collezione collection;
    private String username;
    private String password;
    private ObjectId idCollezione; //id che identifica la sua collezione

    //Costruttore senza idCollezione per creare un nuovo utente
    public Utente(String nome, String cognome, Date data, String username, String password) {
        super(nome, cognome, data);
        this.idCollezione = new ObjectId();
        this.collection = new Collezione(new ArrayList<Souvenir>());
        setUsername(username);
        setPassword(password);
    }

    //Costruttore con idCollezione per caricare un utente dal DB
    public Utente(ObjectId idCollection, String nome, String cognome, Date data, String username, String password) {
        super(nome, cognome, data);
        this.idCollezione = idCollection;
        this.collection = new Collezione(new ArrayList<Souvenir>());
        setUsername(username);
        setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ObjectId getIdCollection() {
        return idCollezione;
    }

    public Collezione getCollection() {
        return collection;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCollection(Collezione collection) {
        this.collection = collection;
    }

    public boolean addSouvenir(Souvenir added){
        return this.collection.addSouvenir(added);
    }

    public Souvenir removeSouvenir(int index){
        return this.collection.removeSouvenir(index);
    }
    public void StampaCollezione(){ //da rimuovere
        System.out.println(this.collection);
    }

    @Override
    public boolean equals(Object compared) {
        //se gli handle sono uguali, ritorna true
        if (this == compared)
            return true;

        //se il tipo di oggetto non e' un Souvenir, ritorna falso
        if(!(compared instanceof Utente))
            return false;

        //Converto l'oggetto in Souvenir
        Utente comparedUser = (Utente) compared;
        //Se username e password sono uguali, allora i souvenir sono uguali.
        if((this.username.equals(comparedUser.getUsername()) && (this.password.equals(comparedUser.getPassword()))))
            return true;
        else
            return false;
    }
}

