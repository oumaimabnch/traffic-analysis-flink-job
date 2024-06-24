package com.sentics.functions;

import com.sentics.models.InputMessage;
import com.sentics.models.ProcessedMessage;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import java.util.List;

public class CreateRowsFunction implements FlatMapFunction<InputMessage, ProcessedMessage> {

    @Override
    public void flatMap(InputMessage inputMessage, Collector<ProcessedMessage> out) throws Exception {
        // Extract classTypes from inputMessage
        List<String> classTypes = inputMessage.getClassType();

        // Emit ProcessedMessage for each classType
        for (String classType : classTypes) {
            ProcessedMessage processedMessage = new ProcessedMessage(inputMessage, classType);
            out.collect(processedMessage);
        }

    }
}
