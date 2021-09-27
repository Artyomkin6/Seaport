package com.epam.jwd.service.logic;

import com.epam.jwd.model.Boat;
import com.epam.jwd.model.Task;
import com.epam.jwd.model.TaskType;
import com.epam.jwd.service.validation.BoatValidator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PortHandlerTest {
    private static final int PORT_CAPACITY = 100;
    private static final int PORT_START_CONTAINER_COUNT = 50;
    private static final int PORT_BERTH_COUNT = 4;
    private static final int BOAT_DEFAULT_CAPACITY = 10;
    private static final int BOAT_DEFAULT_CONTAINER_COUNT = 5;
    private static final int BOAT_DEFAULT_TRANSFER_COUNT = 3;
    private static final int ZERO = 0;
    private static List<Boat> boats;
    private static PortHandler handler;

    @Test
    void shouldKeepContainerSumWhenAllWorks() {
        int allContainerCountBeforeProcess = 0;
        int allContainerCountAfterProcess = 0;

        boats = new ArrayList<>();
        for (int i = 0; i < 2; ++i) {
            boats.add(new Boat(BOAT_DEFAULT_CAPACITY,
                    BOAT_DEFAULT_CONTAINER_COUNT,
                    new Task(BOAT_DEFAULT_TRANSFER_COUNT,
                            TaskType.FROM_PORT_TO_BOAT)));
            boats.add(new Boat(BOAT_DEFAULT_CAPACITY,
                    BOAT_DEFAULT_CONTAINER_COUNT,
                    new Task(BOAT_DEFAULT_TRANSFER_COUNT,
                            TaskType.FROM_BOAT_TO_PORT)));
            boats.add(new Boat(BOAT_DEFAULT_CAPACITY,
                    BOAT_DEFAULT_CONTAINER_COUNT,
                    new Task(BOAT_DEFAULT_TRANSFER_COUNT,
                            TaskType.FROM_BOAT_TO_BOAT)));
        }
        handler = new PortHandler(PORT_CAPACITY,
                PORT_START_CONTAINER_COUNT,
                PORT_BERTH_COUNT,
                boats);
        allContainerCountBeforeProcess += BOAT_DEFAULT_CONTAINER_COUNT * 3 * 2;
        allContainerCountBeforeProcess += PORT_START_CONTAINER_COUNT;

        assertDoesNotThrow(() -> handler.processBoats());

        for (Boat boat : boats) {
            allContainerCountAfterProcess += boat.getContainerCount().get();
        }
        allContainerCountAfterProcess
                += handler.getPort().getContainerCount().get();

        assertEquals(allContainerCountBeforeProcess,
                allContainerCountAfterProcess,
                "Container count before and after process isn't match");
    }

    @Test
    void shouldKeepContainersOnBoatsWhenPortIsOverfilled() {
        boats = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            boats.add(new Boat(BOAT_DEFAULT_CAPACITY,
                    BOAT_DEFAULT_CONTAINER_COUNT,
                    new Task(BOAT_DEFAULT_TRANSFER_COUNT,
                            TaskType.FROM_BOAT_TO_PORT)));
        }
        // port containerCount = port capacity
        handler = new PortHandler(PORT_CAPACITY,
                PORT_CAPACITY,
                PORT_BERTH_COUNT,
                boats);

        assertDoesNotThrow(() -> handler.processBoats());

        assertEquals(PORT_CAPACITY,
                handler.getPort().getContainerCount().get(),
                "Port container count must not change");
        for (Boat boat : boats) {
            assertEquals(BOAT_DEFAULT_CONTAINER_COUNT,
                    boat.getContainerCount().get(),
                    "Boat container count must not change");
        }
    }

    @Test
    void shouldKeepContainersInPortWhenBoatsAreOverfilled() {
        boats = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            // boat containerCount = boat capacity
            boats.add(new Boat(BOAT_DEFAULT_CAPACITY,
                    BOAT_DEFAULT_CAPACITY,
                    new Task(BOAT_DEFAULT_TRANSFER_COUNT,
                            TaskType.FROM_PORT_TO_BOAT)));
        }
        handler = new PortHandler(PORT_CAPACITY,
                PORT_START_CONTAINER_COUNT,
                PORT_BERTH_COUNT,
                boats);

        assertDoesNotThrow(() -> handler.processBoats());

        assertEquals(PORT_START_CONTAINER_COUNT,
                handler.getPort().getContainerCount().get(),
                "Port container count must not change");
        for (Boat boat : boats) {
            assertEquals(BOAT_DEFAULT_CAPACITY,
                    boat.getContainerCount().get(),
                    "Boat container count must not change");
        }
    }

    @Test
    void shouldNotTransferContainersOnBoatsWhenPortIsEmpty() {
        boats = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            boats.add(new Boat(BOAT_DEFAULT_CAPACITY,
                    BOAT_DEFAULT_CONTAINER_COUNT,
                    new Task(BOAT_DEFAULT_TRANSFER_COUNT,
                            TaskType.FROM_PORT_TO_BOAT)));
        }
        // port container count is 0
        handler = new PortHandler(PORT_CAPACITY,
                ZERO,
                PORT_BERTH_COUNT,
                boats);

        assertDoesNotThrow(() -> handler.processBoats());

        assertEquals(ZERO,
                handler.getPort().getContainerCount().get(),
                "Port container count must not change");
        for (Boat boat : boats) {
            assertEquals(BOAT_DEFAULT_CONTAINER_COUNT,
                    boat.getContainerCount().get(),
                    "Boat container count must not change");
        }
    }

    @Test
    void shouldNotTransferContainersToPortWhenBoatsAreEmpty() {
        boats = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            // boat container count is 0
            boats.add(new Boat(BOAT_DEFAULT_CAPACITY,
                    ZERO,
                    new Task(BOAT_DEFAULT_TRANSFER_COUNT,
                            TaskType.FROM_BOAT_TO_PORT)));
        }
        handler = new PortHandler(PORT_CAPACITY,
                PORT_START_CONTAINER_COUNT,
                PORT_BERTH_COUNT,
                boats);

        assertDoesNotThrow(() -> handler.processBoats());

        assertEquals(PORT_START_CONTAINER_COUNT,
                handler.getPort().getContainerCount().get(),
                "Port container count must not change");
        for (Boat boat : boats) {
            assertEquals(ZERO,
                    boat.getContainerCount().get(),
                    "Boat container count must not change");
        }
    }
}