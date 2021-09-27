package com.epam.jwd.service.validation;

import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {
    private static final String VALID_BOAT_INPUT = "50,10,20,1";
    private static final String VALID_PORT_INPUT = "50,10";
    private static InputValidator validator;

    @BeforeAll
    static void init() {
        validator = new InputValidator();
    }

    @Test
    void shouldReturnTrueWhenBoatStringCorrespondsTheFormat() {
        assertTrue(validator.validateBoatInput(VALID_BOAT_INPUT));
    }

    @Test
    void shouldReturnTrueWhenPortStringCorrespondsTheFormat() {
        assertTrue(validator.validatePortInput(VALID_PORT_INPUT));
    }

    @Test
    void shouldReturnFalseWhenBoatStringIsInappropriate() {
        List<String> wrongBoatInputs = Arrays.asList(
                "",
                "1",
                ",",
                ",,,",
                "12,12,12",
                "42,,,42",
                "1,1,1,50"
        );
        for (String input : wrongBoatInputs) {
            assertFalse(validator.validateBoatInput(input),
                    String.format("Input \"%s\" is not valid", input));
        }
    }

    @Test
    void shouldReturnFalseWhenPortStringIsInappropriate() {
        List<String> wrongPortInputs = Arrays.asList(
                "",
                "1",
                ",",
                ",1",
                "1,"
        );
        for (String input : wrongPortInputs) {
            assertFalse(validator.validateBoatInput(input),
                    String.format("Input \"%s\" is not valid", input));
        }
    }
}