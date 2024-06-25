package com.sentics.functions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentics.models.InputMessage;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import java.util.List;

public class ParseJsonFunction implements FlatMapFunction<String, InputMessage> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void flatMap(String value, Collector<InputMessage> out) throws Exception {

        try {
            List<InputMessage> inputMessages = objectMapper.readValue(value, new TypeReference<List<InputMessage>>() {});
            for (InputMessage inputMessage : inputMessages) {

                out.collect(inputMessage);
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            System.err.println("Problematic JSON: " + value);

        }

    }
}
