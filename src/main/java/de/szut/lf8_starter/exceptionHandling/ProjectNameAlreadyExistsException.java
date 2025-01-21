package de.szut.lf8_starter.exceptionHandling;

public class ProjectNameAlreadyExistsException extends RuntimeException {
    public ProjectNameAlreadyExistsException(String message) {
        super(message);
    }
}