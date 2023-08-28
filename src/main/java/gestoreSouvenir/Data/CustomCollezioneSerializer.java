package gestoreSouvenir.Data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CustomCollezioneSerializer extends StdSerializer<Collezione> {
    public CustomCollezioneSerializer(){
        this(null);
    }

    public CustomCollezioneSerializer(Class<Collezione> t){
        super(t);
    }

    @Override
    public void serialize(
            Collezione collezione, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("_id", collezione.getId().toString());
        jsonGenerator.writeFieldName("objects_id");
        jsonGenerator.writeStartArray();
        for(Souvenir souvenir:collezione.getListaSouvenir()){
            jsonGenerator.writeString(souvenir.getId().toString());
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
