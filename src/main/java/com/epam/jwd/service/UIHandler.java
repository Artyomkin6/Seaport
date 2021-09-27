package com.epam.jwd.service;

import com.epam.jwd.model.Boat;
import com.epam.jwd.model.Task;
import com.epam.jwd.model.TaskType;
import com.epam.jwd.service.logic.PortHandler;
import com.epam.jwd.service.validation.BoatValidator;
import com.epam.jwd.service.validation.InputValidator;
import com.epam.jwd.service.validation.PortValidator;
import com.epam.jwd.view.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UIHandler {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String END_TRIGGER = "stop";
    private static final InputValidator INPUT_VALIDATOR
            = new InputValidator();
    private static final BoatValidator BOAT_VALIDATOR
            = new BoatValidator();
    private static final PortValidator PORT_VALIDATOR
            = new PortValidator();
    private static final int LIST_START = 1;
    private UI ui = UI.getInstance();

    public void printStartMessage() {
        System.out.println(ui.getStartMessage());
    }

    public List<Boat> getBoats() {
        List<Boat> result = new ArrayList<>();
        String input;
        int counter = LIST_START;

        System.out.println(ui.getBoatMessage());
        System.out.print(ui.getCounterText(counter));
        while (!(input = readUserInput()).equals(END_TRIGGER)) {
            if (!INPUT_VALIDATOR.validateBoatInput(input)) {
                System.out.println(ui.getInvalidInputMessage());
                System.out.print(ui.getCounterText(counter));
                continue;
            }
            Boat boat;
            if ((boat = getBoat(input)) != null) {
                result.add(boat);
                counter++;
            } else {
                System.out.println(ui.getInvalidBoatDataMessage());
            }
            System.out.print(ui.getCounterText(counter));
        }
        return result;
    }

    private Boat getBoat(String input) {
        String[] parameters = input.split(",");
        int capacity = Integer.parseInt(parameters[0]);
        int containerCount = Integer.parseInt(parameters[1]);
        int transferCount = Integer.parseInt(parameters[2]);
        int taskNumber = Integer.parseInt(parameters[3]);
        TaskType taskType = null;
        switch (taskNumber) {
            case 1:
                taskType = TaskType.FROM_BOAT_TO_PORT;
                break;
            case 2:
                taskType = TaskType.FROM_PORT_TO_BOAT;
                break;
            case 3:
                taskType = TaskType.FROM_BOAT_TO_BOAT;
                break;
        }
        if (!BOAT_VALIDATOR.validateBoat(capacity, containerCount,
                transferCount, taskType)) {
            return null;
        }
        return new Boat(capacity, containerCount,
                new Task(transferCount, taskType));
    }

    public PortHandler getPort(List<Boat> boats, int berthCount) {
        PortHandler portHandler = null;
        String input;
        boolean valid = false;

        System.out.println(ui.getPortMessage());
        while (!valid) {
            input = readUserInput();
            if (!INPUT_VALIDATOR.validatePortInput(input)) {
                System.out.println(ui.getInvalidInputMessage());
                continue;
            } else {
                valid = true;
            }
            String[] parameters = input.split(",");
            int capacity = Integer.parseInt(parameters[0]);
            int containerCount = Integer.parseInt(parameters[1]);
            valid = PORT_VALIDATOR.validatePort(capacity, containerCount);
            if (!valid) {
                System.out.println(ui.getInvalidPortDataMessage());
            } else {
                portHandler = new PortHandler(capacity, containerCount,
                        berthCount, boats);
            }
        }

        return portHandler;
    }

    public void printBoatsAfterProcessing(List<Boat> boats) {
        int counter = LIST_START;

        System.out.println(ui.getBoatsAfterProcessMessage());
        for (Boat boat : boats) {
            System.out.println(ui.getCounterText(counter++) + boat.toString());
        }
    }

    public void printPortAfterProcessing(PortHandler handler) {
        System.out.println(ui.getPortAfterProcessMessage());
        System.out.println(handler.getPort().toString());
    }

    private String readUserInput() {
        return SCANNER.nextLine();
    }
}
