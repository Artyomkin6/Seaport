package com.epam.jwd.model;

import com.epam.jwd.service.logic.Berth;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Port extends Entity {
    private final int BERTH_COUNT;
    private final List<Berth> berths;
    private final ConcurrentLinkedQueue<Boat> boatQueue;
    private final ReentrantLock TRANSFER_LOCK = new ReentrantLock();
    private final Condition APPROPRIATE_LIMITS
            = TRANSFER_LOCK.newCondition();
    private final ReentrantLock BOAT_ACCESS_LOCK = new ReentrantLock();
    private final Condition AVAILABLE_BOATS = BOAT_ACCESS_LOCK.newCondition();

    public Port(int containerCapacity, int containerCount, int berthCount,
                Collection<Boat> boats) {
        super(containerCapacity, containerCount);
        this.BERTH_COUNT = berthCount;
        berths = new ArrayList<>();
        initBerths();
        this.boatQueue = new ConcurrentLinkedQueue<>(boats);
    }

    public int getBerthCount() {
        return BERTH_COUNT;
    }

    public List<Berth> getBerths() {
        return berths;
    }

    public ReentrantLock getTransferLock() {
        return TRANSFER_LOCK;
    }

    public Condition getAppropriateLimits() {
        return APPROPRIATE_LIMITS;
    }

    public ReentrantLock getBoatAccessLock() {
        return BOAT_ACCESS_LOCK;
    }

    public Condition getAvailableBoats() {
        return AVAILABLE_BOATS;
    }

    public ConcurrentLinkedQueue<Boat> getBoatQueue() {
        return boatQueue;
    }

    private void initBerths() {
        for (int i = 0; i < BERTH_COUNT; ++i) {
            berths.add(new Berth(this));
        }
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
        Port port = (Port) object;
        return (BERTH_COUNT == port.BERTH_COUNT)
                && Objects.equals(berths, port.berths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), BERTH_COUNT, berths);
    }

    @Override
    public String toString() {
        return "Port{"
                + super.toString()
                + ", berthCount=" + BERTH_COUNT
                + '}';
    }
}
