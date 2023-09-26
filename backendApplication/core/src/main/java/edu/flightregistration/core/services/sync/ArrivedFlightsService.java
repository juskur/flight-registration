package edu.flightregistration.core.services.sync;

public interface ArrivedFlightsService {

    void addArrivedFlight(String flightNumber);
    String getNextArrivedFlight();
    void removeArrivedFlight(String flightNumber);
}
