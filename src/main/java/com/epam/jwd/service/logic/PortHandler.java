package com.epam.jwd.service.logic;

import com.epam.jwd.model.Boat;
import com.epam.jwd.model.Port;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PortHandler {
    private final Port PORT;
    private final List<Thread> berthThreads;
    private final Random random = new Random();

    public PortHandler(int containerCapacity, int containerCount,
                       int berthCount, Collection<Boat> boats) {
        PORT = new Port(containerCapacity, containerCount, berthCount, boats);
        berthThreads = new ArrayList<>();
    }

    public Port getPort() {
        return PORT;
    }

    public void processBoats() throws InterruptedException {
        for (Runnable berth : PORT.getBerths()) {
            berthThreads.add(new Thread(berth));
        }
        for (Thread berthTread : berthThreads) {
            berthTread.start();
        }
        checkBoatUselessWaiting();
        // at that point the boat queue must be empty
        checkBoatUselessWaitingAfterWork();
        for (Thread berthTread : berthThreads) {
            berthTread.join();
        }
    }

    private int getRandomBerthNumber() {
        return random.nextInt(PORT.getBerthCount());
    }

    private void checkBoatUselessWaiting() throws InterruptedException {
        ConcurrentLinkedQueue<Boat> boats = PORT.getBoatQueue();
        ReentrantLock lock = PORT.getBoatAccessLock();

        while (!boats.isEmpty()) {
            lock.lock();
            try {
                boolean allWaiting = true;
                for (Berth berth : PORT.getBerths()) {
                    allWaiting &= berth.isWaitingForTransfer().get();
                }
                if (allWaiting) {
                    PORT.getBerths()
                            .get(getRandomBerthNumber())
                            .releaseBoat();
                }
            } finally {
                lock.unlock();
            }
            Thread.sleep(100);
        }
    }

    private void releaseAll() {
        for (Berth berth : PORT.getBerths()) {
            berth.releaseBoat();
        }
    }

    private void checkBoatUselessWaitingAfterWork() {
        ReentrantLock lock = PORT.getBoatAccessLock();
        Condition availableBoats = PORT.getAvailableBoats();

        lock.lock();
        try {
            boolean allRemainingWaiting = true;
            for (Berth berth : PORT.getBerths()) {
                allRemainingWaiting &= ((berth.getCurrentBoat() == null)
                        || berth.isWaitingForTransfer().get());
            }
            if (allRemainingWaiting) {
                releaseAll();
                ReentrantLock transferLock = PORT.getTransferLock();
                transferLock.lock();
                try {
                    PORT.getAppropriateLimits().signalAll();
                } finally {
                    transferLock.unlock();
                }
            }
            availableBoats.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
