package de.szut.lf8_starter.project.dto;

import de.szut.lf8_starter.project.ProjectRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueProjectNameValidator implements ConstraintValidator<UniqueProjectName, String> {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null) {
            return true; // let @NotBlank handle null validation
        }
        return !projectRepository.existsByNameIgnoreCase(name);
    }
}