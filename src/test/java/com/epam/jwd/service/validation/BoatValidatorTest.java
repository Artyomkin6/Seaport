package com.epam.jwd.service.validation;

import com.epam.jwd.model.TaskType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

class BoatValidatorTest {
    private static final int VALID_CAPACITY = 50;
    private static final int INVALID_CAPACITY = -1;
    private static final int VALID_CONTAINER_COUNT = 20;
    private static final int INVALID_CONTAINER_COUNT = -1;
    private static final int WRONG_CONTAINER_COUNT = VALID_CAPACITY + 1;
    private static final int VALID_TRANSFER_COUNT = 10;
    private static final int INVALID_TRANSFER_COUNT = -1;
    private static final int WRONG_TRANSFER_COUNT_FROM_BOAT
            = VALID_CONTAINER_COUNT + 1;
    private static final int WRONG_TRANSFER_COUNT_TO_BOAT
            = VALID_CAPACITY - VALID_CONTAINER_COUNT + 1;
    private static BoatValidator validator;

    @BeforeAll
    static void init() {
        validator = new BoatValidator();
    }

    @Test
    void shouldReturnTrueWhenAllParametersAreInBounds() {
        assertTrue(validator.validateBoat(VALID_CAPACITY,
                VALID_CONTAINER_COUNT,
                VALID_TRANSFER_COUNT,
                TaskType.FROM_PORT_TO_BOAT));
        assertTrue(validator.validateBoat(VALID_CAPACITY,
                VALID_CONTAINER_COUNT,
                VALID_TRANSFER_COUNT,
                TaskType.FROM_BOAT_TO_PORT));
        assertTrue(validator.validateBoat(VALID_CAPACITY,
                VALID_CONTAINER_COUNT,
                VALID_TRANSFER_COUNT,
                TaskType.FROM_BOAT_TO_BOAT));
    }

    @Test
    void shouldReturnFalseWhenCapacityIsInvalid() {
        assertFalse(validator.validateBoat(INVALID_CAPACITY,
                VALID_CONTAINER_COUNT,
                VALID_TRANSFER_COUNT,
                TaskType.FROM_PORT_TO_BOAT));
    }

    @Test
    void shouldReturnFalseWhenContainerCountIsInvalid() {
        assertFalse(validator.validateBoat(VALID_CAPACITY,
                INVALID_CONTAINER_COUNT,
                VALID_TRANSFER_COUNT,
                TaskType.FROM_PORT_TO_BOAT));
        assertFalse(validator.validateBoat(VALID_CAPACITY,
                WRONG_CONTAINER_COUNT,
                VALID_TRANSFER_COUNT,
                TaskType.FROM_PORT_TO_BOAT));
    }

    @Test
    void shouldReturnFalseWhenTransferCountIsInvalid() {
        assertFalse(validator.validateBoat(VALID_CAPACITY,
                VALID_CONTAINER_COUNT,
                INVALID_TRANSFER_COUNT,
                TaskType.FROM_PORT_TO_BOAT),
                "Transfer count is out of bounds");
        assertFalse(validator.validateBoat(VALID_CAPACITY,
                VALID_CONTAINER_COUNT,
                WRONG_TRANSFER_COUNT_FROM_BOAT,
                TaskType.FROM_BOAT_TO_PORT),
                "Incorrect transfer count from boat");
        assertFalse(validator.validateBoat(VALID_CAPACITY,
                VALID_CONTAINER_COUNT,
                WRONG_TRANSFER_COUNT_FROM_BOAT,
                TaskType.FROM_BOAT_TO_BOAT),
                "Incorrect transfer count from boat");
        assertFalse(validator.validateBoat(VALID_CAPACITY,
                VALID_CONTAINER_COUNT,
                WRONG_TRANSFER_COUNT_TO_BOAT,
                TaskType.FROM_PORT_TO_BOAT),
                "Incorrect transfer count to boat");
    }
}