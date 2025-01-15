package de.szut.lf8_starter.qualification;

import de.szut.lf8_starter.qualification.dto.QualificationCreateDto;
import de.szut.lf8_starter.qualification.dto.QualificationGetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

public interface QualificationControllerOpenAPI {
    @Operation(summary = "Create a new qualification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Qualification created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QualificationGetDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authorized",
                    content = @Content)
    })
    QualificationGetDto create(QualificationCreateDto qualificationCreateDto);

    @Operation(summary = "Get all qualifications")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all qualifications",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = QualificationGetDto.class)))}),
            @ApiResponse(responseCode = "401", description = "Not authorized",
                    content = @Content)
    })
    List<QualificationGetDto> findAll();
}