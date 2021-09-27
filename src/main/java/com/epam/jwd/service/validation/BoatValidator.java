package com.epam.jwd.service.validation;

import com.epam.jwd.model.TaskType;

public class BoatValidator {
    public boolean validateBoat(int capacity,
                                int containerCount,
                                int transferCount,
                                TaskType taskType) {
        if (capacity < 1) {
            return false;
        }
        if (containerCount < 0 || containerCount > capacity) {
            return false;
        }
        if (transferCount < 0) {
            return false;
        }
        switch (taskType) {
            case FROM_BOAT_TO_PORT:
            case FROM_BOAT_TO_BOAT:
                if (transferCount > containerCount) {
                    return false;
                }
                break;
            case FROM_PORT_TO_BOAT:
                if (transferCount + containerCount > capacity) {
                    return false;
                }
                break;
        }
        return true;
    }
}
