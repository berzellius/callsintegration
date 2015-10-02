package com.callsintegration.jackson.deserializer;

import com.callsintegration.dmodel.Call;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by berz on 21.09.2015.
 */
public class CallDataDeserializer extends JsonDeserializer<List<Call>> {


    @Override
    public List<Call> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        List<Call> calls = new LinkedList<>();

        ObjectCodec oc = jsonParser.getCodec();
        TreeNode tn = oc.readTree(jsonParser);
        Iterator<String> is = tn.fieldNames();

        while (is.hasNext()) {

            String num = is.next();
            TreeNode ntn = tn.get(num);

            Iterator<String> dts = ntn.fieldNames();
            while (dts.hasNext()) {

                String toDate = dts.next();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date date = null;
                try {
                    date = sdf.parse(toDate);
                } catch (ParseException e) {
                    // такова жизнь, нам дали не такую дату
                }

                TreeNode dtn = ntn.get(toDate);
                Iterator<String> srcs = dtn.fieldNames();

                while (srcs.hasNext()) {

                    String source = srcs.next();
                    TreeNode ctn = dtn.get(source);
                    Iterator<String> callStatuses = ctn.fieldNames();

                    while (callStatuses.hasNext()) {
                        String status = callStatuses.next();
                        TreeNode v = ctn.get(status);

                        Iterator<String> callFields = v.fieldNames();

                        HashMap<String, String> params = new LinkedHashMap<>();
                        while (callFields.hasNext()) {
                            String callField = callFields.next();
                            TreeNode callFieldValue = v.get(callField);
                            params.put(callField, callFieldValue.toString());
                        }

                        System.out.println("params from call: " + params);
                        calls.add(new Call(num, date, source, Call.Status.valueByString(status), params));
                    }
                }
            }
        }
        return calls;
    }
}
