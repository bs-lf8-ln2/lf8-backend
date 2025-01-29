package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.dto.AddEmployeeToProjectDto;
import de.szut.lf8_starter.project.dto.ProjectCreateDto;
import de.szut.lf8_starter.project.dto.ProjectEmployeesDto;
import de.szut.lf8_starter.project.dto.ProjectGetDto;
import de.szut.lf8_starter.project.dto.ProjectUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProjectControllerOpenAPI {
    @Operation(summary = "Create a new project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectGetDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authorized",
                    content = @Content)
    })
    ProjectGetDto create(ProjectCreateDto projectCreateDto);

    @Operation(summary = "Get all projects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all projects",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProjectGetDto.class)))}),
            @ApiResponse(responseCode = "401", description = "Not authorized",
                    content = @Content)
    })
    List<ProjectGetDto> findAll();

    @Operation(summary = "Update an existing project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectGetDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content)
    })
    ProjectGetDto update(Long id, ProjectUpdateDto projectUpdateDto);

    @Operation(summary = "Get a project by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectGetDto.class))}),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authorized",
                    content = @Content)
    })
    ProjectGetDto getById(Long id);


    @Operation(summary = "Add an employee to a project with specific qualification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee successfully added to project",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectGetDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input or employee doesn't have required qualification",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Project or Employee not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Employee already assigned or not available",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<?> addEmployeeToProject(Long projectId, AddEmployeeToProjectDto dto);

    @Operation(summary = "Get all employees of a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all project employees",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProjectEmployeesDto.class))}),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authorized",
                    content = @Content)
    })
    ProjectEmployeesDto getProjectEmployees(Long projectId);
}