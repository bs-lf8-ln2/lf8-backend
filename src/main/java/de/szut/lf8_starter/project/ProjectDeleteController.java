package de.szut.lf8_starter.project;

import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectDeleteController {

    private final ProjectService projectService;
    private static final Logger logger = LoggerFactory.getLogger(ProjectDeleteController.class);

    public ProjectDeleteController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable Long projectId) {
        logger.info("Received request to delete project with ID: {}", projectId);

        projectService.deleteById(projectId);

        logger.info("Successfully deleted project with ID: {}", projectId);
        return ResponseEntity.ok("Project with ID " + projectId + " has been successfully deleted.");
    }
}