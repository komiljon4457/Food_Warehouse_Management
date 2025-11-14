package com.warehouse.exception;

public class TaskException extends WarehouseException {
    public TaskException(String message) {
        super(message);
    }

    public TaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
