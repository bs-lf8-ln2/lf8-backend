package de.szut.lf8_starter.project;

import de.szut.lf8_starter.exceptionHandling.ProjectNameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProjectControllerAdvice {
    @ExceptionHandler(ProjectNameAlreadyExistsException.class)
    public ResponseEntity<String> handleProjectNameAlreadyExistsException(ProjectNameAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
