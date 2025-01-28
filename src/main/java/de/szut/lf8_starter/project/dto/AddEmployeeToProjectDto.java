package de.szut.lf8_starter.project.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddEmployeeToProjectDto {
    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotBlank(message = "Qualification is required")
    private String qualification;
}