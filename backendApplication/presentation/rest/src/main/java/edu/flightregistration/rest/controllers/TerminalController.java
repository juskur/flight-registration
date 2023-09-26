package edu.flightregistration.rest.controllers;

import edu.flightregistration.core.services.async.TerminalService;
import edu.flightregistration.rest.dto.AllTerminalsResponseDto;
import edu.flightregistration.rest.dto.ErrorDto;
import edu.flightregistration.rest.mappers.TerminalDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/terminal/v1", produces = "application/json")
@RequiredArgsConstructor
public class TerminalController {

    private final TerminalService terminalService;
    private final TerminalDtoMapper terminalDtoMapper;

    @GetMapping("/all")
    @Operation(summary = "Get all terminals", tags = {"Terminal"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AllTerminalsResponseDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
    })
    public ResponseEntity<AllTerminalsResponseDto> getAllTerminals() {
        var terminals = terminalService.getAllTerminals();
        var allTerminalsResponseDto = AllTerminalsResponseDto.builder()
                .terminals(terminalDtoMapper.modelToDto(terminals))
                .build();
        return ResponseEntity.ok(allTerminalsResponseDto);
    }
}
