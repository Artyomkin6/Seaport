package com.epam.jwd.view;

public class UI {
    private static final UI INSTANCE = new UI();
    private static final String START_MESSAGE
            = "Hello! This is seaport.\n" +
            "And you have had the privilege of making the queue of the boats," +
            " that will pass through this port :)\n" +
            "To enter a new boat, you should enter the boat capacity, " +
            "initial container count, that will be in the boat before " +
            "the process;\n" +
            "then you should enter the task: what port should do with the " +
            "boat containers (their count and to/from what place " +
            "transfer them)";
    private static final String BOAT_MESSAGE
            = "boat format:\n" +
            "containerCapacity,initContainerCount,transferCount,taskType\n" +
            "where\n" +
            "containerCapacity - (int) boat maximum container count, " +
            "that the boat can hold; should be positive (and non zero)\n" +
            "initContainerCount - (int) start container count; should be non " +
            "negative and below containerCapacity\n" +
            "transferCount - (int) container count, that should be " +
            "transferred to or from the target, target is defined by the " +
            "taskType;\n" +
            "   should be non negative and below initContainerCount, " +
            "if we transfer containers from the boat\n" +
            "taskType - (int) is the task type, " +
            "that is defined by a number:\n" +
            "   1. from the current boat to the port\n" +
            "   2. from the port to the current boat\n" +
            "   3. from the current boat to another boat\n" +
            "if you want to finish entering the boats, write \"stop\"\n" +
            "Enter the boats, please:";
    private static final String PORT_MESSAGE
            = "Now you should enter the port parameters with the " +
            "similar format\n" +
            "port format:\n" +
            "containerCapacity,initContainerCount\n" +
            "where\n" +
            "containerCapacity - (int) port maximum container count, " +
            "that the port can hold; should be positive (and non zero)\n" +
            "initContainerCount - (int) start container count; should be non " +
            "negative and below containerCapacity";
    private static final String INVALID_INPUT_MESSAGE
            = "wrong input format\n" +
            "try one more time:";
    private static final String INVALID_DATA_MESSAGE
            = "wrong %s parameters\n" +
            "try one more time:";
    private static final String BOATS_AFTER_PROCESS_MESSAGE
            = "Boats after process:";
    private static final String PORT_AFTER_PROCESS_MESSAGE
            = "Port after process:";
    private static final String BOAT = "boat";
    private static final String PORT = "port";
    private static final String COUNTER_TEXT = "%d. ";

    private UI() {
    }

    public static UI getInstance() {
        return INSTANCE;
    }

    public String getStartMessage() {
        return START_MESSAGE;
    }

    public String getBoatMessage() {
        return BOAT_MESSAGE;
    }

    public String getPortMessage() {
        return PORT_MESSAGE;
    }

    public String getInvalidInputMessage() {
        return INVALID_INPUT_MESSAGE;
    }

    public String getInvalidBoatDataMessage() {
        return String.format(INVALID_DATA_MESSAGE, BOAT);
    }

    public String getInvalidPortDataMessage() {
        return String.format(INVALID_DATA_MESSAGE, PORT);
    }

    public String getBoatsAfterProcessMessage() {
        return BOATS_AFTER_PROCESS_MESSAGE;
    }

    public String getPortAfterProcessMessage() {
        return PORT_AFTER_PROCESS_MESSAGE;
    }

    public String getCounterText(int number) {
        return String.format(COUNTER_TEXT, number);
    }
}
