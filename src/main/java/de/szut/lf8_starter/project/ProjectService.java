package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeEntity;
import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.exceptionHandling.ProjectNameAlreadyExistsException;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ProjectService {
    private final ProjectRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private EmployeeService employeeService;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public ProjectEntity create(ProjectEntity projectEntity) {
        if (repository.existsByName(projectEntity.getName())) {
            throw new ProjectNameAlreadyExistsException("A project with the name " + projectEntity.getName() + " already exists.");
        }
        return repository.save(projectEntity);
    }

    public List<ProjectEntity> readAll() {
        logger.info("Fetching all projects");
        List<ProjectEntity> projects = this.repository.findAll();
        logger.info("Found {} projects", projects.size());
        return projects;
    }

    public ProjectEntity getProjectById(Long id) {
        logger.info("Attempting to find project with id: {}", id);
        return this.repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Project not found with id: {}", id);
                    return new ResourceNotFoundException("Project not found with id: " + id);
                });
    }

    public ProjectEntity update(Long id, ProjectEntity updatedProject) {
        logger.info("Attempting to update project with id: {}", id);

        // Find existing project
        ProjectEntity existingProject = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Project not found with id: {}", id);
                    return new ResourceNotFoundException("Project not found with id: " + id);
                });

        // Update fields
        existingProject.setName(updatedProject.getName());
        existingProject.setProjectManager(updatedProject.getProjectManager());
        existingProject.setCustomer(updatedProject.getCustomer());
        existingProject.setStartDate(updatedProject.getStartDate());
        existingProject.setEndDate(updatedProject.getEndDate());
        existingProject.setEmployees(updatedProject.getEmployees());

        // Save and return
        ProjectEntity savedProject = repository.save(existingProject);
        logger.info("Successfully updated project with id: {}", id);

        return savedProject;
    }


    @Transactional
    public ProjectEntity addEmployeeToProject(Long projectId, Long employeeId, String qualificationName) {
        // 1. Projekt und Mitarbeiter abrufen
        ProjectEntity project = getProjectById(projectId);
        EmployeeEntity employee = employeeService.getEmployeeById(employeeId);

        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found with id: " + employeeId);
        }

        // 2. Prüfen ob Mitarbeiter bereits im Projekt ist
        if (project.getEmployees().stream()
                .anyMatch(emp -> emp.getId().equals(employeeId))) {
            throw new IllegalStateException("Employee is already assigned to this project");
        }

        // 3. Prüfen ob Mitarbeiter die Qualifikation besitzt
        boolean hasQualification = employee.getQualifications().stream()
                .anyMatch(q -> q.getSkill().equalsIgnoreCase(qualificationName));

        if (!hasQualification) {
            throw new IllegalArgumentException(
                    "Employee does not have the required qualification: " + qualificationName);
        }

        // 4. Prüfen ob der Mitarbeiter im Projektzeitraum verfügbar ist
        checkEmployeeAvailability(employee, project.getStartDate(), project.getEndDate());

        // 5. Mitarbeiter zum Projekt hinzufügen
        project.getEmployees().add(employee);

        // 6. Änderungen speichern
        return repository.save(project);
    }

    private void checkEmployeeAvailability(EmployeeEntity employee, LocalDate startDate, LocalDate endDate) {
        // Prüfe in allen Projekten, ob der Mitarbeiter im Zeitraum schon verplant ist
        List<ProjectEntity> allProjects = repository.findAll();

        boolean isEmployeeBooked = allProjects.stream()
                .filter(p -> p.getEmployees().contains(employee))
                .anyMatch(p -> {
                    // Überschneidung der Zeiträume prüfen
                    return !(p.getEndDate().isBefore(startDate) || p.getStartDate().isAfter(endDate));
                });

        if (isEmployeeBooked) {
            throw new IllegalStateException(
                    String.format("Employee %s %s is already booked in this time period",
                            employee.getFirstName(),
                            employee.getLastName())
            );
        }
    }

    public void removeEmployeeFromProject(Long id, Long employeeId) {
        logger.info("Attempting to remove employee with id: {} from project with id: {}", employeeId, id);

        // Find existing project
        ProjectEntity existingProject = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Project not found with id: {}", id);
                    return new ResourceNotFoundException("Project not found with id: " + id);
                });

        // Remove employee
        boolean removed = existingProject.getEmployees().removeIf(employee -> employee.getId().equals(employeeId));
        if (!removed) {
            logger.warn("Employee with id: {} not found in project with id: {}", employeeId, id);
            throw new ResourceNotFoundException("Employee not found in project");
        }

        // Save and return
        repository.save(existingProject);
        logger.info("Successfully removed employee with id: {} from project with id: {}", employeeId, id);
    }

    public Page<ProjectEntity> readAll(Long managerId, Long customerId, Pageable pageable) {
        return repository.findAllWithFilters(managerId, customerId, pageable);
    }

    public void delete(Long id) {
        logger.info("Attempting to delete project with id: {}", id);

        ProjectEntity existingProject = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Project not found with id: {}", id);
                    return new ResourceNotFoundException("Project not found with id: " + id);
                });

        repository.delete(existingProject);
        logger.info("Successfully deleted project with id: {}", id);
    }
}