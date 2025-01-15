package de.szut.lf8_starter.employee.dto;

import de.szut.lf8_starter.qualification.dto.QualificationGetDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class EmployeeGetDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private Set<QualificationGetDto> qualifications;
}