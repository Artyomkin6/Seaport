package com.epam.jwd.controller;

import com.epam.jwd.model.Boat;
import com.epam.jwd.model.Task;
import com.epam.jwd.model.TaskType;
import com.epam.jwd.service.UIHandler;
import com.epam.jwd.service.logic.PortHandler;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Controller {
    private static final Logger logger
            = LogManager.getLogger(Controller.class);
    private static final UIHandler HANDLER = new UIHandler();
    // count of the addition threads
    private static final int BERTH_COUNT = 4;
    private static final String START_MESSAGE
            = "the application started working";
    private static final String END_MESSAGE
            = "the application ended working";
    private static final String INTERRUPTED_ERROR_MESSAGE
            = "Error: thread was interrupted";
    private PortHandler portHandler;

    public static void main(String[] args) {
//        Controller controller = new Controller();
//        try {
//            controller.run();
//        } catch (InterruptedException exception) {
//            logger.error(INTERRUPTED_ERROR_MESSAGE, exception);
//            Thread.currentThread().interrupt();
//        }

        List<Boat> boats = new ArrayList<>();
        boats.add(new Boat(10,5, new Task(3, TaskType.FROM_PORT_TO_BOAT)));
        boats.add(new Boat(10,5, new Task(3, TaskType.FROM_BOAT_TO_PORT)));
        boats.add(new Boat(10,5, new Task(3, TaskType.FROM_BOAT_TO_BOAT)));
        boats.add(new Boat(10,5, new Task(3, TaskType.FROM_PORT_TO_BOAT)));
        boats.add(new Boat(10,5, new Task(3, TaskType.FROM_BOAT_TO_PORT)));
        boats.add(new Boat(10,5, new Task(3, TaskType.FROM_BOAT_TO_BOAT)));
        PortHandler ph = new PortHandler(100, 50, 4, boats);
        try {
            ph.processBoats();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public void run() throws InterruptedException {
        logger.info(START_MESSAGE);
        HANDLER.printStartMessage();
        List<Boat> boats = HANDLER.getBoats();
        portHandler = HANDLER.getPort(boats, BERTH_COUNT);
        portHandler.processBoats();
        HANDLER.printBoatsAfterProcessing(boats);
        HANDLER.printPortAfterProcessing(portHandler);
        logger.info(END_MESSAGE);
    }
}
