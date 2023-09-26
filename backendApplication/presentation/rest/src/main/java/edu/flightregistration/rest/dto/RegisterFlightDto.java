package edu.flightregistration.rest.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegisterFlightDto {

    @NotNull
    private String flightNumber;

    @NotNull
    private String aircraftType;

    @NotNull
    private String origin;

    @NotNull
    private String destination;
}
