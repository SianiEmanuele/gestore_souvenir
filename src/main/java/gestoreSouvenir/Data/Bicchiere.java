package gestoreSouvenir.Data;
import org.bson.types.ObjectId;

import java.util.Date;
/*
La tipologia di bicchiere e' definita con un enum.
*/

public class Bicchiere extends Souvenir{
    private TipoBicchiere tipo;

    //costruttore senza id per creare un nuovo Bicchiere
    public Bicchiere(String provenienza, Date data, String materiale, String tipo) {
        super(provenienza, data, materiale); //richiama costruttore di Souvenir
        setTipo(tipo);
    }

    //costruttore con id per caricare un Bicchiere da DB
    public Bicchiere(ObjectId id, String provenienza, Date data, String materiale, String tipo) {
        super(id,provenienza, data, materiale); //richiama costruttore di Souvenir
        setTipo(tipo);
    }

//Setter e getter
    public TipoBicchiere getTipo() {
        return tipo;
    }

    public void setTipo(String tipoInput) throws IllegalArgumentException{
        try {
            this.tipo = TipoBicchiere.valueOf(tipoInput);
        } catch (IllegalArgumentException e) {
            this.tipo=TipoBicchiere.undefined;
        }
    }

    @Override
    public boolean equals(Object compared) {
        //se gli handle sono uguali, ritorna true
        if (this == compared)
            return true;

        //se il tipo di oggetto non e' una cartolina, ritorna falso
        if(!(compared instanceof Bicchiere))
            return false;

        //Converto l'oggetto in Cartolina
        Bicchiere comparedBicchiere = (Bicchiere) compared;
        if(this.tipo == comparedBicchiere.getTipo())
            return super.equals(compared);
        else
            return false;
    }
    @Override
    public String toString() {
        String info = "Tipo: Bicchiere";
        if(this.tipo != TipoBicchiere.undefined) //controlla che tipo sia undefined, in tal caso il print deve essere diverso
            info += " da " + this.tipo;
        info += "." + super.toString();
        return info;
    }
}





