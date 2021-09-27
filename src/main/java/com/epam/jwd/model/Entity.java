package com.epam.jwd.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Entity {
    private final int CONTAINER_CAPACITY;
    protected AtomicInteger containerCount = new AtomicInteger(0);

    public Entity(int containerCapacity) {
        this.CONTAINER_CAPACITY = containerCapacity;
    }

    public Entity(int containerCapacity, int containerCount) {
        this.CONTAINER_CAPACITY = containerCapacity;
        this.containerCount.set(containerCount);
    }

    public int getContainerCapacity() {
        return CONTAINER_CAPACITY;
    }

    public AtomicInteger getContainerCount() {
        return containerCount;
    }

    public void setContainerCount(int containerCount) {
        this.containerCount.set(containerCount);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Entity entity = (Entity) object;
        return (CONTAINER_CAPACITY == entity.CONTAINER_CAPACITY)
                && (containerCount == entity.containerCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(CONTAINER_CAPACITY, containerCount);
    }

    @Override
    public String toString() {
        return "containerCapacity=" + CONTAINER_CAPACITY +
                ", containerCount=" + containerCount;
    }
}
