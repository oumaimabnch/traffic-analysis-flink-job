package com.sentics;

import com.sentics.functions.CreateRowsFunction;
import com.sentics.functions.NearMissWindowFunction;
import com.sentics.functions.ParseJsonFunction;
import com.sentics.models.InputMessage;
import com.sentics.models.NearMissResult;
import com.sentics.models.ProcessedMessage;
import com.sentics.utils.QuestDBUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;



public class FlinkJob {
    public static void main(String[] args) throws Exception {

   final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // JSON string input
        String jsonString = "[\n" +
                "  {\n" +
                "    \"t\": \"2024-06-11 11:54:30.345\",\n" +
                "    \"pos\": [\n" +
                "      [\n" +
                "        -10.902,\n" +
                "        -24.5646\n" +
                "      ],\n" +
                "      [\n" +
                "        -15.0156,\n" +
                "        -16.7329\n" +
                "      ]\n" +
                "    ],\n" +
                "    \"heading\": [\n" +
                "      61.2467,\n" +
                "      7.4823\n" +
                "    ],\n" +
                "    \"speed\": [\n" +
                "      0.5425,\n" +
                "      0.5425\n" +
                "    ],\n" +
                "    \"classType\": [\n" +
                "      \"vehicle\",\n" +
                "      \"human\"\n" +
                "    ],\n" +
                "    \"id\": [\n" +
                "      47,\n" +
                "      176114\n" +
                "    ],\n" +
                "    \"area\": [\n" +
                "      \"1\",\n" +
                "      \"1\"\n" +
                "    ],\n" +
                "    \"attributes\": [\n" +
                "      {\n" +
                "        \"vest\": \"None\",\n" +
                "        \"with_object\": \"False\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"vest\": \"None\",\n" +
                "        \"with_object\": \"False\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"overall_area\": {\n" +
                "      \"1\": 0,\n" +
                "      \"2\": 0,\n" +
                "      \"3\": 0,\n" +
                "      \"4\": 0,\n" +
                "      \"5\": 0,\n" +
                "      \"6\": 0,\n" +
                "      \"7\": 0,\n" +
                "      \"8\": 0\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"t\": \"2024-06-11 11:54:31.345\",\n" +
                "    \"pos\": [\n" +
                "      [\n" +
                "        -12.4277,\n" +
                "        15.9601\n" +
                "      ],\n" +
                "      [\n" +
                "        11.1977,\n" +
                "        21.5135\n" +
                "      ]\n" +
                "    ],\n" +
                "    \"heading\": [\n" +
                "      55.4665,\n" +
                "      -33.7625\n" +
                "    ],\n" +
                "    \"speed\": [\n" +
                "      1.3712,\n" +
                "      1.3712\n" +
                "    ],\n" +
                "    \"classType\": [\n" +
                "      \"vehicle\",\n" +
                "      \"human\"\n" +
                "    ],\n" +
                "    \"id\": [\n" +
                "      13,\n" +
                "      417634\n" +
                "    ],\n" +
                "    \"area\": [\n" +
                "      \"1\",\n" +
                "      \"1\"\n" +
                "    ],\n" +
                "    \"attributes\": [\n" +
                "      {\n" +
                "        \"vest\": \"None\",\n" +
                "        \"with_object\": \"False\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"vest\": \"None\",\n" +
                "        \"with_object\": \"False\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"overall_area\": {\n" +
                "      \"1\": 0,\n" +
                "      \"2\": 0,\n" +
                "      \"3\": 0,\n" +
                "      \"4\": 0,\n" +
                "      \"5\": 0,\n" +
                "      \"6\": 0,\n" +
                "      \"7\": 0,\n" +
                "      \"8\": 0\n" +
                "    }\n" +
                "  }\n" +
                "]";
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

        System.out.println("nearMisses Result "+ nearMisses.getType().toString());


        // Write near misses to QuestDB
        nearMisses.addSink(new RichSinkFunction<NearMissResult>() {
            private transient QuestDBUtils questDBUtils;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                questDBUtils = new QuestDBUtils();
            }

            @Override
            public void invoke(NearMissResult value, Context context) throws Exception {
                questDBUtils.insertNearMissResult(value);
            }

            @Override
            public void close() throws Exception {
                super.close();
                if (questDBUtils != null) {
                    questDBUtils.close();
                }
            }
        });


        env.execute("Flink Job to Process JSON Data");


    }
    private static String getFilePath() {
        return FlinkJob.class.getClassLoader().getResource("near_misses_data_correct_classes.json").getPath();
    }
}


