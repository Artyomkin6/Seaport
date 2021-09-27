package com.epam.jwd.service.logic;

import com.epam.jwd.model.Entity;

public class LimitChecker {
    private static final LimitChecker INSTANCE
            = new LimitChecker();

    private LimitChecker() {
    }

    public static LimitChecker getInstance() {
        return INSTANCE;
    }

    public boolean checkOnPutIn(Entity entity, int delta) {
        return (entity.getContainerCount().get() + delta
                <= entity.getContainerCapacity());
    }

    public boolean checkOnPutOut(Entity entity, int delta) {
        return (entity.getContainerCount().get() - delta >= 0);
    }

    public boolean checkTransferAbility(Entity from, Entity to, int delta) {
        return (checkOnPutOut(from, delta)
                && checkOnPutIn(to, delta));
    }
}
