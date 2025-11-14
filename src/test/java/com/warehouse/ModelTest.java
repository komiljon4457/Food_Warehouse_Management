package com.warehouse;

import com.warehouse.enums.PetType;
import com.warehouse.enums.TaskType;
import com.warehouse.model.*;
import com.warehouse.exception.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    @Test
    public void testFoodCanCreation() {
        FoodCan can = new FoodCan("CAN001", PetType.DOG, "Pedigree");
        assertEquals("CAN001", can.getCanId());
        assertEquals(PetType.DOG, can.getPetType());
        assertEquals("Pedigree", can.getBrand());
    }

    @Test
    public void testSectionStorage() throws WarehouseException {
        Section section = new Section(PetType.CAT);
        FoodCan can = new FoodCan("CAN001", PetType.CAT, "Whiskas");

        section.storeFoodCan(can);
        assertEquals(1, section.getItemCount());
    }

    @Test
    public void testSectionWrongPetType() {
        Section section = new Section(PetType.CAT);
        FoodCan can = new FoodCan("CAN001", PetType.DOG, "Pedigree");

        assertThrows(WarehouseException.class, () -> {
            section.storeFoodCan(can);
        });
    }

    @Test
    public void testSectionRetrieval() throws WarehouseException {
        Section section = new Section(PetType.BIRD);
        FoodCan can = new FoodCan("CAN001", PetType.BIRD, "Kaytee");

        section.storeFoodCan(can);
        FoodCan retrieved = section.retrieveFoodCan(PetType.BIRD);

        assertNotNull(retrieved);
        assertEquals("CAN001", retrieved.getCanId());
        assertEquals(0, section.getItemCount());
    }

    @Test
    public void testTaskCreation() {
        Task task = new Task(TaskType.LOADING, PetType.DOG, 10);

        assertNotNull(task.getTaskId());
        assertEquals(TaskType.LOADING, task.getTaskType());
        assertEquals(PetType.DOG, task.getPetType());
        assertEquals(10, task.getQuantity());
    }
}
