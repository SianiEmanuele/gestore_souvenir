package gestoreSouvenir.Data;
import org.bson.types.ObjectId;

import java.util.ArrayList;

/*
La collezione e' definita da una lista di generici Souvenir e dal numero di souvenir che contiene.
 */
public class Collezione {
    private ObjectId id;
    private ArrayList<Souvenir> listaSouvenir;
    private int numItem;

    //costruttore senza id per creare una nuova collezione
    public Collezione(ArrayList<Souvenir> lista) {
        this.id = new ObjectId();
        this.listaSouvenir = lista;
        if(!lista.isEmpty())
            this.numItem = listaSouvenir.size();
        else
            this.numItem = 0;
    }
    //costruttore con id per caricare una collezione dal DB
    public Collezione(ObjectId id, ArrayList<Souvenir> lista) {
        this.id = id;
        this.listaSouvenir = lista;
        if(!lista.isEmpty())
            this.numItem = listaSouvenir.size();
        else
            this.numItem = 0;
    }

    public ObjectId getId() {
        return this.id;
    }

    public int getNumItem() {
        return numItem;
    }

    public ArrayList<Souvenir> getListaSouvenir() {
        return listaSouvenir;
    }

    public Souvenir removeSouvenir(int index){
        if(this.numItem>0) {
            Souvenir removed =this.listaSouvenir.remove(index);
            this.numItem -= 1;
            return removed;
        }
        else
            return null;
    }

    public boolean addSouvenir(Souvenir newSouvenir){
        if(!listaSouvenir.contains(newSouvenir)) {
            this.listaSouvenir.add(newSouvenir);
            this.numItem += 1;
            return true;
        }
        else
            return false;
    }
    //stampa in maniera sintetica la collezione per far eliminare un souvenir
    public String stampaPerEliminare(){
        String info="";
        int indiceSouvenir=1;
        for (Souvenir souvenir : this.listaSouvenir) {
            info+= indiceSouvenir + ". ";

            //calamita
            if(souvenir instanceof Calamita){
                Calamita calamita = (Calamita) souvenir;
                info+="Calamita in " + calamita.getMateriale() + " da " + calamita.getProvenienza() + ", di dimensioni: " + calamita.getAltezza() + "x" + calamita.getLarghezza() +"\n";
            }
            if(souvenir instanceof Cartolina){
                Cartolina cartolina = (Cartolina) souvenir;
                info+="Cartolina in "  + cartolina.getMateriale() + " da "+ cartolina.getProvenienza() + ", di dimensioni: " + cartolina.getAltezza() + "x" + cartolina.getLarghezza() +"\n";
            }
            if(souvenir instanceof Bicchiere){
                Bicchiere bicchiere = (Bicchiere) souvenir;
                info+="Bicchiere da " + bicchiere.getTipo() + " in " + bicchiere.getMateriale() +",da " + bicchiere.getProvenienza() +"\n";
            }
            indiceSouvenir++;
        }
        return info;
    }
    @Override
    public String toString() {
        String info = "La collezione contiene " + this.numItem;
        if(this.numItem == 1)
            info+=" elemento. \n";
        else
            info+=" elementi. \n";
        for (Souvenir souvenir:this.listaSouvenir) {
            info+="\n" + souvenir.toString();
        }
        return info;
    }
}
