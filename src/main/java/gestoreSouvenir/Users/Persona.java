package gestoreSouvenir.Users;
import org.bson.types.ObjectId;

import java.text.DateFormat;
import java.util.Date;
public abstract class Persona {
    private final String nome, cognome;
    private final Date dataDiNascita;


    public Persona( String nome, String cognome, Date data) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataDiNascita = data;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public Date getDataDiNascita() {
        return dataDiNascita;
    }

    @Override
    public String toString() {
        String info = this.nome + ", " + this.cognome + " " + DateFormat.getDateInstance().format(this.dataDiNascita) +"\n";
        return info;
    }
}