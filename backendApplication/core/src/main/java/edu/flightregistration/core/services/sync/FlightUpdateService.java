package edu.flightregistration.core.services.sync;

import edu.flightregistration.core.exceptions.RegistrationException;

public interface FlightUpdateService {

    void registerDeparture(String flightNumber) throws RegistrationException;
    void registerArrival(String flightNumber)  throws RegistrationException;
}
