package com.warehouse.ui;

import com.warehouse.model.Robot;  // Add this BEFORE awt import
import com.warehouse.enums.*;
import com.warehouse.exception.*;
import com.warehouse.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;  // This comes AFTER

public class WarehouseSwingUI extends JFrame {

    private PetFoodWarehouse warehouse;
    private JTextArea logArea;
    private DefaultListModel<String> robotListModel;
    private DefaultListModel<String> taskListModel;

    public WarehouseSwingUI() {
        this.warehouse = new PetFoodWarehouse();
        warehouse.start();
        initUI();
    }

    private void initUI() {
        setTitle("Pet Food Warehouse Manager");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Robots", createRobotPanel());
        tabbedPane.addTab("Tasks", createTaskPanel());
        tabbedPane.addTab("Processing", createProcessingPanel());

        logArea = new JTextArea(8, 50);
        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("System Logs"));

        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        add(logScroll, BorderLayout.SOUTH);
    }

    private JPanel createRobotPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top: Add robot form
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField robotIdField = new JTextField(15);
        JButton addBtn = new JButton("Add Robot");

        formPanel.add(new JLabel("Robot ID:"));
        formPanel.add(robotIdField);
        formPanel.add(addBtn);

        // Center: Robot list
        robotListModel = new DefaultListModel<>();
        JList<String> robotList = new JList<>(robotListModel);
        JScrollPane scrollPane = new JScrollPane(robotList);

        addBtn.addActionListener(e -> {
            String robotId = robotIdField.getText().trim();
            if (!robotId.isEmpty()) {
                Robot robot = new Robot(robotId);
                warehouse.addRobot(robot);
                updateRobotList();
                log("✓ Robot added: " + robotId);
                robotIdField.setText("");
                JOptionPane.showMessageDialog(this, "Robot added successfully!");
            }
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        updateRobotList();
        return panel;
    }

    private JPanel createTaskPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top: Create task form
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        JComboBox<TaskType> taskTypeCombo = new JComboBox<>(TaskType.values());
        JComboBox<PetType> petTypeCombo = new JComboBox<>(PetType.values());
        JTextField quantityField = new JTextField("10");
        JButton createBtn = new JButton("Create Task");

        formPanel.add(new JLabel("Task Type:"));
        formPanel.add(taskTypeCombo);
        formPanel.add(new JLabel("Pet Type:"));
        formPanel.add(petTypeCombo);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);
        formPanel.add(new JLabel(""));
        formPanel.add(createBtn);

        // Center: Task list
        taskListModel = new DefaultListModel<>();
        JList<String> taskList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskList);

        createBtn.addActionListener(e -> {
            try {
                int quantity = Integer.parseInt(quantityField.getText());
                Task task = new Task(
                        (TaskType) taskTypeCombo.getSelectedItem(),
                        (PetType) petTypeCombo.getSelectedItem(),
                        quantity
                );
                warehouse.assignTask(task);
                updateTaskList();
                log("✓ Task created: " + task.getTaskId());
                JOptionPane.showMessageDialog(this, "Task created!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        updateTaskList();
        return panel;
    }

    private JPanel createProcessingPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton sequentialBtn = new JButton("⏩ Process Sequential");
        JButton parallelBtn = new JButton("⚡ Process Parallel");

        sequentialBtn.setPreferredSize(new Dimension(200, 40));
        parallelBtn.setPreferredSize(new Dimension(200, 40));

        sequentialBtn.addActionListener(e -> {
            new Thread(() -> {
                try {
                    warehouse.processTasksSequentially();
                    SwingUtilities.invokeLater(() -> {
                        log("✓ Sequential processing completed");
                        updateAll();
                        JOptionPane.showMessageDialog(this, "Processing completed!");
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        log("✗ Error: " + ex.getMessage());
                        JOptionPane.showMessageDialog(this, ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        });

        parallelBtn.addActionListener(e -> {
            new Thread(() -> {
                try {
                    warehouse.processTasksParallel();
                    SwingUtilities.invokeLater(() -> {
                        log("✓ Parallel processing completed");
                        updateAll();
                        JOptionPane.showMessageDialog(this, "Processing completed!");
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        log("✗ Error: " + ex.getMessage());
                        JOptionPane.showMessageDialog(this, ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(sequentialBtn, gbc);

        gbc.gridy = 1;
        panel.add(parallelBtn, gbc);

        return panel;
    }

    private void updateRobotList() {
        robotListModel.clear();
        for (Robot robot : warehouse.getRobots()) {
            robotListModel.addElement(robot.toString());
        }
    }

    private void updateTaskList() {
        taskListModel.clear();
        for (Task task : warehouse.getTaskManager().getPendingTasks()) {
            taskListModel.addElement(task.toString());
        }
    }

    private void updateAll() {
        updateRobotList();
        updateTaskList();
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append("[" + java.time.LocalTime.now() + "] " + message + "\n");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WarehouseSwingUI ui = new WarehouseSwingUI();
            ui.setVisible(true);
        });
    }

}
