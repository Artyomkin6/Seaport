package com.epam.jwd.model;

import java.util.Objects;

public class Boat extends Entity {
    private final Task task;

    public Boat(int containerCapacity, int containerCount, Task task) {
        super(containerCapacity);
        super.setContainerCount(containerCount);
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        Boat boat = (Boat) object;
        return Objects.equals(task, boat.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), task);
    }

    @Override
    public String toString() {
        return "Boat{"
                + super.toString()
                + ", "
                + task.toString()
                + '}';
    }
}
