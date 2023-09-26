package edu.flightregistration.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FlightDto implements Serializable {

    @JsonProperty("flightNumber")
    private String flightNumber;

    @JsonProperty("aircraftType")
    private String aircraftType;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("estimatedDepartureTime")
    private LocalDateTime estimatedDepartureTime;

    @JsonProperty("estimatedArrivalTime")
    private LocalDateTime estimatedArrivalTime;
}
