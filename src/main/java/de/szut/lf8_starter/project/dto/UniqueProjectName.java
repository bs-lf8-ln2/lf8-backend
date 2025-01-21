package de.szut.lf8_starter.project.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueProjectNameValidator.class)
public @interface UniqueProjectName {
    String message() default "Project name must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}