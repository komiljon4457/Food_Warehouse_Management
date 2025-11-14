package com.warehouse.model;

import com.warehouse.enums.PetType;

public class FoodCan {
    private String canId;
    private PetType petType;
    private String brand;

    public FoodCan(String canId, PetType petType, String brand) {
        this.canId = canId;
        this.petType = petType;
        this.brand = brand;
    }

    public String getCanId() {
        return canId;
    }

    public void setCanId(String canId) {
        this.canId = canId;
    }

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "FoodCan{" +
                "canId='" + canId + '\'' +
                ", petType=" + petType +
                ", brand='" + brand + '\'' +
                '}';
    }
}
