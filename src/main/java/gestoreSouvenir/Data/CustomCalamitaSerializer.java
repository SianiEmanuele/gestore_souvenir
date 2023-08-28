package gestoreSouvenir.Data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CustomCalamitaSerializer extends StdSerializer<Calamita> {

    public CustomCalamitaSerializer(){
        this(null);
    }

    public CustomCalamitaSerializer(Class<Calamita> t){
        super(t);
    }

    @Override
    public void serialize(
            Calamita calamita, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("_id", calamita.getId().toString());
        jsonGenerator.writeStringField("materiale", calamita.getMateriale());
        jsonGenerator.writeStringField("provenienza", calamita.getProvenienza());
        jsonGenerator.writeNumberField("data",calamita.getData().getTime());
        jsonGenerator.writeNumberField("altezza",calamita.getAltezza());
        jsonGenerator.writeNumberField("larghezza",calamita.getLarghezza());
        jsonGenerator.writeEndObject();
    }
}
