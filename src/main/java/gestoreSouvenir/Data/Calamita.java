package gestoreSouvenir.Data;

import org.bson.types.ObjectId;

import java.util.Date;

public class Calamita extends Souvenir{
    private final double larghezza;
    private final double altezza;

    //costruttore senza id per creare una Calamita nuova
    public Calamita(String provenienza, Date data, String materiale, double larghezza, double altezza) {
        super(provenienza,data,materiale);
        this.larghezza = larghezza;
        this.altezza = altezza;
    }

    //costruttore con id per caricare una calamita dal DB
    public Calamita(ObjectId id, String provenienza, Date data, String materiale, double larghezza, double altezza) {
        super(id,provenienza,data,materiale);
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
    public String toString() {
        String info = "Tipo: Calamita.\nDimensioni: " + this.altezza + " x " + this.larghezza + ".";
        info += super.toString();
        return info;
    }

    @Override
    public boolean equals(Object compared) {
        //se gli handle sono uguali, ritorna true
        if (this == compared)
            return true;

        //se il tipo di oggetto non e' una calamita, ritorna falso
        if(!(compared instanceof Calamita))
            return false;

        //Converto l'oggetto in Calamita
        Calamita comparedCalamita = (Calamita) compared;
        if((this.altezza == comparedCalamita.getAltezza()) && (this.larghezza == comparedCalamita.getLarghezza()))
            return super.equals(compared);
        else
            return false;
    }

}
