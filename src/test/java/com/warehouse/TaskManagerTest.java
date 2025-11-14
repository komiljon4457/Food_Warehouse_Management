package com.warehouse;

import com.warehouse.enums.*;
import com.warehouse.model.*;
import com.warehouse.exception.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new TaskManager();
    }

    @Test
    public void testAddTask() throws TaskException {
        Task task = new Task(TaskType.LOADING, PetType.DOG, 5);
        taskManager.addTask(task);

        assertEquals(1, taskManager.getPendingTaskCount());
    }

    @Test
    public void testAddNullTask() {
        assertThrows(TaskException.class, () -> {
            taskManager.addTask(null);
        });
    }

    @Test
    public void testGetNextTask() throws TaskException {
        Task task1 = new Task(TaskType.LOADING, PetType.CAT, 3);
        Task task2 = new Task(TaskType.UNLOADING, PetType.DOG, 2);

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Task retrieved = taskManager.getNextTask();
        assertEquals(task1.getTaskId(), retrieved.getTaskId());
        assertEquals(1, taskManager.getPendingTaskCount());
    }

    @Test
    public void testAssignTaskToRobot() throws TaskException, RobotException {
        Robot robot = new Robot("R001");
        Task task = new Task(TaskType.MOVING, PetType.BIRD, 4);

        taskManager.addTask(task);
        boolean assigned = taskManager.assignTaskToRobot(robot);

        assertTrue(assigned);
        assertEquals(0, taskManager.getPendingTaskCount());
    }
}
