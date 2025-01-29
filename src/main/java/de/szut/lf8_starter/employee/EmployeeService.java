package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.ProjectRepository;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository repository;
    private final ProjectRepository projectRepository;

    public EmployeeService(EmployeeRepository repository, ProjectRepository projectRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
    }

    public EmployeeEntity create(EmployeeEntity entity) {
        return this.repository.save(entity);
    }

    public List<EmployeeEntity> readAll() {
        return this.repository.findAll();
    }

    public EmployeeEntity getEmployeeById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public Collection<ProjectEntity> getProjects(Long id) {
        return this.projectRepository.getProjectEntitiesByEmployees(id);
    }

    public boolean isEmployeeAvailable(Long employeeId) {
        EmployeeEntity employee = repository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        
        // Check if employee is already assigned to another project
        return employee.getProjects().isEmpty();
    }
}