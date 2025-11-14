package com.warehouse.enums;

public enum TaskType {
    LOADING("Loading items"),
    UNLOADING("Unloading items"),
    MOVING("Moving items"),
    TRANSFERRING("Transferring items");

    private final String description;

    TaskType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}