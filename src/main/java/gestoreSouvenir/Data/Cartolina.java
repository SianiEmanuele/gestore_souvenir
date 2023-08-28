package gestoreSouvenir.Data;
import org.bson.types.ObjectId;

import java.util.Date;


public class Cartolina extends Souvenir{

    private final double larghezza;
    private final double altezza;

    //cotruttore senza id per creare una nuova Cartolina
    public Cartolina(String provenienza, Date data, String materiale, double larghezza, double altezza){
        super(provenienza, data, materiale);
        this.larghezza = larghezza;
        this.altezza = altezza;
    }

    //cotruttore con id per caricare una Cartolina dal DB
    public Cartolina(ObjectId id, String provenienza, Date data, String materiale, double larghezza, double altezza){
        super(id,provenienza, data, materiale);
        this.larghezza = larghezza;
        this.altezza = altezza;
    }

    public double getLarghezza() {

        return larghezza;
    }

    public double getAltezza() {

        return altezza;
    }

    @Override
    public boolean equals(Object compared) {
        //se gli handle sono uguali, ritorna true
        if (this == compared)
            return true;

        //se il tipo di oggetto non e' una cartolina, ritorna falso
        if(!(compared instanceof Cartolina))
            return false;

        //Converto l'oggetto in Cartolina
        Cartolina comparedCartolina = (Cartolina) compared;
        if((this.altezza == comparedCartolina.getAltezza()) && (this.larghezza == comparedCartolina.getLarghezza()))
            return super.equals(compared);
        else
            return false;
    }
    @Override
    public String toString() {
        String info = "Tipo: Cartolina.\nDimensioni: " + this.altezza + " x " + this.larghezza + ".";
        info += super.toString();
        return info;
    }

}
