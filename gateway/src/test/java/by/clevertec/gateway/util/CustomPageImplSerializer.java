package by.clevertec.gateway.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.data.domain.PageImpl;

import java.io.IOException;

public class CustomPageImplSerializer extends StdSerializer<PageImpl> {

    public CustomPageImplSerializer() {
        this(null);
    }

    public CustomPageImplSerializer(Class<PageImpl> t) {
        super(t);
    }

    @Override
    public void serialize(PageImpl page, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeObjectField("content", page.getContent());
        jsonGenerator.writeNumberField("totalElements", page.getTotalElements());
        jsonGenerator.writeNumberField("totalPages", page.getTotalPages());
        jsonGenerator.writeNumberField("number", page.getNumber());
        jsonGenerator.writeNumberField("size", page.getSize());
        jsonGenerator.writeBooleanField("first", page.isFirst());
        jsonGenerator.writeBooleanField("last", page.isLast());

        jsonGenerator.writeEndObject();
    }
}