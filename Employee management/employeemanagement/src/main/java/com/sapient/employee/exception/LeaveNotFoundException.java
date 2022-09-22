package com.sapient.employee.exception;

public class LeaveNotFoundException extends Exception  {

    public LeaveNotFoundException() {
        super();
    }

    public LeaveNotFoundException(String message) {
        super(message);
    }

    public LeaveNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
