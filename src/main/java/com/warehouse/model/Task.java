package com.warehouse.model;

import com.warehouse.enums.PetType;
import com.warehouse.enums.TaskStatus;
import com.warehouse.enums.TaskType;
import com.warehouse.exception.TaskException;

import java.util.UUID;

public class Task {
    private String taskId;
    private TaskType taskType;
    private PetType petType;
    private int quantity;
    private TaskStatus status;

    public Task(TaskType taskType, PetType petType, int quantity) {
        this.taskId = UUID.randomUUID().toString().substring(0, 8);
        this.taskType = taskType;
        this.petType = petType;
        this.quantity = quantity;
        this.status = TaskStatus.PENDING;
    }

    public void execute() throws TaskException {
        if (status != TaskStatus.PENDING && status != TaskStatus.IN_PROGRESS) {
            throw new TaskException("Task cannot be executed in status: " + status);
        }

        this.status = TaskStatus.IN_PROGRESS;

        try {
            // Simulate task execution
            Thread.sleep((long) (Math.random() * 1000 + 500));
            this.status = TaskStatus.COMPLETED;
        } catch (InterruptedException e) {
            this.status = TaskStatus.FAILED;
            throw new TaskException("Task execution interrupted", e);
        }
    }

    // Getters and setters
    public String getTaskId() {
        return taskId;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public PetType getPetType() {
        return petType;
    }

    public int getQuantity() {
        return quantity;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", taskType=" + taskType +
                ", petType=" + petType +
                ", quantity=" + quantity +
                ", status=" + status +
                '}';
    }
}