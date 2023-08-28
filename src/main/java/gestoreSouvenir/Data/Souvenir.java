package gestoreSouvenir.Data;
import org.bson.types.ObjectId;

import java.text.DateFormat;
import java.util.Date;

public abstract class Souvenir {
    private final String provenienza;
    private final Date data;
    private final String materiale;
    private ObjectId id;

    //costruttore senza id per creare un nuovo Souvenir
    public Souvenir(String provenienza, Date data, String materiale) {
        this.id = new ObjectId();
        this.provenienza = provenienza;
        this.data = data;
        this.materiale = materiale;
    }

    //costruttore con id per caricare il Souvenir dal db
    public Souvenir(ObjectId id, String provenienza, Date data, String materiale) {
        this.id = id;
        this.provenienza = provenienza;
        this.data = data;
        this.materiale = materiale;
    }

    public String getProvenienza() {

        return provenienza;
    }

    public Date getData() {

        return data;
    }

    public String getMateriale() {
        return materiale;
    }

    public ObjectId getId() {
        return id;
    }

    @Override
    public String toString() {
        String info = "\nProvenienza: " + this.provenienza + ".\nMateriale: " + this.materiale + ".\nAcquistato in data: " + DateFormat.getDateInstance().format(this.data) +"\n";
        return info;
        //il comando DateFormat.getDateInstance().format() serve per far scrivere la data in italiano anziche' inglese
    }

    @Override
    public boolean equals(Object compared) {
        //se gli handle sono uguali, ritorna true
        if (this == compared)
            return true;

        //se il tipo di oggetto non e' un Souvenir, ritorna falso
        if(!(compared instanceof Souvenir))
            return false;

        //Converto l'oggetto in Souvenir
        Souvenir comparedSouvenir = (Souvenir) compared;
        //Se Materiale e provenienza sono uguali, allora i souvenir sono uguali.
        if((this.materiale.equals(comparedSouvenir.getMateriale()) && (this.provenienza.equals(comparedSouvenir.getProvenienza()))))
            return true;
        else
            return false;
    }
}
