package com.warehouse.model;

import com.warehouse.enums.PetType;
import com.warehouse.exception.WarehouseException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Section {
    private PetType type;
    private List<FoodCan> foodCans;
    private final ReentrantLock lock;

    public Section(PetType type) {
        this.type = type;
        this.foodCans = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    public void storeFoodCan(FoodCan foodCan) throws WarehouseException {
        lock.lock();
        try {
            if (foodCan.getPetType() != this.type) {
                throw new WarehouseException(
                        "Cannot store " + foodCan.getPetType() + " in " + this.type + " section"
                );
            }
            foodCans.add(foodCan);
        } finally {
            lock.unlock();
        }
    }

    public FoodCan retrieveFoodCan(PetType petType) throws WarehouseException {
        lock.lock();
        try {
            if (petType != this.type) {
                throw new WarehouseException(
                        "Cannot retrieve " + petType + " from " + this.type + " section"
                );
            }
            if (foodCans.isEmpty()) {
                throw new WarehouseException("Section is empty");
            }
            return foodCans.remove(0);
        } finally {
            lock.unlock();
        }
    }

    public PetType getType() {
        return type;
    }

    public int getItemCount() {
        lock.lock();
        try {
            return foodCans.size();
        } finally {
            lock.unlock();
        }
    }

    public List<FoodCan> getFoodCans() {
        lock.lock();
        try {
            return new ArrayList<>(foodCans);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "Section{" +
                "type=" + type +
                ", itemCount=" + getItemCount() +
                '}';
    }
}