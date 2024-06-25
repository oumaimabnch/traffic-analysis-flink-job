package com.sentics.functions;

import com.sentics.entities.InputMessage;
import com.sentics.entities.NearMissResult;
import com.sentics.entities.ProcessedMessage;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

public class NearMissWindowFunction extends ProcessWindowFunction <ProcessedMessage, NearMissResult, String, TimeWindow> {

    @Override
    public void process(String classType, Context context, Iterable<ProcessedMessage> elements, Collector<NearMissResult> out) {
        List<ProcessedMessage> messages = new ArrayList<>();
        elements.forEach(messages::add);

        // Example logic to calculate near misses
        for (int i = 0; i < messages.size(); i++) {
            for (int j = i + 1; j < messages.size(); j++) {
                ProcessedMessage msg1 = messages.get(i);
                ProcessedMessage msg2 = messages.get(j);

                // Calculate distance between msg1 and msg2
                double distance = calculateDistance(msg1.getInputMessage(), msg2.getInputMessage());

                // Calculate speed difference
                double speedDifference = calculateSpeedDifference(msg1.getInputMessage(), msg2.getInputMessage());

                // Check near miss criteria
                if (isNearMiss(speedDifference, distance)) {
                    // Prepare NearMissResult
                    NearMissResult nearMissResult = new NearMissResult(
                            msg1.getInputMessage().getT(),
                            msg1.getInputMessage().getId(),
                            speedDifference,
                            distance,
                            classType,
                            true // Near miss occurred
                    );
                    out.collect(nearMissResult);
                }
            }
        }
    }

    // Calculate distance between two messages
    private double calculateDistance(InputMessage msg1, InputMessage msg2) {
        // Get the first position from each message
        List<Double> pos1 = msg1.getPos().get(0);
        List<Double> pos2 = msg2.getPos().get(0);

        // Calculate distance between the first positions
        double distance = Math.sqrt(Math.pow(pos1.get(0) - pos2.get(0), 2) + Math.pow(pos1.get(1) - pos2.get(1), 2));

        return distance;
    }

    // Calculate speed difference between two messages
    private double calculateSpeedDifference(InputMessage msg1, InputMessage msg2) {
        // Replace with actual speed difference calculation logic
        return Math.abs(msg1.getSpeed().get(0) - msg2.getSpeed().get(0));
    }

    // Check near miss criteria
    private boolean isNearMiss(double speedDifference, double distance) {

        return (0.3 < speedDifference && speedDifference < 0.83 && distance < 4) ||
                (0.833 <= speedDifference && speedDifference <= 2.77 && distance >= 4 && distance <= 8) ||
                (speedDifference > 2.77 && distance >= 8 && distance <= 12);
    }
}

