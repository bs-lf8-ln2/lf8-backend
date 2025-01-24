package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.employee.dto.EmployeeCreateDto;
import de.szut.lf8_starter.employee.dto.EmployeeGetDto;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.ProjectMapper;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employee")
public class EmployeeController implements EmployeeControllerOpenAPI {
    private final EmployeeService service;
    private final EmployeeMapper employeeMapper;
    private final ProjectMapper projectMapper;

    public EmployeeController(EmployeeService service, EmployeeMapper employeeMapper, ProjectMapper projectMapper) {
        this.service = service;
        this.employeeMapper = employeeMapper;
        this.projectMapper = projectMapper;
    }

    @PostMapping
    public EmployeeGetDto create(@RequestBody @Valid EmployeeCreateDto employeeCreateDto) {
        EmployeeEntity employeeEntity = this.employeeMapper.mapCreateDtoToEntity(employeeCreateDto);
        employeeEntity = this.service.create(employeeEntity);
        return this.employeeMapper.mapToGetDto(employeeEntity);
    }

    // TODO why not response Entities?
    @GetMapping("/{id}")
    public EmployeeGetDto getById(@PathVariable final Long id) {
        EmployeeEntity employeeEntity = this.service.getEmployeeById(id);
        if (employeeEntity == null) {
            return null;
        } else {
            return this.employeeMapper.mapToGetDto(employeeEntity);
        }
    }

    @GetMapping
    public List<EmployeeGetDto> findAll() {
        return this.service
                .readAll()
                .stream()
                .map(e -> this.employeeMapper.mapToGetDto(e))
                .collect(Collectors.toList());
    }

    // Get all projects of an employee
    @GetMapping("/{id}/projects")
    public List<ProjectGetDto> getProjects(@PathVariable final Long id) {
        // check if employee exists
        if (this.service.getEmployeeById(id) == null) {
            return null;
        }
        return this.service.getProjects(id)
                .stream()
                .map(p -> this.projectMapper.mapToGetDto((ProjectEntity) p))
                .collect(Collectors.toList());
    }
}