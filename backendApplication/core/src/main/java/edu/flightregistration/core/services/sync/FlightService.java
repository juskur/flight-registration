package edu.flightregistration.core.services.sync;

import edu.flightregistration.core.domain.Flight;
import edu.flightregistration.core.domain.FlightStatus;
import edu.flightregistration.core.exceptions.RegistrationException;

import java.util.List;

public interface FlightService {

    void registerFlight(Flight flight) throws RegistrationException, IllegalArgumentException;
    Flight getRegisteredFlight(String flightNumber);
    FlightStatus getFlightStatus(String flightNumber) throws RegistrationException;
    List<Flight> getAllRegisteredFlights();
    List<Flight> findFlightsByOriginOrDestination(String origin, String destination);
    List<Flight> findFlightsByOriginAndDestination(String origin, String destination);
}
