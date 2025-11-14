package com.warehouse.enums;

public enum PetType {
    CAT("Cat Food"),
    DOG("Dog Food"),
    BIRD("Bird Food");

    private final String description;

    PetType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}