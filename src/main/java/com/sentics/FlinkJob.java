package com.sentics;

import com.sentics.functions.CreateRowsFunction;
import com.sentics.functions.NearMissWindowFunction;
import com.sentics.functions.ParseJsonFunction;
import com.sentics.models.InputMessage;
import com.sentics.models.NearMissResult;
import com.sentics.models.ProcessedMessage;
import com.sentics.utils.QuestDBUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.nio.file.Files;
import java.nio.file.Paths;


public class FlinkJob {
    public static void main(String[] args) throws Exception {

   final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


        // Read the JSON file content
        String jsonFilePath = "near_misses_data_correct_classes.json";
        String jsonString = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        // Create a DataStream from the JSON string
        DataStream<String> inputStream = env.fromElements(jsonString);

        // Parse JSON strings to InputMessage objects
        DataStream<InputMessage> parsedStream = inputStream.flatMap(new ParseJsonFunction());

        // Create rows based on the class field
        DataStream<ProcessedMessage> processedStream = parsedStream.flatMap(new CreateRowsFunction());
        // Implement window operations to calculate near misses
        DataStream<NearMissResult> nearMisses = processedStream
                .keyBy(ProcessedMessage::getClassType) // Key by class type (vehicle or human)
                .window(TumblingEventTimeWindows.of(Time.seconds(10))) // Example of 10-second tumbling window
                .process(new NearMissWindowFunction());

        // Write near misses to QuestDB
        nearMisses.addSink(new RichSinkFunction<NearMissResult>() {
            private transient QuestDBUtils questDBUtils;

//            @Override
//            public void open(Configuration parameters) throws Exception {
//                super.open(parameters);
//                questDBUtils = new QuestDBUtils();
//            }
//
//            @Override
//            public void invoke(NearMissResult value, Context context) throws Exception {
//                questDBUtils.insertNearMissResult(value);
//            }
//
//            @Override
//            public void close() throws Exception {
//                super.close();
//                if (questDBUtils != null) {
//                    questDBUtils.close();
//                }
//            }
        });


        env.execute("Flink Job to Process JSON Data");


    }
}


