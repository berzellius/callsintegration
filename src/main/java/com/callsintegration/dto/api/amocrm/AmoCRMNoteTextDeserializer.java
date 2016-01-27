package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Created by berz on 06.11.2015.
 */
public class AmoCRMNoteTextDeserializer extends JsonDeserializer<AmoCRMNoteText> {
    @Override
    public AmoCRMNoteText deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return null;
    }
}
