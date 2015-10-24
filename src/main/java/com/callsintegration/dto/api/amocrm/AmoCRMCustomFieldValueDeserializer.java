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

public class AmoCRMCustomFieldValueDeserializer extends JsonDeserializer<AmoCRMCustomFieldValue> {

    @Override
    public AmoCRMCustomFieldValue deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        TreeNode tn = oc.readTree(jsonParser);
        Iterator<String> is = tn.fieldNames();

        AmoCRMCustomFieldValue amoCRMCustomFieldValue = new AmoCRMCustomFieldValue();

        while (is.hasNext()) {
            String field = is.next();
            TreeNode fieldNode = tn.get(field);

            if (field.equals("value")) {
                amoCRMCustomFieldValue.setValue(fieldNode.toString().replace("\"", ""));
            }

            if (field.equals("enum")) {
                amoCRMCustomFieldValue.setEnumerated(fieldNode.toString().replace("\"", ""));
            }
        }

        return amoCRMCustomFieldValue;
    }

}
