package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeEntity;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.project.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class ProjectController implements ProjectControllerOpenAPI {
    private final ProjectService service;
    private final ProjectMapper projectMapper;
    private final ProjectRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    public ProjectController(ProjectService service, ProjectMapper projectMapper, ProjectRepository repository) {
        this.service = service;
        this.projectMapper = projectMapper;
        this.repository = repository;
    }

    @PostMapping
    public ProjectGetDto create(@RequestBody @Valid ProjectCreateDto projectCreateDto) {
        ProjectEntity projectEntity = this.projectMapper.mapCreateDtoToEntity(projectCreateDto);
        projectEntity = this.service.create(projectEntity);
        return this.projectMapper.mapToGetDto(projectEntity);
    }

    @GetMapping
    public Page<ProjectGetDto> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) Long managerId,
            @RequestParam(required = false) Long customerId) {
        logger.info("GET request received for all projects");

        Pageable pageable = PageRequest.of(page, Math.min(size, 50), Sort.by(Sort.Direction.DESC, "createdAt"));
        return this.service
                .readAll(managerId, customerId, pageable)
                .map(this.projectMapper::mapToGetDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectGetDto update(@PathVariable Long id, @RequestBody @Valid ProjectUpdateDto projectUpdateDto) {
        logger.info("PUT request received for project id: {}", id);

        try {
            ProjectEntity projectEntity = this.projectMapper.mapUpdateDtoToEntity(projectUpdateDto);
            ProjectEntity updatedEntity = this.service.update(id, projectEntity);
            ProjectGetDto updatedProject = this.projectMapper.mapToGetDto(updatedEntity);

            // Add success message to response headers
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes())
                    .getResponse();
            if (response != null) {
                response.setHeader("X-Success-Message", "Project successfully updated");
            }

            logger.info("Project successfully updated with id: {}", id);
            return updatedProject;

        } catch (ResourceNotFoundException e) {
            logger.error("Project not found with id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input for project update: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ProjectGetDto getById(@PathVariable Long id) {
        logger.info("GET request received for project id: {}", id);
        try {
            ProjectEntity projectEntity = this.service.getProjectById(id);
            logger.info("Found project: {}", projectEntity);
            return this.projectMapper.mapToGetDto(projectEntity);
        } catch (Exception e) {
            logger.error("Error getting project: ", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id: " + id);
        }
    }

    @PostMapping("/{projectId}/add-employee")
    public ResponseEntity<?> addEmployeeToProject(
            @PathVariable Long projectId,
            @RequestBody @Valid AddEmployeeToProjectDto dto) {

        logger.info("Adding employee {} to project {} with qualification {}",
                dto.getEmployeeId(), projectId, dto.getQualification());

        try {
            ProjectEntity updatedProject = service.addEmployeeToProject(
                    projectId,
                    dto.getEmployeeId(),
                    dto.getQualification()
            );
            return ResponseEntity.ok(projectMapper.mapToGetDto(updatedProject));

        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (IllegalStateException e) {
            logger.error("Invalid state: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());

        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (Exception e) {
            logger.error("Unexpected error while adding employee to project", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    @GetMapping("/{projectId}/employees")
    public ProjectEmployeesDto getProjectEmployees(@PathVariable Long projectId) {
        logger.info("GET request received for employees of project id: {}", projectId);

        ProjectEntity project = this.service.getProjectById(projectId);
        Set<EmployeeEntity> employees = this.service.getProjectEmployees(projectId);

        Set<ProjectEmployeesDto.EmployeeRoleDto> employeeDtos = employees.stream()
                .map(employee -> new ProjectEmployeesDto.EmployeeRoleDto(
                        employee.getId(),
                        employee.getQualifications().stream()
                                .map(q -> q.getSkill())
                                .findFirst()
                                .orElse("Unknown")
                ))
                .collect(Collectors.toSet());

        return new ProjectEmployeesDto(project.getId(), project.getName(), employeeDtos);
    }

    @DeleteMapping("/{id}/employees/{employeeId}")
    public ResponseEntity<Map<String, Boolean>> removeEmployeeFromProject(@PathVariable Long id, @PathVariable Long employeeId) {
        logger.info("DELETE request received for project id: {} and employee id: {}", id, employeeId);
        try {
            this.service.removeEmployeeFromProject(id, employeeId);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            logger.error("Project or employee not found with id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/{projectName}/employees")
    public ResponseEntity<?> addEmployeeToProjectByName(
            @PathVariable String projectName,
            @RequestBody @Valid AddEmployeeToProjectDto dto) {

        logger.info("Adding employee {} to project '{}' with qualification {}",
                dto.getEmployeeId(), projectName, dto.getQualification());

        try {
            // Find project by name first
            ProjectEntity project = service.findByName(projectName);
            
            ProjectEntity updatedProject = service.addEmployeeToProject(
                    project.getId(),
                    dto.getEmployeeId(),
                    dto.getQualification()
            );
            return ResponseEntity.ok(projectMapper.mapToGetDto(updatedProject));

        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (IllegalStateException e) {
            logger.error("Invalid state: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());

        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (Exception e) {
            logger.error("Unexpected error while adding employee to project", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    @GetMapping("/debug/all")
    public ResponseEntity<?> getAllProjectsDebug() {
        try {
            logger.info("Debug: Getting all projects");
            List<Map<String, Object>> debugInfo = repository.findAll().stream()
                .map(p -> {
                    Map<String, Object> info = new HashMap<>();
                    info.put("id", p.getId());
                    info.put("name", p.getName());
                    info.put("customerId", p.getCustomer() != null ? p.getCustomer().getId() : null);
                    info.put("managerId", p.getProjectManager() != null ? p.getProjectManager().getId() : null);
                    return info;
                })
                .collect(Collectors.toList());
            
            logger.info("Found {} projects", debugInfo.size());
            return ResponseEntity.ok(debugInfo);
        } catch (Exception e) {
            logger.error("Error in debug endpoint", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/debug/project-employees")
    public ResponseEntity<?> debugProjectEmployees() {
        Map<String, Object> debug = new HashMap<>();
        
        repository.findAll().forEach(project -> {
            Map<String, Object> projectInfo = new HashMap<>();
            projectInfo.put("id", project.getId());
            projectInfo.put("name", project.getName());
            projectInfo.put("employeeIds", project.getEmployees().stream()
                .map(EmployeeEntity::getId)
                .collect(Collectors.toList()));
            debug.put(project.getName(), projectInfo);
        });
        
        return ResponseEntity.ok(debug);
    }
}