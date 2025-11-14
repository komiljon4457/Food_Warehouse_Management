package com.warehouse.model;

import com.warehouse.enums.RobotStatus;
import com.warehouse.exception.RobotException;
import com.warehouse.exception.TaskException;
import com.warehouse.util.Logger;

public class Robot implements Runnable {
    private String robotId;
    private RobotStatus status;
    private Task currentTask;
    private final Logger logger;

    public Robot(String robotId) {
        this.robotId = robotId;
        this.status = RobotStatus.IDLE;
        this.logger = Logger.getInstance();
    }

    public synchronized void performTask(Task task) throws RobotException {
        if (status != RobotStatus.IDLE) {
            throw new RobotException("Robot " + robotId + " is not available");
        }

        this.currentTask = task;
        this.status = RobotStatus.BUSY;
        logger.log("Robot " + robotId + " started task: " + task.getTaskId());

        try {
            task.execute();
            logger.log("Robot " + robotId + " completed task: " + task.getTaskId());
        } catch (TaskException e) {
            this.status = RobotStatus.ERROR;
            logger.log("Robot " + robotId + " failed task: " + task.getTaskId() + " - " + e.getMessage());
            throw new RobotException("Task execution failed", e);
        } finally {
            this.currentTask = null;
            if (status != RobotStatus.ERROR) {
                this.status = RobotStatus.IDLE;
            }
        }
    }

    @Override
    public void run() {
        if (currentTask != null) {
            try {
                performTask(currentTask);
            } catch (RobotException e) {
                logger.log("Error in robot thread: " + e.getMessage());
            }
        }
    }

    public void moveToSection(Section section) {
        logger.log("Robot " + robotId + " moving to section: " + section.getType());
        try {
            Thread.sleep(300); // Simulate movement
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Getters and setters
    public String getRobotId() {
        return robotId;
    }

    public RobotStatus getStatus() {
        return status;
    }

    public void setStatus(RobotStatus status) {
        this.status = status;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    @Override
    public String toString() {
        return "Robot{" +
                "robotId='" + robotId + '\'' +
                ", status=" + status +
                ", currentTask=" + (currentTask != null ? currentTask.getTaskId() : "none") +
                '}';
    }
}