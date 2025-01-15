package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.employee.dto.EmployeeCreateDto;
import de.szut.lf8_starter.employee.dto.EmployeeGetDto;
import de.szut.lf8_starter.qualification.QualificationMapper;
import de.szut.lf8_starter.qualification.QualificationRepository;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.HashSet;

@Service
public class EmployeeMapper {
    private final QualificationMapper qualificationMapper;
    private final QualificationRepository qualificationRepository;

    public EmployeeMapper(QualificationMapper qualificationMapper, QualificationRepository qualificationRepository) {
        this.qualificationMapper = qualificationMapper;
        this.qualificationRepository = qualificationRepository;
    }

    public EmployeeGetDto mapToGetDto(EmployeeEntity entity) {
        return new EmployeeGetDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getDepartment(),
                entity.getQualifications().stream()
                        .map(qualificationMapper::mapToGetDto)
                        .collect(Collectors.toSet())
        );
    }

    public EmployeeEntity mapCreateDtoToEntity(EmployeeCreateDto dto) {
        var entity = new EmployeeEntity();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setDepartment(dto.getDepartment());

        // Map qualification IDs to actual qualification entities
        entity.setQualifications(
                dto.getQualificationIds().stream()
                        .map(id -> qualificationRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Qualification not found with id: " + id)))
                        .collect(Collectors.toSet())
        );

        return entity;
    }
}