package com.epam.jwd.service.logic;

import com.epam.jwd.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Berth implements Runnable {
    private final Port PORT;
    private final LimitChecker CHECKER = LimitChecker.getInstance();
    private final ReentrantLock TRANSFER_LOCK;
    private final Condition APPROPRIATE_LIMITS;
    private final ReentrantLock BOAT_ACCESS_LOCK;
    private final Condition AVAILABLE_BOATS;
    private static final Logger logger = LogManager.getLogger(Berth.class);
    private static final String START_MESSAGE
            = "thread started working";
    private static final String END_MESSAGE
            = "thread ended working";
    private static final String HOLD_BOAT_MESSAGE
            = "boat was held";
    private static final String RELEASE_BOAT_MESSAGE
            = "boat was released";
    private static final String INTERRUPTED_ERROR_MESSAGE
            = "Error: thread was interrupted";
    private static final String EMPTY_QUEUE_MESSAGE
            = "the queue is empty";
    private volatile Boat currentBoat;
    private AtomicBoolean waitingForTransfer
            = new AtomicBoolean(false);

    public Berth(Port port) {
        this.PORT = port;
        TRANSFER_LOCK = PORT.getTransferLock();
        APPROPRIATE_LIMITS = PORT.getAppropriateLimits();
        BOAT_ACCESS_LOCK = PORT.getBoatAccessLock();
        AVAILABLE_BOATS = PORT.getAvailableBoats();
    }

    public AtomicBoolean isWaitingForTransfer() {
        return waitingForTransfer;
    }

    public Boat getCurrentBoat() {
        return currentBoat;
    }

    @Override
    public void run() {
        logger.info(START_MESSAGE);
        while (!PORT.getBoatQueue().isEmpty()) {
            try {
                BOAT_ACCESS_LOCK.lock();
                try {
                    holdBoat();
                    logger.info(HOLD_BOAT_MESSAGE);
                    AVAILABLE_BOATS.signalAll();
                } finally {
                    BOAT_ACCESS_LOCK.unlock();
                }

                processBoat();

                BOAT_ACCESS_LOCK.lock();
                try {
                    releaseBoat();
                    logger.info(RELEASE_BOAT_MESSAGE);
                } finally {
                    BOAT_ACCESS_LOCK.unlock();
                }
            } catch (InterruptedException exception) {
                logger.error(INTERRUPTED_ERROR_MESSAGE, exception);
                Thread.currentThread().interrupt();
            } catch (NoSuchElementException exception) {
                logger.debug(EMPTY_QUEUE_MESSAGE);
                return;
            }
        }
        logger.info(END_MESSAGE);
    }

    void holdBoat() {
        currentBoat = PORT.getBoatQueue().remove();
    }

    void releaseBoat() {
        currentBoat = null;
    }

    private void processBoat() throws InterruptedException {
        int transferCount = currentBoat.getTask().getContainerTransferCount();
        switch (currentBoat.getTask().getTaskType()) {
            case FROM_BOAT_TO_PORT:
                transferContainersThroughPort(currentBoat, PORT, transferCount);
                break;
            case FROM_PORT_TO_BOAT:
                transferContainersThroughPort(PORT, currentBoat, transferCount);
                break;
            case FROM_BOAT_TO_BOAT:
                transferContainersThroughBoats(transferCount);
                break;
        }
    }

    private void transferContainersThroughPort(Entity from,
                                               Entity to,
                                               int count)
            throws InterruptedException {
        TRANSFER_LOCK.lock();
        try {
            while (!CHECKER.checkTransferAbility(from, to, count)) {
                waitingForTransfer.set(true);
                APPROPRIATE_LIMITS.await();
                if (currentBoat == null) {
                    waitingForTransfer.set(false);
                    // in finally TRANSFER_LOCK.unlock();
                    return;
                }
            }

            from.getContainerCount().addAndGet(-count);
            to.getContainerCount().addAndGet(count);

            waitingForTransfer.set(false);
            APPROPRIATE_LIMITS.signalAll();
        } finally {
            TRANSFER_LOCK.unlock();
        }
    }

    private void transferContainersThroughBoats(int count)
            throws InterruptedException {
        BOAT_ACCESS_LOCK.lock();
        try {
            Boat boat;
            while (((boat = checkAvailableBoats()) == null)
                    || !CHECKER.checkOnPutIn(boat, count)) {
                waitingForTransfer.set(true);
                AVAILABLE_BOATS.await();
                if (currentBoat == null) {
                    waitingForTransfer.set(false);
                    // in finally BOAT_ACCESS_LOCK.unlock();
                    return;
                }
            }

            if (CHECKER.checkOnPutOut(currentBoat, count)) {
                currentBoat.getContainerCount()
                        .addAndGet(-count);
                boat.getContainerCount().addAndGet(count);
            }

            waitingForTransfer.set(false);
        } finally {
            BOAT_ACCESS_LOCK.unlock();
        }
    }

    private Boat checkAvailableBoats() {
        List<Berth> berthList = PORT.getBerths();
        for (Berth berth : berthList) {
            if (this == berth) {
                continue;
            }
            if (berth.currentBoat != null) {
                return berth.currentBoat;
            }
        }
        return null;
    }
}
