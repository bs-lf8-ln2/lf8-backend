package de.szut.lf8_starter.exceptionHandling;

public class EmployeeNotAvailableException extends RuntimeException {
    public EmployeeNotAvailableException(String message) {
        super(message);
    }
} 