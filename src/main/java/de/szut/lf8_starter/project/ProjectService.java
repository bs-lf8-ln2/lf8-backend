package de.szut.lf8_starter.project;

import de.szut.lf8_starter.exceptionHandling.ProjectNameAlreadyExistsException;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProjectService {
    private final ProjectRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

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
}