package com.warehouse.exception;

public class RobotException extends WarehouseException {
    public RobotException(String message) {
        super(message);
    }

    public RobotException(String message, Throwable cause) {
        super(message, cause);
    }
}
