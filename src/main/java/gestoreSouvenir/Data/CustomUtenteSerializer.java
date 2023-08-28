package gestoreSouvenir.Data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import gestoreSouvenir.Users.Utente;
import org.bson.types.ObjectId;

import java.io.IOException;

public class CustomUtenteSerializer extends StdSerializer<Utente> {

public CustomUtenteSerializer(){
        this(null);
        }

public CustomUtenteSerializer(Class<Utente> t){
        super(t);
        }

@Override
public void serialize(
        Utente user, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("nome", user.getNome());
        jsonGenerator.writeStringField("cognome", user.getCognome());
        jsonGenerator.writeStringField("password", user.getPassword());
        jsonGenerator.writeStringField("username", user.getUsername());
        jsonGenerator.writeNumberField("dataDiNascita",user.getDataDiNascita().getTime());
        jsonGenerator.writeStringField("idCollezione", String.valueOf(user.getIdCollection()));
        jsonGenerator.writeEndObject();
        }

}
