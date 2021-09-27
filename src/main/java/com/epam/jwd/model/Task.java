package com.epam.jwd.model;

import java.util.Objects;

public class Task {
    private final int containerTransferCount;
    private final TaskType taskType;

    public Task(int count, TaskType taskType) {
        this.containerTransferCount = count;
        this.taskType = taskType;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public int getContainerTransferCount() {
        return containerTransferCount;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (getClass() != object.getClass())) {
            return false;
        }
        Task task = (Task) object;
        return (containerTransferCount == task.containerTransferCount)
                && (taskType == task.taskType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskType, containerTransferCount);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskType=" + taskType +
                ", containerTransferCount=" + containerTransferCount +
                '}';
    }
}
