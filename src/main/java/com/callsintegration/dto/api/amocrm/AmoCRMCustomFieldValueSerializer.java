package com.callsintegration.dto.api.amocrm;


import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by berz on 24.10.2015.
 */

public class AmoCRMCustomFieldValueSerializer extends JsonSerializer<AmoCRMCustomFieldValue> {

    @Override
    public void serialize(AmoCRMCustomFieldValue amoCRMCustomFieldValue, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, org.codehaus.jackson.JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("value", amoCRMCustomFieldValue.getValue());
        jsonGenerator.writeStringField("enum", amoCRMCustomFieldValue.getEnumerated());
        jsonGenerator.writeEndObject();
    }

}
