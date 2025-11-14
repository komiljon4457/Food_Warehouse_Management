package com.warehouse.model;

import com.warehouse.exception.RobotException;
import com.warehouse.exception.TaskException;
import com.warehouse.util.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class TaskManager {
    private Queue<Task> pendingTasks;
    private final ReentrantLock lock;
    private final Logger logger;

    public TaskManager() {
        this.pendingTasks = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.logger = Logger.getInstance();
    }

    public void addTask(Task task) throws TaskException {
        if (task == null) {
            throw new TaskException("Task cannot be null");
        }

        lock.lock();
        try {
            pendingTasks.offer(task);
            logger.log("Task added: " + task.getTaskId() + " - " + task.getTaskType());
        } finally {
            lock.unlock();
        }
    }

    public Task getNextTask() {
        lock.lock();
        try {
            return pendingTasks.poll();
        } finally {
            lock.unlock();
        }
    }

    public boolean assignTaskToRobot(Robot robot) throws RobotException {
        Task task = getNextTask();
        if (task == null) {
            return false;
        }

        try {
            robot.performTask(task);
            return true;
        } catch (RobotException e) {
            logger.log("Failed to assign task to robot: " + e.getMessage());
            // Re-add task to queue if assignment failed
            lock.lock();
            try {
                pendingTasks.offer(task);
            } finally {
                lock.unlock();
            }
            throw e;
        }
    }

    public int getPendingTaskCount() {
        lock.lock();
        try {
            return pendingTasks.size();
        } finally {
            lock.unlock();
        }
    }

    public List<Task> getPendingTasks() {
        lock.lock();
        try {
            return new LinkedList<>(pendingTasks);
        } finally {
            lock.unlock();
        }
    }
}
