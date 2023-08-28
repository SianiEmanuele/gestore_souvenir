package gestoreSouvenir.Data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import gestoreSouvenir.Users.Utente;
import org.bson.types.ObjectId;

import java.io.IOException;

public class CustomCartolinaSerializer extends StdSerializer<Cartolina> {

    public CustomCartolinaSerializer(){
        this(null);
    }

    public CustomCartolinaSerializer(Class<Cartolina> t){
        super(t);
    }

    @Override
    public void serialize(
            Cartolina cartolina, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("_id", cartolina.getId().toString());
        jsonGenerator.writeStringField("materiale", cartolina.getMateriale());
        jsonGenerator.writeStringField("provenienza", cartolina.getProvenienza());
        jsonGenerator.writeNumberField("data",cartolina.getData().getTime());
        jsonGenerator.writeNumberField("altezza",cartolina.getAltezza());
        jsonGenerator.writeNumberField("larghezza",cartolina.getLarghezza());
        jsonGenerator.writeEndObject();
    }
}
