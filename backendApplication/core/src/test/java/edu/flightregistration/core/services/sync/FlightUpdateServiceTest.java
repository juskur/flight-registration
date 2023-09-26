package edu.flightregistration.core.services.sync;

import edu.flightregistration.core.exceptions.RegistrationException;
import edu.flightregistration.core.jpa.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(FlightUpdateServiceTest.TestConfig.class)
class FlightUpdateServiceTest {

    @Autowired
    FlightUpdateService flightUpdateService;

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    ArrivedFlightsService arrivedFlightsService;

    @Test
    void canAutowire() {

        assertNotNull(flightUpdateService, "Service can not be autowired");
    }

    @Test
    void canRegisterArrival() {
        var flightNumber = "X98524";
        var flight = new edu.flightregistration.core.jpa.entity.Flight();
        flight.setId(flightNumber);
        flight.setAircraftType("Airbus A320 (twin-jet)");
        flight.setOrigin("Antalija AYT");
        flight.setDestination("Vilnius VNO");
        flightRepository.save(flight);

        assertDoesNotThrow(
                () -> flightUpdateService.registerArrival(flightNumber));

        var actual = flightRepository.findById(flightNumber);
        assertTrue(actual.isPresent(), String.format("Flight with id %s not found", flightNumber));
        assertNotNull(actual.get().getEstimatedArrivalTime(), "Estimated arrival time is not set");
        assertNull(actual.get().getEstimatedDepartureTime(), "Estimated departure time is not empty");

        var actualRegisteredArrivedFlight =  arrivedFlightsService.getNextArrivedFlight();
        assertEquals(flightNumber, actualRegisteredArrivedFlight, "Arrived flight is not added to arrivedFlights service");
    }

    @Test
    void canRegisterDeparture() {
        var flightNumber = "X98525";
        var flight = new edu.flightregistration.core.jpa.entity.Flight();
        flight.setId(flightNumber);
        flight.setAircraftType("Airbus A320 (twin-jet)");
        flight.setOrigin("Antalija AYT");
        flight.setDestination("Vilnius VNO");
        flightRepository.save(flight);

        assertDoesNotThrow(
                () -> flightUpdateService.registerDeparture(flightNumber));

        var actual = flightRepository.findById(flightNumber);
        assertTrue(actual.isPresent(), String.format("Flight with id %s not found", flightNumber));
        assertNull(actual.get().getEstimatedArrivalTime(), "Estimated arrival time is not empty");
        assertNotNull(actual.get().getEstimatedDepartureTime(), "Estimated departure time is not set");
    }

    @Test
    void throwsExceptionIfFlightDoesNotExistsWhileRegisteringArrival() {
        var flightNumber = "X98523";

        assertThrows(RegistrationException.class,
                () -> flightUpdateService.registerArrival(flightNumber));
    }

    @Test
    void throwsExceptionIfFlightDoesNotExistsWhileRegisteringDeparture() {
        var flightNumber = "X98522";

        assertThrows(RegistrationException.class,
                () -> flightUpdateService.registerDeparture(flightNumber));
    }

    @Configuration
    @ComponentScan(basePackages = {"edu.flightregistration.core"})
    @AutoConfigureDataJpa
    public static class TestConfig {
    }

}