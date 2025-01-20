package de.szut.lf8_starter.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.szut.lf8_starter.customer.dto.CustomerGetDto;
import de.szut.lf8_starter.employee.dto.EmployeeGetDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class ProjectGetDto {
    private Long id;
    private String name;
    private EmployeeGetDto projectManager;
    private CustomerGetDto customer;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private Set<EmployeeGetDto> employees;
}