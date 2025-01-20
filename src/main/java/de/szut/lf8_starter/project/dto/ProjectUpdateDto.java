package de.szut.lf8_starter.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class ProjectUpdateDto {
    @NotBlank(message = "Project name is required")
    private String name;

    @NotNull(message = "Project manager id is required")
    private Long projectManager;

    @NotBlank(message = "Customer is required")
    private Long customer;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "Employee list is required")
    private Set<Long> employeeIds;
} 