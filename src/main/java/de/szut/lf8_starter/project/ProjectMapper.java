package de.szut.lf8_starter.project;

import de.szut.lf8_starter.customer.CustomerMapper;
import de.szut.lf8_starter.customer.CustomerRepository;
import de.szut.lf8_starter.employee.EmployeeMapper;
import de.szut.lf8_starter.employee.EmployeeRepository;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import de.szut.lf8_starter.project.dto.ProjectUpdateDto;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ProjectMapper {
    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    public ProjectMapper(EmployeeMapper employeeMapper, EmployeeRepository employeeRepository, CustomerMapper customerMapper, CustomerRepository customerRepository) {
        this.employeeMapper = employeeMapper;
        this.employeeRepository = employeeRepository;
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }

    public ProjectGetDto mapToGetDto(ProjectEntity entity) {
        return new ProjectGetDto(
                entity.getId(),
                entity.getName(),
                employeeMapper.mapToGetDto(entity.getProjectManager()),
                customerMapper.mapToGetDto(entity.getCustomer()),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getEmployees().stream()
                        .map(employeeMapper::mapToGetDto)
                        .collect(Collectors.toSet())
        );
    }

    public ProjectEntity mapCreateDtoToEntity(ProjectCreateDto dto) {
        var entity = new ProjectEntity();
        entity.setName(dto.getName());
        entity.setProjectManager(employeeRepository.findById(dto.getProjectManager())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + dto.getProjectManager())));
        entity.setCustomer(customerRepository.findById(dto.getCustomer())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + dto.getCustomer())));
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());

        // Map employee IDs to actual employee entities
        entity.setEmployees(
                dto.getEmployeeIds().stream()
                        .map(id -> employeeRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id)))
                        .collect(Collectors.toSet())
        );

        return entity;
    }

    public ProjectEntity mapUpdateDtoToEntity(ProjectUpdateDto dto) {
        var entity = new ProjectEntity();
        entity.setName(dto.getName());
        entity.setProjectManager(employeeRepository.findById(dto.getProjectManager())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + dto.getProjectManager())));
        entity.setCustomer(customerRepository.findById(dto.getCustomer())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + dto.getCustomer())));
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());

        // Map employee IDs to actual employee entities
        entity.setEmployees(
                dto.getEmployeeIds().stream()
                        .map(id -> employeeRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id)))
                        .collect(Collectors.toSet())
        );

        return entity;
    }
}