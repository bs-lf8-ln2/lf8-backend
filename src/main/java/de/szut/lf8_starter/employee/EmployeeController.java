package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.employee.dto.EmployeeCreateDto;
import de.szut.lf8_starter.employee.dto.EmployeeGetDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employee")
public class EmployeeController implements EmployeeControllerOpenAPI {
    private final EmployeeService service;
    private final EmployeeMapper employeeMapper;

    public EmployeeController(EmployeeService service, EmployeeMapper employeeMapper) {
        this.service = service;
        this.employeeMapper = employeeMapper;
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
}