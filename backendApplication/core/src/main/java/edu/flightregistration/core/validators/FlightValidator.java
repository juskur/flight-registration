package edu.flightregistration.core.validators;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import edu.flightregistration.core.domain.Flight;
import edu.flightregistration.core.jpa.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightValidator {

    private final FlightRepository flightRepository;

    public void validateFlightForRegistration(Flight flight) {
        validateFields(flight);
        validateFlightAlreadyExists(flight.getFlightNumber());
    }

    private void validateFlightAlreadyExists(String flightNumber) {
        Preconditions.checkArgument(flightRepository.findById(flightNumber).isEmpty(),
                String.format("Flight with flight number %s already exists", flightNumber));
    }

    public void validateFlightExists(String flightNumber) {
        Preconditions.checkArgument(flightRepository.findById(flightNumber).isPresent(),
                String.format("Flight with flight number %s does not exist", flightNumber));
    }

    private void validateFields(Flight flight) {
        Preconditions.checkArgument(flight != null, "Flight is null");
        validateMandatoryFields(flight);
        validateMandatoryEmptyFields(flight);
    }

    private void validateMandatoryFields(Flight flight) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(flight.getFlightNumber()), "Flight number is empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(flight.getAircraftType()), "Aircraft type is empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(flight.getOrigin()), "Origin is empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(flight.getDestination()), "Destination is empty");
    }

    private void validateMandatoryEmptyFields(Flight flight) {
        Preconditions.checkArgument(flight.getEstimatedArrivalTime() == null, "Arrival time must be empty");
        Preconditions.checkArgument(flight.getEstimatedDepartureTime() == null, "Departure time must be empty");
    }
}
