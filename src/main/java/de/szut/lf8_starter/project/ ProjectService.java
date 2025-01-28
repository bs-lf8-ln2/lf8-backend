package de.szut.lf8_starter.project;

import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void deleteById(Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    logger.error("Project with ID {} does not exist", projectId);
                    return new ResourceNotFoundException("Project with ID " + projectId + " does not exist.");
                });

        projectRepository.delete(project);
        logger.info("Deleted project with ID: {}", projectId);
    }
}