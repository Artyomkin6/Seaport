package com.epam.jwd.service.validation;

import java.util.regex.Pattern;

public class InputValidator {
    private static final String BOAT_PATTERN
            = "((\\d+),){3}([123])";
    private static final String PORT_PATTERN
            = "(\\d+),(\\d+)";

    public boolean validateBoatInput(String input) {
        return Pattern.matches(BOAT_PATTERN, input);
    }

    public boolean validatePortInput(String input) {
        return Pattern.matches(PORT_PATTERN, input);
    }
}
