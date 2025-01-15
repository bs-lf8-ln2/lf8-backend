package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class ProjectController implements ProjectControllerOpenAPI {
    private final ProjectService service;
    private final ProjectMapper projectMapper;

    public ProjectController(ProjectService service, ProjectMapper projectMapper) {
        this.service = service;
        this.projectMapper = projectMapper;
    }

    @PostMapping
    public ProjectGetDto create(@RequestBody @Valid ProjectCreateDto projectCreateDto) {
        ProjectEntity projectEntity = this.projectMapper.mapCreateDtoToEntity(projectCreateDto);
        projectEntity = this.service.create(projectEntity);
        return this.projectMapper.mapToGetDto(projectEntity);
    }

    @GetMapping
    public List<ProjectGetDto> findAll() {
        return this.service
                .readAll()
                .stream()
                .map(e -> this.projectMapper.mapToGetDto(e))
                .collect(Collectors.toList());
    }
}