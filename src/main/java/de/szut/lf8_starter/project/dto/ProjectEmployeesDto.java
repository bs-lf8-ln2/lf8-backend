package de.szut.lf8_starter.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class ProjectEmployeesDto {
    private Long projectId;
    private String projectName;
    private Set<EmployeeRoleDto> employees;

    @AllArgsConstructor
    @Getter
    @Setter
    public static class EmployeeRoleDto {
        private Long id;
        private String role;
    }
}