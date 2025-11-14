package com.warehouse.model;

import com.warehouse.enums.PetType;
import com.warehouse.enums.RobotStatus;
import com.warehouse.exception.RobotException;
import com.warehouse.exception.TaskException;
import com.warehouse.exception.WarehouseException;
import com.warehouse.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PetFoodWarehouse {
    private List<Robot> robots;
    private List<Section> sections;
    private TaskManager taskManager;
    private final Logger logger;

    public PetFoodWarehouse() {
        this.robots = new ArrayList<>();
        this.sections = new ArrayList<>();
        this.taskManager = new TaskManager();
        this.logger = Logger.getInstance();
    }

    public void start() {
        logger.log("Warehouse started");
        initializeSections();
    }

    private void initializeSections() {
        for (PetType type : PetType.values()) {
            sections.add(new Section(type));
        }
        logger.log("Initialized " + sections.size() + " sections");
    }

    public void addRobot(Robot robot) {
        robots.add(robot);
        logger.log("Robot added: " + robot.getRobotId());
    }

    public void assignTask(Task task) throws TaskException {
        taskManager.addTask(task);
    }

    public void processTasksSequentially() throws RobotException {
        logger.log("=== Starting Sequential Processing ===");
        long startTime = System.currentTimeMillis();

        while (taskManager.getPendingTaskCount() > 0) {
            Robot availableRobot = getAvailableRobot();
            if (availableRobot != null) {
                try {
                    taskManager.assignTaskToRobot(availableRobot);
                } catch (RobotException e) {
                    logger.log("Error in sequential processing: " + e.getMessage());
                }
            } else {
                logger.log("No available robots, waiting...");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        long endTime = System.currentTimeMillis();
        logger.log("Sequential processing completed in " + (endTime - startTime) + "ms");
    }

    public void processTasksParallel() throws InterruptedException {
        logger.log("=== Starting Parallel Processing ===");
        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(robots.size());

        while (taskManager.getPendingTaskCount() > 0) {
            for (Robot robot : robots) {
                if (robot.getStatus() == RobotStatus.IDLE && taskManager.getPendingTaskCount() > 0) {
                    Task task = taskManager.getNextTask();
                    if (task != null) {
                        final Robot currentRobot = robot;
                        final Task currentTask = task;
                        executor.submit(() -> {
                            try {
                                currentRobot.performTask(currentTask);
                            } catch (RobotException e) {
                                logger.log("Parallel task error: " + e.getMessage());
                            }
                        });
                    }
                }
            }
            Thread.sleep(100);
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        long endTime = System.currentTimeMillis();
        logger.log("Parallel processing completed in " + (endTime - startTime) + "ms");
    }

    private Robot getAvailableRobot() {
        for (Robot robot : robots) {
            if (robot.getStatus() == RobotStatus.IDLE) {
                return robot;
            }
        }
        return null;
    }

    public Section getSectionByType(PetType type) throws WarehouseException {
        for (Section section : sections) {
            if (section.getType() == type) {
                return section;
            }
        }
        throw new WarehouseException("Section not found for type: " + type);
    }

    // Getters
    public List<Robot> getRobots() {
        return new ArrayList<>(robots);
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public String getWarehouseStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Warehouse Status ===\n");
        sb.append("Robots: ").append(robots.size()).append("\n");
        for (Robot robot : robots) {
            sb.append("  ").append(robot).append("\n");
        }
        sb.append("Sections:\n");
        for (Section section : sections) {
            sb.append("  ").append(section).append("\n");
        }
        sb.append("Pending Tasks: ").append(taskManager.getPendingTaskCount()).append("\n");
        return sb.toString();
    }
}
