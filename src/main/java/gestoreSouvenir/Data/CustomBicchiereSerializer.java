package gestoreSouvenir.Data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CustomBicchiereSerializer extends StdSerializer<Bicchiere> {
    public CustomBicchiereSerializer(){
        this(null);
    }

    public CustomBicchiereSerializer(Class<Bicchiere> t){
        super(t);
    }

    @Override
    public void serialize(
            Bicchiere bicchiere, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("_id", bicchiere.getId().toString());
        jsonGenerator.writeStringField("materiale", bicchiere.getMateriale());
        jsonGenerator.writeStringField("provenienza", bicchiere.getProvenienza());
        jsonGenerator.writeNumberField("data",bicchiere.getData().getTime());
        jsonGenerator.writeStringField("tipo",bicchiere.getTipo().toString());
        jsonGenerator.writeEndObject();
    }
}
