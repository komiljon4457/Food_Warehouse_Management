package com.warehouse.ui;

import com.warehouse.enums.PetType;
import com.warehouse.enums.TaskType;
import com.warehouse.exception.RobotException;
import com.warehouse.exception.TaskException;
import com.warehouse.model.*;

import java.util.Scanner;

public class WarehouseUI {
    private PetFoodWarehouse warehouse;
    private Scanner scanner;

    public WarehouseUI() {
        this.warehouse = new PetFoodWarehouse();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        warehouse.start();
        displayWelcome();

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput();

            try {
                running = handleMenuChoice(choice);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private void displayWelcome() {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║   Pet Food Warehouse Manager v1.0     ║");
        System.out.println("╚═══════════════════════════════════════╝\n");
    }

    private void displayMenu() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           MAIN MENU                 │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│ 1. Add Robot                        │");
        System.out.println("│ 2. Create Task                      │");
        System.out.println("│ 3. Process Tasks (Sequential)       │");
        System.out.println("│ 4. Process Tasks (Parallel)         │");
        System.out.println("│ 5. View Warehouse Status            │");
        System.out.println("│ 6. Add Food Items to Section        │");
        System.out.println("│ 7. View Sections                    │");
        System.out.println("│ 8. Exit                             │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.print("Enter choice: ");
    }

    private boolean handleMenuChoice(int choice) throws Exception {
        switch (choice) {
            case 1:
                addRobot();
                break;
            case 2:
                createTask();
                break;
            case 3:
                processSequential();
                break;
            case 4:
                processParallel();
                break;
            case 5:
                viewStatus();
                break;
            case 6:
                addFoodItems();
                break;
            case 7:
                viewSections();
                break;
            case 8:
                System.out.println("Exiting... Goodbye!");
                return false;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return true;
    }

    private void addRobot() {
        System.out.print("Enter Robot ID: ");
        String robotId = scanner.next();
        Robot robot = new Robot(robotId);
        warehouse.addRobot(robot);
        System.out.println("✓ Robot added successfully!");
    }

    private void createTask() throws TaskException {
        System.out.println("\nTask Types:");
        TaskType[] types = TaskType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Select task type: ");
        int typeChoice = getIntInput() - 1;

        if (typeChoice < 0 || typeChoice >= types.length) {
            System.out.println("Invalid task type!");
            return;
        }

        System.out.println("\nPet Types:");
        PetType[] petTypes = PetType.values();
        for (int i = 0; i < petTypes.length; i++) {
            System.out.println((i + 1) + ". " + petTypes[i]);
        }
        System.out.print("Select pet type: ");
        int petChoice = getIntInput() - 1;

        if (petChoice < 0 || petChoice >= petTypes.length) {
            System.out.println("Invalid pet type!");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = getIntInput();

        Task task = new Task(types[typeChoice], petTypes[petChoice], quantity);
        warehouse.assignTask(task);
        System.out.println("✓ Task created: " + task.getTaskId());
    }

    private void processSequential() {
        try {
            warehouse.processTasksSequentially();
            System.out.println("✓ Sequential processing completed!");
        } catch (RobotException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void processParallel() {
        try {
            warehouse.processTasksParallel();
            System.out.println("✓ Parallel processing completed!");
        } catch (InterruptedException e) {
            System.out.println("✗ Processing interrupted: " + e.getMessage());
        }
    }

    private void viewStatus() {
        System.out.println(warehouse.getWarehouseStatus());
    }

    private void addFoodItems() {
        try {
            System.out.println("\nPet Types:");
            PetType[] petTypes = PetType.values();
            for (int i = 0; i < petTypes.length; i++) {
                System.out.println((i + 1) + ". " + petTypes[i]);
            }
            System.out.print("Select pet type: ");
            int petChoice = getIntInput() - 1;

            if (petChoice < 0 || petChoice >= petTypes.length) {
                System.out.println("Invalid pet type!");
                return;
            }

            System.out.print("Enter brand: ");
            String brand = scanner.next();

            System.out.print("Enter quantity to add: ");
            int quantity = getIntInput();

            Section section = warehouse.getSectionByType(petTypes[petChoice]);

            for (int i = 0; i < quantity; i++) {
                FoodCan can = new FoodCan("CAN-" + System.currentTimeMillis() + "-" + i,
                        petTypes[petChoice], brand);
                section.storeFoodCan(can);
            }

            System.out.println("✓ Added " + quantity + " items to " + petTypes[petChoice] + " section");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void viewSections() {
        System.out.println("\n=== Section Details ===");
        for (Section section : warehouse.getSections()) {
            System.out.println(section);
            System.out.println("  Items: " + section.getItemCount());
        }
    }

    private int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter a number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
}