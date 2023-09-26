package edu.flightregistration.core.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Flight {

    private String flightNumber;
    private String aircraftType;
    private String origin;
    private String destination;
    private LocalDateTime estimatedDepartureTime;
    private LocalDateTime estimatedArrivalTime;

    public FlightStatus getFlightStatus() {
        if (estimatedDepartureTime != null) {
            return FlightStatus.DEPARTED;
        } else if (estimatedArrivalTime != null) {
            return FlightStatus.ARRIVED;
        }
        return FlightStatus.SCHEDULED;
    }
}
