package com.sentics.models;

public class ProcessedMessage {
    private final InputMessage inputMessage;
    private final String classType;

    public ProcessedMessage(InputMessage inputMessage, String classType) {
        this.inputMessage = inputMessage;
        this.classType = classType;
    }

    public InputMessage getInputMessage() {
        return inputMessage;
    }

    public String getClassType() {
        return classType;
    }
}
