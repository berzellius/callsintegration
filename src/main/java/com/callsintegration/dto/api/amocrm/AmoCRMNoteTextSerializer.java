package com.callsintegration.dto.api.amocrm;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;

/**
 * Created by berz on 11.10.2015.
 * О необходимости отдельного сериализатора см. класс AmoCRMNoteText
 */
public class AmoCRMNoteTextSerializer extends JsonSerializer<AmoCRMNoteText> {
    @Override
    public void serialize(AmoCRMNoteText amoCRMNoteText, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        String str = "{\"PHONE\":\"" + amoCRMNoteText.getPhone() + "\"," +
                "\"DURATION\":" + amoCRMNoteText.getDuration() + "," +
                "\"SRC\":\"" + amoCRMNoteText.getSrc() + "\"," +
                "\"call_status\":\"" + amoCRMNoteText.getCall_status() + "\"," +
                "\"call_result\":\"" + amoCRMNoteText.getCall_result() + "\"}";
        jsonGenerator.writeString(str);
    }
}
