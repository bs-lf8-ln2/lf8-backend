package de.szut.lf8_starter.customer;

import de.szut.lf8_starter.customer.dto.CustomerCreateDto;
import de.szut.lf8_starter.customer.dto.CustomerGetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

public interface CustomerControllerOpenAPI {
    @Operation(summary = "Creates a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerGetDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authorized",
                    content = @Content)
    })
    CustomerGetDto create(CustomerCreateDto customerCreateDto);

    @Operation(summary = "Get all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all customers",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CustomerGetDto.class)))}),
            @ApiResponse(responseCode = "401", description = "Not authorized",
                    content = @Content)
    })
    List<CustomerGetDto> findAll();

    @Operation(summary = "Get a customer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerGetDto.class))}),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content)
    })
    CustomerGetDto getById(Long id);

    @Operation(summary = "Delete a customer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted"),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content)
    })
    void deleteById(Long id);

    @Operation(summary = "Update a customer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerGetDto.class))}),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content)
    })
    CustomerGetDto updateById(Long id, CustomerCreateDto customerCreateDto);
}
