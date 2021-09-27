package com.epam.jwd.service.validation;

public class PortValidator {
    public boolean validatePort(int capacity, int containerCount) {
        return ((capacity >= 1)
                && (containerCount >= 0)
                && (containerCount <= capacity));
    }
}
