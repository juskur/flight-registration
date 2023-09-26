package edu.flightregistration.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisteredFlightResponseDto {

    @JsonProperty("flight")
    private FlightDto flight;

    @JsonProperty("flightStatus")
    private FlightStatusDto flightStatus;

    @JsonProperty("terminalNumber")
    private String terminalNumber;
}
