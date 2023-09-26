package edu.flightregistration.rest.controllers;

import com.sun.istack.NotNull;
import edu.flightregistration.core.services.async.TerminalService;
import edu.flightregistration.core.services.sync.FlightService;
import edu.flightregistration.core.services.sync.FlightUpdateService;
import edu.flightregistration.rest.dto.*;
import edu.flightregistration.rest.mappers.FlightDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/flight/v1", produces = "application/json")
@Validated
@RequiredArgsConstructor
public class FlightController {

    private final FlightUpdateService flightUpdateService;
    private final FlightService flightService;
    private final TerminalService terminalService;
    private final FlightDtoMapper flightDtoMapper;

    @PostMapping()
    @Operation(summary = "Register flight ", tags = {"Flight"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content, description = "Flight registered"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class)),
                    description = "Validation or registration error"),
    })
    public ResponseEntity<Void> registerFlight(@Valid @RequestBody RegisterFlightDto dto) {
        flightService.registerFlight(flightDtoMapper.dtoToModel(dto));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{flightNumber}/arrived")
    @Operation(summary = "Mark registered flight as arrived", tags = {"Flight"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content, description = "Flight updated"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class)),
                    description = "Validation or update error"),
    })
    @Parameter(name = "flightNumber", required = true)
    public ResponseEntity<Void> flightArrived(@PathVariable("flightNumber") @NotNull String flightNumber) {
        flightUpdateService.registerArrival(flightNumber);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{flightNumber}/departed")
    @Operation(summary = "Mark registered flight as departed", tags = {"Flight"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content, description = "Flight updated"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class)),
                    description = "Validation or update error"),
    })
    @Parameter(name = "flightNumber", required = true)
    public ResponseEntity<Void> flightDeparted(@PathVariable("flightNumber") @NotNull String flightNumber) {
        flightUpdateService.registerDeparture(flightNumber);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{flightNumber}")
    @Operation(summary = "Get registered flight ", tags = {"Flight"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = RegisteredFlightResponseDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class)),
                description = "Validation error"),
    })
    @Parameter(name = "flightNumber", required = true)
    public ResponseEntity<RegisteredFlightResponseDto> getRegisteredFlight(@PathVariable("flightNumber") @NotNull String flightNumber) {
        var flight = flightService.getRegisteredFlight(flightNumber);
        var flightStatus = flightService.getFlightStatus(flightNumber);
        var terminalNumber = terminalService.getTerminalNumber(flightNumber);
        var flightResponseDto = RegisteredFlightResponseDto.builder()
                .flight(flightDtoMapper.modelToDto(flight))
                .flightStatus(flightDtoMapper.modelToDto(flightStatus))
                .terminalNumber(terminalNumber)
                .build();
        return ResponseEntity.ok(flightResponseDto);
    }

    @GetMapping("/{flightNumber}/status")
    @Operation(summary = "Get status of registered flight ", tags = {"Flight"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = RegisteredFlightStatusResponseDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class)),
                    description = "Validation error"),
    })
    @Parameter(name = "flightNumber", required = true)
    public ResponseEntity<RegisteredFlightStatusResponseDto> getRegisteredFlightStatus(@PathVariable("flightNumber") @NotNull String flightNumber) {
        var flightStatus = flightService.getFlightStatus(flightNumber);
        var flightStatusResponseDto = RegisteredFlightStatusResponseDto.builder()
                .flightStatus(flightDtoMapper.modelToDto(flightStatus))
                .build();
        return ResponseEntity.ok(flightStatusResponseDto);
    }

    @GetMapping("/searchOr")
    @Operation(summary = "Search flights by origin or destination ", tags = {"Flight"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SearchResponseDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class)),
                description = "Validation error"),
    })
    @Parameter(name = "origin", required = true)
    @Parameter(name = "destination", required = true)
    public ResponseEntity<SearchResponseDto> searchByOriginOrDestination(String origin, String destination) {
        var flights = flightService.findFlightsByOriginOrDestination(origin, destination);
        var searchResponseDto = SearchResponseDto.builder()
                .flights(flightDtoMapper.modelToDto(flights))
                .build();
        return ResponseEntity.ok(searchResponseDto);
    }

    @GetMapping("/searchAnd")
    @Operation(summary = "Search flights by origin and destination ", tags = {"Flight"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SearchResponseDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class)),
                description = "Validation error"),
    })
    @Parameter(name = "origin", required = true)
    @Parameter(name = "destination", required = true)
    public ResponseEntity<SearchResponseDto> searchByOriginAndDestination(String origin, String destination) {
        var flights = flightService.findFlightsByOriginAndDestination(origin, destination);
        var searchResponseDto = SearchResponseDto.builder()
                .flights(flightDtoMapper.modelToDto(flights))
                .build();
        return ResponseEntity.ok(searchResponseDto);
    }
}
