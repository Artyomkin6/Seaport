package com.epam.jwd.service.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

class PortValidatorTest {
    private static final int VALID_CAPACITY = 50;
    private static final int INVALID_CAPACITY = -1;
    private static final int VALID_CONTAINER_COUNT = 20;
    private static final int INVALID_CONTAINER_COUNT = -1;
    private static final int WRONG_CONTAINER_COUNT = VALID_CAPACITY + 1;
    private static PortValidator validator;

    @BeforeAll
    static void init() {
        validator = new PortValidator();
    }

    @Test
    void shouldReturnTrueWhenAllParametersAreInBounds() {
        assertTrue(validator.validatePort(VALID_CAPACITY,
                VALID_CONTAINER_COUNT));
    }

    @Test
    void shouldReturnFalseWhenCapacityIsInvalid() {
        assertFalse(validator.validatePort(INVALID_CAPACITY,
                VALID_CONTAINER_COUNT));
    }

    @Test
    void shouldReturnFalseWhenContainerCountIsInvalid() {
        assertFalse(validator.validatePort(VALID_CAPACITY,
                INVALID_CONTAINER_COUNT));
        assertFalse(validator.validatePort(VALID_CAPACITY,
                WRONG_CONTAINER_COUNT));
    }
}