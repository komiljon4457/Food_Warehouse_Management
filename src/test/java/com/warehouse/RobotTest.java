package com.warehouse;

import com.warehouse.enums.*;
import com.warehouse.model.*;
import com.warehouse.exception.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class RobotTest {

    @Test
    public void testRobotCreation() {
        Robot robot = new Robot("R001");
        assertEquals("R001", robot.getRobotId());
        assertEquals(RobotStatus.IDLE, robot.getStatus());
    }

    @Test
    public void testRobotPerformTask() throws RobotException {
        Robot robot = new Robot("R001");
        Task task = new Task(TaskType.LOADING, PetType.CAT, 5);

        robot.performTask(task);

        assertEquals(RobotStatus.IDLE, robot.getStatus());
        assertEquals(TaskStatus.COMPLETED, task.getStatus());
    }

    @Test
    public void testRobotBusyException() throws RobotException {
        Robot robot = new Robot("R001");
        robot.setStatus(RobotStatus.BUSY);
        Task task = new Task(TaskType.LOADING, PetType.CAT, 5);

        assertThrows(RobotException.class, () -> {
            robot.performTask(task);
        });
    }
}
